package com.goog.effect.utils

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import com.goog.effect.model.GLVersion
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object EGLUtil {
    const val NO_TEXTURE: Int = -1

    /**
     * 加载shader资源
     * @param isFragmentShader true 为片元着色器，false 为顶点着色器
     */
    @JvmStatic
    fun loadShader(shaderCode: String, isFragmentShader: Boolean): Int {
        val shaderType = if (isFragmentShader) GLES20.GL_FRAGMENT_SHADER else GLES20.GL_VERTEX_SHADER
        val shader = GLES20.glCreateShader(shaderType)
        if (shader == 0) {
            throw RuntimeException("Could not create shader")
        }
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            val s = "Load Shader Failed $compiled ${GLES20.glGetShaderInfoLog(shader)}"
            throw RuntimeException(s)
        }
        return shader
    }

    /**
     * 创建Program 并链接shader
     * shader 对应的索引值由 loadShader 返回值确定
     */
    @JvmStatic
    fun createProgram(vertexShader: Int, frameShader: Int): Int {
        val program = GLES20.glCreateProgram()
        if (program == 0) {
            throw RuntimeException("Could not create program")
        }
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, frameShader)
        GLES20.glLinkProgram(program)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            GLES20.glDeleteProgram(program)
            throw RuntimeException("Could not link program")
        }
        return program
    }

    /**
     * 创建一个纹理并绑定
     * 返回对应的纹理指针 如果失败则返回0
     */
    fun createAndBindTexture(textureType: Int = GLES20.GL_TEXTURE_2D): Int {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        if (textures[0] != 0) {
            GLES20.glBindTexture(textureType, textures[0])
        }
        return textures[0]
    }

    /**
     * 初始化 texture(sampler2D) 相关参数
     * 主要是填充模式和过滤模式
     * @param  textureType 参数是指目标纹理的类型
     *          常见值有 GLES20.TEXTURE_2D 、GLES20.TEXTURE_EXTERNAL_OES、GLES20.GL_TEXTURE_CUBE_MAP
     */
    fun configTexture(textureType: Int, filterMaxUseLinear: Boolean = true,filterMinUseLinear:Boolean=false, wrapUseToEdge: Boolean = true) {
        ///GL_TEXTURE_MAG_FILTER和GL_TEXTURE_MIN_FILTER 设置纹理过滤参数作用是当纹理渲染时比原理的纹理小或者大时要如何处理，
        // GL_LINEAR是线性处理方式，展示效果更平滑；
        // GL_NEAREST选择与最近的像素，展示效果有锯齿感。
        val filter = if (filterMaxUseLinear) GLES20.GL_LINEAR else GLES20.GL_NEAREST
        val filter2 = if (filterMinUseLinear) GLES20.GL_LINEAR else GLES20.GL_NEAREST

        GLES20.glTexParameteri(textureType, GLES20.GL_TEXTURE_MAG_FILTER, filter)
        GLES20.glTexParameteri(textureType, GLES20.GL_TEXTURE_MIN_FILTER, filter2)

        //GL_TEXTURE_WRAP_T与GL_TEXTURE_WRAP_S是纹理坐标超出纹理范围的处理参数
        //GL_CLAMP_TO_EDGE 已填充方式进行处理
        //GL_REPEAT 以重复方式进行处理
        val warp = if (wrapUseToEdge) GLES20.GL_CLAMP_TO_EDGE else GLES20.GL_REPEAT
        GLES20.glTexParameteri(textureType, GLES20.GL_TEXTURE_WRAP_S, warp)
        GLES20.glTexParameteri(textureType, GLES20.GL_TEXTURE_WRAP_T, warp)
    }

    /**
     * 从Bitmap中创建或者更新纹理对象
     */
    @JvmStatic
    fun loadOrUpdateTextureFromBitmap(img: Bitmap, textureId: Int?, unBindOnEnd: Boolean = true): Int {
        val textures = IntArray(1)
        if (textureId == null || textureId == NO_TEXTURE || textureId == 0) {
            textures[0] = createAndBindTexture()
            configTexture(GLES20.GL_TEXTURE_2D, true,true, true)
            //将Bitmap 数据加载到Texture中
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0)
            if (unBindOnEnd) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            }
        } else {
            textures[0] = textureId
            ///绑定纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            ///更新Bitmap 数据
            GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, img)
            if (unBindOnEnd) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            }
        }
        return textures[0]
    }

    @JvmStatic
    fun createBuffer(data: FloatArray): Int {
        return createBuffer(toFloatBuffer(data))
    }

    fun createBuffer(data: FloatBuffer): Int {
        val buffers = IntArray(1)
        GLES20.glGenBuffers(buffers.size, buffers, 0)
        updateBufferData(buffers[0], data)
        return buffers[0]
    }

    fun toFloatBuffer(data: FloatArray): FloatBuffer {
        val buffer = ByteBuffer
            .allocateDirect(data.size * Float.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        buffer.put(data).position(0)
        return buffer
    }

    /**
     * 更新Buffer Data
     */
    fun updateBufferData(bufferName: Int, data: FloatBuffer) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferName)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.capacity() * Float.SIZE_BYTES, data, GLES20.GL_STATIC_DRAW)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    /**
     * 检查是否支持Open GL ES 3.0
     */
    fun isSupportGLES30(): Boolean {
        try {
            val manager = ContextUtil.getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val config = manager.deviceConfigurationInfo
            if (config.reqGlEsVersion >= 0x30000) {
                return true
            }
            val versionString = GLES20.glGetString(GLES20.GL_VERSION);
            return versionString != null && versionString.startsWith("OpenGL ES 3.", ignoreCase = true)

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * 获取当前环境的GLES 版本
     */
    fun getCurrentGLVersion(): GLVersion {
        val version = GLES20.glGetString(GLES20.GL_VERSION)
        if (version != null && version.startsWith("OpenGL ES 3.", ignoreCase = true)) {
            return GLVersion.V30
        }
        return GLVersion.V20
    }

    fun checkEglError(operation: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            throw RuntimeException("$operation: glError $error")
        }
    }


}
