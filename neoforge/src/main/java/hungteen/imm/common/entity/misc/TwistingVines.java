package hungteen.imm.common.entity.misc;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.common.cultivation.RealmManager;
import hungteen.imm.util.NBTUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/6 22:49
 **/
public class TwistingVines extends HTTraceableEntity implements IEntityWithComplexSpawn {

    private static final int SPAWN_CD = 20;
    private static final EntityDataAccessor<Integer> SPAWN_TICKS = SynchedEntityData.defineId(TwistingVines.class, EntityDataSerializers.INT);
    public int vineHealth = 100;

    public TwistingVines(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPAWN_TICKS, 0);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeInt(vineHealth);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        vineHealth = registryFriendlyByteBuf.readInt();
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.getSpawnTicks() < SPAWN_CD){
                this.setSpawnTicks(this.getSpawnTicks() + 1);
            }
            if(this.getPassengers().isEmpty()){
                vineHealth --;
            } else {
                Entity passenger = this.getFirstPassenger();
                vineHealth -= RealmManager.getRealm(passenger).getRealmLevel().level() + 1;
            }
            if(vineHealth <= 0){
                this.disappear();
            }
        } else {
            if(this.getSpawnTicks() < SPAWN_CD){
                BlockState blockState = level().getBlockState(blockPosition().below());
                if(blockState.isSolid()){
                    ParticleUtil.spawnParticleOnFace(level(), blockPosition(), Direction.DOWN, ParticleUtil.block(blockState), 5, Vec3.ZERO, 0.5);
                }
            }
        }
        tickMove();
    }

    public void disappear(){
        playSound(SoundEvents.VINE_BREAK);
        if(level() instanceof ServerLevel serverLevel){
//            ParticleHelper.sendParticles(serverLevel, ParticleUtil.block(Blocks.VINE.defaultBlockState()), getX(), getY(), getZ(), 10, 0.5, 0.1, 0.5, 0.05D);
        }
        this.discard();
    }

    @Override
    public void push(Entity entity) {
        // 抓碰到的第一个实体。
        if(this.getPassengers().isEmpty()){
            entity.startRiding(this);
        }
        super.push(entity);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag::putInt, "spawnTicks", getSpawnTicks());
        NBTUtil.write(tag::putInt, "vineHealth", getVineHealth());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, tag::getInt, "spawnTicks", this::setSpawnTicks);
        NBTUtil.read(tag, tag::getInt, "vineHealth", this::setVineHealth);
    }

    public int getSpawnTicks(){
        return this.entityData.get(SPAWN_TICKS);
    }

    public void setSpawnTicks(int ticks){
        this.entityData.set(SPAWN_TICKS, ticks);
    }

    public void setVineHealth(int vineHealth) {
        this.vineHealth = vineHealth;
    }

    public int getVineHealth() {
        return vineHealth;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

}
