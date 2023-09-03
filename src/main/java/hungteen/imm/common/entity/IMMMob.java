package hungteen.imm.common.entity;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.interfaces.IHasRoot;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 18:37
 **/
public abstract class IMMMob extends PathfinderMob implements IHasRoot, IHasRealm, IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<IRealmType> REALM = SynchedEntityData.defineId(IMMMob.class, IMMDataSerializers.REALM.get());
    private static final EntityDataAccessor<Integer> REALM_STAGE = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ANIMATIONS = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.INT);
    private final Set<ISpiritualType> spiritualRoots = new HashSet<>();

    public IMMMob(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REALM, getDefaultRealm());
        entityData.define(REALM_STAGE, 0);
        entityData.define(ANIMATIONS, 0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if (!accessor.isClientSide()) {
            Optional.ofNullable(this.getSpawnSound()).ifPresent(l -> this.playSound(l, this.getSoundVolume(), this.getVoicePitch()));
            this.spiritualRoots.addAll(this.createSpiritualRoots(accessor));
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, data, tag);
    }

    protected Collection<ISpiritualType> createSpiritualRoots(ServerLevelAccessor accessor){
        return List.of();
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        final int count = additionalData.readInt();
        this.spiritualRoots.clear();
        for (int i = 0; i < count; ++i) {
            this.spiritualRoots.add(additionalData.readRegistryIdUnsafe(SpiritualTypes.registry().getRegistry()));
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.spiritualRoots.size());
        this.spiritualRoots.forEach(type -> {
            buffer.writeRegistryId(SpiritualTypes.registry().getRegistry(), type);
        });
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("EntityRealm")) {
            RealmTypes.registry().byNameCodec().parse(NbtOps.INSTANCE, tag.get("EntityRealm"))
                    .result().ifPresentOrElse(this::setRealm, () -> this.setRealm(this.getDefaultRealm()));
        }
        if (tag.contains("RealmStage")) {
            this.setRealmStage(RealmStages.values()[tag.getInt("RealmStage")]);
        }
        if (tag.contains("SpiritualRoots")) {
            this.spiritualRoots.clear();
            final CompoundTag nbt = tag.getCompound("SpiritualRoots");
            final int count = nbt.getInt("Count");
            for(int i = 0; i < count; ++ i){
                SpiritualTypes.registry().getValue(nbt.getString("Root_" + i)).ifPresent(this.spiritualRoots::add);
            }
        }
        if (tag.contains("AnimationFlags")) {
            this.setAnimations(tag.getInt("AnimationFlags"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getRealm() != null) {
            IMMAPI.get().realmRegistry().ifPresent(l -> {
                l.byNameCodec().encodeStart(NbtOps.INSTANCE, this.getRealm())
                        .result().ifPresent(nbt -> tag.put("EntityRealm", nbt));
            });
        }
        tag.putInt("RealmStage", this.getRealmStage().ordinal());
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.putInt("Count", this.spiritualRoots.size());
            for(int i = 0; i < this.spiritualRoots.size(); ++ i){
                nbt.putString("Root_" + i, this.getSpiritualTypes().get(i).getRegistryName());
            }
            tag.put("SpiritualRoots", nbt);
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

    public void setRealmStage(RealmStages stage) {
        entityData.set(REALM_STAGE, stage.ordinal());
    }

    @Override
    public RealmStages getRealmStage() {
        return RealmStages.values()[entityData.get(REALM_STAGE)];
    }

    public void setAnimation(int id, boolean flag) {
        if (flag) {
            this.setAnimations(this.getAnimations() | (1 << id));
        } else {
            this.setAnimations(this.getAnimations() ^ (1 << id));
        }
    }

    public boolean hasAnimation(int id) {
        return ((this.getAnimations() >> id) & 1) == 1;
    }

    protected void setAnimations(int animations) {
        entityData.set(ANIMATIONS, animations);
    }

    protected int getAnimations() {
        return entityData.get(ANIMATIONS);
    }

    protected IRealmType getDefaultRealm() {
        return RealmTypes.MORTALITY;
    }

    @Nullable
    protected SoundEvent getSpawnSound() {
        return null;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public List<ISpiritualType> getSpiritualTypes() {
        return spiritualRoots.stream().toList();
    }
}
