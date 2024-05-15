package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

/**
 * Adjusts the individual RGB channels of an image
 * red: Normalized values by which each color channel is multiplied.
 * The range is from 0.0 up, with 1.0 as the default.
 * green:
 * blue:
 */
class GlRGBFilter : GlFilter() {
    private var red = 1f
    private var green = 1f
    private var blue = 1f
    private var brightness = 0f
    private var saturation = 1f
    private var contrast = 1.2f

    fun setRed(red: Float) {
        this.red = red
    }

    fun setGreen(green: Float) {
        this.green = green
    }

    fun setBlue(blue: Float) {
        this.blue = blue
    }

    fun setBrightness(brightness: Float) {
        this.brightness = brightness
    }

    fun setSaturation(saturation: Float) {
        this.saturation = saturation
    }

    fun setContrast(contrast: Float) {
        this.contrast = contrast
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("red",red)
        put("green",green)
        put("blue",blue)
        put("brightness",brightness)
        put("saturation",saturation)
        put("contrast",contrast)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;

            uniform lowp sampler2D sTexture;
            uniform highp float red;
            uniform highp float green;
            uniform highp float blue;
            uniform lowp float brightness;
            uniform lowp float saturation;
            uniform lowp float contrast;

            const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);

            void main() {
                highp vec4 textureColor = texture2D(sTexture, vTextureCoord);
                lowp vec4 textureOtherColor = texture2D(sTexture, vTextureCoord);
                lowp float luminance = dot(textureOtherColor.rgb, luminanceWeighting);
                lowp vec3 greyScaleColor = vec3(luminance);

                gl_FragColor = vec4(textureColor.r * red, textureColor.g * green, textureColor.b * blue, 1.0);
                gl_FragColor = vec4((textureOtherColor.rgb + vec3(brightness)), textureOtherColor.w);
                gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureOtherColor.w);
                gl_FragColor = vec4(((textureOtherColor.rgb - vec3(0.5)) * contrast + vec3(0.5)), textureOtherColor.w);
            }
        """.trimIndent()
    }
}
