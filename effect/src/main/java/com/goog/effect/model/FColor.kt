package com.goog.effect.model

class FColor(red: Float = 0f, green: Float = 0f, blue: Float = 0f) {

    var r by FloatDelegate(0.5f, 0f, 1f)

    var g by FloatDelegate(0.5f, 0f, 1f)

    var b by FloatDelegate(0.5f, 0f, 1f)

    init {
        r = red
        g = green
        b = blue
    }
}

class FColor4(red: Float = 0f, green: Float = 0f, blue: Float = 0f, alpha: Float = 1f) {

    var r by FloatDelegate(0.5f, 0f, 1f)

    var g by FloatDelegate(0.5f, 0f, 1f)

    var b by FloatDelegate(0.5f, 0f, 1f)

    var a by FloatDelegate(1f, 0f, 1f)

    init {
        r = red
        g = green
        b = blue
        a = alpha
    }
}