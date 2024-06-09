package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

class GLPixelateFilter : GLFilter() {

    var pixelWidthFactor by FloatDelegate(0.02f,0f,1f)

    var aspectRatio by FloatDelegate(1f,0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("uAspectRatio", aspectRatio)
        put("uWidthFactor", pixelWidthFactor)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;

            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;

            uniform float uWidthFactor;
            uniform float uAspectRatio;

            void main() {
                vec2 sampleDivisor = vec2(uWidthFactor, uWidthFactor / uAspectRatio);
                vec2 samplePos = vTextureCoord - mod(vTextureCoord, sampleDivisor) + 0.5 * sampleDivisor;
                gl_FragColor = texture2D(sTexture, samplePos);
            }

        """.trimIndent()
    }
}