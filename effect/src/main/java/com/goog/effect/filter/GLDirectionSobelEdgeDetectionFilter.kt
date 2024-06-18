package com.goog.effect.filter

import com.goog.effect.filter.core.GLBoxBoundFilter
import com.goog.effect.utils.loadFilterFromAsset

class GLDirectionSobelEdgeDetectionFilter:GLBoxBoundFilter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/directional_sobel_edge_detection.fsh")
    }
}