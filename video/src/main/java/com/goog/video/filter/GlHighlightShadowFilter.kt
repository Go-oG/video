package com.goog.video.filter

import android.opengl.GLES20
import com.goog.video.gl.EFrameBufferObject

class GlHighlightShadowFilter : GlFilter() {
    private var shadows = 1f
    private var highlights = 0f

    fun setShadows(shadows: Float) {
        this.shadows = shadows
    }

    fun setHighlights(highlights: Float) {
        this.highlights = highlights
    }

    override fun onDraw(fbo: EFrameBufferObject?) {
        put("shadows", shadows)
        put("highlights", highlights)
    }
    
    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            uniform lowp sampler2D sTexture;
            varying vec2 vTextureCoord;

            uniform lowp float shadows;
            uniform lowp float highlights;

            const mediump vec3 luminanceWeighting = vec3(0.3, 0.3, 0.3);

            void main() {
                lowp vec4 source = texture2D(sTexture, vTextureCoord);
                mediump float luminance = dot(source.rgb, luminanceWeighting);

                mediump float shadow = clamp((pow(luminance, 1.0 / (shadows + 1.0)) + (-0.76) * pow(luminance, 2.0 / (shadows + 1.0))) - luminance, 0.0, 1.0);
                mediump float highlight = clamp((1.0 - (pow(1.0 - luminance, 1.0 / (2.0 - highlights)) + (-0.8) * pow(1.0 - luminance, 2.0 / (2.0 - highlights)))) - luminance, -1.0, 0.0);
                lowp vec3 result = vec3(0.0, 0.0, 0.0) + ((luminance + shadow + highlight) - 0.0) * ((source.rgb - vec3(0.0, 0.0, 0.0)) / (luminance - 0.0));

                gl_FragColor = vec4(result.rgb, source.a);
            }
        """.trimIndent()
    }
}
