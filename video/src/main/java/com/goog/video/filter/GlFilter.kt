package com.goog.video.filter

import android.opengl.GLES20
import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.EGLUtil.createBuffer
import com.goog.video.utils.EGLUtil.createProgram
import com.goog.video.utils.EGLUtil.loadShader


open class GlFilter {
    private var program = 0

    private var vertexShader = 0

    private var fragmentShader = 0

    protected var vertexBufferName: Int = 0
        private set

    private val handleMap = HashMap<String, Int>()

    open fun setup() {
        release()
        vertexShader = loadShader(getVertexShader(), GLES20.GL_VERTEX_SHADER)
        fragmentShader = loadShader(getFragmentShader(), GLES20.GL_FRAGMENT_SHADER)
        program = createProgram(vertexShader, fragmentShader)
        vertexBufferName = createBuffer(VERTICES_DATA)
    }

    open fun setFrameSize(width: Int, height: Int) {

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

    open fun draw(texName: Int, fbo: EFrameBufferObject?) {
        useProgram()

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferName)
        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_POSITION), VERTICES_DATA_POS_SIZE, GLES20.GL_FLOAT, false,
                VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_POS_OFFSET)

        GLES20.glEnableVertexAttribArray(getHandle(K_ATTR_TEXTURE_COORD))
        GLES20.glVertexAttribPointer(getHandle(K_ATTR_TEXTURE_COORD), VERTICES_DATA_UV_SIZE, GLES20.GL_FLOAT, false,
                VERTICES_DATA_STRIDE_BYTES, VERTICES_DATA_UV_OFFSET)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texName)
        GLES20.glUniform1i(getHandle(K_UNIFORM_SAMPLER2D), 0)

        onDraw(fbo)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(getHandle(K_ATTR_POSITION))
        GLES20.glDisableVertexAttribArray(getHandle(K_ATTR_TEXTURE_COORD))
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    protected open fun onDraw(fbo: EFrameBufferObject?) {
        onDraw()
    }

    @Deprecated("")
    protected open fun onDraw() {
    }

    protected open fun useProgram() {
        GLES20.glUseProgram(program)
    }

    ///subclass overwrite this
    protected open fun getVertexShader(): String {
        return DEFAULT_VERTEX_SHADER
    }

    protected open fun getFragmentShader(): String {
        return DEFAULT_FRAGMENT_SHADER
    }

    protected fun put(name: String, value: FloatArray) {
        GLES20.glUniform4fv(getHandle(name), 1, value, 0)
    }

    protected fun put(name: String, v1: Float, v2: Float) {
        GLES20.glUniform2f(getHandle(name), v1, v2)
    }

    protected fun put(name: String, v1: Float, v2: Float, v3: Float) {
        GLES20.glUniform3f(getHandle(name), v1, v2, v3)
    }

    protected fun put(name: String, value: Int) {
        GLES20.glUniform1i(getHandle(name), value)
    }

    protected fun put(name: String, v1: Int, v2: Int) {
        GLES20.glUniform2i(getHandle(name), v1, v2)
    }

    protected fun put(name: String, v1: Int, v2: Int, v3: Int) {
        GLES20.glUniform3i(getHandle(name), v1, v2, v3)
    }

    protected fun put(name: String, value: Float) {
        GLES20.glUniform1f(getHandle(name), value)
    }

    protected fun putMatrix(name: String, value: FloatArray, offset: Int) {
        GLES20.glUniformMatrix4fv(getHandle(name), 1, false, value, offset)
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

//attribute vec4 aPosition
const val K_ATTR_POSITION = "aPosition"

//attribute vec4 aTextureCoord
const val K_ATTR_TEXTURE_COORD = "aTextureCoord"

//varying highp vec2 vTextureCoord
const val K_VAR_TEXTURE_COORD = "vTextureCoord"

//uniform lowp sampler2D sTexture
const val K_UNIFORM_SAMPLER2D = "sTexture"

const val DEFAULT_VERTEX_SHADER: String = """
            attribute vec4 aPosition;
                attribute vec4 aTextureCoord;
                varying highp vec2 vTextureCoord;
                void main() {
                gl_Position = aPosition;
                vTextureCoord = aTextureCoord.xy;
                }
        """

const val DEFAULT_FRAGMENT_SHADER: String = """
            precision mediump float;
                varying highp vec2 vTextureCoord;
                uniform lowp sampler2D sTexture;
                void main() {
                  gl_FragColor = texture2D(sTexture, vTextureCoord);
                }
        """
private val VERTICES_DATA = floatArrayOf( // X, Y, Z, U, V
        -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
        1.0f, -1.0f, 0.0f, 1.0f, 0.0f)

private const val FLOAT_SIZE_BYTES = Float.SIZE_BYTES

const val VERTICES_DATA_POS_SIZE: Int = 3

const val VERTICES_DATA_UV_SIZE: Int = 2

const val VERTICES_DATA_STRIDE_BYTES: Int =
    (VERTICES_DATA_POS_SIZE + VERTICES_DATA_UV_SIZE) * FLOAT_SIZE_BYTES

const val VERTICES_DATA_POS_OFFSET: Int = 0 * FLOAT_SIZE_BYTES

const val VERTICES_DATA_UV_OFFSET: Int =
    VERTICES_DATA_POS_OFFSET + VERTICES_DATA_POS_SIZE * FLOAT_SIZE_BYTES
