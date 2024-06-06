package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate


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
