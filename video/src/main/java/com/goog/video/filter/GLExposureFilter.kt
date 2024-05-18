package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs


class GLExposureFilter : GLFilter() {
    var exposure by FloatDelegate(1f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("exposure", exposure)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp float exposure;

            void main() {
                highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);
            } 
        """.trimIndent()
    }

}
