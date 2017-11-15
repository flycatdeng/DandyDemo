package com.dandy.module.ms3dload;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 * </pre>
 * <pre>
 * Created by Dandy
 * Wechat: flycatdeng
 * </pre>
 */

public class MS3DMaterial implements Serializable {
    private String name;        //材质名称
    private float[] ambient_color;    //环境光
    private float[] diffuse_color;    //散射光
    private float[] specular_color;    //镜面光
    private float[] emissive_color;    //自发光
    private float shininess;        //粗糙度 0-128
    private float transparency;        //透明度 0-1
    private byte flagMode;//暂时没有作用
    private String textureName;        //材质文件名称
    private String transparencyTextureName;//材质对应透明度贴图文件的路劲

    public MS3DMaterial(String name, float[] ambient_color, float[] diffuse_color, float[] specular_color,
                        float[] emissive_color, float shininess, float transparency, byte flagMode,
                        String textureName, String transparencyTextureName) {
        this.name = name;
        this.ambient_color = ambient_color;
        this.diffuse_color = diffuse_color;
        this.specular_color = specular_color;
        this.emissive_color = emissive_color;
        this.shininess = shininess;
        this.transparency = transparency;
        this.textureName = textureName;
        this.flagMode = flagMode;
        this.transparencyTextureName = transparencyTextureName;
    }

    public byte getFlagMode() {
        return flagMode;
    }

    public String getTransparencyTextureName() {
        return transparencyTextureName;
    }

    public String getName() {
        return name;
    }

    public float[] getAmbient_color() {
        return ambient_color;
    }

    public float[] getDiffuse_color() {
        return diffuse_color;
    }

    public float[] getSpecular_color() {
        return specular_color;
    }

    public float[] getEmissive_color() {
        return emissive_color;
    }

    public float getShininess() {
        return shininess;
    }

    public float getTransparency() {
        return transparency;
    }

    public String getTextureName() {
        return textureName;
    }

    @Override
    public String toString() {
        return "MS3DMaterial{" +
                "name='" + name + '\'' +
                ", ambient_color=" + Arrays.toString(ambient_color) +
                ", diffuse_color=" + Arrays.toString(diffuse_color) +
                ", specular_color=" + Arrays.toString(specular_color) +
                ", emissive_color=" + Arrays.toString(emissive_color) +
                ", shininess=" + shininess +
                ", transparency=" + transparency +
                ", textureName='" + textureName + '\'' +
                '}';
    }
}
