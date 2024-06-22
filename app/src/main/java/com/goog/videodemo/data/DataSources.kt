package com.goog.videodemo.data

import android.content.Context
import com.goog.effect.filter.GLAdaptiveThresholdFilter
import com.goog.effect.filter.GLAverageColorFilter
import com.goog.effect.filter.GLBilateralFilter
import com.goog.effect.filter.GLBrightnessFilter
import com.goog.effect.filter.GLBulgeDistortionFilter
import com.goog.effect.filter.GLCGAColorSpaceFilter
import com.goog.effect.filter.GLChromaKeyFilter
import com.goog.effect.filter.GLColorFastDescriptorFilter
import com.goog.effect.filter.GLColorMatrixFilter
import com.goog.effect.filter.GLColorSwizzlingFilter
import com.goog.effect.filter.GLContrastFilter
import com.goog.effect.filter.GLCornerFilter
import com.goog.effect.filter.GLCrosshatchFilter
import com.goog.effect.filter.GLDirectionNonMaxSuppressionFilter
import com.goog.effect.filter.GLDirectionSobelEdgeDetectionFilter
import com.goog.effect.filter.GLExposureFilter
import com.goog.effect.filter.GLGammaFilter
import com.goog.effect.filter.GLGlassSphereFilter
import com.goog.effect.filter.GLGrayScaleFilter
import com.goog.effect.filter.GLHalftoneFilter
import com.goog.effect.filter.GLHighLightShadowTintFilter
import com.goog.effect.filter.GLHighlightShadowFilter
import com.goog.effect.filter.GLHistogramBlueSamplingFilter
import com.goog.effect.filter.GLHistogramDisplayFilter
import com.goog.effect.filter.GLHistogramEqualizationGreenFilter
import com.goog.effect.filter.GLHistogramEqualizationLuminanceFilter
import com.goog.effect.filter.GLHistogramEqualizationRGBFilter
import com.goog.effect.filter.GLHistogramEqualizationRedFilter
import com.goog.effect.filter.GLHistogramRedSamplingFilter
import com.goog.effect.filter.GLHueBlendFilter
import com.goog.effect.filter.GLHueFilter
import com.goog.effect.filter.GLInvertFilter
import com.goog.effect.filter.GLKuwaharaFilter
import com.goog.effect.filter.GLKuwaharaRadius3Filter
import com.goog.effect.filter.GLLanczosResamplingFilter
import com.goog.effect.filter.GLLaplacian3X3Filter
import com.goog.effect.filter.GLLevelsFilter
import com.goog.effect.filter.GLLocalBinaryPattern3X3Filter
import com.goog.effect.filter.GLLookupFilter
import com.goog.effect.filter.GLLuminanceFilter
import com.goog.effect.filter.GLLuminanceRangeFilter
import com.goog.effect.filter.GLLuminanceThresholdFilter
import com.goog.effect.filter.GLMonochromeFilter
import com.goog.effect.filter.GLNobleCornerDetectorFilter
import com.goog.effect.filter.GLOpacityFilter
import com.goog.effect.filter.GLPassthroughFilter
import com.goog.effect.filter.GLPinchDistortionFilter
import com.goog.effect.filter.GLPixelateFilter
import com.goog.effect.filter.GLPolarPixelateFilter
import com.goog.effect.filter.GLPolkaDotFilter
import com.goog.effect.filter.GLPosterizeFilter
import com.goog.effect.filter.GLRGBFilter
import com.goog.effect.filter.GLSaturationFilter
import com.goog.effect.filter.GLScaleFilter
import com.goog.effect.filter.GLSepiaFilter
import com.goog.effect.filter.GLSharpenFilter
import com.goog.effect.filter.GLShiTomasiFeatureDetectorFilter
import com.goog.effect.filter.GLSketchFilter
import com.goog.effect.filter.GLSobelEdgeDetection3X3Filter
import com.goog.effect.filter.GLSolarizeFilter
import com.goog.effect.filter.GLSphereRefractionFilter
import com.goog.effect.filter.GLStretchDistortionFilter
import com.goog.effect.filter.GLSwirlFilter
import com.goog.effect.filter.GLThresholdEdgeDetection3X3Filter
import com.goog.effect.filter.GLThresholdedNonMaximumSuppression3X3Filter
import com.goog.effect.filter.GLToneFilter
import com.goog.effect.filter.GLUnsharpMaskFilter
import com.goog.effect.filter.GLVibranceFilter
import com.goog.effect.filter.GLVignetteFilter
import com.goog.effect.filter.GLWeakPixelInclusionFilter
import com.goog.effect.filter.GLWhiteBalanceFilter
import com.goog.effect.filter.GLXyDerivative3X3Filter
import com.goog.effect.filter.GLYuvConversionFullRangeFilter
import com.goog.effect.filter.GLYuvConversionFullRangeUVPlanarFilter
import com.goog.effect.filter.GLYuvConversionVideoRangeFilter
import com.goog.effect.filter.blend.GLAddBlendFilter
import com.goog.effect.filter.blend.GLChromaKeyBlendFilter
import com.goog.effect.filter.blur.GLBoxBlurFilter
import com.goog.effect.filter.blur.GLDirectionBlurFilter
import com.goog.effect.filter.blur.GLDualKawaseBlurFilter
import com.goog.effect.filter.blur.GLGaussianBlurFilter
import com.goog.effect.filter.blur.GLGrainyBlurFilter
import com.goog.effect.filter.blur.GLRadialBlurFilter
import com.goog.effect.filter.blur.GLZoomBlurFilter
import com.goog.effect.filter.core.GLConvolution3X3Filter

