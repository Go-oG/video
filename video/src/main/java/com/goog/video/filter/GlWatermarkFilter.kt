package com.goog.video.filter

import android.graphics.Bitmap
import android.graphics.Canvas

class GlWatermarkFilter : GlOverlayFilter {
    private val bitmap: Bitmap?
    private var position = Position.LEFT_TOP

    constructor(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }

    constructor(bitmap: Bitmap?, position: Position) {
        this.bitmap = bitmap
        this.position = position
    }

    override fun drawCanvas(canvas: Canvas) {
        if (bitmap != null && !bitmap.isRecycled) {
            when (position) {
                Position.LEFT_TOP -> canvas.drawBitmap(bitmap, 0f, 0f, null)
                Position.LEFT_BOTTOM -> canvas.drawBitmap(bitmap, 0f, (canvas.height - bitmap.height).toFloat(), null)
                Position.RIGHT_TOP -> canvas.drawBitmap(bitmap, (canvas.width - bitmap.width).toFloat(), 0f, null)
                Position.RIGHT_BOTTOM -> canvas.drawBitmap(bitmap, (canvas.width - bitmap.width).toFloat(),
                        (canvas.height - bitmap.height).toFloat(), null)
            }
        }
    }

    enum class Position {
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM
    }

}
