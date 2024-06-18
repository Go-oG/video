 precision mediump float;

 varying vec2 vTextureCoord;
 uniform sampler2D sTexture;

 varying vec2 leftTextureCoord;
 varying vec2 rightTextureCoord;
 
 varying vec2 topTextureCoord;
 varying vec2 topLeftTextureCoord;
 varying vec2 topRightTextureCoord;
 
 varying vec2 bottomTextureCoord;
 varying vec2 bottomLeftTextureCoord;
 varying vec2 bottomRightTextureCoord;

#define s2(a, b)                temp = a; a = min(a, b); b = max(temp, b);
#define mn3(a, b, c)            s2(a, b); s2(a, c);
#define mx3(a, b, c)            s2(b, c); s2(a, c);
 
#define mnmx3(a, b, c)          mx3(a, b, c); s2(a, b);                                   // 3 exchanges
#define mnmx4(a, b, c, d)       s2(a, b); s2(c, d); s2(a, c); s2(b, d);                   // 4 exchanges
#define mnmx5(a, b, c, d, e)    s2(a, b); s2(c, d); mn3(a, c, e); mx3(b, d, e);           // 6 exchanges
#define mnmx6(a, b, c, d, e, f) s2(a, d); s2(b, e); s2(c, f); mn3(a, b, c); mx3(d, e, f); // 7 exchanges

 void main(){
     vec3 v[6];

     v[0] = texture2D(inputImageTexture, bottomLeftTextureCoord).rgb;
     v[1] = texture2D(inputImageTexture, topRightTextureCoord).rgb;
     v[2] = texture2D(inputImageTexture, topLeftTextureCoord).rgb;
     v[3] = texture2D(inputImageTexture, bottomRightTextureCoord).rgb;
     v[4] = texture2D(inputImageTexture, leftTextureCoord).rgb;
     v[5] = texture2D(inputImageTexture, rightTextureCoord).rgb;
//     v[6] = texture2D(inputImageTexture, bottomTextureCoord).rgb;
//     v[7] = texture2D(inputImageTexture, topTextureCoord).rgb;
     vec3 temp;

     mnmx6(v[0], v[1], v[2], v[3], v[4], v[5]);
     
     v[5] = texture2D(sTexture, bottomTextureCoord).rgb;
                  
     mnmx5(v[1], v[2], v[3], v[4], v[5]);
                  
     v[5] = texture2D(sTexture, topTextureCoord).rgb;
                               
     mnmx4(v[2], v[3], v[4], v[5]);
                               
     v[5] = texture2D(sTexture, vTextureCoord).rgb;
                                            
     mnmx3(v[3], v[4], v[5]);
    
     gl_FragColor = vec4(v[4], 1.0);
 }