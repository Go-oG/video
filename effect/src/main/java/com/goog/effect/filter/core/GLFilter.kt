package com.goog.effect.filter.core

import android.opengl.GLES20
import androidx.annotation.CallSuper
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.gl.GLConstant.DEF_FRAGMENT_SHADER
import com.goog.effect.gl.GLConstant.DEF_VERTEX_SHADER
import com.goog.effect.gl.GLConstant.K_ATTR_COORD
import com.goog.effect.gl.GLConstant.K_ATTR_POSITION
import com.goog.effect.gl.GLConstant.K_UNIFORM_TEX
import com.goog.effect.model.FColor
import com.goog.effect.model.FColor4
import com.goog.effect.utils.EGLUtil.createBuffer
import com.goog.effect.utils.EGLUtil.createProgram
import com.goog.effect.utils.EGLUtil.loadShader
import com.goog.effect.utils.checkArgs

open class GLFilter {
    protected var program = 0

    private var vertexShader = 0

    private var fragmentShader = 0

    protected var vertexBufferName: Int = 0
        private set

    private val handleMap = HashMap<String, Int>()

    protected var width = 0

    protected var height = 0

    open fun initialize() {
        release()
        onInitialize()
    }

    protected open fun onInitialize() {
        vertexShader = loadShader(getVertexShader(), false)
        fragmentShader = loadShader(getFragmentShader(), true)
        program = createProgram(vertexShader, fragmentShader)
        vertexBufferName = createBuffer(VERTICES_DATA)
    }

    @CallSuper
    open fun setFrameSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    open fun release() {
        GLES20.glDeleteProgram(program)
        program = 0

        GLES20.glDeleteShader(vertexShader)
        vertexShader = 0

        GLES20.glDeleteShader(fragmentShader)
        fragmentShader = 0

        GLES20.glDeleteBuffers(1, intArrayOf(vertexBufferName), 0)
        vertexBufferName = 0

        handleMap.clear()
    }

