package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLIteratorFilter
import com.goog.effect.filter.core.IterationInfo
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

/**
 * Kawase Blur — a high-performance blur for mobile.
 * Uses iterative dual-filtering with increasing sample offsets,
 * producing smooth results at a fraction of Gaussian blur's cost.
 */
class GLKawaseBlurFilter(
    iterationCount: Int = DEFAULT_ITERATIONS
) : GLIteratorFilter(iterationCount) {

    var blurRadius by FloatDelegate(DEFAULT_RADIUS, 0f)

    override fun onDraw2(fbo: FrameBufferObject?, info: IterationInfo) {
        val iteration = info.iterationIndex
        val offset = blurRadius * (iteration + 1f) / mIteratorCount.toFloat()
        put("uOffset", offset)
        putVec2("uPixelSize", pixelWidth, pixelHeight)
    }

    override fun getFragmentShader(): String = FRAGMENT_SHADER

    companion object {
        private const val DEFAULT_RADIUS = 3f
        private const val DEFAULT_ITERATIONS = 4

        private val FRAGMENT_SHADER = """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform vec2 uPixelSize;
            uniform float uOffset;

            void main() {
                vec2 offset = uPixelSize * uOffset;
                vec4 color = texture2D(sTexture, vTextureCoord + vec2(offset.x, offset.y));
                color += texture2D(sTexture, vTextureCoord + vec2(-offset.x, offset.y));
                color += texture2D(sTexture, vTextureCoord + vec2(offset.x, -offset.y));
                color += texture2D(sTexture, vTextureCoord + vec2(-offset.x, -offset.y));
                gl_FragColor = color * 0.25;
            }
        """.trimIndent()
    }
}
