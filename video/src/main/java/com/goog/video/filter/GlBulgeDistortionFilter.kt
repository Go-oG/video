package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

class GlBulgeDistortionFilter : GlFilter() {
    var centerX: Float = 0.5f
    var centerY: Float = 0.5f
    var radius: Float = 0.25f
    var scale: Float = 0.5f

    override fun onDraw(fbo: EFrameBufferObject?) {
        putVec2("center", centerX, centerY)
        put("radius",radius)
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
