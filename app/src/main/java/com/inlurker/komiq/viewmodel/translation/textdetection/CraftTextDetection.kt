package com.inlurker.komiq.viewmodel.translation.textdetection

import android.content.Context
import android.graphics.Bitmap
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import java.io.ByteArrayOutputStream


class CraftTextDetection(activityContext: Context) {
    private val pythonInstance: Python
    private val module: PyObject

    init {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(activityContext))
        }
        pythonInstance = Python.getInstance()
        module = pythonInstance.getModule("craft_tflite_inference")
    }

    fun initialize() {
        module.callAttr("start_inference")
    }

    fun detectText(bitmap: Bitmap): Array<Array<FloatArray>> {
        val resizedBitmap = resizeImage(bitmap)

        val result = module.callAttr("detect_text", bitmapToByteArray(resizedBitmap))

        return result.toJava(Array<Array<FloatArray>>::class.java)
    }

    private fun resizeImage(bitmap: Bitmap): Bitmap {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 800, true)

        return resizedBitmap
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun endInference() {
        module.callAttr("end_inference")
    }
}

