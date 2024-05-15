package com.goog.video.gl

import android.graphics.SurfaceTexture
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import com.goog.video.filter.GlPreviewFilter

internal class ESurfaceTexture(texName: Int) : OnFrameAvailableListener {
    val texture: SurfaceTexture = SurfaceTexture(texName)

    private var frameAvailableListener: OnFrameAvailableListener? = null

    init {
        texture.setOnFrameAvailableListener(this)
    }

    fun setFrameAvailableListener(l: OnFrameAvailableListener?) {
        frameAvailableListener = l
    }

    val textureTarget: Int
        get() = GlPreviewFilter.GL_TEXTURE_EXTERNAL_OES

    fun updateTexImage() {
        texture.updateTexImage()
    }

    fun getTransformMatrix(mtx: FloatArray?) {
        texture.getTransformMatrix(mtx)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture) {
        frameAvailableListener?.onFrameAvailable(this.texture)
    }

    fun release() {
        texture.release()
    }
}
