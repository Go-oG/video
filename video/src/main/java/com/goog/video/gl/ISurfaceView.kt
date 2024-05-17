package com.goog.video.gl

interface ISurfaceView {

    fun queueEvent(r: Runnable?)

    fun requestRender()
}