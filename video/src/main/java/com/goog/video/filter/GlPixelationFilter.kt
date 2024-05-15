package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

class GlPixelationFilter : GlFilter() {
    private val pixel = 1f
    private var imageWidthFactor = 1f / 720
    private var imageHeightFactor = 1f / 720

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        imageWidthFactor = 1f / width
        imageHeightFactor = 1f / height
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("imageWidthFactor", imageWidthFactor)
        put("imageHeightFactor", imageHeightFactor)
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
