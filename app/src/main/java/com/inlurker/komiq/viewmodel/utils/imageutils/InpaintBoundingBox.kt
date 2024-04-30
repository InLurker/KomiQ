package com.inlurker.komiq.viewmodel.utils.imageutils

import android.graphics.Bitmap
import com.inlurker.komiq.model.data.boundingbox.BoundingBox
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo


fun inpaintBitmap(inputBitmap: Bitmap, boundingBoxes: List<BoundingBox>): Bitmap {
    // Convert Bitmap to Mat in the correct format (ensure it's 8-bit 3-channel)
    val bitmap = inputBitmap.copy(Bitmap.Config.ARGB_8888, true)
    val srcMat = Mat()
    Utils.bitmapToMat(bitmap, srcMat)
    Imgproc.cvtColor(srcMat, srcMat, Imgproc.COLOR_RGBA2RGB)

    // Create a mask in the correct format (8-bit 1-channel)
    val mask = Mat.zeros(srcMat.size(), CvType.CV_8UC1)

    // Define the mask using bounding boxes
    boundingBoxes.forEach { box ->
        val rect = Rect(
            Math.max(0, box.X1.toInt()),
            Math.max(0, box.Y1.toInt()),
            Math.min(srcMat.width() - box.X1.toInt(), box.X2.toInt() - box.X1.toInt()),
            Math.min(srcMat.height() - box.Y1.toInt(), box.Y2.toInt() - box.Y1.toInt())
        )
        // Fill the rectangle in the mask
        Imgproc.rectangle(mask, rect.tl(), rect.br(), Scalar(255.0), Core.FILLED)
    }

    // Prepare destination Mat
    val dstMat = Mat()

    // Apply inpainting
    Photo.inpaint(srcMat, mask, dstMat, 3.0, Photo.INPAINT_TELEA)

    // Convert the processed Mat back to Bitmap
    val outputBitmap = Bitmap.createBitmap(dstMat.cols(), dstMat.rows(), Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(dstMat, outputBitmap)

    // Release resources
    srcMat.release()
    mask.release()
    dstMat.release()

    return outputBitmap
}