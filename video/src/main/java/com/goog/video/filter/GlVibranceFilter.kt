package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GlVibranceFilter(vibrance: Float = 0f) : GlFilter() {
    private var vibrance = 0f

    init {
        setVibrance(vibrance)
    }
    fun setVibrance(vibrance: Float) {
        checkArgs(vibrance >= 0)
        this.vibrance = vibrance
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("vibrance", vibrance)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform lowp float vibrance;

            void main() {
                lowp vec4 color = texture2D(sTexture, vTextureCoord);
                lowp float average = (color.r + color.g + color.b) / 3.0;
                lowp float mx = max(color.r, max(color.g, color.b));
                lowp float amt = (mx - average) * (-vibrance * 3.0);
                color.rgb = mix(color.rgb, vec3(mx), amt);
                gl_FragColor = color;
            }
        """.trimIndent()
    }
}
