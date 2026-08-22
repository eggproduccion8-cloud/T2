package com.mundodetronos.item;

import com.mundodetronos.npc.CustomNPCEntity;
import com.mundodetronos.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class NPCSpawnEggItem extends Item {
    private final String npcType;

    public NPCSpawnEggItem(String npcType, Properties properties) {
        super(properties);
        this.npcType = npcType;
    }

    public String getNpcType() {
        return npcType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos spawnPos = clickedPos.relative(face);

        CustomNPCEntity npc = ModEntities.NPC_ENTITY.get().create(serverLevel);
        if (npc != null) {
            npc.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, context.getRotation(), 0.0F);
            npc.setNpcType(this.npcType);
            serverLevel.addFreshEntity(npc);
            context.getItemInHand().shrink(1);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
