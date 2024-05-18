package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

/**
 * Applies a pixelation effect to the image.
 */
class GLPixelationFilter : GLFilter() {
    var pixel by FloatDelegate(1f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("imageWidthFactor", 1f / width.toFloat())
        put("imageHeightFactor", 1f / height.toFloat())
        put("pixel", pixel)
    }

    override fun getFragmentShader(): String {
        return """
            precision highp float;
            varying highp vec2 vTextureCoord;
            uniform float imageWidthFactor;
            uniform float imageHeightFactor;
            uniform lowp sampler2D sTexture;
            uniform float pixel;
            void main() {
                vec2 uv = vTextureCoord.xy;
                float dx = pixel * imageWidthFactor;
                float dy = pixel * imageHeightFactor;
                vec2 coord = vec2(dx * floor(uv.x / dx), dy * floor(uv.y / dy));
                vec3 tc = texture2D(sTexture, coord).xyz;
                gl_FragColor = vec4(tc, 1.0);
            }
        """.trimIndent()
    }

}
