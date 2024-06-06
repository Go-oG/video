package com.goog.videodemo

import android.view.Surface
import com.goog.effect.Player

class PlayerWrapper(val player: androidx.media3.common.Player) : Player {
    override fun setVideoSurface(surface: Surface?) {
        player.setVideoSurface(surface)
    }
}