precision highp float;
uniform sampler2D uTexture;//纹理内容数据,可能不传
uniform float uHasTexture;//是否有纹理图，0无1有

varying highp vec2 vTextureCoord;
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying float vHasTexCoor;//是否有纹理坐标（有些模型可能没有纹理坐标），0无1有

void main()
{
    vec4 originColor=vec4(0.9,0.9,0.9,1.0);
    if(uHasTexture>0.5&&vHasTexCoor>0.5){//有纹理图，有纹理坐标
        originColor=texture2D(uTexture, vTextureCoord);
    }
   gl_FragColor=originColor*ambient+originColor*specular+originColor*diffuse;
   //gl_FragColor=finalColor;
}