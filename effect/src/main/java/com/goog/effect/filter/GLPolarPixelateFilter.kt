package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FloatDelegate

//TODO 效果不明确
class GLPolarPixelateFilter : GLFilter() {
    var centerX by FloatDelegate(0.5f, 0f, 1f)
    var centerY by FloatDelegate(0.5f, 0f, 1f)

    override fun onDraw(fbo: FrameBufferObject?) {
        putVec2("uCenter", centerX, centerY)
        putVec2("uPixelSize", 1f / width, 1f / height)
    }

    override fun getFragmentShader(): String {
        return """
            precision highp float;
            varying highp vec2 vTextureCoord;
            uniform sampler2D sTexture;

            uniform vec2 uCenter;
            uniform vec2 uPixelSize;

            void main() {
                vec2 normCoord = 2.0 * vTextureCoord - 1.0;
                vec2 normCenter = 2.0 * uCenter - 1.0;

                normCoord -= normCenter;

                float r = length(normCoord); 
                float phi = atan(normCoord.y, normCoord.x);

                r = r - mod(r, uPixelSize.x) + 0.03;
                phi = phi - mod(phi, uPixelSize.y);

                normCoord.x = r * cos(phi);
                normCoord.y = r * sin(phi);
                normCoord += normCenter;
                
                vec2 textureCoordinateToUse = normCoord / 2.0 + 0.5;

                gl_FragColor = texture2D(sTexture, textureCoordinateToUse);

            }
        """.trimIndent()
    }
}