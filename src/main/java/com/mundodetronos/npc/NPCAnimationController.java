package com.mundodetronos.npc;

import com.mundodetronos.animation.AnimationDefinition;
import com.mundodetronos.animation.AnimationInterpolator;
import com.mundodetronos.animation.BoneAnimationChannel;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class NPCAnimationController {

    public enum State {
        IDLE,
        WALK,
        TEMPORARY_ACTION,
        COMBAT,
        SPECIAL
    }

    private State currentState = State.IDLE;
    private String currentAnimationName = "idle";
    private float animationTime = 0.0f;
    private boolean isTemporaryPlaying = false;
    private String temporaryAnimationName = null;

    private Map<String, AnimationDefinition> availableAnimations = new HashMap<>();

    public NPCAnimationController() {
    }

    public void setAnimations(Map<String, AnimationDefinition> animations) {
        this.availableAnimations = animations != null ? animations : new HashMap<>();
    }

    public void update(float deltaSeconds, boolean isMoving) {
        animationTime += deltaSeconds;

        if (isTemporaryPlaying && temporaryAnimationName != null) {
            AnimationDefinition tempAnim = availableAnimations.get(temporaryAnimationName);
            if (tempAnim != null) {
                if (animationTime >= tempAnim.getAnimationLength() && !tempAnim.isLoop()) {
                    isTemporaryPlaying = false;
                    temporaryAnimationName = null;
                    currentState = isMoving ? State.WALK : State.IDLE;
                    setAnimationInternal(isMoving ? "walk" : "idle");
                }
            } else {
                isTemporaryPlaying = false;
                temporaryAnimationName = null;
            }
        }

        if (!isTemporaryPlaying) {
            State desiredState = isMoving ? State.WALK : State.IDLE;
            String desiredAnim = isMoving ? "walk" : "idle";

            if (currentState != desiredState || !currentAnimationName.equals(desiredAnim)) {
                currentState = desiredState;
                setAnimationInternal(desiredAnim);
            }
        }
    }

    public void playTemporaryAnimation(String animName) {
        AnimationDefinition anim = availableAnimations.get(animName);
        if (anim != null) {
            this.isTemporaryPlaying = true;
            this.temporaryAnimationName = animName;
            this.currentState = State.TEMPORARY_ACTION;
            setAnimationInternal(animName);
        }
    }

    private void setAnimationInternal(String animName) {
        if (!this.currentAnimationName.equals(animName)) {
            this.currentAnimationName = animName;
            this.animationTime = 0.0f;
        }
    }

    public Transform getBoneTransform(String boneName) {
        AnimationDefinition anim = availableAnimations.get(currentAnimationName);
        if (anim == null) {
            return new Transform(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        }

        float time = animationTime;
        if (anim.isLoop() && anim.getAnimationLength() > 0) {
            time = animationTime % anim.getAnimationLength();
        } else {
            time = Math.min(animationTime, anim.getAnimationLength());
        }

        BoneAnimationChannel channel = anim.getChannel(boneName);
        if (channel == null) {
            return new Transform(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        }

        Vector3f rot = AnimationInterpolator.interpolate(channel.getRotationKeyframes(), time, new Vector3f(0, 0, 0));
        Vector3f pos = AnimationInterpolator.interpolate(channel.getPositionKeyframes(), time, new Vector3f(0, 0, 0));
        Vector3f scale = AnimationInterpolator.interpolate(channel.getScaleKeyframes(), time, new Vector3f(1, 1, 1));

        return new Transform(rot, pos, scale);
    }

    public State getCurrentState() {
        return currentState;
    }

    public String getCurrentAnimationName() {
        return currentAnimationName;
    }

    public static class Transform {
        public final Vector3f rotation;
        public final Vector3f position;
        public final Vector3f scale;

        public Transform(Vector3f rotation, Vector3f position, Vector3f scale) {
            this.rotation = rotation;
            this.position = position;
            this.scale = scale;
        }
    }
}
