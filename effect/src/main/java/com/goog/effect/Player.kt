package com.goog.effect

import android.net.Uri
import android.view.Surface

interface Player {
    fun setVideoSurface(surface: Surface?)

    fun pause()

    fun play(uri: Uri)

    fun stop()

    fun setPlayerListener(listener: PlayerListener)
}

interface PlayerListener {

    fun onPlaying()

    fun onPlayPause()

    fun onPlayResume()

    fun onPlayStop()

    fun onPlayError()

    fun onPlayComplete()

}