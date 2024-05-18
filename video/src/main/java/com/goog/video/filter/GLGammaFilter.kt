package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

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
