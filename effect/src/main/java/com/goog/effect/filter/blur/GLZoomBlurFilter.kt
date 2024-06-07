package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate

class GLZoomBlurFilter : GLCenterFilter() {
    var blurSize by FloatDelegate(0.5f, 0f,1f)
    var sampleCount by IntDelegate(1, 1)

    override fun onDraw(fbo: FrameBufferObject?) {
        putVec2("uCenter", centerX, centerY)
        put("uBlurSize", blurSize)
        put("uSampleCount", sampleCount)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;

            uniform int uSampleCount;
            uniform float uBlurSize;
            uniform vec2 uCenter;

            void main() {
                vec4 result = vec4(0);
                float fs = float(uSampleCount);
                for (int i = 0; i <= uSampleCount; i++) {
                    float q = float(i) / fs;
                    result += texture2D(sTexture, vTextureCoord + (uCenter - vTextureCoord) * q * uBlurSize) / fs;
                }
                gl_FragColor = result;
            }
        """.trimIndent()
    }
}
