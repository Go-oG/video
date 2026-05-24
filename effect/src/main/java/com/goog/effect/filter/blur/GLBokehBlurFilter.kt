package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

/**
 * Bokeh blur — simulates out-of-focus light discs characteristic of
 * wide-aperture lenses. Uses hexagonal sampling with brightness thresholding
 * to create visible bokeh spots in highlight areas.
 */
class GLBokehBlurFilter : GLFilter() {

    var blurSize by FloatDelegate(12f, 0f)
    var bokehRadius by FloatDelegate(0.5f, 0f, 2f)
    var brightness by FloatDelegate(1.2f, 0.5f, 3f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uBlurSize", if (mEnable) blurSize else 0f)
        put("uBokehRadius", bokehRadius)
        put("uBrightness", brightness)
        putVec2("uTexelSize", pixelWidth, pixelHeight)
    }

    override fun getFragmentShader(): String = FRAGMENT_SHADER

    companion object {
        private val FRAGMENT_SHADER = """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            uniform float uBlurSize;
            uniform float uBokehRadius;
            uniform float uBrightness;
            uniform vec2 uTexelSize;

            void main() {
                if (uBlurSize <= 0.0) {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                    return;
                }

                vec4 color = vec4(0.0);
                float totalWeight = 0.0;

                // Hexagonal bokeh sampling kernel
                const float SQRT3 = 1.7320508;
                vec2 hexOffsets[6];
                hexOffsets[0] = vec2(0.0, 1.0);
                hexOffsets[1] = vec2(SQRT3 * 0.5, 0.5);
                hexOffsets[2] = vec2(SQRT3 * 0.5, -0.5);
                hexOffsets[3] = vec2(0.0, -1.0);
                hexOffsets[4] = vec2(-SQRT3 * 0.5, -0.5);
                hexOffsets[5] = vec2(-SQRT3 * 0.5, 0.5);

                float radius = uBlurSize * uBokehRadius;
                int rings = int(ceil(radius));
                if (rings < 1) rings = 1;

                for (int r = 0; r < rings; r++) {
                    float ringRadius = float(r + 1) * radius / float(rings);
                    int samples = 6 * (r + 1);
                    for (int s = 0; s < samples; s++) {
                        float angle = 2.0 * 3.14159265 * float(s) / float(samples);
                        vec2 offset = vec2(cos(angle), sin(angle)) * ringRadius * uTexelSize;
                        vec4 sampleColor = texture2D(sTexture, vTextureCoord + offset);
                        float lum = dot(sampleColor.rgb, vec3(0.299, 0.587, 0.114));
                        float weight = 1.0;
                        if (lum > 0.6) {
                            weight = uBrightness;
                        }
                        color += sampleColor * weight;
                        totalWeight += weight;
                    }
                }

                // center sample
                vec4 centerColor = texture2D(sTexture, vTextureCoord);
                color += centerColor * 0.5;
                totalWeight += 0.5;

                gl_FragColor = color / totalWeight;
            }
        """.trimIndent()
    }
}
