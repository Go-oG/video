package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLGammaFilter(gamma: Float = 1f) : GLFilter() {
    private var gamma: Float = 1f

    fun setGamma(v: Float) {
        checkArgs(v in 0.0..3.0)
        this.gamma = v
    }


    override fun onDraw(fbo: EFrameBufferObject?) {
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
