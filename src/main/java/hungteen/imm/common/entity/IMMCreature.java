package hungteen.imm.common.entity;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.interfaces.IHasRoot;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.registry.RealmTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
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
public abstract class IMMCreature extends PathfinderMob implements IHasRoot, IHasRealm {

    private static final EntityDataAccessor<IRealmType> REALM = SynchedEntityData.defineId(IMMCreature.class, IMMDataSerializers.REALM.get());
    private static final EntityDataAccessor<Integer> ANIMATIONS = SynchedEntityData.defineId(IMMCreature.class, EntityDataSerializers.INT);

    protected IMMCreature(EntityType<? extends IMMCreature> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REALM, getDefaultRealm());
        entityData.define(ANIMATIONS, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("EntityRealm")){
            IMMAPI.get().realmRegistry().ifPresent(l ->{
                l.byNameCodec().parse(NbtOps.INSTANCE, tag.get("EntityRealm"))
                        .result().ifPresentOrElse(this::setRealm, () -> this.setRealm(this.getDefaultRealm()));
            });
        }
        if(tag.contains("AnimationFlags")){
            this.setAnimations(tag.getInt("AnimationFlags"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if(this.getRealm() != null){
            IMMAPI.get().realmRegistry().ifPresent(l ->{
                l.byNameCodec().encodeStart(NbtOps.INSTANCE, this.getRealm())
                        .result().ifPresent(nbt -> tag.put("EntityRealm", nbt));
            });
        }
        tag.putInt("AnimationFlags", this.getAnimations());
    }

    public void setRealm(IRealmType realm) {
        entityData.set(REALM, realm);
    }

    @Override
    public IRealmType getRealm() {
        return entityData.get(REALM);
    }

    public void setAnimation(int id, boolean flag){
        if(flag){
            this.setAnimations(this.getAnimations() | (1 << id));
        } else{
            this.setAnimations(this.getAnimations() ^ (1 << id));
        }
    }

    public boolean hasAnimation(int id) {
        return ((this.getAnimations() >> id) & 1) == 1;
    }

    protected void setAnimations(int animations){
        entityData.set(ANIMATIONS, animations);
    }

    protected int getAnimations(){
        return entityData.get(ANIMATIONS);
    }

    protected IRealmType getDefaultRealm(){
        return RealmTypes.MORTALITY;
    }
}