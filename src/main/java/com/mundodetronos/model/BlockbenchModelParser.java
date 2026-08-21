package com.mundodetronos.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joml.Vector3f;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BlockbenchModelParser {

    public static BlockbenchModel parse(InputStream inputStream) {
        JsonObject root = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();

        int texWidth = 64;
        int texHeight = 64;
        if (root.has("resolution")) {
            JsonObject res = root.getAsJsonObject("resolution");
            if (res.has("width")) texWidth = res.get("width").getAsInt();
            if (res.has("height")) texHeight = res.get("height").getAsInt();
        }

        Map<String, ModelCube> elementsByUuid = new HashMap<>();
        if (root.has("elements")) {
            JsonArray elements = root.getAsJsonArray("elements");
            for (JsonElement elem : elements) {
                if (!elem.isJsonObject()) continue;
                JsonObject obj = elem.getAsJsonObject();
                ModelCube cube = parseCube(obj);
                String uuid = obj.has("uuid") ? obj.get("uuid").getAsString() : UUID.randomUUID().toString();
                elementsByUuid.put(uuid, cube);
            }
        }

        List<ModelBone> rootBones = new ArrayList<>();
        Map<String, ModelBone> boneMapByName = new HashMap<>();

        if (root.has("outliner")) {
            JsonArray outliner = root.getAsJsonArray("outliner");
            for (JsonElement item : outliner) {
                if (item.isJsonObject()) {
                    ModelBone bone = parseBone(item.getAsJsonObject(), elementsByUuid, boneMapByName);
                    if (bone != null) {
                        rootBones.add(bone);
                    }
                }
            }
        }

        return new BlockbenchModel(rootBones, boneMapByName, texWidth, texHeight);
    }

    private static ModelBone parseBone(JsonObject obj, Map<String, ModelCube> elementsByUuid, Map<String, ModelBone> boneMapByName) {
        String name = obj.has("name") ? obj.get("name").getAsString() : "bone";

        Vector3f pivot = new Vector3f(0, 0, 0);
        if (obj.has("origin")) {
            JsonArray orig = obj.getAsJsonArray("origin");
            pivot.set(orig.get(0).getAsFloat(), orig.get(1).getAsFloat(), orig.get(2).getAsFloat());
        }

        Vector3f rotation = new Vector3f(0, 0, 0);
        if (obj.has("rotation")) {
            JsonArray rot = obj.getAsJsonArray("rotation");
            rotation.set(rot.get(0).getAsFloat(), rot.get(1).getAsFloat(), rot.get(2).getAsFloat());
        }

        List<ModelCube> cubes = new ArrayList<>();
        List<ModelBone> children = new ArrayList<>();

        if (obj.has("children")) {
            JsonArray childArr = obj.getAsJsonArray("children");
            for (JsonElement childElem : childArr) {
                if (childElem.isJsonPrimitive()) {
                    String uuid = childElem.getAsString();
                    if (elementsByUuid.containsKey(uuid)) {
                        cubes.add(elementsByUuid.get(uuid));
                    }
                } else if (childElem.isJsonObject()) {
                    ModelBone childBone = parseBone(childElem.getAsJsonObject(), elementsByUuid, boneMapByName);
                    if (childBone != null) {
                        children.add(childBone);
                    }
                }
            }
        }

        ModelBone bone = new ModelBone(name, pivot, rotation, cubes, children);
        boneMapByName.put(name, bone);
        return bone;
    }

    private static ModelCube parseCube(JsonObject obj) {
        Vector3f from = new Vector3f(0, 0, 0);
        if (obj.has("from")) {
            JsonArray arr = obj.getAsJsonArray("from");
            from.set(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
        }

        Vector3f to = new Vector3f(0, 0, 0);
        if (obj.has("to")) {
            JsonArray arr = obj.getAsJsonArray("to");
            to.set(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
        }

        Vector3f origin = new Vector3f(0, 0, 0);
        if (obj.has("origin")) {
            JsonArray arr = obj.getAsJsonArray("origin");
            origin.set(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
        }

        Vector3f rotation = new Vector3f(0, 0, 0);
        if (obj.has("rotation")) {
            JsonArray arr = obj.getAsJsonArray("rotation");
            rotation.set(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
        }

        Map<String, ModelCube.Face> faces = new HashMap<>();
        if (obj.has("faces")) {
            JsonObject facesObj = obj.getAsJsonObject("faces");
            for (Map.Entry<String, JsonElement> entry : facesObj.entrySet()) {
                if (!entry.getValue().isJsonObject()) continue;
                JsonObject faceObj = entry.getValue().getAsJsonObject();
                float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
                if (faceObj.has("uv")) {
                    JsonArray uvArr = faceObj.getAsJsonArray("uv");
                    u1 = uvArr.get(0).getAsFloat();
                    v1 = uvArr.get(1).getAsFloat();
                    u2 = uvArr.get(2).getAsFloat();
                    v2 = uvArr.get(3).getAsFloat();
                }
                int rotationDeg = faceObj.has("rotation") ? faceObj.get("rotation").getAsInt() : 0;
                faces.put(entry.getKey(), new ModelCube.Face(u1, v1, u2, v2, rotationDeg));
            }
        }

        return new ModelCube(from, to, origin, rotation, faces);
    }
}
