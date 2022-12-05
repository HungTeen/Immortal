package hungteen.immortal.common.entity;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IHasRealm;
import hungteen.immortal.api.interfaces.IHasRoot;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.impl.RealmTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
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

    private static final EntityDataAccessor<IRealmType> REALM = SynchedEntityData.defineId(ImmortalCreature.class, ImmortalDataSerializers.REALM.get());

    protected ImmortalCreature(EntityType<? extends ImmortalCreature> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REALM, getDefaultRealm());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("EntityRealm")){
            ImmortalAPI.get().realmRegistry().ifPresent(l ->{
                l.byNameCodec().parse(NbtOps.INSTANCE, tag.get("EntityRealm"))
                        .result().ifPresentOrElse(this::setRealm, () -> this.setRealm(this.getDefaultRealm()));
            });
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if(this.getRealm() != null){
            ImmortalAPI.get().realmRegistry().ifPresent(l ->{
                l.byNameCodec().encodeStart(NbtOps.INSTANCE, this.getRealm())
                        .result().ifPresent(nbt -> tag.put("EntityRealm", nbt));
            });
        }
    }

    public void setRealm(IRealmType realm) {
        entityData.set(REALM, realm);
    }

    @Override
    public IRealmType getRealm() {
        return entityData.get(REALM);
    }

    protected IRealmType getDefaultRealm(){
        return RealmTypes.MORTALITY;
    }
}
