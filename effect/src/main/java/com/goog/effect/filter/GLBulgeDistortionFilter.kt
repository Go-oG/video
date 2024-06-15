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
        put("radius", if (mEnable) radius else 0f)
        put("scale", if (mEnable) scale else -1f)
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
                if (radius <= 0.0 || scale < 0.0) {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                } else {
                    vec2 useTexCoord = vTextureCoord;
                    float dist = distance(center, vTextureCoord);
                    useTexCoord -= center;
                    if (dist < radius) {
                        float percent = 1.0 - ((radius - dist) / radius) * scale;
                        percent = percent * percent;
                        useTexCoord = useTexCoord * percent;
                    }
                    useTexCoord += center;
                    gl_FragColor = texture2D(sTexture, useTexCoord);
                }

            }
        """.trimIndent()
    }
}
