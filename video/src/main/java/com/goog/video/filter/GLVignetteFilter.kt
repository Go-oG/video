package com.goog.video.filter

import com.goog.video.filter.core.GLFilter
import com.goog.video.gl.FrameBufferObject
import com.goog.video.model.FloatDelegate
import com.goog.video.utils.checkArgs

class GLVignetteFilter : GLFilter() {
    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)
    var vignetteStart by FloatDelegate(0.5f, 0f, 1f)
    var vignetteEnd by FloatDelegate(0.75f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        putVec2("vignetteCenter", centerX, centerY)
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
