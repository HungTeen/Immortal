package hungteen.immortal.common.entity;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IHasRealm;
import hungteen.immortal.api.interfaces.IHasRoot;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.impl.Realms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 18:37
 **/
public abstract class ImmortalCreature extends PathfinderMob implements IHasRoot, IHasRealm {

    private static final EntityDataAccessor<String> REALM = SynchedEntityData.defineId(ImmortalCreature.class, EntityDataSerializers.STRING);
    protected IRealm cacheRealm;

    protected ImmortalCreature(EntityType<? extends ImmortalCreature> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REALM, getDefaultRealm().getRegistryName());
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
        return Realms.MORTALITY;
    }
}