    open fun draw(texName: Int, fbo: FrameBufferObject?) {
        useProgram(texName, fbo)
        ///绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferName)
        ///设置顶点Position
        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_POSITION), VERTICES_DATA_POS_SIZE, GLES20.GL_FLOAT,
            false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET)
        ///设置坐标
        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_COORD))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_COORD), VERTICES_DATA_UV_SIZE, GLES20.GL_FLOAT,
            false, VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET)
        //激活并绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName)
        //设置纹理单元(sampler2D)
        GLES20.glUniform1i(getHandle(K_UNIFORM_TEX), 0)

        onDraw(fbo)

        ///绘制顶点数据
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        onDrawEndHook(texName, fbo)
        ///禁用顶点着色器相关属性
        GLES20.glDisableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glDisableVertexAttribArray(getHandle(K_ATTR_COORD))
        //解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

    }

    protected open fun onDraw(fbo: FrameBufferObject?) {

    }

    protected open fun onDrawEndHook(texName: Int, fbo: FrameBufferObject?) {}

    protected open fun useProgram(texName: Int, fbo: FrameBufferObject?) {
        GLES20.glUseProgram(program)
    }

    ///subclass overwrite this
    protected open fun getVertexShader(): String {
        return DEF_VERTEX_SHADER
    }

    protected open fun getFragmentShader(): String {
        return DEF_FRAGMENT_SHADER
    }

    protected fun putTextureSize() {
        put("texelWidth", width.toFloat())
        put("texelHeight", height.toFloat())
    }

    protected fun put(name: String, value: Int) {
        GLES20.glUniform1i(getHandle(name), value)
    }

    protected fun put(name: String, value: Float) {
        GLES20.glUniform1f(getHandle(name), value)
    }

    protected fun putColor(name: String, color: FColor) {
        putVec3(name, color.r, color.g, color.b)
    }

    protected fun putColor(name: String, color: FColor4) {
        putVec4(name, color.r, color.g, color.b, color.a)
    }

    protected fun putVec2(name: String, v1: Float, v2: Float) {
        GLES20.glUniform2f(getHandle(name), v1, v2)
    }

    protected fun putVec2(name: String, v1: Int, v2: Int) {
        GLES20.glUniform2i(getHandle(name), v1, v2)
    }

    protected fun putVec3(name: String, v1: Float, v2: Float, v3: Float) {
        GLES20.glUniform3f(getHandle(name), v1, v2, v3)
    }

    protected fun putVec3(name: String, v1: Int, v2: Int, v3: Int) {
        GLES20.glUniform3i(getHandle(name), v1, v2, v3)
    }

    protected fun putVec4(name: String, v1: Float, v2: Float, v3: Float, v4: Float) {
        GLES20.glUniform4f(getHandle(name), v1, v2, v3, v4)
    }

    protected fun putVec4(name: String, v1: Int, v2: Int, v3: Int, v4: Int) {
        GLES20.glUniform4i(getHandle(name), v1, v2, v3, v4)
    }

    protected fun putArray(name: String, value: FloatArray) {
        GLES20.glUniform1fv(getHandle(name), value.size, value, 0)
    }

    protected fun putArray(name: String, value: IntArray) {
        GLES20.glUniform1iv(getHandle(name), value.size, value, 0)
    }

    protected fun putVec2Array(name: String, value: FloatArray, count: Int) {
        checkArgs(value.size == count * 2, "value.size must be ${count * 2}")
        GLES20.glUniform2fv(getHandle(name), count, value, 0)
    }

    protected fun putVec2Array(name: String, value: IntArray, count: Int) {
        checkArgs(value.size == count * 2, "value.size must be ${count * 2}")
        GLES20.glUniform2iv(getHandle(name), count, value, 0)
    }

    protected fun putVec3Array(name: String, value: FloatArray, count: Int) {
        checkArgs(value.size == count * 3, "value.size must be ${count * 3}")
        GLES20.glUniform3fv(getHandle(name), count, value, 0)
    }

    protected fun putVec3Array(name: String, value: IntArray, count: Int) {
        checkArgs(value.size == count * 3, "value.size must be ${count * 3}")
        GLES20.glUniform3iv(getHandle(name), count, value, 0)
    }

    protected fun putVec4Array(name: String, value: FloatArray, count: Int) {
        checkArgs(value.size == count * 4, "value.size must be ${count * 4}")
        GLES20.glUniform4fv(getHandle(name), count, value, 0)
    }

    protected fun putVec4Array(name: String, value: IntArray, count: Int) {
        checkArgs(value.size == count * 4, "value.size must be ${count * 4}")
        GLES20.glUniform4iv(getHandle(name), count, value, 0)
    }

    protected fun putMatrix4(name: String, value: FloatArray, offset: Int = 0) {
        checkArgs(value.size == 16, "value.size must be 16")
        GLES20.glUniformMatrix4fv(getHandle(name), 1, false, value, offset)
    }

    protected fun putMatrix3(name: String, value: FloatArray, offset: Int = 0) {
        checkArgs(value.size == 9, "value.size must be 9")
        GLES20.glUniformMatrix3fv(getHandle(name), 1, false, value, offset)
    }

    protected fun putMatrix2(name: String, value: FloatArray, offset: Int = 0) {
        checkArgs(value.size == 4, "value.size must be 4")
        GLES20.glUniformMatrix4fv(getHandle(name), 1, false, value, offset)
    }

    protected fun putMatrix2Array(name: String, value: FloatArray, count: Int) {
        checkArgs(value.size == count * 4, "value.size must be ${count * 4}")
        GLES20.glUniformMatrix2fv(getHandle(name), count, false, value, 0)
    }

    protected fun putMatrix3Array(name: String, value: FloatArray, count: Int) {
        checkArgs(value.size == count * 9, "value.size must be ${count * 9}")
        GLES20.glUniformMatrix3fv(getHandle(name), count, false, value, 0)
    }

    protected fun putMatrix4Array(name: String, value: FloatArray, count: Int) {
        checkArgs(value.size == count * 16, "value.size must be ${count * 16}")
        GLES20.glUniformMatrix4fv(getHandle(name), count, false, value, 0)
    }

    protected fun getHandle(name: String): Int {
        val value = handleMap[name]
        if (value != null) {
            return value
        }

        var location = GLES20.glGetAttribLocation(program, name)
        if (location == -1) {
            location = GLES20.glGetUniformLocation(program, name)
        }
        check(location != -1) { "Could not get attrib or uniform location for $name" }
        handleMap[name] = location
        return location
    }

}

private val VERTICES_DATA = floatArrayOf( // X, Y, Z, U, V
    -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
    1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
    -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
    1.0f, -1.0f, 0.0f, 1.0f, 0.0f
)

private const val FLOAT_SIZE_BYTES = Float.SIZE_BYTES

const val VERTICES_DATA_POS_SIZE: Int = 3

const val VERTICES_DATA_UV_SIZE: Int = 2

const val VERTICES_DATA_STRIDE_BYTES: Int =
    (VERTICES_DATA_POS_SIZE + VERTICES_DATA_UV_SIZE) * FLOAT_SIZE_BYTES

const val VERTICES_DATA_POS_OFFSET: Int = 0 * FLOAT_SIZE_BYTES

const val VERTICES_DATA_UV_OFFSET: Int =
    VERTICES_DATA_POS_OFFSET + VERTICES_DATA_POS_SIZE * FLOAT_SIZE_BYTES
