package com.goog.effect.model

enum class GLVersion {
    V20,
    V30;
}

enum class ResizeMode {
    FIT,
    FIXED_WIDTH,
    FIXED_HEIGHT,
    FILL,
    ZOOM;
}

enum class FillType(val type: Int) {
    DISCARD(1),
    WHITE(2),
    BLACK(3),
    TRANSPARENT(4);
}

enum class Level(val sort: Int) {
    L1(1),
    L2(2),
    L3(3),
    L4(4),
    L5(5),
    L6(6),
    L7(7),
    L8(8),
    L9(9),
    L10(10);
}

enum class CallBy {
    NORMAL,
    PAUSE,
    UPDATE_ARGS,
    DESTROY;
}

