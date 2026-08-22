package com.mundodetronos.npc;

import com.mundodetronos.MundoDeTronos;
import com.mundodetronos.model.BlockbenchModel;
import com.mundodetronos.model.ModelCache;
import com.mundodetronos.animation.AnimationDefinition;
import com.mundodetronos.util.AssetTextureResolver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

public class CustomNPCEntity extends PathfinderMob {

    private static final EntityDataAccessor<String> DATA_NPC_TYPE = SynchedEntityData.defineId(CustomNPCEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(CustomNPCEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<String> DATA_TRIGGERED_ANIMATION = SynchedEntityData.defineId(CustomNPCEntity.class, EntityDataSerializers.STRING);

    @OnlyIn(Dist.CLIENT)
    private NPCAnimationController animationController;
    private String lastNpcType = "";

    public CustomNPCEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_NPC_TYPE, "guard");
        this.entityData.define(DATA_SCALE, 1.0F);
        this.entityData.define(DATA_TRIGGERED_ANIMATION, "");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    public String getNpcType() {
        return this.entityData.get(DATA_NPC_TYPE);
    }

    public void setNpcType(String npcType) {
        this.entityData.set(DATA_NPC_TYPE, npcType);
    }

    public float getVisualScale() {
        return this.entityData.get(DATA_SCALE);
    }

    public void setVisualScale(float scale) {
        this.entityData.set(DATA_SCALE, scale);
    }

    public void triggerAnimation(String animName) {
        this.entityData.set(DATA_TRIGGERED_ANIMATION, animName);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            updateClientAnimation();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void updateClientAnimation() {
        if (animationController == null) {
            animationController = new NPCAnimationController();
        }

        String currentType = getNpcType();
        if (!currentType.equals(lastNpcType)) {
            lastNpcType = currentType;
            Map<String, AnimationDefinition> anims = ModelCache.getAnimations(currentType, "animations/npc/" + currentType + ".animation.json");
            animationController.setAnimations(anims);
        }

        String triggeredAnim = this.entityData.get(DATA_TRIGGERED_ANIMATION);
        if (triggeredAnim != null && !triggeredAnim.isEmpty()) {
            animationController.playTemporaryAnimation(triggeredAnim);
            this.entityData.set(DATA_TRIGGERED_ANIMATION, "");
        }

        double horizSpeedSqr = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z;
        boolean isMoving = horizSpeedSqr > 0.001D;

        animationController.update(0.05F, isMoving);
    }

    @OnlyIn(Dist.CLIENT)
    public NPCAnimationController getAnimationController() {
        return animationController;
    }

    @OnlyIn(Dist.CLIENT)
    public BlockbenchModel getModel() {
        return ModelCache.getModel(getNpcType(), "models/npc/" + getNpcType() + ".bbmodel");
    }

    public ResourceLocation getTextureLocation() {
        return AssetTextureResolver.resolveEntityTexture(getNpcType());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("NpcType", getNpcType());
        tag.putFloat("VisualScale", getVisualScale());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("NpcType")) setNpcType(tag.getString("NpcType"));
        if (tag.contains("VisualScale")) setVisualScale(tag.getFloat("VisualScale"));
    }
}
