package com.goog.video.filter

import com.goog.video.filter.blur.GLBoxBlurFilter
import com.goog.video.filter.core.GLFilterGroup
import com.goog.video.filter.core.GLMultiTextureFilter
import com.goog.video.gl.FrameBufferObject

///TODO 暂时不可用
class GLAdaptiveThresholdFilter(blurSize: Float = 4f) : GLFilterGroup() {

    init {
        val luminance = GLLuminanceFilter()
        val boxBlur = GLBoxBlurFilter()
        boxBlur.blurSize = blurSize
        val adaptiveThreshold = AdaptiveThresholdInner()
        filters = listOf(luminance, boxBlur, adaptiveThreshold)
    }
}

private class AdaptiveThresholdInner : GLMultiTextureFilter(2) {
    override fun onDraw2(fbo: FrameBufferObject?) {

    }

    ///TODO 暂不可用
    override fun getFragmentShader(): String {
        return """
            precision highp float;
            varying highp vec2 vTextureCoord;
            varying highp vec2 vTextureCoord2;

            uniform sampler2D sTexture;
            uniform sampler2D sTexture2; 

            void main() {
                 float blurredInput = texture2D(sTexture, vTextureCoord).r;
                 float localLuminance = texture2D(sTexture2, vTextureCoord2).r;
                 float thresholdResult = step(blurredInput - 0.05, localLuminance);
               
                gl_FragColor = vec4(vec3(thresholdResult), 1.0);
            }
        """.trimIndent()
    }
}