package hungteen.immortal.common.entity.misc;

import hungteen.htlib.entity.HTEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 13:47
 **/
public class FlyingItemEntity extends HTEntity {

    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(FlyingItemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final int MAX_PASSENGER_SIZE = 1;
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

        if(! this.level.isClientSide){
            if(this.getControllingPassenger() != null){
                this.updateMotion(this.getControllingPassenger());

            } else{
                this.setDeltaMovement(Vec3.ZERO);
            }
        }

        this.checkInsideBlocks();

        this.move(MoverType.SELF, this.getDeltaMovement());
//        if(this.getTrailParticle() != null){
//            this.level.addParticle(this.getTrailParticle(), d2, d0 + 0.5D, d1, 0.0D, 0.0D, 0.0D);
//        }

        if (!level.isClientSide) {// hasRecipe collide or passenger
            for (Entity entity : this.level.getEntities(this,
                    this.getBoundingBox().inflate((double) 0.2F, (double) -0.01F, (double) 0.2F),
                    EntitySelector.pushableBy(this))) {
                if (!entity.isPassenger()) {
                    this.push(entity);
                }
            }
        }
    }

    private void tickLerp() {
        if (! this.level.isClientSide) {
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

    public void updateMotion(@NotNull Entity player){
        final double speed = 1;
        final Vec3 lookVec = player.getLookAngle();
        this.setDeltaMovement(lookVec.normalize().scale(speed));
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
        this.lerpYaw = (double) yaw;
        this.lerpPitch = (double) pitch;
        this.lerpSteps = 10;
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            if(player.getMainHandItem().isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, this.getItemStack());
                this.discard();
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        } else {
            if (!this.level.isClientSide) {
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return this.getPassengers().size() < MAX_PASSENGER_SIZE;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isRidingPlayer(Player player) {
        return player.getVehicle() != null && player.getVehicle() == this;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("FlyingItemStack")){
            this.setItemStack(ItemStack.of(tag.getCompound("FlyingItemStack")));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("FlyingItemStack", this.getItemStack().save(new CompoundTag()));
    }

    public void setItemStack(ItemStack itemStack) {
        entityData.set(ITEM_STACK, itemStack);
        refreshDimensions();
    }

    public ItemStack getItemStack() {
        return entityData.get(ITEM_STACK);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_19975_) {
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
