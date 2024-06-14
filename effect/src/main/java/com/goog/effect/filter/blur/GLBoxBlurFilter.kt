package com.goog.effect.filter.blur

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

class GLBoxBlurFilter : GLFilter() {
    var texelWidthOffset by FloatDelegate(0.003f, 0f)
    var texelHeightOffset by FloatDelegate(0.003f, 0f)
    var blurSize by FloatDelegate(1f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uEnable", mEnable)
        put("texelWidthOffset", texelWidthOffset)
        put("texelHeightOffset", texelHeightOffset)
        put("blurSize", blurSize)
    }

    public override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            uniform float texelWidthOffset;
            uniform float texelHeightOffset;
            uniform float blurSize;
            
            varying vec2 centerTexCoord;
            varying vec2 oneStepLeftTexCoord;
            varying vec2 twoStepsLeftTexCoord;
            varying vec2 oneStepRightTexCoord;
            varying vec2 twoStepsRightTexCoord;
            
            void main() {
                gl_Position = aPosition;
                vec2 firstOffset = vec2(1.5 * texelWidthOffset, 1.5 * texelHeightOffset) * blurSize;
                vec2 secondOffset = vec2(3.5 * texelWidthOffset, 3.5 * texelHeightOffset) * blurSize;
                centerTexCoord = aTextureCoord.xy;
                oneStepLeftTexCoord = centerTexCoord - firstOffset;
                twoStepsLeftTexCoord = centerTexCoord - secondOffset;
                oneStepRightTexCoord = centerTexCoord + firstOffset;
                twoStepsRightTexCoord = centerTexCoord + secondOffset;
            }
        """.trimIndent()
    }

    public override fun getFragmentShader(): String {
        return """
            precision mediump float;
            uniform lowp sampler2D sTexture;
            uniform bool uEnable;
            varying vec2 centerTexCoord;
            varying vec2 oneStepLeftTexCoord;
            varying vec2 twoStepsLeftTexCoord;
            varying vec2 oneStepRightTexCoord;
            varying vec2 twoStepsRightTexCoord;
            void main() {
                if(uEnable){
                    vec4 color = texture2D(sTexture, centerTexCoord) * 0.2;
                    color += texture2D(sTexture, oneStepLeftTexCoord) * 0.2;
                    color += texture2D(sTexture, oneStepRightTexCoord) * 0.2;
                    color += texture2D(sTexture, twoStepsLeftTexCoord) * 0.2;
                    color += texture2D(sTexture, twoStepsRightTexCoord) * 0.2;
                    gl_FragColor = color;
                }else{
                    gl_FragColor=texture2D(sTexture,centerTexCoord);
                }
            }
        """.trimIndent()
    }
}
