package com.mundodetronos.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mundodetronos.client.model.CustomNPCGeoModel;
import com.mundodetronos.npc.CustomNPCEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CustomNPCGeoRenderer extends GeoEntityRenderer<CustomNPCEntity> {

    public CustomNPCGeoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CustomNPCGeoModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public RenderType getRenderType(CustomNPCEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public void render(CustomNPCEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float scale = entity.getVisualScale();
        poseStack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
