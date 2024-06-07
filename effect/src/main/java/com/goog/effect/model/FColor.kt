package com.goog.effect.model

class FColor(red: Float = 1f, green: Float = 1f, blue: Float = 1f) {

    var r by FloatDelegate(1f, 0f, 1f)

    var g by FloatDelegate(1f, 0f, 1f)

    var b by FloatDelegate(1f, 0f, 1f)

    init {
        r = red
        g = green
        b = blue
    }
}

class FColor4(red: Float = 1f, green: Float = 1f, blue: Float = 1f, alpha: Float = 1f) {

    var r by FloatDelegate(1f, 0f, 1f)

    var g by FloatDelegate(1f, 0f, 1f)

    var b by FloatDelegate(1f, 0f, 1f)

    var a by FloatDelegate(1f, 0f, 1f)

    init {
        r = red
        g = green
        b = blue
        a = alpha
    }
}