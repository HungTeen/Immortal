package hungteen.immortal.common.entity.undead;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IUndead;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.impl.Realms;
import hungteen.immortal.impl.SpiritualRoots;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 23:11
 **/
public abstract class UndeadEntity extends PathfinderMob implements IUndead {

    private static final EntityDataAccessor<String> REALM = SynchedEntityData.defineId(UndeadEntity.class, EntityDataSerializers.STRING);
    protected IRealm cacheRealm;

    public UndeadEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REALM, getDefaultRealm().getRegistryName());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        return super.finalizeSpawn(levelAccessor, difficulty, spawnType, groupData, tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("EntityRealm")){
            ImmortalAPI.get().getRealm(tag.getString("EntityRealm")).ifPresentOrElse(this::setRealm, () -> this.setRealm(getDefaultRealm()));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("EntityRealm", getRealm().getRegistryName());
    }

    public void setRealm(IRealm realm) {
        entityData.set(REALM, realm.getRegistryName());
        cacheRealm = realm;
    }

    @Override
    public IRealm getRealm() {
        if(cacheRealm == null) {
            cacheRealm = ImmortalAPI.get().getRealm(entityData.get(REALM)).orElse(getDefaultRealm());
        }
        return cacheRealm;
    }

    protected IRealm getDefaultRealm(){
        return Realms.UNDEAD_LEVEL1;
    }

    @Override
    public Collection<ISpiritualRoot> getSpiritualRoots() {
        return List.of(SpiritualRoots.DEAD);
    }
}
