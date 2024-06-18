package com.goog.effect.filter

import android.graphics.Bitmap
import android.opengl.GLES20
import androidx.annotation.CallSuper
import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.CallBy
import com.goog.effect.utils.EGLUtil
import com.goog.effect.utils.EGLUtil.loadOrUpdateTextureFromBitmap
import com.goog.effect.utils.loadFilterFromAsset
import kotlin.properties.Delegates

class GLLookUpTableFilter(lutTexture: Bitmap?) : GLFilter() {
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

    override fun onInitialize(callBy: CallBy) {
        super.onInitialize(callBy)
        loadTexture()
    }

    @CallSuper
    override fun onDraw(fbo: FrameBufferObject?) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, hTex)
        put("lutTexture", 3)
    }

    private fun loadTexture() {
        val bitmap = lutTexture
        if (hTex == EGLUtil.NO_TEXTURE && bitmap != null) {
            hTex = loadOrUpdateTextureFromBitmap(bitmap, null)
        }
    }

    override fun release(callBy: CallBy) {
        super.release(callBy)
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
        hTex = loadOrUpdateTextureFromBitmap(lutTexture!!, EGLUtil.NO_TEXTURE, false)
    }

    override fun getFragmentShader(): String {
       return loadFilterFromAsset("filters/lookup_table.frag")
    }

}
