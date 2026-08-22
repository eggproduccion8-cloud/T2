package com.mundodetronos.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mundodetronos.model.BlockbenchModel;
import com.mundodetronos.model.ModelBone;
import com.mundodetronos.model.ModelCube;
import com.mundodetronos.npc.CustomNPCEntity;
import com.mundodetronos.npc.NPCAnimationController;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;

public class CustomNPCRenderer extends EntityRenderer<CustomNPCEntity> {

    public CustomNPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CustomNPCEntity entity) {
        return entity.getTextureLocation();
    }

    @Override
    public void render(CustomNPCEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

        BlockbenchModel model = entity.getModel();
        if (model == null) return;

        poseStack.pushPose();

        float scale = entity.getVisualScale();
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F - entityYaw));

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));

        NPCAnimationController animController = entity.getAnimationController();

        for (ModelBone rootBone : model.getRootBones()) {
            renderBone(rootBone, animController, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, model.getTextureWidth(), model.getTextureHeight());
        }

        poseStack.popPose();
    }

    private void renderBone(ModelBone bone, NPCAnimationController animController, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int texW, int texH) {
        poseStack.pushPose();

        Vector3f pivot = bone.getPivot();
        poseStack.translate(pivot.x() / 16.0F, pivot.y() / 16.0F, pivot.z() / 16.0F);

        Vector3f baseRot = bone.getRotation();
        if (baseRot.z() != 0) poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(baseRot.z()));
        if (baseRot.y() != 0) poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(baseRot.y()));
        if (baseRot.x() != 0) poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(baseRot.x()));

        if (animController != null) {
            NPCAnimationController.Transform transform = animController.getBoneTransform(bone.getName());
            if (transform != null) {
                poseStack.translate(transform.position.x() / 16.0F, transform.position.y() / 16.0F, transform.position.z() / 16.0F);

                if (transform.rotation.z() != 0) poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(transform.rotation.z()));
                if (transform.rotation.y() != 0) poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(transform.rotation.y()));
                if (transform.rotation.x() != 0) poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(transform.rotation.x()));

                poseStack.scale(transform.scale.x(), transform.scale.y(), transform.scale.z());
            }
        }

        poseStack.translate(-pivot.x() / 16.0F, -pivot.y() / 16.0F, -pivot.z() / 16.0F);

        for (ModelCube cube : bone.getCubes()) {
            poseStack.pushPose();

            Vector3f origin = cube.getOrigin();
            Vector3f cubeRot = cube.getRotation();

            poseStack.translate(origin.x() / 16.0F, origin.y() / 16.0F, origin.z() / 16.0F);
            if (cubeRot.z() != 0) poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(cubeRot.z()));
            if (cubeRot.y() != 0) poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(cubeRot.y()));
            if (cubeRot.x() != 0) poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(cubeRot.x()));
            poseStack.translate(-origin.x() / 16.0F, -origin.y() / 16.0F, -origin.z() / 16.0F);

            Matrix4f poseMatrix = poseStack.last().pose();
            renderCube(cube, poseMatrix, buffer, packedLight, packedOverlay, texW, texH);

            poseStack.popPose();
        }

        for (ModelBone child : bone.getChildren()) {
            renderBone(child, animController, poseStack, buffer, packedLight, packedOverlay, texW, texH);
        }

        poseStack.popPose();
    }

    private void renderCube(ModelCube cube, Matrix4f matrix, VertexConsumer buffer, int packedLight, int packedOverlay, int texW, int texH) {
        Vector3f from = cube.getFrom();
        Vector3f to = cube.getTo();

        float x1 = from.x() / 16.0F;
        float y1 = from.y() / 16.0F;
        float z1 = from.z() / 16.0F;
        float x2 = to.x() / 16.0F;
        float y2 = to.y() / 16.0F;
        float z2 = to.z() / 16.0F;

        Map<String, ModelCube.Face> faces = cube.getFaces();

        if (faces.containsKey("north")) renderFace(matrix, buffer, faces.get("north"), x2, y2, z1, x1, y2, z1, x1, y1, z1, x2, y1, z1, 0, 0, -1, packedLight, packedOverlay, texW, texH);
        if (faces.containsKey("south")) renderFace(matrix, buffer, faces.get("south"), x1, y2, z2, x2, y2, z2, x2, y1, z2, x1, y1, z2, 0, 0, 1, packedLight, packedOverlay, texW, texH);
        if (faces.containsKey("west"))  renderFace(matrix, buffer, faces.get("west"),  x1, y2, z1, x1, y2, z2, x1, y1, z2, x1, y1, z1, -1, 0, 0, packedLight, packedOverlay, texW, texH);
        if (faces.containsKey("east"))  renderFace(matrix, buffer, faces.get("east"),  x2, y2, z2, x2, y2, z1, x2, y1, z1, x2, y1, z2, 1, 0, 0, packedLight, packedOverlay, texW, texH);
        if (faces.containsKey("up"))    renderFace(matrix, buffer, faces.get("up"),    x1, y2, z2, x2, y2, z2, x2, y2, z1, x1, y2, z1, 0, 1, 0, packedLight, packedOverlay, texW, texH);
        if (faces.containsKey("down"))  renderFace(matrix, buffer, faces.get("down"),  x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, 0, -1, 0, packedLight, packedOverlay, texW, texH);
    }

    private void renderFace(Matrix4f matrix, VertexConsumer buffer, ModelCube.Face face, float px1, float py1, float pz1, float px2, float py2, float pz2, float px3, float py3, float pz3, float px4, float py4, float pz4, float nx, float ny, float nz, int light, int overlay, int texW, int texH) {
        float u1 = face.getU1() / (float) texW;
        float v1 = face.getV1() / (float) texH;
        float u2 = face.getU2() / (float) texW;
        float v2 = face.getV2() / (float) texH;

        vertex(matrix, buffer, px1, py1, pz1, u1, v1, nx, ny, nz, light, overlay);
        vertex(matrix, buffer, px2, py2, pz2, u2, v1, nx, ny, nz, light, overlay);
        vertex(matrix, buffer, px3, py3, pz3, u2, v2, nx, ny, nz, light, overlay);
        vertex(matrix, buffer, px4, py4, pz4, u1, v2, nx, ny, nz, light, overlay);
    }

    private void vertex(Matrix4f matrix, VertexConsumer buffer, float x, float y, float z, float u, float v, float nx, float ny, float nz, int light, int overlay) {
        buffer.vertex(matrix, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(nx, ny, nz)
                .endVertex();
    }
}
