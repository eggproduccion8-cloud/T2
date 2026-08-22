package com.mundodetronos.npc;

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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CustomNPCEntity extends PathfinderMob implements GeoEntity {

    private static final EntityDataAccessor<String> DATA_NPC_TYPE = SynchedEntityData.defineId(CustomNPCEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(CustomNPCEntity.class, EntityDataSerializers.FLOAT);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CustomNPCEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_NPC_TYPE, "guard");
        this.entityData.define(DATA_SCALE, 1.0F);
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
        if (!this.level().isClientSide()) {
            triggerAnim("controller", animName);
        }
    }

    public ResourceLocation getTextureLocation() {
        return AssetTextureResolver.resolveEntityTexture(getNpcType());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<CustomNPCEntity> controller = new AnimationController<>(this, "controller", 5, event -> {
            if (event.isMoving()) {
                return event.setAndContinue(WALK_ANIM);
            }
            return event.setAndContinue(IDLE_ANIM);
        });

        // Guard animations
        controller.triggerableAnim("attack", RawAnimation.begin().thenPlay("attack"));
        controller.triggerableAnim("grabsword", RawAnimation.begin().thenPlay("grabsword"));
        controller.triggerableAnim("grabtorch", RawAnimation.begin().thenPlay("grabtorch"));
        controller.triggerableAnim("torch", RawAnimation.begin().thenPlay("torch"));

        // General Vol.1 / Vol.2 animations
        controller.triggerableAnim("greet", RawAnimation.begin().thenPlay("greet"));
        controller.triggerableAnim("fidget", RawAnimation.begin().thenPlay("fidget"));
        controller.triggerableAnim("alone", RawAnimation.begin().thenPlay("alone"));

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
