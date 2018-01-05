precision highp float;

attribute vec3 aPosition;  //顶点位置
attribute vec3 aNormal;    //顶点法向量
attribute vec2 aTexCoor;    //顶点纹理坐标

uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uModelMatrix; //变换矩阵
uniform mat4 uViewMatrix; //相机位置矩阵
uniform mat4 uModelViewMatrix; //变换矩阵

uniform vec3 uLightLocation;	//光源位置
uniform vec3 uCameraLocation;	//摄像机位置
uniform float uHasTexCoor;//是否有纹理坐标（有些模型可能没有纹理坐标），0无1有

uniform float uAmbientInstensity;//环境光强
uniform vec4 uLightAttrib;//r:定向光还是定位光0-定向光，1-定位光；g:散射光光强；b:镜面光光强；a:镜面光粗糙度

//用于传递给片元着色器的变量
varying highp vec2 vTextureCoord;
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying float vHasTexCoor;

//定向光光照计算的方法
void directLight(					//定位光光照计算的方法
  in vec3 position,              //顶点位置
  in vec3 normal,				//法向量
  in float shininess,           //镜面光粗糙度，越小越光滑
  inout vec4 ambient,			//环境光最终强度
  inout vec4 diffuse,				//散射光最终强度
  inout vec4 specular,			//镜面光最终强度
  in vec3 lightLocation,			//光源位置
  in vec4 lightAmbient,			//环境光强度
  in vec4 lightDiffuse,			//散射光强度
  in vec4 lightSpecular			//镜面光强度
){
  ambient=lightAmbient;			//直接得出环境光的最终强度
  vec3 normalTarget=position+normal;	//计算变换后的法向量
  vec3 newNormal=(uModelMatrix*vec4(normalTarget,1)).xyz-(uModelMatrix*vec4(position,1)).xyz;
  newNormal=normalize(newNormal); 	//对法向量规格化
  newNormal=normal;
	   vec3 eye= normalize(uCameraLocation);
	  //计算从表面点到光源位置的向量vp
	  	  vec3 vp= normalize(lightLocation);
	  vp=normalize(vp);//格式化vp
	  vec3 halfVector=normalize(vp+eye);	//求视线与光线的半向量
	  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//求法向量与vp的点积与0的最大值
	  diffuse=lightDiffuse*nDotViewPosition;				//计算散射光的最终强度
	  float nDotViewHalfVector=dot(newNormal,halfVector);	//法线与半向量的点积
	  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//镜面反射光强度因子
	  specular=lightSpecular*powerFactor;    			//计算镜面光的最终强度
}
//定位光光照计算的方法
void pointLight(					//定位光光照计算的方法
  in vec3 position,              //顶点位置
  in vec3 normal,				//法向量
  in float shininess,           //镜面光粗糙度，越小越光滑
  inout vec4 ambient,			//环境光最终强度
  inout vec4 diffuse,				//散射光最终强度
  inout vec4 specular,			//镜面光最终强度
  in vec3 lightLocation,			//光源位置
  in vec4 lightAmbient,			//环境光强度
  in vec4 lightDiffuse,			//散射光强度
  in vec4 lightSpecular			//镜面光强度
){
  ambient=lightAmbient;			//直接得出环境光的最终强度
  vec3 normalTarget=position+normal;	//计算变换后的法向量
  vec3 newNormal=(uModelMatrix*vec4(normalTarget,1)).xyz-(uModelMatrix*vec4(position,1)).xyz;
  newNormal=normalize(newNormal); 	//对法向量规格化
	   vec3 eye= normalize(uCameraLocation);
	  //计算从表面点到光源位置的向量vp
	  	  vec3 vp= normalize(lightLocation);
	  vp=normalize(vp);//格式化vp
	  vec3 halfVector=normalize(vp+eye);	//求视线与光线的半向量
	  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//求法向量与vp的点积与0的最大值
	  diffuse=lightDiffuse*nDotViewPosition;				//计算散射光的最终强度
	  float nDotViewHalfVector=dot(newNormal,halfVector);	//法线与半向量的点积
	  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//镜面反射光强度因子
	  specular=lightSpecular*powerFactor;    			//计算镜面光的最终强度
}


void main()
{
    vec3 finalPosition=aPosition;
    gl_Position = uMVPMatrix * vec4(finalPosition,1); //根据总变换矩阵计算此次绘制此顶点位置

    if(uHasTexCoor>0.5){
       vTextureCoord.x=aTexCoor.x;
       vTextureCoord.y=1.0-aTexCoor.y;
    }
    vHasTexCoor=uHasTexCoor;

   vec4 ambientTemp, diffuseTemp, specularTemp;   //存放环境光、散射光、镜面反射光的临时变量
   if(uLightAttrib.r<0.5){//定向光
        directLight(finalPosition,normalize(aNormal),uLightAttrib.a,ambientTemp,diffuseTemp,specularTemp,uLightLocation,
                    vec4(vec3(uAmbientInstensity),1.0),vec4(vec3(uLightAttrib.g),1.0),vec4(vec3(uLightAttrib.b),1.0));
   }else{//定位光
        pointLight(finalPosition,normalize(aNormal),uLightAttrib.a,ambientTemp,diffuseTemp,specularTemp,uLightLocation,
                    vec4(vec3(uAmbientInstensity),1.0),vec4(vec3(uLightAttrib.g),1.0),vec4(vec3(uLightAttrib.b),1.0));
   }
   ambient=ambientTemp;
   diffuse=diffuseTemp;
   specular=specularTemp;
}