package com.mundodetronos.animation;

import org.joml.Vector3f;

public class AnimationKeyframe {
    private final float timestamp;
    private final Vector3f value;
    private final String lerpMode;

    public AnimationKeyframe(float timestamp, Vector3f value, String lerpMode) {
        this.timestamp = timestamp;
        this.value = value;
        this.lerpMode = lerpMode;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public Vector3f getValue() {
        return value;
    }

    public String getLerpMode() {
        return lerpMode;
    }
}
