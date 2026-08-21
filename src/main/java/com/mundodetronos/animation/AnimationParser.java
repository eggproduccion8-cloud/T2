package com.mundodetronos.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joml.Vector3f;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AnimationParser {

    public static Map<String, AnimationDefinition> parse(InputStream inputStream) {
        JsonObject root = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
        Map<String, AnimationDefinition> animations = new HashMap<>();

        if (root.has("animations")) {
            JsonObject animsObj = root.getAsJsonObject("animations");
            for (Map.Entry<String, JsonElement> entry : animsObj.entrySet()) {
                String animName = entry.getKey();
                if (entry.getValue().isJsonObject()) {
                    AnimationDefinition animDef = parseSingleAnimation(animName, entry.getValue().getAsJsonObject());
                    animations.put(animName, animDef);
                }
            }
        }

        return animations;
    }

    private static AnimationDefinition parseSingleAnimation(String name, JsonObject obj) {
        float length = obj.has("animation_length") ? obj.get("animation_length").getAsFloat() : 1.0f;
        boolean loop = false;
        if (obj.has("loop")) {
            JsonElement loopElem = obj.get("loop");
            if (loopElem.isJsonPrimitive() && loopElem.getAsJsonPrimitive().isBoolean()) {
                loop = loopElem.getAsBoolean();
            } else if (loopElem.isJsonPrimitive() && loopElem.getAsJsonPrimitive().isString()) {
                loop = "true".equalsIgnoreCase(loopElem.getAsString());
            }
        }

        Map<String, BoneAnimationChannel> boneChannels = new HashMap<>();

        if (obj.has("bones")) {
            JsonObject bonesObj = obj.getAsJsonObject("bones");
            for (Map.Entry<String, JsonElement> entry : bonesObj.entrySet()) {
                String boneName = entry.getKey();
                if (entry.getValue().isJsonObject()) {
                    BoneAnimationChannel channel = parseBoneChannel(entry.getValue().getAsJsonObject());
                    boneChannels.put(boneName, channel);
                }
            }
        }

        return new AnimationDefinition(name, length, loop, boneChannels);
    }

    private static BoneAnimationChannel parseBoneChannel(JsonObject obj) {
        List<AnimationKeyframe> rotationKeyframes = parseKeyframeTrack(obj, "rotation");
        List<AnimationKeyframe> positionKeyframes = parseKeyframeTrack(obj, "position");
        List<AnimationKeyframe> scaleKeyframes = parseKeyframeTrack(obj, "scale");

        return new BoneAnimationChannel(rotationKeyframes, positionKeyframes, scaleKeyframes);
    }

    private static List<AnimationKeyframe> parseKeyframeTrack(JsonObject obj, String trackName) {
        List<AnimationKeyframe> keyframes = new ArrayList<>();
        if (!obj.has(trackName)) return keyframes;

        JsonElement trackElem = obj.get(trackName);

        if (trackElem.isJsonArray()) {
            Vector3f val = parseVector3f(trackElem.getAsJsonArray());
            keyframes.add(new AnimationKeyframe(0.0f, val, "linear"));
        } else if (trackElem.isJsonObject()) {
            JsonObject trackObj = trackElem.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : trackObj.entrySet()) {
                try {
                    float timestamp = Float.parseFloat(entry.getKey());
                    Vector3f val = new Vector3f(0, 0, 0);
                    String lerpMode = "linear";

                    if (entry.getValue().isJsonArray()) {
                        val = parseVector3f(entry.getValue().getAsJsonArray());
                    } else if (entry.getValue().isJsonObject()) {
                        JsonObject kfObj = entry.getValue().getAsJsonObject();
                        if (kfObj.has("post")) {
                            JsonElement postElem = kfObj.get("post");
                            if (postElem.isJsonArray()) {
                                val = parseVector3f(postElem.getAsJsonArray());
                            }
                        }
                        if (kfObj.has("lerp_mode")) {
                            lerpMode = kfObj.get("lerp_mode").getAsString();
                        }
                    }

                    keyframes.add(new AnimationKeyframe(timestamp, val, lerpMode));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        keyframes.sort(Comparator.comparingDouble(AnimationKeyframe::getTimestamp));
        return keyframes;
    }

    private static Vector3f parseVector3f(com.google.gson.JsonArray arr) {
        float x = arr.size() > 0 ? arr.get(0).getAsFloat() : 0.0f;
        float y = arr.size() > 1 ? arr.get(1).getAsFloat() : 0.0f;
        float z = arr.size() > 2 ? arr.get(2).getAsFloat() : 0.0f;
        return new Vector3f(x, y, z);
    }
}
