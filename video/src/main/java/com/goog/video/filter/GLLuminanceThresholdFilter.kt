package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

class GLLuminanceThresholdFilter(threshold: Float = 0.5f) : GLFilter() {
    private var threshold = 0.5f

    init {
        setThreshold(threshold)
    }

    fun setThreshold(v: Float) {
        checkArgs(v in 0f..1f, "threshold must be in [0, 1]")
        this.threshold = v
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("threshold", threshold)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp float threshold;

            const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

            void main() {
                highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                highp float luminance = dot(textureColor.rgb, W);
                highp float thresholdResult = step(threshold, luminance);
                gl_FragColor = vec4(vec3(thresholdResult), textureColor.w);
            }
        """.trimIndent()
    }

}
