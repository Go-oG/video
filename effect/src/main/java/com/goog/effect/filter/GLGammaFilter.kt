package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

class GLGammaFilter : GLFilter() {
    var gamma by FloatDelegate(1f, 0f, 4f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("gamma", gamma)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform lowp float gamma;

            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = vec4(pow(textureColor.rgb, vec3(gamma)), textureColor.w);
            }
        """.trimIndent()
    }
}
