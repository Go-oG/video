package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.utils.loadFilterFromAsset
import kotlin.math.min

class GLScaleFilter : GLFilter() {
    private var scaleMatrix = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f,
    )

    private var scaleType: ScaleType = ScaleType.CENTER_INSIDE

    private var videoWidth: Int = 0

    private var videoHeight: Int = 0

    init {
        updateMatrix()
    }

    override fun getVertexShader(): String {
        return loadFilterFromAsset("filters/scale.vert")
    }

    fun setScaleType(scaleType: ScaleType) {
        if (scaleType == this.scaleType) {
            return
        }
        this.scaleType = scaleType
        updateMatrix()
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        updateMatrix()
    }

    fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        this.videoWidth = videoWidth
        this.videoHeight = videoHeight
        updateMatrix()
    }


    private fun updateMatrix() {
        scaleMatrix = when (scaleType) {
            ScaleType.CENTER_CROP -> centerCropMatrix(videoWidth, videoHeight, width, height)
            ScaleType.CENTER -> centerMatrix(videoWidth, videoHeight, width, height)
            ScaleType.FIT_XY -> fitXYMatrix(videoWidth, videoHeight, width, height)
            ScaleType.FIT_START -> fitStartMatrix(videoWidth, videoHeight, width, height)
            ScaleType.FIT_END -> fitEndMatrix(videoWidth, videoHeight, width, height)
            ScaleType.FIT_CENTER -> fitCenterMatrix(videoWidth, videoHeight, width, height)
            ScaleType.CENTER_INSIDE -> centerInsideMatrix(videoWidth, videoHeight, width, height)
            ScaleType.NONE -> floatArrayOf(
                    1f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 1f,
            )
        }
    }

    private fun centerCropMatrix(videoWidth: Int, videoHeight: Int, viewWidth: Int,
        viewHeight: Int): FloatArray {
        val videoAspectRatio = videoWidth.toFloat() / videoHeight
        val viewAspectRatio = viewWidth.toFloat() / viewHeight
        val scale = if (viewAspectRatio > videoAspectRatio) {
            viewWidth.toFloat() / videoWidth
        } else {
            viewHeight.toFloat() / videoHeight
        }

        val dx = (viewWidth - videoWidth * scale) * 0.5f
        val dy = (viewHeight - videoHeight * scale) * 0.5f

        return floatArrayOf(scale, 0f, 0f, 0f,
                0f, scale, 0f, 0f,
                0f, 0f, 1f, 0f,
                dx, dy, 0f, 1f
        )
    }

    private fun fitCenterMatrix(videoWidth: Int, videoHeight: Int, viewWidth: Int, viewHeight: Int): FloatArray {
        val videoAspectRatio = videoWidth.toFloat() / videoHeight
        val viewAspectRatio = viewWidth.toFloat() / viewHeight
        val scale = if (viewAspectRatio > videoAspectRatio) {
            viewHeight.toFloat() / videoHeight
        } else {
            viewWidth.toFloat() / videoWidth
        }
        val dx = (viewWidth - videoWidth * scale) * 0.5f
        val dy = (viewHeight - videoHeight * scale) * 0.5f
        return floatArrayOf(scale, 0f, 0f, 0f,
                0f, scale, 0f, 0f,
                0f, 0f, 1f, 0f,
                dx, dy, 0f, 1f
        )
    }

    private fun fitXYMatrix(videoWidth: Int, videoHeight: Int, viewWidth: Int, viewHeight: Int): FloatArray {
        val scaleX = viewWidth.toFloat() / videoWidth
        val scaleY = viewHeight.toFloat() / videoHeight

        return floatArrayOf(scaleX, 0f, 0f, 0f,
                0f, scaleY, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        )
    }

    private fun fitStartMatrix(videoWidth: Int, videoHeight: Int, viewWidth: Int,
        viewHeight: Int): FloatArray {
        val videoAspectRatio = videoWidth.toFloat() / videoHeight
        val viewAspectRatio = viewWidth.toFloat() / viewHeight
        val scale = if (viewAspectRatio > videoAspectRatio) {
            viewHeight.toFloat() / videoHeight
        } else {
            viewWidth.toFloat() / videoWidth
        }

        val dx = 0f
        val dy = 0f

        return floatArrayOf(scale, 0f, 0f, 0f,
                0f, scale, 0f, 0f,
                0f, 0f, 1f, 0f,
                dx, dy, 0f, 1f
        )
    }

    private fun fitEndMatrix(videoWidth: Int, videoHeight: Int, viewWidth: Int, viewHeight: Int): FloatArray {
        val videoAspectRatio = videoWidth.toFloat() / videoHeight
        val viewAspectRatio = viewWidth.toFloat() / viewHeight
        val scale = if (viewAspectRatio > videoAspectRatio) {
            viewHeight.toFloat() / videoHeight
        } else {
            viewWidth.toFloat() / videoWidth
        }

        val dx = viewWidth - videoWidth * scale
        val dy = viewHeight - videoHeight * scale

        return floatArrayOf(scale, 0f, 0f, 0f,
                0f, scale, 0f, 0f,
                0f, 0f, 1f, 0f,
                dx, dy, 0f, 1f)
    }

    private fun centerInsideMatrix(videoWidth: Int, videoHeight: Int, viewWidth: Int,
        viewHeight: Int): FloatArray {
        val videoAspectRatio = videoWidth.toFloat() / videoHeight
        val viewAspectRatio = viewWidth.toFloat() / viewHeight
        val scale = if (viewAspectRatio > videoAspectRatio) {
            min((viewHeight.toFloat() / videoHeight).toDouble(), 1.0).toFloat()
        } else {
            min((viewWidth.toFloat() / videoWidth).toDouble(), 1.0).toFloat()
        }

        val dx = ((viewWidth - videoWidth * scale) * 0.5f)
        val dy = ((viewHeight - videoHeight * scale) * 0.5f)

        return floatArrayOf(scale, 0f, 0f, 0f,
                0f, scale, 0f, 0f,
                0f, 0f, 1f, 0f,
                dx, dy, 0f, 1f
        )
    }

    private fun centerMatrix(videoWidth: Int, videoHeight: Int, viewWidth: Int, viewHeight: Int): FloatArray {
        val dx = (viewWidth - videoWidth) * 0.5f
        val dy = (viewHeight - videoHeight) * 0.5f

        return floatArrayOf(1.0f, 0f, 0f, 0f,
                0f, 1.0f, 0f, 0f,
                0f, 0f, 1f, 0f,
                dx, dy, 0f, 1f
        )
    }
}

enum class ScaleType {
    NONE,
    CENTER_CROP,
    CENTER,
    FIT_XY,
    FIT_START,
    FIT_CENTER,
    FIT_END,
    CENTER_INSIDE;
}