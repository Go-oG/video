package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

class GLHueFilter(hue: Float = 90f) : GLFilter() {
    private var hue = 90f

    init {
        setHue(hue)
    }

    fun setHue(v: Float) {
        checkArgs(v in 0f..360f, "Hue must be in range [0, 360]")
        this.hue = v
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        put("hueAdjust", hue)
    }

    override fun getFragmentShader(): String {
        return """
            precision highp float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform mediump float hueAdjust;
            const highp vec4 kRGBToYPrime = vec4(0.299, 0.587, 0.114, 0.0);
            const highp vec4 kRGBToI = vec4(0.595716, -0.274453, -0.321263, 0.0);
            const highp vec4 kRGBToQ = vec4(0.211456, -0.522591, 0.31135, 0.0);

            const highp vec4 kYIQToR = vec4(1.0, 0.9563, 0.6210, 0.0);
            const highp vec4 kYIQToG = vec4(1.0, -0.2721, -0.6474, 0.0);
            const highp vec4 kYIQToB = vec4(1.0, -1.1070, 1.7046, 0.0);

            void main() {
                // Sample the input pixel
                highp vec4 color = texture2D(sTexture, vTextureCoord);

                // Convert to YIQ
                highp float YPrime = dot(color, kRGBToYPrime);
                highp float I = dot(color, kRGBToI);
                highp float Q = dot(color, kRGBToQ);

                // Calculate the hue and chroma
                highp float hue = atan(Q, I);
                highp float chroma = sqrt(I * I + Q * Q);

                // Make the user's adjustments
                hue += (-hueAdjust); //why negative rotation?

                // Convert back to YIQ
                Q = chroma * sin(hue);
                I = chroma * cos(hue);

                // Convert back to RGB
                highp vec4 yIQ = vec4(YPrime, I, Q, 0.0);
                color.r = dot(yIQ, kYIQToR);
                color.g = dot(yIQ, kYIQToG);
                color.b = dot(yIQ, kYIQToB);
                
                gl_FragColor = color;
            }
        """.trimIndent()
    }
}
