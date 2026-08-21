package com.mundodetronos.model;

import com.mundodetronos.MundoDeTronos;
import com.mundodetronos.animation.AnimationDefinition;
import com.mundodetronos.animation.AnimationParser;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ModelCache {
    private static final Map<String, BlockbenchModel> MODEL_CACHE = new HashMap<>();
    private static final Map<String, Map<String, AnimationDefinition>> ANIMATION_CACHE = new HashMap<>();

    public static BlockbenchModel getModel(String modelId, String fileLocation) {
        return MODEL_CACHE.computeIfAbsent(modelId, k -> {
            try {
                ResourceLocation loc = new ResourceLocation(MundoDeTronos.MOD_ID, fileLocation);
                InputStream is = Minecraft.getInstance().getResourceManager().open(loc);
                return BlockbenchModelParser.parse(is);
            } catch (Exception e) {
                MundoDeTronos.LOGGER.error("Failed to load model: " + modelId + " at " + fileLocation, e);
                return null;
            }
        });
    }

    public static Map<String, AnimationDefinition> getAnimations(String animId, String fileLocation) {
        return ANIMATION_CACHE.computeIfAbsent(animId, k -> {
            try {
                ResourceLocation loc = new ResourceLocation(MundoDeTronos.MOD_ID, fileLocation);
                InputStream is = Minecraft.getInstance().getResourceManager().open(loc);
                return AnimationParser.parse(is);
            } catch (Exception e) {
                MundoDeTronos.LOGGER.error("Failed to load animations: " + animId + " at " + fileLocation, e);
                return new HashMap<>();
            }
        });
    }

    public static void clear() {
        MODEL_CACHE.clear();
        ANIMATION_CACHE.clear();
    }
}
