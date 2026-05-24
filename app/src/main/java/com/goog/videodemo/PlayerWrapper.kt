package com.goog.videodemo

import android.net.Uri
import android.view.Surface
import com.goog.effect.Player
import com.goog.effect.PlayerListener

class PlayerWrapper(val player: androidx.media3.common.Player) : Player {
    override fun setVideoSurface(surface: Surface?) {
        player.setVideoSurface(surface)
    }

    override fun pause() {
        player.pause()
    }

    override fun play(uri: Uri) {
   //     player.play(uri)
    }

    override fun stop() {
     player.stop();
    }

    override fun setPlayerListener(listener: PlayerListener) {

    }
}