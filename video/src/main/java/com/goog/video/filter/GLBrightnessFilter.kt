package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

/**
 * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
class GLBrightnessFilter(value: Float = 0f) : GLFilter() {
    init {
        setBrightness(value)
    }
    private var brightness = 0f

    fun setBrightness(brightness: Float) {
        checkArgs(brightness >= -1.0f && brightness <= 1.0f)
        this.brightness = brightness
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("brightness", brightness)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform lowp float brightness;

            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = vec4((textureColor.rgb + vec3(brightness)), textureColor.w);
            }
        """.trimIndent()
    }


}
