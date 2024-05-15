package com.goog.video.filter

import com.goog.video.gl.EFrameBufferObject

class GlVignetteFilter : GlFilter() {
    private val vignetteCenterX = 0.5f
    private val vignetteCenterY = 0.5f
    var vignetteStart: Float = 0.2f
    var vignetteEnd: Float = 0.85f


    override fun onDraw(fbo: EFrameBufferObject?) {
        putVec2("vignetteCenter", vignetteCenterX, vignetteCenterY)
        put("vignetteStart", vignetteStart)
        put("vignetteEnd", vignetteEnd)
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying vec2 vTextureCoord;
            uniform lowp sampler2D sTexture;
            uniform lowp vec2 vignetteCenter;
            uniform highp float vignetteStart;
            uniform highp float vignetteEnd;
            void main() {
                lowp vec3 rgb = texture2D(sTexture, vTextureCoord).rgb;
                lowp float d = distance(vTextureCoord, vec2(vignetteCenter.x, vignetteCenter.y));
                lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);
                gl_FragColor = vec4(mix(rgb.x, 0.0, percent), mix(rgb.y, 0.0, percent), mix(rgb.z, 0.0, percent), 1.0);
            }
        """.trimIndent()
    }
}
