package com.goog.video.filter

import com.goog.video.filter.core.GLFilter

class GLLuminanceFilter : GLFilter() {

    //TODO 待实现参数？
    override fun getFragmentShader(): String {
        return """
          precision mediump float;

          const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);
          varying vec2 vTextureCoord;
          uniform lowp sampler2D sTexture;

          void main() {
              lowp vec4 textureColor = texture2D(sTexture, vTextureCoord);
              float luminance = dot(textureColor.rgb, W);
              gl_FragColor = vec4(vec3(luminance), textureColor.a);
          }
      """.trimIndent()
    }
}
