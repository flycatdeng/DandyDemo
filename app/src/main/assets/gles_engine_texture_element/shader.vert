precision mediump float;

attribute vec4 aPosition;
attribute vec4 aTexCoor;
varying vec2 vTextureCoord;

void main()
{
    gl_Position = aPosition;
    vTextureCoord.x = aTexCoor.x;
    vTextureCoord.y = 1.0-aTexCoor.y;
}