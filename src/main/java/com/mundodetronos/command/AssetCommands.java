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
        List<String> errors = new ArrayList<>();
        int checked = 0;

        for (String npc : npcs) {
            checked++;
            String modelPath = "/assets/mundodetronos/models/npc/" + npc + ".bbmodel";
            String animPath = "/assets/mundodetronos/animations/npc/" + npc + ".animation.json";
            String texPath = "/assets/mundodetronos/textures/entity/" + npc + ".png";

            if (AssetCommands.class.getResource(modelPath) == null) errors.add("Missing model: " + modelPath);
            if (AssetCommands.class.getResource(animPath) == null && !"pirate".equals(npc)) errors.add("Missing anim: " + animPath);
            if (AssetCommands.class.getResource(texPath) == null) errors.add("Missing texture: " + texPath);
        }

        final int totalChecked = checked;
        if (errors.isEmpty()) {
            source.sendSuccess(() -> Component.literal("Asset Validation OK! Checked " + totalChecked + " NPCs. Zero missing textures/models."), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("Asset Validation Failed (" + errors.size() + " errors):\n" + String.join("\n", errors)));
            return 0;
        }
    }

    private static int testAssets(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Asset test mode active. Use '/npc spawn <type>' or creative tab to test content."), false);
        return 1;
    }
}
