package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

/**
 * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
class GLBrightnessFilter : GLFilter() {

    var brightness by FloatDelegate(0f, -1f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
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
