package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

class GLVibranceFilter : GLFilter() {
    //suggest [-1.2,1.2]
    var vibrance by FloatDelegate(0f, -2f, 2f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
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
