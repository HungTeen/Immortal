package hungteen.imm.common.entity.misc.talisman;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.entity.misc.HTTraceableEntity;
import hungteen.imm.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/19 10:07
 **/
public abstract class RangeEffectTalismanEntity extends HTTraceableEntity implements ItemSupplier {

    private static final EntityDataAccessor<Integer> EXIST_TICK = SynchedEntityData.defineId(RangeEffectTalismanEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SPELL_LEVEL = SynchedEntityData.defineId(RangeEffectTalismanEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> CENTER = SynchedEntityData.defineId(RangeEffectTalismanEntity.class, EntityDataSerializers.BLOCK_POS);
    private int maxExistTick = 0;

    public RangeEffectTalismanEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(EXIST_TICK, 0);
        builder.define(SPELL_LEVEL, 1);
        builder.define(CENTER, BlockPos.ZERO);
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.getExistTick() >= this.getMaxExistTick()){
                this.discard();
            } else {
                this.setExistTick(this.getExistTick() + 1);
                this.tickTalisman();
            }
        }
    }
    
    public void tickTalisman() {
        if (level() instanceof ServerLevel serverLevel) {
            ParticleHelper.sendParticles(serverLevel, IMMParticles.QI.get(), getX(), getY(), getZ(), 1, 1D, 1D, 1D, 0.05);
        }
    }

    /**
     * @return 计算符箓存活时间。
     */
    public abstract int calculateExistTick(float scale);

    public double getExistPercent(){
        return this.getMaxExistTick() == 0 ? 0 : (double) this.getExistTick() / (double) this.getMaxExistTick();
    }
    @Override
    public ItemStack getItem() {
        return getSpell().getTalismanItem(getSpellLevel());
    }

    public abstract TalismanSpell getSpell();

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, tag::getInt, "exist_tick", this::setExistTick);
        NBTUtil.read(tag, tag::getInt, "spell_level", this::setSpellLevel);
        NBTUtil.read(tag, BlockPos.CODEC, "center", this::setCenter);
        NBTUtil.read(tag, tag::getInt, "max_exist_tick", this::setMaxExistTick);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag::putInt, "exist_tick", this.getExistTick());
        NBTUtil.write(tag::putInt, "spell_level", this.getSpellLevel());
        NBTUtil.write(tag, BlockPos.CODEC, "center", this.getCenter());
        NBTUtil.write(tag::putInt, "max_exist_tick", this.getMaxExistTick());
    }
    
    public void setExistTick(int existTick) {
        this.entityData.set(EXIST_TICK, existTick);
    }
    
    public int getExistTick() {
        return this.entityData.get(EXIST_TICK);
    }

    public void setSpellLevel(int level) {
        this.entityData.set(SPELL_LEVEL, level);
    }

    public int getSpellLevel() {
        return this.entityData.get(SPELL_LEVEL);
    }

    public void setCenter(BlockPos pos) {
        this.entityData.set(CENTER, pos);
    }

    public BlockPos getCenter() {
        return this.entityData.get(CENTER);
    }

    public void setMaxExistTick(int existTick) {
        this.maxExistTick = existTick;
    }

    public int getMaxExistTick() {
        return maxExistTick;
    }
}
