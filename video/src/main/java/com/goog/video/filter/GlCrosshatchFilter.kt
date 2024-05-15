package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GlCrosshatchFilter(space: Float = 0.03f, lineWidth: Float = 0.003f) : GlFilter() {
    private var crossHatchSpacing = 0.03f
    private var lineWidth = 0.003f

    init {
        setCrossHatchSpacing(space)
        setLineWidth(lineWidth)
    }
    override fun onDraw(fbo: EFrameBufferObject?) {
        put("crossHatchSpacing", crossHatchSpacing)
        put("lineWidth", lineWidth)
    }

    fun setCrossHatchSpacing(crossHatchSpacing: Float) {
        checkArgs(crossHatchSpacing >=0.0f, "crossHatchSpacing must be >= 0")
        this.crossHatchSpacing = crossHatchSpacing
    }

    fun setLineWidth(lineWidth: Float) {
        checkArgs(lineWidth >=0.0f, "lineWidth must be >= 0")
        this.lineWidth = lineWidth
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        val singlePixelSpacing = if (width != 0) {
            1.0f / width.toFloat()
        } else {
            1.0f / 2048.0f
        }
        if (crossHatchSpacing < singlePixelSpacing) {
            this.crossHatchSpacing = singlePixelSpacing
        }
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform highp float crossHatchSpacing;
            uniform highp float lineWidth;
            const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

            void main() {
                highp float luminance = dot(texture2D(sTexture, vTextureCoord).rgb, W);
                lowp vec4 colorToDisplay = vec4(1.0, 1.0, 1.0, 1.0);
                if (luminance < 1.00) {
                    if (mod(vTextureCoord.x + vTextureCoord.y, crossHatchSpacing) <= lineWidth) {
                        colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
                    }
                }
                if (luminance < 0.75) {
                    if (mod(vTextureCoord.x - vTextureCoord.y, crossHatchSpacing) <= lineWidth) {
                        colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
                    }
                }
                if (luminance < 0.50) {
                    if (mod(vTextureCoord.x + vTextureCoord.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth) {
                        colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
                    }
                }
                if (luminance < 0.3) {
                    if (mod(vTextureCoord.x - vTextureCoord.y - (crossHatchSpacing / 2.0), crossHatchSpacing) <= lineWidth) {
                        colorToDisplay = vec4(0.0, 0.0, 0.0, 1.0);
                    }
                }
                gl_FragColor = colorToDisplay;
            }
        """.trimIndent()
    }
}
