package com.mundodetronos.registry;

import com.mundodetronos.MundoDeTronos;
import com.mundodetronos.item.NPCSpawnEggItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MundoDeTronos.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MundoDeTronos.MOD_ID);

    // NPC Vol. 1 Spawn Eggs
    public static final RegistryObject<Item> GUARD_SPAWN_EGG = ITEMS.register("guard_spawn_egg", () -> new NPCSpawnEggItem("guard", new Item.Properties()));
    public static final RegistryObject<Item> ARCHER_SPAWN_EGG = ITEMS.register("archer_spawn_egg", () -> new NPCSpawnEggItem("archer", new Item.Properties()));
    public static final RegistryObject<Item> BLACKSMITH_SPAWN_EGG = ITEMS.register("blacksmith_spawn_egg", () -> new NPCSpawnEggItem("blacksmith", new Item.Properties()));
    public static final RegistryObject<Item> BUTCHER_SPAWN_EGG = ITEMS.register("butcher_spawn_egg", () -> new NPCSpawnEggItem("butcher", new Item.Properties()));
    public static final RegistryObject<Item> FARMER_SPAWN_EGG = ITEMS.register("farmer_spawn_egg", () -> new NPCSpawnEggItem("farmer", new Item.Properties()));
    public static final RegistryObject<Item> WIZARD_SPAWN_EGG = ITEMS.register("wizard_spawn_egg", () -> new NPCSpawnEggItem("wizard", new Item.Properties()));

    // NPC Vol. 2 Spawn Eggs
    public static final RegistryObject<Item> ADVENTURER_SPAWN_EGG = ITEMS.register("adventurer_spawn_egg", () -> new NPCSpawnEggItem("adventurer", new Item.Properties()));
    public static final RegistryObject<Item> KING_SPAWN_EGG = ITEMS.register("king_spawn_egg", () -> new NPCSpawnEggItem("king", new Item.Properties()));
    public static final RegistryObject<Item> MINER_SPAWN_EGG = ITEMS.register("miner_spawn_egg", () -> new NPCSpawnEggItem("miner", new Item.Properties()));
    public static final RegistryObject<Item> PIRATE_SPAWN_EGG = ITEMS.register("pirate_spawn_egg", () -> new NPCSpawnEggItem("pirate", new Item.Properties()));
    public static final RegistryObject<Item> TAVERN_SPAWN_EGG = ITEMS.register("tavern_spawn_egg", () -> new NPCSpawnEggItem("tavern", new Item.Properties()));

    // Block Items
    public static final RegistryObject<Item> CRATE_LVL1_ITEM = ITEMS.register("crate_lvl1", () -> new BlockItem(ModBlocks.CRATE_LVL1.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRATE_LVL2_ITEM = ITEMS.register("crate_lvl2", () -> new BlockItem(ModBlocks.CRATE_LVL2.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRATE_LVL3_ITEM = ITEMS.register("crate_lvl3", () -> new BlockItem(ModBlocks.CRATE_LVL3.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRATE_LVL4_ITEM = ITEMS.register("crate_lvl4", () -> new BlockItem(ModBlocks.CRATE_LVL4.get(), new Item.Properties()));

    public static final RegistryObject<Item> TRAINING_DUMMY_ITEM = ITEMS.register("training_dummy", () -> new BlockItem(ModBlocks.TRAINING_DUMMY_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> ARM_CHAIR_ITEM = ITEMS.register("arm_chair", () -> new BlockItem(ModBlocks.ARM_CHAIR.get(), new Item.Properties()));
    public static final RegistryObject<Item> DESK_ITEM = ITEMS.register("desk", () -> new BlockItem(ModBlocks.DESK.get(), new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> MUNDO_TAB = CREATIVE_MODE_TABS.register("mundo_de_tronos_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mundodetronos"))
                    .icon(() -> new ItemStack(GUARD_SPAWN_EGG.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(GUARD_SPAWN_EGG.get());
                        output.accept(ARCHER_SPAWN_EGG.get());
                        output.accept(BLACKSMITH_SPAWN_EGG.get());
                        output.accept(BUTCHER_SPAWN_EGG.get());
                        output.accept(FARMER_SPAWN_EGG.get());
                        output.accept(WIZARD_SPAWN_EGG.get());
                        output.accept(ADVENTURER_SPAWN_EGG.get());
                        output.accept(KING_SPAWN_EGG.get());
                        output.accept(MINER_SPAWN_EGG.get());
                        output.accept(PIRATE_SPAWN_EGG.get());
                        output.accept(TAVERN_SPAWN_EGG.get());

                        output.accept(CRATE_LVL1_ITEM.get());
                        output.accept(CRATE_LVL2_ITEM.get());
                        output.accept(CRATE_LVL3_ITEM.get());
                        output.accept(CRATE_LVL4_ITEM.get());

                        output.accept(TRAINING_DUMMY_ITEM.get());
                        output.accept(ARM_CHAIR_ITEM.get());
                        output.accept(DESK_ITEM.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
