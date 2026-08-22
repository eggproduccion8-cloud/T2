package com.mundodetronos;

import com.mundodetronos.command.AssetCommands;
import com.mundodetronos.command.NPCCommands;
import com.mundodetronos.registry.ModBlocks;
import com.mundodetronos.registry.ModEntities;
import com.mundodetronos.registry.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MundoDeTronos.MOD_ID)
public class MundoDeTronos {
    public static final String MOD_ID = "mundodetronos";
    public static final Logger LOGGER = LogManager.getLogger();

    public MundoDeTronos() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);

        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Mundo de Tronos Base Mod Setup Initialized.");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        NPCCommands.register(event.getDispatcher());
        AssetCommands.register(event.getDispatcher());
    }
}
