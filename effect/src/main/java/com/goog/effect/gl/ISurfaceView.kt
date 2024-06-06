package com.goog.effect.gl

interface ISurfaceView {

    fun queueEvent(r: Runnable?)

    fun requestRender()
}