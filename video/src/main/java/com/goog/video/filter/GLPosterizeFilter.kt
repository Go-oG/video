package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLPosterizeFilter(colorLevels: Int = 10) : GLFilter() {
    private var colorLevels = 10

    init {
        setColorLevels(colorLevels)
    }

    fun setColorLevels(v: Int) {
        checkArgs(v in 1..256)
        this.colorLevels=v
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("colorLevels", colorLevels.toFloat())
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp float colorLevels;

            void main() {
                highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = floor((textureColor * colorLevels) + vec4(0.5)) / colorLevels;
            }
        """.trimIndent()
    }
}
