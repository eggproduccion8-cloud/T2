package com.mundodetronos.animation;

import org.joml.Vector3f;

import java.util.List;

public class AnimationInterpolator {

    public static Vector3f interpolate(List<AnimationKeyframe> keyframes, float time, Vector3f defaultValue) {
        if (keyframes == null || keyframes.isEmpty()) {
            return defaultValue;
        }

        if (keyframes.size() == 1 || time <= keyframes.get(0).getTimestamp()) {
            return new Vector3f(keyframes.get(0).getValue());
        }

        if (time >= keyframes.get(keyframes.size() - 1).getTimestamp()) {
            return new Vector3f(keyframes.get(keyframes.size() - 1).getValue());
        }

        int prevIdx = 0;
        for (int i = 0; i < keyframes.size(); i++) {
            if (keyframes.get(i).getTimestamp() <= time) {
                prevIdx = i;
            } else {
                break;
            }
        }

        int nextIdx = Math.min(prevIdx + 1, keyframes.size() - 1);
        AnimationKeyframe kfPrev = keyframes.get(prevIdx);
        AnimationKeyframe kfNext = keyframes.get(nextIdx);

        float duration = kfNext.getTimestamp() - kfPrev.getTimestamp();
        float t = duration > 0 ? (time - kfPrev.getTimestamp()) / duration : 0.0f;

        if ("catmullrom".equalsIgnoreCase(kfPrev.getLerpMode()) && keyframes.size() >= 4) {
            int p0 = Math.max(0, prevIdx - 1);
            int p1 = prevIdx;
            int p2 = nextIdx;
            int p3 = Math.min(keyframes.size() - 1, nextIdx + 1);

            return catmullRom(
                    keyframes.get(p0).getValue(),
                    keyframes.get(p1).getValue(),
                    keyframes.get(p2).getValue(),
                    keyframes.get(p3).getValue(),
                    t
            );
        } else {
            return lerp(kfPrev.getValue(), kfNext.getValue(), t);
        }
    }

    private static Vector3f lerp(Vector3f v0, Vector3f v1, float t) {
        return new Vector3f(v0).lerp(v1, t);
    }

    private static Vector3f catmullRom(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, float t) {
        float t2 = t * t;
        float t3 = t2 * t;

        float f0 = -0.5f * t3 + t2 - 0.5f * t;
        float f1 = 1.5f * t3 - 2.5f * t2 + 1.0f;
        float f2 = -1.5f * t3 + 2.0f * t2 + 0.5f * t;
        float f3 = 0.5f * t3 - 0.5f * t2;

        return new Vector3f(
                p0.x * f0 + p1.x * f1 + p2.x * f2 + p3.x * f3,
                p0.y * f0 + p1.y * f1 + p2.y * f2 + p3.y * f3,
                p0.z * f0 + p1.z * f1 + p2.z * f2 + p3.z * f3
        );
    }
}
