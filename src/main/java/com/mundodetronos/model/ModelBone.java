package com.mundodetronos.model;

import org.joml.Vector3f;

import java.util.List;

public class ModelBone {
    private final String name;
    private final Vector3f pivot;
    private final Vector3f rotation;
    private final List<ModelCube> cubes;
    private final List<ModelBone> children;

    public ModelBone(String name, Vector3f pivot, Vector3f rotation, List<ModelCube> cubes, List<ModelBone> children) {
        this.name = name;
        this.pivot = pivot;
        this.rotation = rotation;
        this.cubes = cubes;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public Vector3f getPivot() {
        return pivot;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public List<ModelCube> getCubes() {
        return cubes;
    }

    public List<ModelBone> getChildren() {
        return children;
    }
}
