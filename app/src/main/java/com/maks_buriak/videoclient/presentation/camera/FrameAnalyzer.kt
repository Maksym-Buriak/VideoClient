package com.maks_buriak.videoclient.presentation.camera

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

class FrameAnalyzer(private val onFrameProcessed: (ByteArray) -> Unit) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        // Convert frame YUV_420_888 to compressed JPEG ByteArray
        val bytes = image.toJpegByteArray()
        if (bytes != null) {
            onFrameProcessed(bytes)
        }
        image.close() // close frame
    }

    private fun ImageProxy.toJpegByteArray(): ByteArray? {
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, android.graphics.ImageFormat.NV21, this.width, this.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 70, out) // 70% якість для швидкості
        return out.toByteArray()
    }
}