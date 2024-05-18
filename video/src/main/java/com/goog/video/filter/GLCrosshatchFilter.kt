package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

class GLCrosshatchFilter : GLFilter() {
    var crossHatchSpacing by FloatDelegate(0.03f, 0f)
    var lineWidth by FloatDelegate(0.003f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("crossHatchSpacing", crossHatchSpacing)
        put("lineWidth", lineWidth)
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
