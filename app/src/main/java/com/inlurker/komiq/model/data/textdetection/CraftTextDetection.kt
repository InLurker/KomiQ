package com.inlurker.komiq.model.data.textdetection

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.scale
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import com.inlurker.komiq.model.data.boundingbox.parsers.coordinatesToBoundingBox
import com.inlurker.komiq.viewmodel.utils.scaleCoordinates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.LinkedList


class CraftTextDetection(activityContext: Context) {
    private val pythonInstance: Python
    private val module: PyObject
    private val taskQueue: LinkedList<Triple<Int, Bitmap, (List<BoundingBox>) -> Unit>> = LinkedList()
    private val queuedIndices: MutableSet<Int> = mutableSetOf()
    @Volatile private var isProcessing: Boolean = false

    init {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(activityContext))
        }
        pythonInstance = Python.getInstance()
        module = pythonInstance.getModule("craft_tflite_inference")
        initialize()
    }

    private fun initialize() {
        module.callAttr("start_inference")
    }

    fun queueDetectText(index: Int, bitmap: Bitmap, onResult: (List<BoundingBox>) -> Unit) {
        synchronized(taskQueue) {
            // Check if the index is already in the queue
            val existingTaskIndex = taskQueue.indexOfFirst { it.first == index }

            // If index is already being processed (found in queuedIndices but not in taskQueue)
            if (queuedIndices.contains(index) && existingTaskIndex == -1) {
                // The task is either currently being processed or just finished; do not re-queue it
                return
            }

            if (existingTaskIndex != -1) {
                // If index is found in the queue, remove the existing task
                taskQueue.removeAt(existingTaskIndex)
                // No need to modify queuedIndices here as we are going to re-add the task
            } else {
                // Add index to queuedIndices if it's a new task
                queuedIndices.add(index)
            }

            // Add (or re-add) task to the front of the list for LIFO processing
            taskQueue.addFirst(Triple(index, bitmap, onResult))

            if (!isProcessing) {
                isProcessing = true
                processNextTask()
            }
        }
    }

    private fun processNextTask() {
        CoroutineScope(Dispatchers.IO).launch {
            val task = synchronized(taskQueue) {
                if (taskQueue.isEmpty()) {
                    isProcessing = false
                    return@launch
                }
                taskQueue.removeFirst().also {
                    queuedIndices.remove(it.first) // Remove index from queuedIndices when starting processing
                }
            }

            try {
                val result = detectText(task.second)
                withContext(Dispatchers.Main) {
                    task.third(result)
                }
            } finally {
                processNextTask()
            }
        }
    }

    private fun detectText(bitmap: Bitmap): List<BoundingBox> {
        val resizedBitmap: Bitmap = resizeImage(bitmap)

        val result: Array<Array<FloatArray>> = module.callAttr("detect_text", bitmapToByteArray(resizedBitmap))
            .toJava(Array<Array<FloatArray>>::class.java)

        val scaledResult = scaleCoordinates(
            result,
            Pair(300, 400),
            Pair(bitmap.width, bitmap.height)
        )

        val detectedPixels = coordinatesToBoundingBox(scaledResult)

        return detectedPixels // Assume this function exists and returns List<BoundingBox>
    }

    private fun resizeImage(bitmap: Bitmap): Bitmap {
        return bitmap.scale(600, 800, true)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun endInference() {
        module.callAttr("end_inference")
    }
}