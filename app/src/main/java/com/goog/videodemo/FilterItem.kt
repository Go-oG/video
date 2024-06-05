package com.goog.videodemo

import android.content.Context
import com.goog.video.filter.GLAdaptiveThresholdFilter
import com.goog.video.filter.GLAlphaFrameFilter
import com.goog.video.filter.GLAverageColorFilter
import com.goog.video.filter.GLAverageLuminanceFilter
import com.goog.video.filter.GLBilateralFilter
import com.goog.video.filter.GLBrightnessFilter
import com.goog.video.filter.GLBulgeDistortionFilter
import com.goog.video.filter.GLCGAColorSpaceFilter
import com.goog.video.filter.GLChromaKeyFilter
import com.goog.video.filter.GLCircleFilter
import com.goog.video.filter.GLColorFASTDescriptorFilter
import com.goog.video.filter.GLColorLocalNonaryPatternFilter
import com.goog.video.filter.GLColorMatrixFilter
import com.goog.video.filter.GLColorSwizzlingFilter
import com.goog.video.filter.GLContrastFilter
import com.goog.video.filter.GLConvolution3X3Filter
import com.goog.video.filter.GLCornerFilter
import com.goog.video.filter.GLCrosshairFilter
import com.goog.video.filter.GLCrosshatchFilter
import com.goog.video.filter.GLDilationFilter
import com.goog.video.filter.GLDirectionalNonMaxSuppressionFilter
import com.goog.video.filter.GLDirectionalSobelEdgeDetectionFilter
import com.goog.video.filter.GLErosionFilter
import com.goog.video.filter.GLExposureFilter
import com.goog.video.filter.GLFalseColorFilter
import com.goog.video.filter.GLGammaFilter
import com.goog.video.filter.GLGlassSphereFilter
import com.goog.video.filter.GLGrayScaleFilter
import com.goog.video.filter.GLHalftoneFilter
import com.goog.video.filter.GLHighLightShadowTintFilter
import com.goog.video.filter.GLHighlightShadowFilter
import com.goog.video.filter.GLHistogramAccumulationFilter
import com.goog.video.filter.GLHistogramBlueSamplingFilter
import com.goog.video.filter.GLHistogramDisplayFilter
import com.goog.video.filter.GLHistogramEqualizationGreenFilter
import com.goog.video.filter.GLHistogramEqualizationLuminanceFilter
import com.goog.video.filter.GLHistogramEqualizationRGBFilter
import com.goog.video.filter.GLHistogramEqualizationRedFilter
import com.goog.video.filter.GLHistogramGreenSamplingFilter
import com.goog.video.filter.GLHistogramLuminanceSamplingFilter
import com.goog.video.filter.GLHistogramRedSamplingFilter
import com.goog.video.filter.GLHueBlendFilter
import com.goog.video.filter.GLHueFilter
import com.goog.video.filter.GLInvertFilter
import com.goog.video.filter.GLKuwaharaFilter
import com.goog.video.filter.GLKuwaharaRadius3Filter
import com.goog.video.filter.GLLanczosResamplingFilter
import com.goog.video.filter.GLLaplacianFilter
import com.goog.video.filter.GLLevelsFilter
import com.goog.video.filter.GLLineFilter
import com.goog.video.filter.GLLocalBinaryPatternFilter
import com.goog.video.filter.GLLookupFilter
import com.goog.video.filter.GLLuminanceFilter
import com.goog.video.filter.GLLuminanceRangeFilter
import com.goog.video.filter.GLLuminanceThresholdFilter
import com.goog.video.filter.GLMedianFilter
import com.goog.video.filter.GLMonochromeFilter
import com.goog.video.filter.GLMotionComparisonFilter
import com.goog.video.filter.GLNobleCornerDetectorFilter
import com.goog.video.filter.GLOpacityFilter
import com.goog.video.filter.GLPassthroughFilter
import com.goog.video.filter.GLPinchDistortionFilter
import com.goog.video.filter.GLPixelationFilter
import com.goog.video.filter.GLPixellateFilter
import com.goog.video.filter.GLPolarPixellateFilter
import com.goog.video.filter.GLPolkaDotFilter
import com.goog.video.filter.GLPosterizeFilter
import com.goog.video.filter.GLRGBFilter
import com.goog.video.filter.GLSaturationFilter
import com.goog.video.filter.GLScaleFilter
import com.goog.video.filter.GLSepiaFilter
import com.goog.video.filter.GLSharpenFilter
import com.goog.video.filter.GLShiTomasiFeatureDetectorFilter
import com.goog.video.filter.GLSketchFilter
import com.goog.video.filter.GLSobelEdgeDetectionFilter
import com.goog.video.filter.GLSolarizeFilter
import com.goog.video.filter.GLSphereRefractionFilter
import com.goog.video.filter.GLStretchDistortionFilter
import com.goog.video.filter.GLSuperResolutionFilter
import com.goog.video.filter.GLSwirlFilter
import com.goog.video.filter.GLThreex3TextureSamplingFilter
import com.goog.video.filter.GLThresholdEdgeDetectionFilter
import com.goog.video.filter.GLThresholdSketchFilter
import com.goog.video.filter.GLThresholdedNonMaximumSuppressionFilter
import com.goog.video.filter.GLTiltShiftFilter
import com.goog.video.filter.GLToneFilter
import com.goog.video.filter.GLTransformFilter
import com.goog.video.filter.GLUnsharpMaskFilter
import com.goog.video.filter.GLVibranceFilter
import com.goog.video.filter.GLVignetteFilter
import com.goog.video.filter.GLWeakPixelInclusionFilter
import com.goog.video.filter.GLWhiteBalanceFilter
import com.goog.video.filter.GLXyDerivativeFilter
import com.goog.video.filter.GLYuvConversionFullRangeFilter
import com.goog.video.filter.GLYuvConversionFullRangeUVPlanarFilter
import com.goog.video.filter.GLYuvConversionVideoRangeFilter
import com.goog.video.filter.blend.GLAddBlendFilter
import com.goog.video.filter.blend.GLAlphaBlendFilter
import com.goog.video.filter.blend.GLChromaKeyBlendFilter
import com.goog.video.filter.blend.GLColorBlendFilter
import com.goog.video.filter.blend.GLColorBurnBlendFilter
import com.goog.video.filter.blend.GLColorDodgeBlendFilter
import com.goog.video.filter.blend.GLDarkenBlendFilter
import com.goog.video.filter.blend.GLDifferenceBlendFilter
import com.goog.video.filter.blend.GLDissolveBlendFilter
import com.goog.video.filter.blend.GLDivideBlendFilter
import com.goog.video.filter.blend.GLExclusionBlendFilter
import com.goog.video.filter.blend.GLHardLightBlendFilter
import com.goog.video.filter.blend.GLHistogramEqualizationBlueFilter
import com.goog.video.filter.blend.GLLightenBlendFilter
import com.goog.video.filter.blend.GLLinearBurnBlendFilter
import com.goog.video.filter.blend.GLLuminosityBlendFilter
import com.goog.video.filter.blend.GLMultiplyBlendFilter
import com.goog.video.filter.blend.GLNormalBlendFilter
import com.goog.video.filter.blend.GLOverlayBlendFilter
import com.goog.video.filter.blend.GLSaturationBlendFilter
import com.goog.video.filter.blend.GLScreenBlendFilter
import com.goog.video.filter.blend.GLSoftLightBlendFilter
import com.goog.video.filter.blend.GLSourceOverBlendFilter
import com.goog.video.filter.blend.GLSubtractBlendFilter
import com.goog.video.filter.blur.GLBilateralBlurFilter
import com.goog.video.filter.blur.GLBoxBlurFilter
import com.goog.video.filter.blur.GLGaussianBlur2Filter
import com.goog.video.filter.blur.GLGaussianBlur3Filter
import com.goog.video.filter.blur.GLGaussianBlurFilter
import com.goog.video.filter.blur.GLMotionBlurFilter
import com.goog.video.filter.blur.GLZoomBlurFilter
import com.goog.video.filter.core.GLFilter
import com.goog.video.model.FloatDelegate
import com.goog.video.model.IntDelegate
import com.goog.videodemo.ParameterBuilder.loadParameters
import java.lang.Float.max
import java.util.Locale

