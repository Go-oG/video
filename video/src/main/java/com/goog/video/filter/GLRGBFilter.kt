package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

class GLRGBFilter : GLFilter() {

    var red by FloatDelegate(1f, 0f, 1f)
    var green by FloatDelegate(1f, 0f, 1f)
    var blue by FloatDelegate(1f, 0f, 1f)
    var brightness by FloatDelegate(1f, 0f, 1f)
    var saturation by FloatDelegate(1f, 0f, 1f)
    var contrast by FloatDelegate(1f, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("red", red)
        put("green", green)
        put("blue", blue)
        put("brightness", brightness)
        put("saturation", saturation)
        put("contrast", contrast)
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
