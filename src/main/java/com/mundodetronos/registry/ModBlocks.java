package com.mundodetronos.registry;

import com.mundodetronos.MundoDeTronos;
import com.mundodetronos.block.CustomCrateBlock;
import com.mundodetronos.block.CustomFurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MundoDeTronos.MOD_ID);

    // Crates
    public static final RegistryObject<Block> CRATE_LVL1 = BLOCKS.register("crate_lvl1",
            () -> new CustomCrateBlock(1, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> CRATE_LVL2 = BLOCKS.register("crate_lvl2",
            () -> new CustomCrateBlock(2, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> CRATE_LVL3 = BLOCKS.register("crate_lvl3",
            () -> new CustomCrateBlock(3, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> CRATE_LVL4 = BLOCKS.register("crate_lvl4",
            () -> new CustomCrateBlock(4, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(3.5F).sound(SoundType.WOOD).noOcclusion()));

    // Furniture & Decor
    public static final RegistryObject<Block> TRAINING_DUMMY_BLOCK = BLOCKS.register("training_dummy",
            () -> new CustomFurnitureBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> ARM_CHAIR = BLOCKS.register("arm_chair",
            () -> new CustomFurnitureBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> DESK = BLOCKS.register("desk",
            () -> new CustomFurnitureBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD).noOcclusion()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
