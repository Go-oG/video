package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.IntDelegate

///TODO 暂不可用(显示很奇怪)
class GLMotionBlurFilter : GLFilter() {

    var hSamples by IntDelegate(100, 1)
    var mSamples by IntDelegate(8, 1)

    private var playTime = 0L
    override fun initialize() {
        super.initialize()
        playTime = System.currentTimeMillis()
    }
    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        playTime = System.currentTimeMillis()
    }
    override fun release() {
        super.release()
        playTime = 0L
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("hSamples", hSamples)
        put("mSamples", mSamples)
        putVec2("uResolution", width.toFloat(), height.toFloat())
        put("iTime", (System.currentTimeMillis() - playTime) / 1000f)
    }

    override fun getFragmentShader(): String {
      return """
          precision mediump float;
          varying highp vec2 vTextureCoord;
          uniform sampler2D sTexture;

          uniform lowp int hSamples;
          uniform lowp int mSamples;
          uniform float iTime;
          uniform vec2 uResolution;

          const vec2 uc1 = vec2(0.03, 0.015);
          const vec3 uc2 = vec3(0.28, 0.392, 0.49);
          const vec3 uc3 = vec3(16.0, 9.0, 3.0);

          void main() {
              vec2 p = (2.0 * vTextureCoord.xy - uResolution.xy) / uResolution.y;
              float t = 12.25 + iTime;
              float an = 0.2 * sin(-0.5 * t);
              p = mat2(cos(an), -sin(an), sin(an), cos(an)) * p;

              float fmSamples = float(mSamples);
              float fhSamples = float(hSamples);
              float ra = texture2D(sTexture, vTextureCoord).x;
              vec3 tot = vec3(0.0);

              for (float j = 0.0; j < fmSamples; j += 1.0) {
                  float time = t + 0.02083 * (j + ra) / fmSamples;
                  vec2 offset = time * uc1;

                  vec3 uv;
                  for (float i = 0.0; i < fhSamples; i += 1.0f) {
                      uv.z = (i + ra) / (fhSamples - 1.0);
                      uv.xy = offset + vec2(p.x, 1.0) / abs(p.y) * (0.001 + 0.0125 * uv.z) * 0.5 + sign(p.y) * 0.1;
                      if (texture2D(sTexture, uv.xy).x < uv.z) {
                          break;
                      }
                  }

                  vec2 uv2 = offset + vec2(p.x - 0.04, 1.0) / abs(p.y) * (0.001 + 0.0125 * uv.z) * 0.5 + sign(p.y) * 0.1;
                  float dif = clamp(texture2D(sTexture, uv.xy).x - texture2D(sTexture, uv2.xy).x, 0.0, 1.0);
                  vec3 col = vec3(2.0);
                  col *= 0.2 + 0.9 * texture2D(sTexture, 24.0 * uv.xy, 0.0).xyz;
                  col *= 0.5 + 0.5 * texture2D(sTexture, 128.0 * uv.xy, 0.0).xyz;
                  col *= 1.0 - 1.0 * uv.z;
                  col *= uc2 + uc3 * dif;
                  col *= clamp(3.0 * abs(p.y) - 0.6 * uv.z + 0.1, 0.0, 2.0);
                  tot += col;
              }
              tot /= float(mSamples);
              gl_FragColor = vec4(tot * smoothstep(0.0, 2.0, iTime), 1.0);
          }
      """.trimIndent()
    }
}