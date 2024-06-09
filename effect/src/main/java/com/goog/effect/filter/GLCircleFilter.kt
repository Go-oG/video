package com.goog.effect.filter

import com.goog.effect.filter.core.GLCenterFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FColor4
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.loadFilterFromAsset

//TODO 带查看效果
class GLCircleFilter : GLCenterFilter() {
    var circleColor = FColor4()
    var backgroundColor = FColor4()
    var radius by FloatDelegate(0.5f)
    var aspectRatio by FloatDelegate(1f, 0f);

    override fun onDraw(fbo: FrameBufferObject?) {
        super.onDraw(fbo)
        put("radius", radius)
        put("aspectRatio", aspectRatio)
        putColor("circleColor", circleColor)
        putColor("backgroundColor", backgroundColor)
    }

    override fun getVertexShader(): String {
        return """
            attribute vec4 aPosition;
            attribute vec4 aTextureCoord;
            varying vec2 vTextureCoord;
            uniform float aspectRatio;
            void main() {
                vTextureCoord = vec2(aPosition.x, aPosition.y * aspectRatio);
                gl_Position = aPosition;
            }
        """.trimIndent()
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;
            
            uniform  vec4 circleColor;
            uniform  vec4 backgroundColor;
            uniform  vec2 center;
            uniform  float radius;

            void main() {
                 float distanceFromCenter = distance(center, vTextureCoord);
                 float checkForPresenceWithinCircle = step(distanceFromCenter, radius);
                gl_FragColor = mix(backgroundColor, circleColor, checkForPresenceWithinCircle);
            }
        """.trimIndent()
    }
}