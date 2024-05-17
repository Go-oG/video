package com.goog.video.filter

import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

class GLSaturationFilter(saturation: Float = 1f) : GLFilter() {
    private var saturation = 1f

    init {
        setSaturation(saturation)
    }

    fun setSaturation(v: Float) {
        checkArgs(v in 0f..2f, "saturation must be in [0, 2]")
        this.saturation = v
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("saturation", saturation)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform lowp float saturation;

            const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);

            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                lowp float luminance = dot(textureColor.rgb, luminanceWeighting);
                lowp vec3 greyScaleColor = vec3(luminance);
                gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureColor.w);
            }
        """.trimIndent()
    }
}
