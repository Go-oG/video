package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

class GlGammaFilter(var gamma: Float=1.2f) : GlFilter() {

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
