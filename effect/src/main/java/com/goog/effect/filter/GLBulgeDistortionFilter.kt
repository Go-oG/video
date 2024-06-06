package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

class GLBulgeDistortionFilter : GLFilter() {

    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)
    var radius by FloatDelegate(0.5f, 0f, 1f)
    var scale by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        putVec2("center", centerX, centerY)
        put("radius", radius)
        put("scale", scale)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform highp vec2 center;
            uniform highp float radius;
            uniform highp float scale;

            void main() {
                highp vec2 textureCoordinateToUse = vTextureCoord;
                highp float dist = distance(center, vTextureCoord);
                textureCoordinateToUse -= center;
                if (dist < radius) {
                    highp float percent = 1.0 - ((radius - dist) / radius) * scale;
                    percent = percent * percent;
                    textureCoordinateToUse = textureCoordinateToUse * percent;
                }
                textureCoordinateToUse += center;
                gl_FragColor = texture2D(sTexture, textureCoordinateToUse);
            }
        """.trimIndent()
    }
}
