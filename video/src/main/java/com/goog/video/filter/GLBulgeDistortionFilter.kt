package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

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
