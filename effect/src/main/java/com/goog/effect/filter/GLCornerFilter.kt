package com.goog.effect.filter

import com.goog.effect.filter.core.GLFilter
import com.goog.effect.gl.FrameBufferObject
import com.goog.effect.model.FillType
import com.goog.effect.model.FloatDelegate
import com.goog.effect.utils.dp
import com.goog.effect.utils.loadFilterFromAsset

class GLCornerFilter : GLFilter() {
    var fileType = FillType.TRANSPARENT
    var topLeftRadius by FloatDelegate(8.dp, 0f)
    var topRightRadius by FloatDelegate(18.dp, 0f)
    var bottomLeftRadius by FloatDelegate(16.dp, 0f)
    var bottomRightRadius by FloatDelegate(8.dp, 0f)

    override fun onDraw(fbo: FrameBufferObject?) {
        put("uFillType", if (mEnable) fileType.type else 0)
        putVec4("uCornerRadius", topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        putVec2("uResolution", width.toFloat(), height.toFloat())
    }

    override fun getFragmentShader(): String {
        return """
            precision mediump float;

            varying vec2 vTextureCoord;
            uniform sampler2D sTexture;

            // 顺序为左上，右上，右下，左下的圆角半径
            uniform vec4 uCornerRadius;
            // 视图的分辨率
            uniform vec2 uResolution;
            // DISCARD(1),
            // WHITE(2),
            // BLACK(3),
            // TRANSPARENT(4);

            uniform int uFillType;

            void main() {
                if (uFillType >= 5 || uFillType <= 0) {
                    gl_FragColor = texture2D(sTexture, vTextureCoord);
                } else {
                    vec2 texCoord = vTextureCoord * uResolution;
                    vec2 resolution = uResolution;
                    float radiusTL = uCornerRadius.x;
                    float radiusTR = uCornerRadius.y;
                    float radiusBR = uCornerRadius.z;
                    float radiusBL = uCornerRadius.w;

                    vec2 cornerTL = vec2(radiusTL, resolution.y - radiusTL);
                    vec2 cornerTR = vec2(resolution.x - radiusTR, resolution.y - radiusTR);
                    vec2 cornerBR = vec2(resolution.x - radiusBR, radiusBR);
                    vec2 cornerBL = vec2(radiusBL, radiusBL);

                    bool isOutside = ((texCoord.x < radiusTL && texCoord.y > resolution.y - radiusTL && length(texCoord - cornerTL) > radiusTL) ||
                    (texCoord.x > resolution.x - radiusTR && texCoord.y > resolution.y - radiusTR && length(texCoord - cornerTR) > radiusTR) ||
                    (texCoord.x > resolution.x - radiusBR && texCoord.y < radiusBR && length(texCoord - cornerBR) > radiusBR) ||
                    (texCoord.x < radiusBL && texCoord.y < radiusBL && length(texCoord - cornerBL) > radiusBL));

                    vec4 gColor = texture2D(sTexture, vTextureCoord);
                    if (isOutside) {
                        if (uFillType == 1) {
                            discard;
                            gl_FragColor = texture2D(sTexture, vTextureCoord);
                        } else if (uFillType == 2) {
                            gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
                        } else if (uFillType == 3) {
                            gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
                        } else {
                            gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
                        }
                    } else {
                        gl_FragColor = texture2D(sTexture, vTextureCoord);
                    }
                }
            }

        """.trimIndent()
    }

}

