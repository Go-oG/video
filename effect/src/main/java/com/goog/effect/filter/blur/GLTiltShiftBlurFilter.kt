package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

/**
 * Tilt-shift blur — simulates the miniature diorama effect produced by
 * tilt-shift lenses. The image is sharp in a horizontal band and
 * progressively blurred toward the top and bottom edges.
 */
class GLTiltShiftBlurFilter : GLFilter() {

    /** Overall blur intensity */
    var blurSize by FloatDelegate(10f, 0f)

    /** Center position of the in-focus band (0 = top, 0.5 = center, 1 = bottom) */
    var focusPosition by FloatDelegate(0.5f, 0f, 1f)

    /** Width of the in-focus band (0 = none, 1 = full screen) */
    var focusRange by FloatDelegate(0.25f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uBlurSize", if (mEnable) blurSize else 0f)
        put("uFocusPosition", focusPosition)
        put("uFocusRange", focusRange)
        putVec2("uTexelSize", pixelWidth, pixelHeight)
    }

    override fun getFragmentShader(): String = FRAGMENT_SHADER

    companion object {
        private val FRAGMENT_SHADER = """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform float uBlurSize;
            uniform float uFocusPosition;
            uniform float uFocusRange;
            uniform vec2 uTexelSize;

            void main() {
                if (uBlurSize <= 0.0) {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                    return;
                }

                // Calculate distance from focus center (in UV space)
                float distFromFocus = abs(vTextureCoord.y - uFocusPosition);

                // Normalize distance: 0 at focus center, 1 at edges of focus range
                float halfRange = uFocusRange * 0.5;
                float normalizedDist;
                if (distFromFocus <= halfRange) {
                    // Inside focus band — sharp
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                    return;
                } else {
                    // Outside focus band — progressive blur
                    normalizedDist = (distFromFocus - halfRange) / (0.5 - halfRange);
                    normalizedDist = clamp(normalizedDist, 0.0, 1.0);
                }

                // Smooth falloff for blur radius
                float blurRadius = uBlurSize * normalizedDist;

                vec4 color = vec4(0.0);
                float totalWeight = 0.0;
                float sampleCount = blurRadius * 3.0;
                int samples = int(ceil(sampleCount));
                if (samples < 1) samples = 1;

                for (int i = -samples; i <= samples; i++) {
                    float weight = 1.0 - abs(float(i)) / float(samples + 1);
                    vec2 offset = vec2(0.0, float(i) * uTexelSize.y * blurRadius);
                    color += texture2D(sTexture, vTextureCoord + offset) * weight;
                    totalWeight += weight;
                }

                gl_FragColor = color / totalWeight;
            }
        """.trimIndent()
    }
}
