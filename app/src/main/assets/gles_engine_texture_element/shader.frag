precision highp float;

uniform sampler2D uTexture;

varying vec2 vTextureCoord;

void main()
{
    gl_FragColor = texture2D(uTexture, vTextureCoord);
    //gl_FragColor=vec4(0.0,1.0,0.0,1.0);
}