package com.goog.video.gl

import android.graphics.SurfaceTexture
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import android.view.Surface
import com.goog.video.filter.core.GLPreviewFilter

internal class ESurfaceTexture(texName: Int) : OnFrameAvailableListener {
    private val texture: SurfaceTexture = SurfaceTexture(texName)

    private var frameAvailableListener: OnFrameAvailableListener? = null

    init {
        texture.setOnFrameAvailableListener(this)
    }

    fun setFrameAvailableListener(l: OnFrameAvailableListener?) {
        frameAvailableListener = l
    }

    val textureTarget: Int
        get() = GLPreviewFilter.GL_TEXTURE_EXTERNAL_OES

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

    fun obtainSurface(): Surface {
        return Surface(texture)
    }
}
