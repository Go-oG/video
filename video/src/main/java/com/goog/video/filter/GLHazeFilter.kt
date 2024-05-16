package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLHazeFilter(distance: Float = 0.2f, slope: Float = 0f,var checkArgs:Boolean=true ) : GLFilter() {
    private var distance: Float = 0.2f
    private var slope: Float = 0.0f

    init {
        setDistance(distance)
        setSlope(slope)
    }

    fun setDistance(v: Float) {
        if(checkArgs){
            checkArgs(v >= -0.3 && v <= 0.3)
        }
        this.distance = v
    }

    fun setSlope(v: Float) {
        if(checkArgs){
            checkArgs(v >= -0.3 && v <= 0.3)
        }
        this.slope = v
    }


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