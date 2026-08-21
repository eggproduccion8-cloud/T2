package com.mundodetronos.animation;

import java.util.Map;

public class AnimationDefinition {
    private final String name;
    private final float animationLength;
    private final boolean loop;
    private final Map<String, BoneAnimationChannel> boneChannels;

    public AnimationDefinition(String name, float animationLength, boolean loop, Map<String, BoneAnimationChannel> boneChannels) {
        this.name = name;
        this.animationLength = animationLength;
        this.loop = loop;
        this.boneChannels = boneChannels;
    }

    public String getName() {
        return name;
    }

    public float getAnimationLength() {
        return animationLength;
    }

    public boolean isLoop() {
        return loop;
    }

    public Map<String, BoneAnimationChannel> getBoneChannels() {
        return boneChannels;
    }

    public BoneAnimationChannel getChannel(String boneName) {
        return boneChannels.get(boneName);
    }
}
