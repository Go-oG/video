package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.utils.checkArgs

class GLRGBFilter(r: Float = 1f, g: Float = 1f, b: Float = 1f, brightness: Float = 0f,
    saturation: Float = 1f, contrast: Float = 1.2f) : GLFilter() {
    private var red = 1f
    private var green = 1f
    private var blue = 1f
    private var brightness = 0f
    private var saturation = 1f
    private var contrast = 1.2f

    init {
        setRed(r)
        setGreen(g)
        setBlue(b)
        setBrightness(brightness)
        setSaturation(saturation)
        setContrast(contrast)
    }

    fun setRed(v: Float) {
        checkArgs(v in 0f..1f, "red must be >= 0 and <=1")
        this.red = v
    }

    fun setGreen(v: Float) {
        checkArgs(v in 0f..1f, "green must be >= 0 and <=1")
        this.green = v
    }

    fun setBlue(blue: Float) {
        checkArgs(blue in 0f..1f, "blue must be >= 0 and <=1")
        this.blue = blue
    }

    fun setBrightness(brightness: Float) {
        checkArgs(brightness in 0f..1f, "brightness must be >= 0 and <=1")
        this.brightness = brightness
    }

    fun setSaturation(saturation: Float) {
        checkArgs(saturation in 0f..2f, "saturation must be >= 0 and <=2")
        this.saturation = saturation
    }

    fun setContrast(contrast: Float) {
        checkArgs(contrast >= 0f, "contrast must be >= 0")
        this.contrast = contrast
    }

    override fun onDraw(fbo: FrameBufferObject?) {
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
