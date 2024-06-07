package com.goog.effect.filter.core

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.gl.GLConstant
import com.goog.effect.gl.GLConstant.VERTEX_SHADERS
import com.goog.effect.utils.EGLUtil
import com.goog.effect.utils.checkArgs

/**
 * 实现多纹理对象的过滤器
 */
@Deprecated("待完善")
abstract class GLMultiTextureFilter(val texCount: Int) : GLFilter() {
    private val texturePosArray = arrayOf(
        GLES20.GL_TEXTURE1,
        GLES20.GL_TEXTURE2,
        GLES20.GL_TEXTURE3,
        GLES20.GL_TEXTURE4,
        GLES20.GL_TEXTURE5,
        GLES20.GL_TEXTURE6,
        GLES20.GL_TEXTURE7,
        GLES20.GL_TEXTURE8,
        GLES20.GL_TEXTURE9,
        GLES20.GL_TEXTURE10,
        GLES20.GL_TEXTURE11
    ).toIntArray()

    private var uniformTexArray = arrayOf(
        GLConstant.K_UNIFORM_TEX2,
        GLConstant.K_UNIFORM_TEX3,
        GLConstant.K_UNIFORM_TEX4,
        GLConstant.K_UNIFORM_TEX5,
        GLConstant.K_UNIFORM_TEX6,
        GLConstant.K_UNIFORM_TEX7,
        GLConstant.K_UNIFORM_TEX8,
        GLConstant.K_UNIFORM_TEX9,
        GLConstant.K_UNIFORM_TEX10
    )
    private var itemList = listOf<MultiItem>()

    init {
        checkArgs(texCount in 2..10)
        val count = texCount - 1
        val list = mutableListOf<MultiItem>()
        for (i in 2..count) {
            val item = MultiItem(i - 2, texturePosArray[i - 2], uniformTexArray[i - 2])
            list.add(item)
        }
        this.itemList = list
    }

    override fun initialize() {
        super.initialize()
        setTextures(itemList)
    }

    private fun setTextures(items: List<MultiItem>) {
        for (item in items) {
            releaseBitmap(item.bitmap)
            item.bitmap = null
            val bitmap = createdBitmap()
            item.bitmap = bitmap
            // 加载纹理并获得纹理ID
            val textureId = loadTexture(bitmap)
            item.texPoint = textureId
            //并没有激活纹理单元，只是绑定纹理ID

        }
    }

    final override fun onDraw(fbo: FrameBufferObject?) {
        for (item in itemList) {
            item.activeTexture(program)
        }
        onDraw2(fbo)
    }

    open fun onDraw2(fbo: FrameBufferObject?) {}

    private fun loadTexture(bitmap: Bitmap): Int {
        val args = IntArray(1)
        GLES20.glGenTextures(1, args, 0)
        if (args[0] != 0) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, args[0])
            EGLUtil.configTexture(args[0])
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        }
        return args[0]
    }

    private fun createdBitmap(): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    protected fun releaseBitmap(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }

    override fun getVertexShader(): String {
        return VERTEX_SHADERS[texCount - 1]
    }
}


internal class MultiItem(val listIndex: Int, val texPosOffset: Int, val uniformName: String) {
    var bitmap: Bitmap? = null

    ///纹理指针
    var texPoint = 0

    fun activeTexture(program: Int) {
        GLES20.glActiveTexture(texPosOffset)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texPoint)
        val handle = GLES20.glGetUniformLocation(program, uniformName)
        GLES20.glUniform1i(handle, listIndex + 1)

        val map = bitmap
        if (map != null && !map.isRecycled) {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, map, 0)
        }

    }


}