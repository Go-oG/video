package com.goog.effect.filter.core

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.gl.GLConstant
import com.goog.effect.gl.GLConstant.VERTEX_SHADERS
import com.goog.effect.model.CallBy
import com.goog.effect.utils.EGLUtil
import com.goog.effect.utils.checkArgs

/**
 * 实现多纹理对象的过滤器
 */
@Deprecated("待完善")
abstract class GLMultiTextureFilter(val texCount: Int) : GLFilter() {
    companion object{
        private val TEXTURE_PART_LIST = listOf(
            InnerPart(GLES20.GL_TEXTURE3, GLConstant.K_UNIFORM_TEX2,3),
            InnerPart(GLES20.GL_TEXTURE4, GLConstant.K_UNIFORM_TEX3,4),
            InnerPart(GLES20.GL_TEXTURE5, GLConstant.K_UNIFORM_TEX4,5),
            InnerPart(GLES20.GL_TEXTURE6, GLConstant.K_UNIFORM_TEX5,6),
            InnerPart(GLES20.GL_TEXTURE7, GLConstant.K_UNIFORM_TEX6,7)
        )
    }

    private var mTextureList = listOf<TextureItem>()

    init {
        checkArgs(texCount in 2..6)
        val count = texCount - 1
        val list = mutableListOf<TextureItem>()
        for (i in 0..<count) {
            val part = TEXTURE_PART_LIST[i]
            val item = TextureItem(part.textureUnit, part.uniformName,part.offset)
            list.add(item)
        }
        this.mTextureList = list
    }

    override fun setFrameSize(width: Int, height: Int) {
        super.setFrameSize(width, height)
        for (item in mTextureList) {
            releaseBitmap(item.bitmap)
            item.bitmap = null
            val bitmap = createdBitmap()
            item.bitmap = bitmap
            item.texPoint = EGLUtil.loadOrUpdateTextureFromBitmap(bitmap,null,true)
        }
    }

    final override fun onDraw(fbo: FrameBufferObject?) {
        for (item in mTextureList) {
            item.activeTexture(program)
        }
        onDraw2(fbo)
    }

    open fun onDraw2(fbo: FrameBufferObject?) {}

    private fun createdBitmap(): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    protected fun releaseBitmap(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }

}

private class TextureItem(val textureUnit: Int, val uniformName: String, val offset:Int) {
    var bitmap: Bitmap? = null

    ///纹理指针
    var texPoint = 0

    fun activeTexture(program: Int) {
        val bitmap=this.bitmap
        if(bitmap!=null){
            GLES20.glActiveTexture(textureUnit)
            EGLUtil.loadOrUpdateTextureFromBitmap(bitmap,texPoint,false)
            val handle = GLES20.glGetUniformLocation(program, uniformName)
            GLES20.glUniform1i(handle, offset)
        }
    }
}


private class InnerPart(val textureUnit:Int,val uniformName:String,val offset:Int)