package com.goog.videodemo

import android.content.Context
import android.graphics.BitmapFactory
import com.goog.video.filter.GlBilateralFilter
import com.goog.video.filter.GlBoxBlurFilter
import com.goog.video.filter.GlBrightnessFilter
import com.goog.video.filter.GlBulgeDistortionFilter
import com.goog.video.filter.GlCGAColorSpaceFilter
import com.goog.video.filter.GlContrastFilter
import com.goog.video.filter.GlCornerFilter
import com.goog.video.filter.GlCrosshatchFilter
import com.goog.video.filter.GlExposureFilter
import com.goog.video.filter.GlFilter
import com.goog.video.filter.GlGammaFilter
import com.goog.video.filter.GlGaussianBlur2Filter
import com.goog.video.filter.GlGaussianBlur3Filter
import com.goog.video.filter.GlGaussianBlurFilter
import com.goog.video.filter.GlGrayScaleFilter
import com.goog.video.filter.GlHalftoneFilter
import com.goog.video.filter.GlHazeFilter
import com.goog.video.filter.GlHighlightShadowFilter
import com.goog.video.filter.GlHueFilter
import com.goog.video.filter.GlInvertFilter
import com.goog.video.filter.GlLookUpTableFilter
import com.goog.video.filter.GlLuminanceFilter
import com.goog.video.filter.GlLuminanceThresholdFilter
import com.goog.video.filter.GlMonochromeFilter
import com.goog.video.filter.GlOpacityFilter
import com.goog.video.filter.GlPixelationFilter
import com.goog.video.filter.GlPosterizeFilter
import com.goog.video.filter.GlRGBFilter
import com.goog.video.filter.GlSaturationFilter
import com.goog.video.filter.GlSepiaFilter
import com.goog.video.filter.GlSharpenFilter
import com.goog.video.filter.GlSolarizeFilter
import com.goog.video.filter.GlSphereRefractionFilter
import com.goog.video.filter.GlSwirlFilter
import com.goog.video.filter.GlThreex3TextureSamplingFilter
import com.goog.video.filter.GlToneFilter
import com.goog.video.filter.GlVibranceFilter
import com.goog.video.filter.GlVignetteFilter
import com.goog.video.filter.GlWatermarkFilter
import com.goog.video.filter.GlWeakPixelInclusionFilter
import com.goog.video.filter.GlWhiteBalanceFilter
import com.goog.video.filter.GlZoomBlurFilter

class FilterItem(val name: String, val builder: () -> GlFilter) {
    companion object {

        fun loadFiltersData(context: Context): List<FilterItem> {
            val list = mutableListOf<FilterItem>()
            list.add(FilterItem("Bilateral") { return@FilterItem GlBilateralFilter() })
            list.add(FilterItem("BoxBlur") { return@FilterItem GlBoxBlurFilter() })
            list.add(FilterItem("Brightness") { return@FilterItem GlBrightnessFilter() })
            list.add(FilterItem("BulgeDistortion") { return@FilterItem GlBulgeDistortionFilter() })
            list.add(FilterItem("CGAColorspace") { return@FilterItem GlCGAColorSpaceFilter() })
            list.add(FilterItem("Contrast") { return@FilterItem GlContrastFilter() })
            list.add(FilterItem("Corner") { return@FilterItem GlCornerFilter() })
            list.add(FilterItem("Crosshatch") { return@FilterItem GlCrosshatchFilter() })

            list.add(FilterItem("Exposure") { return@FilterItem GlExposureFilter() })

            list.add(FilterItem("Gamma") { return@FilterItem GlGammaFilter() })
            list.add(FilterItem("GaussianBlur") { return@FilterItem GlGaussianBlurFilter() })
            list.add(FilterItem("GaussianBlur2") { return@FilterItem GlGaussianBlur2Filter() })
            list.add(FilterItem("GaussianBlur3") { return@FilterItem GlGaussianBlur3Filter() })
            list.add(FilterItem("GrayScale") { return@FilterItem GlGrayScaleFilter() })

            list.add(FilterItem("Halftone") { return@FilterItem GlHalftoneFilter() })
            list.add(FilterItem("Haze") { return@FilterItem GlHazeFilter() })
            list.add(FilterItem("HighlightShadow") { return@FilterItem GlHighlightShadowFilter() })
            list.add(FilterItem("Hue") { return@FilterItem GlHueFilter() })

            list.add(FilterItem("Invert") { return@FilterItem GlInvertFilter() })

            list.add(FilterItem("LookUpTable") {
                val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round)
                return@FilterItem GlLookUpTableFilter(bitmap)
            })

            list.add(FilterItem("Luminance") { return@FilterItem GlLuminanceFilter() })
            list.add(FilterItem("LuminanceThreshold") { return@FilterItem GlLuminanceThresholdFilter() })

            list.add(FilterItem("Monochrome") { return@FilterItem GlMonochromeFilter() })

            list.add(FilterItem("Opacity") { return@FilterItem GlOpacityFilter(0.5f) })

            list.add(FilterItem("Pixelation") { return@FilterItem GlPixelationFilter() })
            list.add(FilterItem("Posterize") { return@FilterItem GlPosterizeFilter() })

            list.add(FilterItem("RGBFilter") { return@FilterItem GlRGBFilter() })

            list.add(FilterItem("Saturation") { return@FilterItem GlSaturationFilter() })
            list.add(FilterItem("Sepia") { return@FilterItem GlSepiaFilter() })
            list.add(FilterItem("Sharpen") { return@FilterItem GlSharpenFilter() })
            list.add(FilterItem("Solarize") { return@FilterItem GlSolarizeFilter() })
            list.add(FilterItem("SphereRefraction") { return@FilterItem GlSphereRefractionFilter() })
            list.add(FilterItem("Swirl") { return@FilterItem GlSwirlFilter() })

            list.add(FilterItem("Threex3TextureSampling") { return@FilterItem GlThreex3TextureSamplingFilter() })
            //  list.add(FilterItem("ToneCurve") { return@FilterItem GlToneCurveFilter() })
            list.add(FilterItem("Tone") { return@FilterItem GlToneFilter() })

            list.add(FilterItem("Vibrance") { return@FilterItem GlVibranceFilter() })
            list.add(FilterItem("Vignette") { return@FilterItem GlVignetteFilter() })

            list.add(FilterItem("Watermark") {
                val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round)
                return@FilterItem GlWatermarkFilter(bitmap)
            })

            list.add(FilterItem("WeakPixelInclusion") { return@FilterItem GlWeakPixelInclusionFilter() })
            list.add(FilterItem("WhiteBalance") { return@FilterItem GlWhiteBalanceFilter() })
            list.add(FilterItem("ZoomBlur") { return@FilterItem GlZoomBlurFilter() })

            return list
        }
    }
}
