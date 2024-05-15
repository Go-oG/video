package com.goog.video.filter

import android.opengl.GLES20
import com.goog.video.gl.EFrameBufferObject


/**
 * Changes the contrast of the image.
 * contrast value ranges from 0.0 to 4.0, with 1.0 as the normal level
 */
class GlContrastFilter : GlFilter() {
    private var contrast = 1.2f

    fun setContrast(contrast: Float) {
        this.contrast = contrast
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("contrast", contrast)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform lowp float contrast;

            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureColor.w);
            }
        """.trimIndent()
    }

}
