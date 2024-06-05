package com.goog.video.utils

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLException
import android.opengl.GLUtils
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object EGLUtil {
    const val NO_TEXTURE: Int = -1
    private const val FLOAT_SIZE_BYTES = Float.SIZE_BYTES

    /**
     * 加载shader资源
     * @param isFragmentShader true 为片元着色器，false 为顶点着色器
     */
    @JvmStatic
    fun loadShader(shaderCode: String, isFragmentShader: Boolean): Int {
        val compiled = IntArray(1)
        val shaderType = if (isFragmentShader) GLES20.GL_FRAGMENT_SHADER else GLES20.GL_VERTEX_SHADER
        val shader = GLES20.glCreateShader(shaderType)

        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.d("Load Shader Failed", "$compiled ${GLES20.glGetShaderInfoLog(shader)}")
            return 0
        }
        return shader
    }

    /**
     * 创建Program 并链接shader
     * shader 对应的索引值由 loadShader 返回值确定
     *
     */
    @JvmStatic
    @Throws(GLException::class)
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
     * 返回对应的纹理指针
     */
    fun createTexture(): Int {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        return textures[0]
    }

    /**
     * 初始化 texture(sampler2D) 相关参数
     * 主要是填充模式和过滤模式
     */
    fun setupTexture(target: Int, magUseUseLinear: Boolean = true, minUseLinear: Boolean = true,
        wrapUseToEdge: Boolean = true) {
        ///GL_TEXTURE_MAG_FILTER和GL_TEXTURE_MIN_FILTER 设置纹理过滤参数作用是当纹理渲染时比原理的纹理小或者大时要如何处理，
        // GL_LINEAR是线性处理方式，展示效果更平滑；
        // GL_NEAREST选择与最近的像素，展示效果有锯齿感。
        val mag = if (magUseUseLinear) GLES20.GL_LINEAR else GLES20.GL_NEAREST
        val min = if (minUseLinear) GLES20.GL_LINEAR else GLES20.GL_NEAREST

        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MAG_FILTER, mag)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MIN_FILTER, min)

        //GL_TEXTURE_WRAP_T与GL_TEXTURE_WRAP_S是纹理坐标超出纹理范围的处理参数
        //GL_CLAMP_TO_EDGE 已填充方式进行处理
        //GL_REPEAT 以重复方式进行处理
        val warp = if (wrapUseToEdge) GLES20.GL_CLAMP_TO_EDGE else GLES20.GL_REPEAT
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_S, warp)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_T, warp)
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
            .allocateDirect(data.size * FLOAT_SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        buffer.put(data).position(0)
        return buffer
    }

    fun updateBufferData(bufferName: Int, data: FloatBuffer) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferName)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.capacity() * FLOAT_SIZE_BYTES, data, GLES20.GL_STATIC_DRAW)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    @JvmStatic
    fun loadTexture(img: Bitmap, usedTexId: Int, recycle: Boolean): Int {
        val textures = IntArray(1)
        if (usedTexId == NO_TEXTURE) {
            GLES20.glGenTextures(1, textures, 0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0)

        } else {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId)
            GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, img)
            textures[0] = usedTexId
        }

        if (recycle) {
            img.recycle()
        }
        return textures[0]
    }

    fun checkEglError(operation: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            throw RuntimeException("$operation: glError $error")
        }
    }

}
