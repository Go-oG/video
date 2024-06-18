package com.goog.effect.filter

import com.goog.effect.filter.blur.GLBoxBlurFilter
import com.goog.effect.filter.core.GLFilterGroup
import com.goog.effect.filter.core.GLMultiTextureFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.utils.loadFilterFromAsset

///TODO 暂时不可用
class GLAdaptiveThresholdFilter(blurSize: Float = 4f) : GLFilterGroup() {

    init {
        val luminance = GLLuminanceFilter()
        val boxBlur = GLBoxBlurFilter()
        boxBlur.blurSize = blurSize
        val adaptiveThreshold = AdaptiveThresholdInner()
        mFilters = listOf(luminance, boxBlur, adaptiveThreshold)
    }
}

private class AdaptiveThresholdInner : GLMultiTextureFilter(2) {
    override fun onDraw2(fbo: FrameBufferObject?) {

    }

    ///TODO 暂不可用
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/adaptive_threshold.frag")
    }
}