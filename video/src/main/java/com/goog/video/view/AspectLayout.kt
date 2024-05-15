package com.goog.video.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.goog.video.listeners.AspectRatioListener
import com.goog.video.model.ResizeMode
import kotlin.math.abs

class AspectLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {
    private val ratioUpdateDispatcher: RatioUpdateDispatcher
    private var ratioListener: AspectRatioListener? = null
    private var videoRatio = 0f
    private var resizeMode: ResizeMode

    init {
        resizeMode = ResizeMode.ZOOM
        ratioUpdateDispatcher = RatioUpdateDispatcher()
    }

    fun updateRatio(widthHeightRatio: Float) {
        var ratio = widthHeightRatio
        if (ratio >= 2) {
            val c = ratio.toInt()
            ratio -= c.toFloat()
            ratio += 1f
        }
        if (this.videoRatio != ratio) {
            this.videoRatio = ratio
            requestLayout()
        }
    }

    fun setRatioListener(listener: AspectRatioListener?) {
        this.ratioListener = listener
    }

    fun getResizeMode(): ResizeMode {
        return resizeMode
    }

    fun setResizeMode(resizeMode: ResizeMode) {
        if (this.resizeMode != resizeMode) {
            this.resizeMode = resizeMode
            requestLayout()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (videoRatio <= 0) {
            return
        }
        var width = measuredWidth
        var height = measuredHeight
        val viewAspectRatio = width.toFloat() / height
        val aspectDeformation = videoRatio / viewAspectRatio - 1
        if (abs(aspectDeformation.toDouble()) <= MAX_RATIO_DEFORMATION_FRACTION) {
            ratioUpdateDispatcher.scheduleUpdate(videoRatio, viewAspectRatio, false)
            return
        }

        when (resizeMode) {
            ResizeMode.FIXED_WIDTH -> height = (width / videoRatio).toInt()
            ResizeMode.FIXED_HEIGHT -> width = (height * videoRatio).toInt()
            ResizeMode.ZOOM -> if (aspectDeformation > 0) {
                width = (height * videoRatio).toInt()
            } else {
                height = (width / videoRatio).toInt()
            }
            ResizeMode.FIT -> if (aspectDeformation > 0) {
                height = (width / videoRatio).toInt()
            } else {
                width = (height * videoRatio).toInt()
            }
            ResizeMode.FILL -> {}
        }
        ratioUpdateDispatcher.scheduleUpdate(videoRatio, viewAspectRatio, true)
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }

    private inner class RatioUpdateDispatcher : Runnable {
        private var targetAspectRatio = 0f
        private var naturalAspectRatio = 0f
        private var aspectRatioMismatch = false
        private var isScheduled = false

        fun scheduleUpdate(targetAspectRatio: Float, naturalAspectRatio: Float,
            aspectRatioMismatch: Boolean) {
            this.targetAspectRatio = targetAspectRatio
            this.naturalAspectRatio = naturalAspectRatio
            this.aspectRatioMismatch = aspectRatioMismatch

            if (!isScheduled) {
                isScheduled = true
                post(this)
            }
        }

        override fun run() {
            isScheduled = false
            if (ratioListener == null) {
                return
            }
            ratioListener!!.onRatioUpdated(targetAspectRatio, naturalAspectRatio,
                    aspectRatioMismatch)
        }
    }

    companion object {
        private const val MAX_RATIO_DEFORMATION_FRACTION = 0.01f
    }
}
