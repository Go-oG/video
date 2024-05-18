package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

class GLSwirlFilter : GLFilter() {
    var angle by FloatDelegate(1f, 0f)
    var radius by FloatDelegate(0.5f, 0f, 1f)
    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("center", centerX, centerY)
        put("radius", radius)
        put("angle", angle)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp vec2 center;
            uniform highp float radius;
            uniform highp float angle;

            void main() {
                highp vec2 textureCoordinateToUse = vTextureCoord;
                highp float dist = distance(center, vTextureCoord);
                if (dist < radius) {
                    textureCoordinateToUse -= center;
                    highp float percent = (radius - dist) / radius;
                    highp float theta = percent * percent * angle * 8.0;
                    highp float s = sin(theta);
                    highp float c = cos(theta);
                    textureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));
                    textureCoordinateToUse += center;
                }
                gl_FragColor = texture2D(sTexture, textureCoordinateToUse);
            }
        """.trimIndent()
    }
}
