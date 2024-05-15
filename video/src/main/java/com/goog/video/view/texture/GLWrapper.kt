package com.goog.video.view.texture

import javax.microedition.khronos.opengles.GL

interface GLWrapper {
    fun wrap(gl: GL?): GL?
}
