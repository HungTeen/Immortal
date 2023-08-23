package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.spell.spells.FlyWithItemSpell;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 13:47
 **/
public class FlyingItemEntity extends HTEntity implements TraceableEntity {

    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(FlyingItemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final int MAX_PASSENGER_SIZE = 1;
    private int flyingTick = 0;
    @Nullable
    private UUID thrower;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;

    public FlyingItemEntity(EntityType<?> type, Level world) {
        super(type, world);
        setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ITEM_STACK, ItemStack.EMPTY);
    }

    public void tick() {
        super.tick();

        this.tickLerp();

        if(EntityHelper.isServer(this)){
            if(this.getControllingPassenger() != null){
                this.updateMotion(this.getControllingPassenger());
            } else{
                this.setDeltaMovement(Vec3.ZERO);
            }
            if(this.getControllingPassenger() == null){
                this.goBackToInventory();
            }
        }

        this.checkInsideBlocks();

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (EntityHelper.isServer(this)) {// hasRecipe collide or passenger
            for (Entity entity : this.level().getEntities(this,
                    this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F),
                    EntitySelector.pushableBy(this))) {
                if (!entity.isPassenger()) {
                    this.push(entity);
                }
            }
        }
    }

    private void tickLerp() {
        if (EntityHelper.isServer(this)) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYaw - (double) this.getYRot());
            this.setYRot((float) ((double) this.getYRot() + d3 / (double) this.lerpSteps));
            this.setXRot((float) ((double) this.getXRot()
                    + (this.lerpPitch - (double) this.getXRot()) / (double) this.lerpSteps));
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }

    public void updateMotion(@NotNull Entity entity){
        final float cost = FlyWithItemSpell.getFlyingCost(entity);
        final boolean enough = EntityUtil.getMana(entity) >= cost;
        if(++ this.flyingTick % 20 == 0){
            if(enough){
                EntityUtil.addMana(entity, - cost);
            } else {
                entity.stopRiding();
                return;
            }
        }
        if(this.flyingTick % 5 == 0){
            ParticleHelper.spawnParticles(entity.level(), IMMParticles.SPIRITUAL.get(), position(), 1, 0, 0);
        }
        final double speed = 1;
        final Vec3 lookVec = entity.getLookAngle();
        this.setDeltaMovement(lookVec.normalize().scale(speed));
    }

    public void goBackToInventory(){
        if(EntityHelper.isEntityValid(this.getOwner())){
            if(this.getOwner() instanceof Player player){
                PlayerUtil.addItem(player, this.getItemStack().copy());
            }
        } else {
            spawnAtLocation(this.getItemStack().copy());
        }
        this.discard();
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch,
                       int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = yaw;
        this.lerpPitch = pitch;
        this.lerpSteps = 10;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            if(player.getMainHandItem().isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, this.getItemStack());
                this.discard();
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        } else {
            if (EntityHelper.isServer(this)) {
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return this.getPassengers().size() < MAX_PASSENGER_SIZE && entity == this.getOwner();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return ! this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity entity ? entity : null;
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isRidingPlayer(Player player) {
        return player.getVehicle() != null && player.getVehicle() == this;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        if (this.thrower != null) {
            Level level = this.level();
            if (level instanceof ServerLevel serverlevel) {
                return serverlevel.getEntity(this.thrower);
            }
        }

        return null;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("FlyingItemStack")){
            this.setItemStack(ItemStack.of(tag.getCompound("FlyingItemStack")));
        }
        if (tag.hasUUID("Thrower")) {
            this.thrower = tag.getUUID("Thrower");
        }
        if(tag.contains("FlyingTick")){
            this.flyingTick = tag.getInt("FlyingTick");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("FlyingItemStack", this.getItemStack().save(new CompoundTag()));
        if (this.thrower != null) {
            tag.putUUID("Thrower", this.thrower);
        }
        tag.putInt("FlyingTick", this.flyingTick);
    }


    public void setItemStack(ItemStack itemStack) {
        entityData.set(ITEM_STACK, itemStack);
        refreshDimensions();
    }

    public void setThrower(@NotNull Entity thrower) {
        this.setThrower(thrower.getUUID());
    }

    public void setThrower(@Nullable UUID uuid) {
        this.thrower = uuid;
    }

    public ItemStack getItemStack() {
        return entityData.get(ITEM_STACK);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.getItemStack().getItem() instanceof BlockItem ? EntityDimensions.scalable(1F, 1F) : EntityDimensions.scalable(0.9F, 0.2F);
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getItemStack().getItem() instanceof BlockItem ? 1.3 : 0.5;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ItemStack getPickResult() {
        return this.getItemStack().copy();
    }

    /**
     * 御剑飞行怎么能坐着呢？
     */
    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }
}
