package com.goog.effect.gl

import android.graphics.SurfaceTexture
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.view.Surface
import com.goog.effect.filter.core.GLPreviewFilter

internal class ESurfaceTexture(texName: Int) : OnFrameAvailableListener {
    private val texture: SurfaceTexture = SurfaceTexture(texName)
    private var frameAvailableListener: OnFrameAvailableListener? = null

    init {
        texture.setOnFrameAvailableListener(this)
    }

    fun setFrameAvailableListener(l: OnFrameAvailableListener?) {
        frameAvailableListener = l
    }

    val textureTarget: Int get() =GLES11Ext.GL_TEXTURE_EXTERNAL_OES

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
