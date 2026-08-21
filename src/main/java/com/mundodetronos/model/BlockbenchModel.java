package com.mundodetronos.model;

import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class BlockbenchModel {
    private final List<ModelBone> rootBones;
    private final Map<String, ModelBone> boneMap;
    private final int textureWidth;
    private final int textureHeight;

    public BlockbenchModel(List<ModelBone> rootBones, Map<String, ModelBone> boneMap, int textureWidth, int textureHeight) {
        this.rootBones = rootBones;
        this.boneMap = boneMap;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public List<ModelBone> getRootBones() {
        return rootBones;
    }

    public Map<String, ModelBone> getBoneMap() {
        return boneMap;
    }

    public ModelBone getBone(String name) {
        return boneMap.get(name);
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }
}
