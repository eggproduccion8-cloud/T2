package com.mundodetronos.model;

import org.joml.Vector3f;
import java.util.Map;

public class ModelCube {
    private final Vector3f from;
    private final Vector3f to;
    private final Vector3f origin;
    private final Vector3f rotation;
    private final Map<String, Face> faces;

    public ModelCube(Vector3f from, Vector3f to, Vector3f origin, Vector3f rotation, Map<String, Face> faces) {
        this.from = from;
        this.to = to;
        this.origin = origin;
        this.rotation = rotation;
        this.faces = faces;
    }

    public Vector3f getFrom() {
        return from;
    }

    public Vector3f getTo() {
        return to;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Map<String, Face> getFaces() {
        return faces;
    }

    public static class Face {
        private final float u1, v1, u2, v2;
        private final int rotation;
        private final int textureIndex;

        public Face(float u1, float v1, float u2, float v2, int rotation, int textureIndex) {
            this.u1 = u1;
            this.v1 = v1;
            this.u2 = u2;
            this.v2 = v2;
            this.rotation = rotation;
            this.textureIndex = textureIndex;
        }

        public float getU1() { return u1; }
        public float getV1() { return v1; }
        public float getU2() { return u2; }
        public float getV2() { return v2; }
        public int getRotation() { return rotation; }
        public int getTextureIndex() { return textureIndex; }
    }
}