object DataSources {

    fun loadFiltersData(context: Context): List<FilterItem> {
        val list = mutableListOf<Class<*>>()

        list.add(GLDualKawaseBlurFilter::class.java)
        list.add((GLDualKawaseBlurFilter::class.java))
        list.add((GLGrainyBlurFilter::class.java))
        list.add((GLDirectionBlurFilter::class.java))
        list.add((GLRadialBlurFilter::class.java))
        //============================
        list.add((GLAdaptiveThresholdFilter::class.java))
        list.add((GLAverageColorFilter::class.java))

        //  list.add(FilterItem(GLAverageLuminanceFilter::class.java))

        list.add((GLBilateralFilter::class.java))
        //  list.add(FilterItem(GLBilateralBlurFilter::class.java))
        list.add((GLBoxBlurFilter::class.java))
        list.add((GLBrightnessFilter::class.java))
        list.add((GLBulgeDistortionFilter::class.java))

        list.add((GLCGAColorSpaceFilter::class.java))
        list.add((GLChromaKeyFilter::class.java))
        //  list.add(FilterItem(GLCircleFilter::class.java))
        list.add((GLColorFastDescriptorFilter::class.java))
        //  list.add(FilterItem(GLColorLocalNonaryPatternFilter::class.java))
        list.add((GLColorMatrixFilter::class.java))
        list.add((GLColorSwizzlingFilter::class.java))

        list.add((GLContrastFilter::class.java))
        list.add((GLCornerFilter::class.java))
        // list.add(FilterItem(GLCrosshairFilter::class.java))
        list.add((GLCrosshatchFilter::class.java))

        //list.add(FilterItem(GLDilationFilter::class.java))
        list.add((GLDirectionNonMaxSuppressionFilter::class.java))
        list.add((GLDirectionSobelEdgeDetectionFilter::class.java))
        //list.add(FilterItem(GLErosionFilter::class.java))
        list.add((GLExposureFilter::class.java))

        //list.add(FilterItem(GLFalseColorFilter::class.java))
        list.add((GLGammaFilter::class.java))

        list.add((GLGaussianBlurFilter::class.java))
        list.add((GLGlassSphereFilter::class.java))
        list.add((GLGrayScaleFilter::class.java))

        list.add((GLHalftoneFilter::class.java))
        list.add((GLHighlightShadowFilter::class.java))
        list.add((GLHighLightShadowTintFilter::class.java))
        // list.add(FilterItem(GLHistogramAccumulationFilter::class.java))
        list.add((GLHistogramBlueSamplingFilter::class.java))
        list.add((GLHistogramDisplayFilter::class.java))
        list.add((GLHistogramEqualizationGreenFilter::class.java))
        list.add((GLHistogramEqualizationLuminanceFilter::class.java))
        list.add((GLHistogramEqualizationRedFilter::class.java))
        list.add((GLHistogramEqualizationRGBFilter::class.java))
        //list.add(FilterItem(GLHistogramGreenSamplingFilter::class.java))
        //list.add(FilterItem(GLHistogramLuminanceSamplingFilter::class.java))
        list.add((GLHistogramRedSamplingFilter::class.java))
        list.add((GLHueBlendFilter::class.java))
        list.add((GLHueFilter::class.java))

        list.add((GLInvertFilter::class.java))

        list.add((GLKuwaharaFilter::class.java))
        list.add((GLKuwaharaRadius3Filter::class.java))

        list.add((GLLanczosResamplingFilter::class.java))
        list.add((GLLaplacian3X3Filter::class.java))

        list.add((GLLevelsFilter::class.java))
        //list.add(FilterItem(GLLineFilter::class.java))
        list.add((GLLocalBinaryPattern3X3Filter::class.java))
        list.add((GLLookupFilter::class.java))
        //list.add(FilterItem(GLLookUpTableFilter::class.java))
        list.add((GLLuminanceFilter::class.java))
        list.add((GLLuminanceRangeFilter::class.java))
        list.add((GLLuminanceThresholdFilter::class.java))

        //list.add(FilterItem(GLMedianFilter::class.java))
        list.add((GLMonochromeFilter::class.java))
        //list.add(FilterItem(GLMotionComparisonFilter::class.java))

        list.add((GLNobleCornerDetectorFilter::class.java))

        list.add((GLOpacityFilter::class.java))

        list.add((GLPassthroughFilter::class.java))
        list.add((GLPinchDistortionFilter::class.java))
        list.add((GLPixelateFilter::class.java))
        list.add((GLPolarPixelateFilter::class.java))
        list.add((GLPolkaDotFilter::class.java))
        list.add((GLPosterizeFilter::class.java))

        list.add((GLRGBFilter::class.java))

        list.add((GLSaturationFilter::class.java))
        list.add((GLScaleFilter::class.java))

        list.add((GLSepiaFilter::class.java))
        list.add((GLSharpenFilter::class.java))
        list.add((GLShiTomasiFeatureDetectorFilter::class.java))
        list.add((GLSketchFilter::class.java))
        list.add((GLSobelEdgeDetection3X3Filter::class.java))
        list.add((GLSolarizeFilter::class.java))
        list.add((GLSphereRefractionFilter::class.java))
        list.add((GLStretchDistortionFilter::class.java))
        //   list.add(FilterItem(GLSuperResolutionFilter::class.java))
        list.add((GLSwirlFilter::class.java))

        //     list.add(FilterItem(GLThreex3TextureSamplingFilter::class.java))
        list.add((GLThresholdEdgeDetection3X3Filter::class.java))
        list.add((GLThresholdedNonMaximumSuppression3X3Filter::class.java))
        //   list.add(FilterItem(GLThresholdSketchFilter::class.java))
        //  list.add(FilterItem(GLTiltShiftFilter::class.java))

        //  list.add(FilterItem(GLToneCurveFilter::class.java))

        list.add((GLToneFilter::class.java))
        //  list.add(FilterItem(GLTransformFilter::class.java))

        list.add((GLUnsharpMaskFilter::class.java))

        list.add((GLVibranceFilter::class.java))
        list.add((GLVignetteFilter::class.java))

        //   list.add(FilterItem(GLWatermarkFilter::class.java))

        list.add((GLWeakPixelInclusionFilter::class.java))
        list.add((GLWhiteBalanceFilter::class.java))

        list.add((GLXyDerivative3X3Filter::class.java))

        list.add((GLYuvConversionFullRangeFilter::class.java))
        list.add((GLYuvConversionFullRangeUVPlanarFilter::class.java))
        list.add((GLYuvConversionVideoRangeFilter::class.java))

        list.add((GLZoomBlurFilter::class.java))

        //Blend
        list.add((GLAddBlendFilter::class.java))
        //  list.add(FilterItem(GLAlphaBlendFilter::class.java))
        list.add((GLChromaKeyBlendFilter::class.java))
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

        return list.map { FilterConvert.parse(it) }.toList()
    }


}