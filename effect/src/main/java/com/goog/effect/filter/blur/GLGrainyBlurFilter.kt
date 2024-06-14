package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate

// 粒状模糊
class GLGrainyBlurFilter : GLFilter() {

    var sampleCount by IntDelegate(6, 0)

    var blurSize by FloatDelegate(8f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uBlurSize", if (mEnable) blurSize else 0f)
        put("uSampleCount", if (mEnable) sampleCount else 0)
        putVec2("uResolution", width * 1f, height * 1f)
    }

    override fun getFragmentShader(): String {
        return """
               precision mediump float;
               varying highp vec2 vTextureCoord;
               uniform sampler2D sTexture;
               uniform vec2 uResolution;
               uniform float uBlurSize;
               uniform int uSampleCount;

               float rand(vec2 uv) {
                   return fract(sin(dot(uv, vec2(12.9898, 78.233))) * 43578.5453);
               }

               void main() {
                   if (uBlurSize <= 0.0 || uSampleCount <= 0) {
                       gl_FragColor = texture2D(sTexture, vTextureCoord);
                   } else {
                       vec4 t = vec4(0.0);
                       vec2 uv = vTextureCoord;
                       vec2 texel = 1.0 / uResolution.xy;
                       vec2 d = texel * uBlurSize;
                       for (int i = 0; i < uSampleCount; i++) {
                           float r1 = clamp(rand(uv * float(i)) * 2.0 - 1.0, -d.x, d.x);
                           float r2 = clamp(rand(uv * float(i + uSampleCount)) * 2.0 - 1.0, -d.y, d.y);
                           t += texture2D(sTexture, uv + vec2(r1, r2));
                       }
                       t /= float(uSampleCount);
                       gl_FragColor = t;
                   }
               }
        """.trimIndent()
    }

}