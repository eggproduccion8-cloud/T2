package com.mundodetronos.util;

import com.mundodetronos.MundoDeTronos;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class AssetTextureResolver {

    public static ResourceLocation resolveEntityTexture(String rawName) {
        String clean = normalizeName(rawName);
        if ("dummy".equals(clean)) {
            return new ResourceLocation(MundoDeTronos.MOD_ID, "textures/entity/dummy.png");
        }
        return new ResourceLocation(MundoDeTronos.MOD_ID, "textures/entity/" + clean + ".png");
    }

    public static ResourceLocation resolveBlockTexture(String rawName) {
        String clean = normalizeName(rawName);
        if ("crate_1".equals(clean)) clean = "crate_lvl1";
        if ("crate_2".equals(clean)) clean = "crate_lvl2";
        if ("crate_3".equals(clean)) clean = "crate_lvl3";
        if ("crate_4".equals(clean)) clean = "crate_lvl4";

        return new ResourceLocation(MundoDeTronos.MOD_ID, "textures/block/" + clean + ".png");
    }

    public static String normalizeName(String raw) {
        if (raw == null) return "missing";
        String s = raw.trim().toLowerCase(Locale.ROOT);
        if (s.contains(":")) {
            s = s.substring(s.indexOf(':') + 1);
        }
        if (s.contains("/")) {
            s = s.substring(s.lastIndexOf('/') + 1);
        }
        if (s.endsWith(".png")) {
            s = s.substring(0, s.length() - 4);
        }
        return s.replace(" ", "_");
    }
}
