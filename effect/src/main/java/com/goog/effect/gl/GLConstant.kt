package com.goog.effect.gl

import com.goog.effect.utils.loadFilterFromAsset

object GLConstant {
    //attribute vec4 aPosition
    const val K_ATTR_POSITION = "aPosition"

    //attribute vec4 aTextureCoord
    const val K_ATTR_COORD = "aTextureCoord"
    const val K_ATTR_COORD2 = "aTextureCoord2"
    const val K_ATTR_COORD3 = "aTextureCoord3"
    const val K_ATTR_COORD4 = "aTextureCoord4"

    //varying vec2 vTextureCoord
    const val K_VAR_COORD = "vTextureCoord"
    const val K_VAR_COORD2 = "vTextureCoord2"
    const val K_VAR_COORD3 = "vTextureCoord3"
    const val K_VAR_COORD4 = "vTextureCoord4"

    //uniform  sampler2D sTexture
    const val K_UNIFORM_TEX = "sTexture"
    const val K_UNIFORM_TEX2 = "sTexture2"
    const val K_UNIFORM_TEX3 = "sTexture3"
    const val K_UNIFORM_TEX4 = "sTexture4"

    ///预先创建好
    val ONE_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/one.vert")
    val TWO_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/two.vert")
    val THREE_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/three.vert")
    val FOUR_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/four.vert")

    val VERTEX_SHADERS = arrayOf(
        ONE_VERTEX_SHADER,
        TWO_VERTEX_SHADER,
        THREE_VERTEX_SHADER,
            FOUR_VERTEX_SHADER)

    val DEF_VERTEX_SHADER = """
        attribute vec4 aPosition;
        attribute vec4 aTextureCoord;
        varying highp vec2 vTextureCoord;

        void main() {
            gl_Position = aPosition;
            vTextureCoord = aTextureCoord.xy;
        }
    """.trimIndent()

    const val DEF_FRAGMENT_SHADER = """
            precision mediump float;
            varying mediump vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
                void main() {
                  gl_FragColor = texture2D(sTexture, vTextureCoord);
            }
        """
}