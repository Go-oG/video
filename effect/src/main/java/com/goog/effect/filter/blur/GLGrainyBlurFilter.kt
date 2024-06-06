package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate
import kotlin.math.sin

// 粒状模糊
class GLGrainyBlurFilter : GLFilter() {
    var directionX by FloatDelegate(0.25f, 0f, 1f)
    var directionY by FloatDelegate(0.25f, 0f, 1f)

    var blurRadius by IntDelegate(5, 1)

    ///不能出现负数
    var iteratorCount by IntDelegate(1, 1)

    fun setDirection(angle: Float) {
        val r = sin(Math.toRadians(angle.toDouble())).toFloat()
        directionX = r
        directionY = r
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("uDirection", directionX, directionY)
        put("uLoopCount", iteratorCount)
        putVec2("uTexOffset", 1.0f / width, 1.0f / height)
        put("uBlurSize", blurRadius)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;

            uniform vec2 uDirection;
            uniform int uLoopCount;
            uniform vec2 uTexOffset;
            uniform int uBlurSize;

            float rand(vec2 uv) {
                return fract(sin(dot(uv, vec2(12.9898, 78.233))) * 43578.5453);
            }

            void main() {
                vec4 t = vec4(0.0);
                vec2 d = uTexOffset * uBlurSize;
                float fc = float(uLoopCount);
                for (float i = 0.0; i < fc; i += 1.0) {
                    float r1 = clamp(rand(vTextureCoord * i) * 2.0 - 1.0, -d.x, d.x);
                    float r2 = clamp(rand(vTextureCoord * (i + uLoopCount)) * 2.0 - 1.0, -d.y, d.y);
                    t += texture2D(sTexture, vTextureCoord + vec2(r1, r2));
                }
                gl_FragColor = t / float(uLoopCount);
            }
        """.trimIndent()
    }
}