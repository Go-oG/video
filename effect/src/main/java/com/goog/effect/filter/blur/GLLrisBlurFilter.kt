package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate
import com.goog.effect.model.IntDelegate

/**
 * Iris / aperture blur — simulates polygonal bokeh shapes produced by
 * camera aperture blades (hexagon, octagon, etc.).
 */
class GLLrisBlurFilter : GLFilter() {

    var blurSize by FloatDelegate(8f, 0f)

    /** Number of aperture blades (3 = triangle, 6 = hexagon, 8 = octagon) */
    var bladeCount by IntDelegate(6, 3, 12)

    /** Rotation of the aperture shape in radians */
    var rotation by FloatDelegate(0f, 0f, 6.283f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uBlurSize", if (mEnable) blurSize else 0f)
        put("uBladeCount", bladeCount)
        put("uRotation", rotation)
        putVec2("uTexelSize", pixelWidth, pixelHeight)
    }

    override fun getFragmentShader(): String = FRAGMENT_SHADER

    companion object {
        private val FRAGMENT_SHADER = """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform float uBlurSize;
            uniform int uBladeCount;
            uniform float uRotation;
            uniform vec2 uTexelSize;

            void main() {
                if (uBlurSize <= 0.0 || uBladeCount < 3) {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                    return;
                }

                vec4 color = vec4(0.0);
                float totalWeight = 0.0;

                int rings = int(ceil(uBlurSize));
                if (rings < 1) rings = 1;
                float bladeAngle = 2.0 * 3.14159265 / float(uBladeCount);

                for (int r = 0; r < rings; r++) {
                    float ringRadius = float(r + 1) * uBlurSize / float(rings);
                    for (int b = 0; b < uBladeCount; b++) {
                        float angle = float(b) * bladeAngle + uRotation;
                        vec2 offset = vec2(cos(angle), sin(angle)) * ringRadius * uTexelSize;
                        vec4 sampleColor = texture2D(sTexture, vTextureCoord + offset);
                        float weight = 1.0 / (1.0 + ringRadius * 0.5);
                        color += sampleColor * weight;
                        totalWeight += weight;
                    }

                    // Sub-samples between blades for smoother result
                    for (int b = 0; b < uBladeCount; b++) {
                        float angle = (float(b) + 0.5) * bladeAngle + uRotation;
                        vec2 offset = vec2(cos(angle), sin(angle)) * ringRadius * 0.7 * uTexelSize;
                        vec4 sampleColor = texture2D(sTexture, vTextureCoord + offset);
                        float weight = 0.5 / (1.0 + ringRadius * 0.5);
                        color += sampleColor * weight;
                        totalWeight += weight;
                    }
                }

                color += texture2D(sTexture, vTextureCoord);
                totalWeight += 1.0;

                gl_FragColor = color / totalWeight;
            }
        """.trimIndent()
    }
}
