package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

/**
 * Applies a pixelation effect to the image.
 */
class GlPixelationFilter(pixel: Float = 1f, widthFactor: Float = 0.0014F, heightFactor: Float = 0.0014f) : GlFilter() {
    private var pixel = 1f
    private var imageWidthFactor = 1f / 720
    private var imageHeightFactor = 1f / 720

    //TODO 参数范围待确认
    init {
        setPixel(pixel)
        setImageWidthFactor(widthFactor)
        setImageHeightFactor(heightFactor)
    }

    fun setPixel(v: Float) {
        pixel = v
    }

    fun setImageWidthFactor(v: Float) {
        imageWidthFactor = v
    }

    fun setImageHeightFactor(v: Float) {
        imageHeightFactor = v
    }


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
