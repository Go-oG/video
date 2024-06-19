precision mediump float;
precision mediump int;

varying highp vec4 vTextureCoord;
uniform mediump sampler2D sTexture;

uniform int operationMode;
uniform float edgeThreshold;
uniform float edgeSharpness;

///视图Rect 信息
//{1.0/original_tex_w,1.0/original_tex_h,original_tex_w,original_tex_h}?
uniform vec4 viewportInfo;

float fastLanczos2(float x) {
    float wA = x - 4.0;
    float wB = x * wA - wA;
    wA *= wA;
    return wB * wA;
}
vec2 weightY(float dx, float dy, float c, float std) {
    float x = ((dx * dx) + (dy * dy)) * 0.55 + clamp(abs(c) * std, 0.0, 1.0);
    float w = fastLanczos2(x);
    return vec2(w, w * c);
}

void main() {
    int mode = operationMode;
    vec4 color;
    if (mode == 1) {
        color.xyz = textureLod(sTexture, vTextureCoord.xy, 0.0).xyz;
    } else {
        color.xyzw = textureLod(sTexture, vTextureCoord.xy, 0.0).xyzw;
    }

    highp float xCenter;
    xCenter = abs(vTextureCoord.x + -0.5);
    highp float yCenter;
    yCenter = abs(vTextureCoord.y + -0.5);

    if (mode != 4) {
        highp vec2 imgCoord = ((vTextureCoord.xy * viewportInfo.zw) + vec2(-0.5, 0.5));
        highp vec2 imgCoordPixel = floor(imgCoord);
        highp vec2 coord = (imgCoordPixel * viewportInfo.xy);
        vec2 pl = (imgCoord + (-imgCoordPixel));
        vec4 left = textureGather(sTexture, coord, mode);

        float edgeVote = abs(left.z - left.y) + abs(color[mode] - left.y) + abs(color[mode] - left.z);
        if (edgeVote > edgeThreshold) {
            coord.x += viewportInfo.x;
            vec4 right = textureGather(sTexture, coord + vec2(viewportInfo.x, 0.0), mode);
            vec4 upDown;
            upDown.xy = textureGather(sTexture, coord + vec2(0.0, -viewportInfo.y), mode).wz;
            upDown.zw = textureGather(sTexture, coord + vec2(0.0, viewportInfo.y), mode).yx;

            float mean = (left.y + left.z + right.x + right.w) * 0.25;
            left = left - vec4(mean);
            right = right - vec4(mean);
            upDown = upDown - vec4(mean);
            color.w = color[mode] - mean;

            float sum = (((((abs(left.x) + abs(left.y)) + abs(left.z)) + abs(left.w)) + (((abs(right.x) + abs(right.y)) + abs(right.z)) + abs(right.w))) + (((abs(upDown.x) + abs(upDown.y)) + abs(upDown.z)) + abs(upDown.w)));
            float std = 2.181818 / sum;

            vec2 aWY = weightY(pl.x, pl.y + 1.0, upDown.x, std);
            aWY += weightY(pl.x - 1.0, pl.y + 1.0, upDown.y, std);
            aWY += weightY(pl.x - 1.0, pl.y - 2.0, upDown.z, std);
            aWY += weightY(pl.x, pl.y - 2.0, upDown.w, std);
            aWY += weightY(pl.x + 1.0, pl.y - 1.0, left.x, std);
            aWY += weightY(pl.x, pl.y - 1.0, left.y, std);
            aWY += weightY(pl.x, pl.y, left.z, std);
            aWY += weightY(pl.x + 1.0, pl.y, left.w, std);
            aWY += weightY(pl.x - 1.0, pl.y - 1.0, right.x, std);
            aWY += weightY(pl.x - 2.0, pl.y - 1.0, right.y, std);
            aWY += weightY(pl.x - 2.0, pl.y, right.z, std);
            aWY += weightY(pl.x - 1.0, pl.y, right.w, std);

            float finalY = aWY.y / aWY.x;

            float maxY = max(max(left.y, left.z), max(right.x, right.w));
            float minY = min(min(left.y, left.z), min(right.x, right.w));
            finalY = clamp(edgeSharpness * finalY, minY, maxY);

            float deltaY = finalY - color.w;

            //smooth high contrast input
            deltaY = clamp(deltaY, -23.0 / 255.0, 23.0 / 255.0);
            color.x = clamp((color.x + deltaY), 0.0, 1.0);
            color.y = clamp((color.y + deltaY), 0.0, 1.0);
            color.z = clamp((color.z + deltaY), 0.0, 1.0);
        }
    }
    //assume alpha channel is not used
    color.w = 1.0;
    gl_FragColor.xyzw = color;
}