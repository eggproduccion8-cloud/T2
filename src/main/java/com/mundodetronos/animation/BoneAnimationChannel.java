package com.mundodetronos.animation;

import java.util.List;

public class BoneAnimationChannel {
    private final List<AnimationKeyframe> rotationKeyframes;
    private final List<AnimationKeyframe> positionKeyframes;
    private final List<AnimationKeyframe> scaleKeyframes;

    public BoneAnimationChannel(List<AnimationKeyframe> rotationKeyframes, List<AnimationKeyframe> positionKeyframes, List<AnimationKeyframe> scaleKeyframes) {
        this.rotationKeyframes = rotationKeyframes;
        this.positionKeyframes = positionKeyframes;
        this.scaleKeyframes = scaleKeyframes;
    }

    public List<AnimationKeyframe> getRotationKeyframes() {
        return rotationKeyframes;
    }

    public List<AnimationKeyframe> getPositionKeyframes() {
        return positionKeyframes;
    }

    public List<AnimationKeyframe> getScaleKeyframes() {
        return scaleKeyframes;
    }
}
