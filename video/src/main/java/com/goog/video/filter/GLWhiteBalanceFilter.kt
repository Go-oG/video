package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject
import com.goog.video.utils.checkArgs

class GLWhiteBalanceFilter : GLFilter() {
    private var temperature = 5000f
    private var tint = 0f

    fun setTemperature(v: Float) {
        checkArgs(v > 0, "Temperature must be >0")
        this.temperature = v
        // if (temperature < 5000) (0.0004 * (temperature - 5000.0)).toFloat() else (0.00006 * (temperature - 5000.0)).toFloat()
    }

    fun setTint(tint: Float) {
        checkArgs(tint in -200.0..200.0, "Tint must be in [-220,200]")
        this.tint = (tint / 100.0).toFloat()
    }
    
    override fun onDraw(fbo: EFrameBufferObject?) {
        super.onDraw(fbo)
        put("temperature", temperature)
        put("tint", tint)
    }

    override fun getFragmentShader(): String {
        return """
            uniform lowp sampler2D sTexture;
            varying vec2 vTextureCoord;

            uniform lowp float temperature;
            uniform lowp float tint;

            const lowp vec3 warmFilter = vec3(0.93, 0.54, 0.0);

            const mediump mat3 RGBtoYIQ = mat3(0.299, 0.587, 0.114, 0.596, -0.274, -0.322, 0.212, -0.523, 0.311);
            const mediump mat3 YIQtoRGB = mat3(1.0, 0.956, 0.621, 1.0, -0.272, -0.647, 1.0, -1.105, 1.702);

            void main() {
                lowp vec4 source = texture2D(sTexture, vTextureCoord);
                mediump vec3 yiq = RGBtoYIQ * source.rgb;
                yiq.b = clamp(yiq.b + tint * 0.5226 * 0.1, -0.5226, 0.5226);
                lowp vec3 rgb = YIQtoRGB * yiq;
                lowp vec3 processed = vec3(
                    (rgb.r < 0.5 ? (2.0 * rgb.r * warmFilter.r) : (1.0 - 2.0 * (1.0 - rgb.r) * (1.0 - warmFilter.r))),
                    (rgb.g < 0.5 ? (2.0 * rgb.g * warmFilter.g) : (1.0 - 2.0 * (1.0 - rgb.g) * (1.0 - warmFilter.g))),
                    (rgb.b < 0.5 ? (2.0 * rgb.b * warmFilter.b) : (1.0 - 2.0 * (1.0 - rgb.b) * (1.0 - warmFilter.b))));
                gl_FragColor = vec4(mix(rgb, processed, temperature), source.a);
            }
        """.trimIndent()
    }

}