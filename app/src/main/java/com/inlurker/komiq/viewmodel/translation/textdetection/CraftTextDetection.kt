package com.inlurker.komiq.viewmodel.translation.textdetection

import android.content.Context
import android.graphics.Bitmap
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.inlurker.komiq.viewmodel.utils.bitmapToByteArray
import com.inlurker.komiq.viewmodel.utils.resizeImage


class CraftTextDetection(private val activityContext: Context) {
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

    fun endInference() {
        module.callAttr("end_inference")
    }
}
