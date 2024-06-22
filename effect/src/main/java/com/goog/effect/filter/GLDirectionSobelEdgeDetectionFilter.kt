package com.goog.effect.filter

import com.goog.effect.filter.core.GLConvolution3X3Filter
import com.goog.effect.utils.loadFilterFromAsset

class GLDirectionSobelEdgeDetectionFilter : GLConvolution3X3Filter() {
    override fun getFragmentShader(): String {
        return loadFilterFromAsset("filters/direction_sobel_edge_detection.fsh")
    }
}