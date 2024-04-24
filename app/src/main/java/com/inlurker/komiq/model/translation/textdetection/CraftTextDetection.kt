package com.inlurker.komiq.model.translation.textdetection

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.scale
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import com.inlurker.komiq.model.data.boundingbox.parsers.combineAndConvertBoxes
import com.inlurker.komiq.viewmodel.utils.bitmapToByteArray
import com.inlurker.komiq.viewmodel.utils.scaleBoundingBoxes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList


class CraftTextDetection(activityContext: Context) {
    private val pythonInstance: Python
    private val module: PyObject
    private val taskQueue: LinkedList<Triple<Int, Bitmap, (List<BoundingBox>) -> Unit>> = LinkedList()
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
            // Remove any existing task with the same index to prevent duplicate processing
            taskQueue.removeAll { it.first == index }

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
                taskQueue.removeFirst()
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
        val resizedBitmap = bitmap.scale(600, 800, true)
        val result = module.callAttr("detect_text", bitmapToByteArray(resizedBitmap)).toJava(Array<Array<FloatArray>>::class.java)
        val combinedResult = combineAndConvertBoxes(result)
        val scaledResult = scaleBoundingBoxes(combinedResult, Pair(300, 400), Pair(bitmap.width, bitmap.height))
        return scaledResult
    }

    fun endInference() {
        module.callAttr("end_inference")
    }
}