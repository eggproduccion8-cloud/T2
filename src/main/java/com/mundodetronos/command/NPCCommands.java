package com.mundodetronos.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mundodetronos.npc.CustomNPCEntity;
import com.mundodetronos.registry.ModEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NPCCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("npc")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("list")
                        .executes(ctx -> listNPCs(ctx.getSource())))
                .then(Commands.literal("spawn")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .executes(ctx -> spawnNPC(ctx.getSource(), StringArgumentType.getString(ctx, "type")))))
                .then(Commands.literal("remove")
                        .executes(ctx -> removeNearestNPC(ctx.getSource())))
                .then(Commands.literal("remove_all")
                        .executes(ctx -> removeAllNPCs(ctx.getSource())))
                .then(Commands.literal("info")
                        .executes(ctx -> getNearestNPCInfo(ctx.getSource())))
                .then(Commands.literal("animation")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> triggerAnimation(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                .then(Commands.literal("setanimation")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> triggerAnimation(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                .then(Commands.literal("setscale")
                        .then(Commands.argument("scale", FloatArgumentType.floatArg(0.1F, 10.0F))
                                .executes(ctx -> setScale(ctx.getSource(), FloatArgumentType.getFloat(ctx, "scale")))))
        );
    }

    private static int listNPCs(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Available NPCs:\nguard\narcher\nblacksmith\nbutcher\nfarmer\nwizard\nadventurer\nbard\nking\nminer\npirate\ntavern"), false);
        return 1;
    }

    private static int spawnNPC(CommandSourceStack source, String type) {
        if (source.getEntity() == null) return 0;
        Vec3 pos = source.getPosition();
        CustomNPCEntity npc = ModEntities.NPC_ENTITY.get().create(source.getLevel());
        if (npc != null) {
            npc.moveTo(pos.x, pos.y, pos.z, source.getEntity().getYRot(), 0.0F);
            npc.setNpcType(type);
            source.getLevel().addFreshEntity(npc);
            source.sendSuccess(() -> Component.literal("Spawned NPC '" + type + "' at position " + pos), true);
            return 1;
        }
        return 0;
    }

    private static int removeNearestNPC(CommandSourceStack source) {
        Vec3 pos = source.getPosition();
        List<CustomNPCEntity> npcs = source.getLevel().getEntitiesOfClass(CustomNPCEntity.class, new AABB(pos.x - 5, pos.y - 5, pos.z - 5, pos.x + 5, pos.y + 5, pos.z + 5));
        if (!npcs.isEmpty()) {
            CustomNPCEntity nearest = npcs.get(0);
            nearest.discard();
            source.sendSuccess(() -> Component.literal("Removed nearest NPC."), true);
            return 1;
        }
        source.sendFailure(Component.literal("No NPC found nearby."));
        return 0;
    }

    private static int removeAllNPCs(CommandSourceStack source) {
        Vec3 pos = source.getPosition();
        List<CustomNPCEntity> npcs = source.getLevel().getEntitiesOfClass(CustomNPCEntity.class, new AABB(pos.x - 100, pos.y - 100, pos.z - 100, pos.x + 100, pos.y + 100, pos.z + 100));
        int count = npcs.size();
        for (CustomNPCEntity npc : npcs) {
            npc.discard();
        }
        source.sendSuccess(() -> Component.literal("Removed " + count + " NPCs."), true);
        return count;
    }

    private static int getNearestNPCInfo(CommandSourceStack source) {
        Vec3 pos = source.getPosition();
        List<CustomNPCEntity> npcs = source.getLevel().getEntitiesOfClass(CustomNPCEntity.class, new AABB(pos.x - 5, pos.y - 5, pos.z - 5, pos.x + 5, pos.y + 5, pos.z + 5));
        if (!npcs.isEmpty()) {
            CustomNPCEntity nearest = npcs.get(0);
            source.sendSuccess(() -> Component.literal("NPC Info -> Type: " + nearest.getNpcType() + ", Scale: " + nearest.getVisualScale() + ", Pos: " + nearest.position()), false);
            return 1;
        }
        source.sendFailure(Component.literal("No NPC found nearby."));
        return 0;
    }

    private static int triggerAnimation(CommandSourceStack source, String animName) {
        Vec3 pos = source.getPosition();
        List<CustomNPCEntity> npcs = source.getLevel().getEntitiesOfClass(CustomNPCEntity.class, new AABB(pos.x - 5, pos.y - 5, pos.z - 5, pos.x + 5, pos.y + 5, pos.z + 5));
        if (!npcs.isEmpty()) {
            CustomNPCEntity nearest = npcs.get(0);
            nearest.triggerAnimation(animName);
            source.sendSuccess(() -> Component.literal("Triggered animation '" + animName + "' on NPC " + nearest.getNpcType()), true);
            return 1;
        }
        source.sendFailure(Component.literal("No NPC found nearby."));
        return 0;
    }

    private static int setScale(CommandSourceStack source, float scale) {
        Vec3 pos = source.getPosition();
        List<CustomNPCEntity> npcs = source.getLevel().getEntitiesOfClass(CustomNPCEntity.class, new AABB(pos.x - 5, pos.y - 5, pos.z - 5, pos.x + 5, pos.y + 5, pos.z + 5));
        if (!npcs.isEmpty()) {
            CustomNPCEntity nearest = npcs.get(0);
            nearest.setVisualScale(scale);
            source.sendSuccess(() -> Component.literal("Set scale of NPC " + nearest.getNpcType() + " to " + scale), true);
            return 1;
        }
        source.sendFailure(Component.literal("No NPC found nearby."));
        return 0;
    }
}
