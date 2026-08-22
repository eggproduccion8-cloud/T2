package com.mundodetronos.client.model;

import com.mundodetronos.MundoDeTronos;
import com.mundodetronos.npc.CustomNPCEntity;
import com.mundodetronos.util.AssetTextureResolver;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CustomNPCGeoModel extends GeoModel<CustomNPCEntity> {

    @Override
    public ResourceLocation getModelResource(CustomNPCEntity animatable) {
        return new ResourceLocation(MundoDeTronos.MOD_ID, "geckolib/models/entity/" + animatable.getNpcType() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CustomNPCEntity animatable) {
        return AssetTextureResolver.resolveEntityTexture(animatable.getNpcType());
    }

    @Override
    public ResourceLocation getAnimationResource(CustomNPCEntity animatable) {
        return new ResourceLocation(MundoDeTronos.MOD_ID, "animations/npc/" + animatable.getNpcType() + ".animation.json");
    }
}