class Parameter(
    val varName: String,
    val setMethodName: String,
    val minValue: Float,
    val maxValue: Float,
    val step: Float,
    var curValue: Float, val useFloat: Boolean
) {
    val showName: String
        get() {
            return varName.replace("\$delegate", "")
        }
}

class FilterItem(val clsName: Class<*>) {
    val parameter = loadParameters(clsName)
    var filter: GLFilter? = null
    var select = false

    val name: String
        get() {
            var itemName = clsName.simpleName.replace(".kt", "")
            itemName = itemName.replace("GL", "")
            itemName = itemName.replace("Filter", "")
            return itemName
        }

    fun select() {
        select = true
        filter = clsName.newInstance() as GLFilter
    }

    fun unselect() {
        select = false
        filter = null
    }

    fun changeParameter(parameter: Parameter, value: Float) {
        val filter = this.filter ?: return
        val cls = filter::class.java
        val methodName = parameter.setMethodName
        val method = if (parameter.useFloat) cls.getMethod(methodName, Float::class.java) else cls.getMethod(methodName, Int::class.java)
        method.isAccessible = true
        if (parameter.useFloat) {
            method.invoke(filter, value)
        } else {
            method.invoke(filter, value.toInt())
        }
        parameter.curValue = value
    }

    companion object {
        fun loadFiltersData(context: Context): List<FilterItem> {
            val list = mutableListOf<FilterItem>()

            list.add(FilterItem(GLAdaptiveThresholdFilter::class.java))
            list.add(FilterItem(GLAlphaFrameFilter::class.java))
            list.add(FilterItem(GLAverageColorFilter::class.java))

          //  list.add(FilterItem(GLAverageLuminanceFilter::class.java))

            list.add(FilterItem(GLBilateralFilter::class.java))
          //  list.add(FilterItem(GLBilateralBlurFilter::class.java))
            list.add(FilterItem(GLBoxBlurFilter::class.java))
            list.add(FilterItem(GLBrightnessFilter::class.java))
            list.add(FilterItem(GLBulgeDistortionFilter::class.java))

            list.add(FilterItem(GLCGAColorSpaceFilter::class.java))
            list.add(FilterItem(GLChromaKeyFilter::class.java))
          //  list.add(FilterItem(GLCircleFilter::class.java))
            list.add(FilterItem(GLColorFASTDescriptorFilter::class.java))
          //  list.add(FilterItem(GLColorLocalNonaryPatternFilter::class.java))
            list.add(FilterItem(GLColorMatrixFilter::class.java))
            list.add(FilterItem(GLColorSwizzlingFilter::class.java))

            list.add(FilterItem(GLContrastFilter::class.java))
            list.add(FilterItem(GLConvolution3X3Filter::class.java))
            list.add(FilterItem(GLCornerFilter::class.java))
           // list.add(FilterItem(GLCrosshairFilter::class.java))
            list.add(FilterItem(GLCrosshatchFilter::class.java))

         //   list.add(FilterItem(GLDilationFilter::class.java))
            list.add(FilterItem(GLDirectionalNonMaxSuppressionFilter::class.java))
            list.add(FilterItem(GLDirectionalSobelEdgeDetectionFilter::class.java))
         //   list.add(FilterItem(GLErosionFilter::class.java))
            list.add(FilterItem(GLExposureFilter::class.java))

         //   list.add(FilterItem(GLFalseColorFilter::class.java))

            list.add(FilterItem(GLGammaFilter::class.java))
            list.add(FilterItem(GLGaussianBlurFilter::class.java))
            list.add(FilterItem(GLGaussianBlur2Filter::class.java))
            list.add(FilterItem(GLGaussianBlur3Filter::class.java))
            list.add(FilterItem(GLGlassSphereFilter::class.java))
            list.add(FilterItem(GLGrayScaleFilter::class.java))

            list.add(FilterItem(GLHalftoneFilter::class.java))
            list.add(FilterItem(GLHighlightShadowFilter::class.java))
            list.add(FilterItem(GLHighLightShadowTintFilter::class.java))
           // list.add(FilterItem(GLHistogramAccumulationFilter::class.java))
            list.add(FilterItem(GLHistogramBlueSamplingFilter::class.java))
            list.add(FilterItem(GLHistogramDisplayFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationGreenFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationLuminanceFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationRedFilter::class.java))
            list.add(FilterItem(GLHistogramEqualizationRGBFilter::class.java))
      //      list.add(FilterItem(GLHistogramGreenSamplingFilter::class.java))
       //     list.add(FilterItem(GLHistogramLuminanceSamplingFilter::class.java))
            list.add(FilterItem(GLHistogramRedSamplingFilter::class.java))
            list.add(FilterItem(GLHueBlendFilter::class.java))
            list.add(FilterItem(GLHueFilter::class.java))

            list.add(FilterItem(GLInvertFilter::class.java))

            list.add(FilterItem(GLKuwaharaFilter::class.java))
            list.add(FilterItem(GLKuwaharaRadius3Filter::class.java))

            list.add(FilterItem(GLLanczosResamplingFilter::class.java))
            list.add(FilterItem(GLLaplacianFilter::class.java))

            list.add(FilterItem(GLLevelsFilter::class.java))
          //  list.add(FilterItem(GLLineFilter::class.java))
            list.add(FilterItem(GLLocalBinaryPatternFilter::class.java))
            list.add(FilterItem(GLLookupFilter::class.java))
            //  list.add(FilterItem(GLLookUpTableFilter::class.java))
            list.add(FilterItem(GLLuminanceFilter::class.java))
            list.add(FilterItem(GLLuminanceRangeFilter::class.java))
            list.add(FilterItem(GLLuminanceThresholdFilter::class.java))

         //   list.add(FilterItem(GLMedianFilter::class.java))
            list.add(FilterItem(GLMonochromeFilter::class.java))
            list.add(FilterItem(GLMotionBlurFilter::class.java))
          //  list.add(FilterItem(GLMotionComparisonFilter::class.java))

            list.add(FilterItem(GLNobleCornerDetectorFilter::class.java))

            list.add(FilterItem(GLOpacityFilter::class.java))

            list.add(FilterItem(GLPassthroughFilter::class.java))
            list.add(FilterItem(GLPinchDistortionFilter::class.java))
            list.add(FilterItem(GLPixelationFilter::class.java))
            list.add(FilterItem(GLPixellateFilter::class.java))
            list.add(FilterItem(GLPolarPixellateFilter::class.java))
            list.add(FilterItem(GLPolkaDotFilter::class.java))
            list.add(FilterItem(GLPosterizeFilter::class.java))

            list.add(FilterItem(GLRGBFilter::class.java))

            list.add(FilterItem(GLSaturationFilter::class.java))
            list.add(FilterItem(GLScaleFilter::class.java))

            list.add(FilterItem(GLSepiaFilter::class.java))
            list.add(FilterItem(GLSharpenFilter::class.java))
            list.add(FilterItem(GLShiTomasiFeatureDetectorFilter::class.java))
            list.add(FilterItem(GLSketchFilter::class.java))
            list.add(FilterItem(GLSobelEdgeDetectionFilter::class.java))
            list.add(FilterItem(GLSolarizeFilter::class.java))
            list.add(FilterItem(GLSphereRefractionFilter::class.java))
            list.add(FilterItem(GLStretchDistortionFilter::class.java))
         //   list.add(FilterItem(GLSuperResolutionFilter::class.java))
            list.add(FilterItem(GLSwirlFilter::class.java))

       //     list.add(FilterItem(GLThreex3TextureSamplingFilter::class.java))
            list.add(FilterItem(GLThresholdEdgeDetectionFilter::class.java))
            list.add(FilterItem(GLThresholdedNonMaximumSuppressionFilter::class.java))
         //   list.add(FilterItem(GLThresholdSketchFilter::class.java))
          //  list.add(FilterItem(GLTiltShiftFilter::class.java))

            //  list.add(FilterItem(GLToneCurveFilter::class.java))

            list.add(FilterItem(GLToneFilter::class.java))
          //  list.add(FilterItem(GLTransformFilter::class.java))

            list.add(FilterItem(GLUnsharpMaskFilter::class.java))

            list.add(FilterItem(GLVibranceFilter::class.java))
            list.add(FilterItem(GLVignetteFilter::class.java))

            //   list.add(FilterItem(GLWatermarkFilter::class.java))

            list.add(FilterItem(GLWeakPixelInclusionFilter::class.java))
            list.add(FilterItem(GLWhiteBalanceFilter::class.java))

            list.add(FilterItem(GLXyDerivativeFilter::class.java))

            list.add(FilterItem(GLYuvConversionFullRangeFilter::class.java))
            list.add(FilterItem(GLYuvConversionFullRangeUVPlanarFilter::class.java))
            list.add(FilterItem(GLYuvConversionVideoRangeFilter::class.java))

            list.add(FilterItem(GLZoomBlurFilter::class.java))

            //Blend
            list.add(FilterItem(GLAddBlendFilter::class.java))
          //  list.add(FilterItem(GLAlphaBlendFilter::class.java))
            list.add(FilterItem(GLChromaKeyBlendFilter::class.java))
           // list.add(FilterItem(GLColorBlendFilter::class.java))
          //  list.add(FilterItem(GLColorBurnBlendFilter::class.java))
         //   list.add(FilterItem(GLColorDodgeBlendFilter::class.java))

           // list.add(FilterItem(GLDarkenBlendFilter::class.java))
           // list.add(FilterItem(GLDifferenceBlendFilter::class.java))
//            list.add(FilterItem(GLDissolveBlendFilter::class.java))
//            list.add(FilterItem(GLDivideBlendFilter::class.java))
//
//            list.add(FilterItem(GLExclusionBlendFilter::class.java))
//
//            list.add(FilterItem(GLHardLightBlendFilter::class.java))
//            list.add(FilterItem(GLHistogramEqualizationBlueFilter::class.java))
//
//            list.add(FilterItem(GLLightenBlendFilter::class.java))
//            list.add(FilterItem(GLLinearBurnBlendFilter::class.java))
//            list.add(FilterItem(GLLuminosityBlendFilter::class.java))
//
//            list.add(FilterItem(GLMultiplyBlendFilter::class.java))
//
//            list.add(FilterItem(GLNormalBlendFilter::class.java))
//            list.add(FilterItem(GLOverlayBlendFilter::class.java))
//            list.add(FilterItem(GLSaturationBlendFilter::class.java))
//            list.add(FilterItem(GLScreenBlendFilter::class.java))
//            list.add(FilterItem(GLSoftLightBlendFilter::class.java))
//            list.add(FilterItem(GLSourceOverBlendFilter::class.java))
//            list.add(FilterItem(GLSubtractBlendFilter::class.java))

            return list
        }
    }
}

fun String.firstUpCase(): String {
    if (isBlank()) {
        return ""
    }
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
}

fun String.firstLowCase(): String {
    if (isBlank()) {
        return ""
    }
    return replaceFirstChar {
        if (it.isUpperCase()) it.lowercase() else it.toString()
    }
}










