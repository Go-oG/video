package com.goog.effect.view

import com.goog.effect.Player
import com.goog.effect.filter.core.GLFilter

interface IVideoView {

    fun setGLFilter(glFilter: GLFilter?)

    fun setPlayer(player: Player?)

}