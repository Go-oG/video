package com.goog.video.gl

import com.goog.video.utils.loadFilterFromAsset

object GLConstant {
    //attribute vec4 aPosition
    const val K_ATTR_POSITION = "aPosition"

    //attribute vec4 aTextureCoord
    const val K_ATTR_COORD = "aTextureCoord"
    const val K_ATTR_COORD2 = "aTextureCoord2"
    const val K_ATTR_COORD3 = "aTextureCoord3"
    const val K_ATTR_COORD4 = "aTextureCoord4"
    const val K_ATTR_COORD5 = "aTextureCoord5"
    const val K_ATTR_COORD6 = "aTextureCoord6"
    const val K_ATTR_COORD7 = "aTextureCoord7"
    const val K_ATTR_COORD8 = "aTextureCoord8"
    const val K_ATTR_COORD9 = "aTextureCoord9"
    const val K_ATTR_COORD10 = "aTextureCoord10"

    val K_ATTR_COORDS = arrayOf(
        K_ATTR_COORD, K_ATTR_COORD2, K_ATTR_COORD3, K_ATTR_COORD4,
        K_ATTR_COORD5, K_ATTR_COORD6, K_ATTR_COORD7, K_ATTR_COORD8,
        K_ATTR_COORD9, K_ATTR_COORD10,
    )

    //varying highp vec2 vTextureCoord
    const val K_VAR_COORD = "vTextureCoord"
    const val K_VAR_COORD2 = "vTextureCoord2"
    const val K_VAR_COORD3 = "vTextureCoord3"
    const val K_VAR_COORD4 = "vTextureCoord4"
    const val K_VAR_COORD5 = "vTextureCoord5"
    const val K_VAR_COORD6 = "vTextureCoord6"
    const val K_VAR_COORD7 = "vTextureCoord7"
    const val K_VAR_COORD8 = "vTextureCoord8"
    const val K_VAR_COORD9 = "vTextureCoord9"
    const val K_VAR_COORD10 = "vTextureCoord10"

    val K_VAR_COORDS = arrayOf(
        K_VAR_COORD, K_VAR_COORD2, K_VAR_COORD3, K_VAR_COORD4,
        K_VAR_COORD5, K_VAR_COORD6, K_VAR_COORD7, K_VAR_COORD8,
        K_VAR_COORD9, K_VAR_COORD10,
    )

    //uniform  sampler2D sTexture
    const val K_UNIFORM_TEX = "sTexture"
    const val K_UNIFORM_TEX2 = "sTexture2"
    const val K_UNIFORM_TEX3 = "sTexture3"
    const val K_UNIFORM_TEX4 = "sTexture4"
    const val K_UNIFORM_TEX5 = "sTexture5"
    const val K_UNIFORM_TEX6 = "sTexture6"
    const val K_UNIFORM_TEX7 = "sTexture7"
    const val K_UNIFORM_TEX8 = "sTexture8"
    const val K_UNIFORM_TEX9 = "sTexture9"
    const val K_UNIFORM_TEX10 = "sTexture10"

    val K_UNIFORM_TEXS =
        arrayOf(
            K_UNIFORM_TEX, K_UNIFORM_TEX2, K_UNIFORM_TEX3, K_UNIFORM_TEX4,
            K_UNIFORM_TEX5, K_UNIFORM_TEX6, K_UNIFORM_TEX7, K_UNIFORM_TEX8,
            K_UNIFORM_TEX9, K_UNIFORM_TEX10,
        )


    ///预先创建好
    val ONE_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/one.vert")
    val TWO_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/two.vert")
    val THREE_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/three.vert")
    val FOUR_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/four.vert")
    val FIVE_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/five.vert")
    val SIX_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/six.vert")
    val SEVEN_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/seven.vert")
    val EIGHT_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/eight.vert")
    val NINE_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/nine.vert")
    val TEN_VERTEX_SHADER = loadFilterFromAsset("filters/vertex/ten.vert")
    val VERTEX_SHADERS = arrayOf(
        ONE_VERTEX_SHADER,
        TWO_VERTEX_SHADER,
        THREE_VERTEX_SHADER,
        FOUR_VERTEX_SHADER,
        FIVE_VERTEX_SHADER,
        SIX_VERTEX_SHADER,
        SEVEN_VERTEX_SHADER,
        EIGHT_VERTEX_SHADER,
        NINE_VERTEX_SHADER,
        TEN_VERTEX_SHADER
    )


    val DEF_VERTEX_SHADER: String = ONE_VERTEX_SHADER

    const val DEF_FRAGMENT_SHADER: String = """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
                void main() {
                  gl_FragColor = texture2D(sTexture, vTextureCoord);
            }
        """

}