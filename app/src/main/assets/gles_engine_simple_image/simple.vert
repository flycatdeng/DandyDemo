precision highp float;

attribute vec4 position;
attribute vec4 inputTextureCoordinate;
varying vec2 textureCoordinate;
 
void main() {
    gl_Position = position;
    textureCoordinate.x=inputTextureCoordinate.x;
    textureCoordinate.y = 1.0-inputTextureCoordinate.y;
}