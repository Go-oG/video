package com.goog.effect.filter.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.opengl.GLES20
import android.opengl.GLUtils
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.CallBy
import com.goog.effect.utils.EGLUtil

abstract class GLOverlayFilter : GLFilter() {
    private val textures = IntArray(1)
    private var bitmap: Bitmap? = null

    private fun createBitmap() {
        releaseBitmap(bitmap)
        bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888)
    }

    override fun onInitialize(callBy: CallBy) {
        super.onInitialize(callBy)
        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        EGLUtil.configTexture(GLES20.GL_TEXTURE_2D, true, true, true)
        createBitmap()
    }

    override fun onDraw(fbo: FrameBufferObject?) {
        if (this.bitmap == null) {
            createBitmap()
        }
        var bitmap = this.bitmap!!
        if (bitmap.width != width || bitmap.height != height) {
            createBitmap()
            bitmap = this.bitmap!!
        }
        bitmap.eraseColor(Color.argb(0, 0, 0, 0))
        val bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.scale(1f, -1f, bitmapCanvas.width / 2f, bitmapCanvas.height / 2f)
        drawCanvas(bitmapCanvas)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        if (!bitmap.isRecycled) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0)
        }

        put("oTexture", 3)

    }

    protected abstract fun drawCanvas(canvas: Canvas)

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform lowp sampler2D oTexture;
            void main() {
                lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                lowp vec4 textureColor2 = texture2D(oTexture, vTextureCoord);
                gl_FragColor = mix(textureColor, textureColor2, textureColor2.a); 

        """.trimIndent()
    }

    protected fun releaseBitmap(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }


}
