package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

class GlHazeFilter : GlFilter() {
    var distance: Float = 0.2f
    var slope: Float = 0.0f

    override fun onDraw(fbo: EFrameBufferObject?) {
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
