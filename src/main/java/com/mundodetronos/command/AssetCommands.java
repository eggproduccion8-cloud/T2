package com.mundodetronos.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class AssetCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mundoassets")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("validate")
                        .executes(ctx -> validateAssets(ctx.getSource())))
                .then(Commands.literal("test")
                        .executes(ctx -> testAssets(ctx.getSource())))
                .then(Commands.literal("test_npcs")
                        .executes(ctx -> testAssets(ctx.getSource())))
                .then(Commands.literal("test_blocks")
                        .executes(ctx -> testAssets(ctx.getSource())))
        );
    }

    private static int validateAssets(CommandSourceStack source) {
        String[] npcs = {"guard", "archer", "blacksmith", "butcher", "farmer", "wizard", "adventurer", "king", "miner", "pirate", "tavern"};
        String[] blocks = {"crate_lvl1", "crate_lvl2", "crate_lvl3", "crate_lvl4", "arm_chair", "desk", "training_dummy"};
        List<String> errors = new ArrayList<>();
        int checked = 0;

        if (AssetCommands.class.getResource("/pack.mcmeta") == null) {
            errors.add("Missing /pack.mcmeta in resources root!");
        }

        for (String npc : npcs) {
            checked++;
            String geoModelPath = "/assets/mundodetronos/geckolib/models/entity/" + npc + ".geo.json";
            String animPath = "/assets/mundodetronos/animations/npc/" + npc + ".animation.json";
            String texPath = "/assets/mundodetronos/textures/entity/" + npc + ".png";
            String eggItemPath = "/assets/mundodetronos/models/item/" + npc + "_spawn_egg.json";

            if (AssetCommands.class.getResource(geoModelPath) == null) errors.add("Missing GeckoLib geo model: " + geoModelPath);
            if (AssetCommands.class.getResource(animPath) == null && !"pirate".equals(npc)) errors.add("Missing anim: " + animPath);
            if (AssetCommands.class.getResource(texPath) == null) errors.add("Missing texture: " + texPath);
            if (AssetCommands.class.getResource(eggItemPath) == null) errors.add("Missing spawn egg item model: " + eggItemPath);
        }

        for (String b : blocks) {
            checked++;
            String bsPath = "/assets/mundodetronos/blockstates/" + b + ".json";
            String bmPath = "/assets/mundodetronos/models/block/" + b + ".json";
            String imPath = "/assets/mundodetronos/models/item/" + b + ".json";

            if (AssetCommands.class.getResource(bsPath) == null) errors.add("Missing blockstate: " + bsPath);
            if (AssetCommands.class.getResource(bmPath) == null) errors.add("Missing block model: " + bmPath);
            if (AssetCommands.class.getResource(imPath) == null) errors.add("Missing item model: " + imPath);
        }

        final int totalChecked = checked;
        if (errors.isEmpty()) {
            source.sendSuccess(() -> Component.literal("GeckoLib Asset Validation OK! Checked " + totalChecked + " assets (pack.mcmeta, GeoModels, animations, textures, blockstates). Zero missing references!"), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("GeckoLib Asset Validation Failed (" + errors.size() + " errors):\n" + String.join("\n", errors)));
            return 0;
        }
    }

    private static int testAssets(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Asset test mode active. Use '/npc spawn <type>' or creative tab to test content."), false);
        return 1;
    }
}
