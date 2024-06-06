package com.goog.effect.view.texture

import javax.microedition.khronos.opengles.GL

interface GLWrapper {
    fun wrap(gl: GL?): GL?
}
