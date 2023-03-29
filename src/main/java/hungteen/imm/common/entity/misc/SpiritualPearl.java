package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/1 10:38
 */
public class SpiritualPearl extends HTEntity implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(SpiritualPearl.class, EntityDataSerializers.ITEM_STACK);
    private UUID uuid;
    private Entity owner;

    public SpiritualPearl(EntityType<?> type, Level world) {
        super(type, world);
    }

    public SpiritualPearl(Level world, double x, double y, double z) {
        super(IMMEntities.SPIRITUAL_PEARL.get(), world);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public boolean shouldRenderAtSqrDistance(double dis) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return dis < d0 * d0;
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public boolean isAttackable() {
        return false;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) {
            tag.put("Item", itemstack.save(new CompoundTag()));
        }
        this.getOwnerUUID().ifPresent(uuid -> {
            tag.putUUID("OwnerUUID", uuid);
        });
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        if(tag.contains("Item")) {
            this.setItem(ItemStack.of(tag.getCompound("Item")));
        }
        if(tag.contains("OwnerUUID")) {
            this.setOwnerUUID(tag.getUUID("OwnerUUID"));
        }
    }

    @Nullable
    public Entity getOwner(){
        if(this.owner == null && this.level instanceof ServerLevel){
            this.owner = ((ServerLevel) this.level).getEntity(this.uuid);
        }
        return this.owner;
    }

    public Optional<UUID> getOwnerUUID() {
        return Optional.ofNullable(this.uuid);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.uuid = uuid;
    }

    public void setItem(ItemStack stack) {
        if (!stack.is(IMMItems.SPIRITUAL_PEARL.get()) || stack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(stack.copy(), (itemStack) -> {
                itemStack.setCount(1);
            }));
        }

    }

    private ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(IMMItems.SPIRITUAL_PEARL.get()) : itemstack;
    }

}
