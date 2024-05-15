package com.goog.video.filter

import android.graphics.Bitmap
import android.opengl.GLES20
import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.EGLUtil
import com.goog.video.utils.EGLUtil.loadTexture
import kotlin.properties.Delegates

class GlLookUpTableFilter(lutTexture: Bitmap?) : GlFilter() {
    private var lutTexture: Bitmap? = null
    private var hTex by Delegates.notNull<Int>()

    init {
        hTex = EGLUtil.NO_TEXTURE
        setLutTexture(lutTexture)
    }

    fun setLutTexture(lutTexture: Bitmap?) {
        releaseLutBitmap()
        this.lutTexture = lutTexture
    }

    override fun setup() {
        super.setup()
        loadTexture()
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, hTex)
        put("lutTexture",3)
    }

    private fun loadTexture() {
        val bitmap = lutTexture
        if (hTex == EGLUtil.NO_TEXTURE && bitmap != null) {
            hTex = loadTexture(bitmap, EGLUtil.NO_TEXTURE, false)
        }
    }

    override fun release() {
        super.release()
        releaseLutBitmap()
    }

    private fun releaseLutBitmap() {
        lutTexture?.let {
            if (!it.isRecycled) {
                it.recycle()
            }
        }
        lutTexture = null
    }

    fun reset() {
        hTex = EGLUtil.NO_TEXTURE
        hTex = loadTexture(lutTexture!!, EGLUtil.NO_TEXTURE, false)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            uniform mediump sampler2D lutTexture;
            uniform lowp sampler2D sTexture;
            varying highp vec2 vTextureCoord;
            vec4 sampleAs3DTexture(vec3 uv) {
                float width = 16.;
                float sliceSize = 1.0 / width;
                float slicePixelSize = sliceSize / width;
                float sliceInnerSize = slicePixelSize * (width - 1.0);
                float zSlice0 = min(floor(uv.z * width), width - 1.0);
                float zSlice1 = min(zSlice0 + 1.0, width - 1.0);
                float xOffset = slicePixelSize * 0.5 + uv.x * sliceInnerSize;
                float s0 = xOffset + (zSlice0 * sliceSize);
                float s1 = xOffset + (zSlice1 * sliceSize);
                vec4 slice0Color = texture2D(lutTexture, vec2(s0, uv.y));
                vec4 slice1Color = texture2D(lutTexture, vec2(s1, uv.y));
                float zOffset = mod(uv.z * width, 1.0);
                vec4 result = mix(slice0Color, slice1Color, zOffset);
                return result;
            }
            void main() {
                vec4 pixel = texture2D(sTexture, vTextureCoord);
                vec4 gradedPixel = sampleAs3DTexture(pixel.rgb);
                gradedPixel.a = pixel.a;
                pixel = gradedPixel;
                gl_FragColor = pixel;
            }
        """.trimIndent()
    }

}
