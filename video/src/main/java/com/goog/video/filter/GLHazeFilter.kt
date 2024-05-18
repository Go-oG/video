package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

class GLHazeFilter : GLFilter() {

    //suggest -0.3 ->0.3
    var distance by FloatDelegate(0.2f,-1f,1f)
    //suggest -0.3 ->0.3
    var slope by FloatDelegate(0f,-1f,1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("distance",distance)
        put("slope",slope)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform lowp float distance;
            uniform highp float slope;
            void main() {
                highp vec4 color = vec4(1.0);
                highp float d = vTextureCoord.y * slope + distance;
                highp vec4 c = texture2D(sTexture, vTextureCoord);
                c = (c - d * color) / (1.0 - d);
                gl_FragColor = c; 
            }
        """.trimIndent()
    }
}
