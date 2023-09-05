package hungteen.imm.common.entity;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.enums.SpellCategories;
import hungteen.imm.api.interfaces.IHasMana;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.interfaces.IHasRoot;
import hungteen.imm.api.interfaces.IHasSpell;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
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
public abstract class IMMMob extends PathfinderMob implements IHasRoot, IHasRealm, IEntityAdditionalSpawnData, IHasMana, IHasSpell {

    private static final EntityDataAccessor<IRealmType> REALM = SynchedEntityData.defineId(IMMMob.class, IMMDataSerializers.REALM.get());
    private static final EntityDataAccessor<Integer> REALM_STAGE = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<ISpellType>> USING_SPELL = SynchedEntityData.defineId(IMMMob.class, IMMDataSerializers.OPT_SPELL.get());
    private static final EntityDataAccessor<Integer> ANIMATIONS = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.INT);
    private final Map<ISpellType, Integer> learnedSpells = new HashMap<>();
    private final Set<ISpiritualType> spiritualRoots = new HashSet<>();
    protected float spiritualMana;
    private int spellCooldown;

    public IMMMob(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REALM, getDefaultRealm());
        entityData.define(REALM_STAGE, 0);
        entityData.define(USING_SPELL, Optional.empty());
        entityData.define(ANIMATIONS, 0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if (!accessor.isClientSide()) {
            Optional.ofNullable(this.getSpawnSound()).ifPresent(l -> this.playSound(l, this.getSoundVolume(), this.getVoicePitch()));
            this.spiritualRoots.addAll(this.createSpiritualRoots(accessor));
            createLearnSpells().forEach(spell -> this.learnedSpells.put(spell.spell(), spell.level()));
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, data, tag);
    }

    /**
     * 随机初始化灵根。
     */
    protected Collection<ISpiritualType> createSpiritualRoots(ServerLevelAccessor accessor){
        return List.of();
    }

    protected List<Spell> createLearnSpells(){
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
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)) {
            if(this.spellCooldown > 0){
                -- this.spellCooldown;
            }
        }
    }

    @Override
    public boolean canUseSpell(ISpellType spell) {
        if(spell.getCategory() == SpellCategories.PLAYER_ONLY) return false;
        if(spell.getCategory().requireEntityTarget()) return EntityHelper.isEntityValid(this.getTarget());
        return true;
    }

    @Override
    public void trigger(@Nullable Spell spell) {
        if(spell == null){
            this.spellCooldown = 20;
        } else {
            this.setUsingSpell(spell.spell());
            this.addMana(- spell.spell().getConsumeMana());
            this.spellCooldown = spell.spell().getCooldown();
        }
    }

    @Override
    public HTHitResult createHitResult() {
        return new HTHitResult(this.getTarget());
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
        if(tag.contains("UsingSpell")){
            SpellTypes.registry().getValue(tag.getString("UsingSpell")).ifPresent(this::setUsingSpell);
        }
        if(tag.contains("LearnedSpells")){
            this.learnedSpells.clear();
            final ListTag list = NBTUtil.list(tag, "LearnedSpells");
            for(int i = 0; i < list.size(); ++ i){
                final CompoundTag nbt = list.getCompound(i);
                if(nbt.contains("Spell") && nbt.contains("Level")){
                    SpellTypes.registry().getValue(nbt.getString("Spell")).ifPresent(type -> {
                        this.learnedSpells.put(type, nbt.getInt("Level"));
                    });
                }
            }
        }
        if (tag.contains("SpiritualRoots")) {
            this.spiritualRoots.clear();
            final CompoundTag nbt = tag.getCompound("SpiritualRoots");
            final int count = nbt.getInt("Count");
            for(int i = 0; i < count; ++ i){
                SpiritualTypes.registry().getValue(nbt.getString("Root_" + i)).ifPresent(this.spiritualRoots::add);
            }
        }
        if(tag.contains("SpiritualMana")){
            this.spiritualMana = tag.getFloat("SpiritualMana");
        }
        if(tag.contains("SpellCooldown")){
            this.spellCooldown = tag.getInt("SpellCooldown");
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
        this.getUsingSpell().ifPresent(spell -> tag.putString("UsingSpell", spell.getRegistryName()));
        {
            final ListTag list = new ListTag();
            this.learnedSpells.forEach((spell, level) -> {
                final CompoundTag nbt = new CompoundTag();
                nbt.putString("Spell", spell.getRegistryName());
                nbt.putInt("Level", level);
                list.add(nbt);
            });
            tag.put("LearnedSpells", list);
        }
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.putInt("Count", this.spiritualRoots.size());
            for(int i = 0; i < this.spiritualRoots.size(); ++ i){
                nbt.putString("Root_" + i, this.getSpiritualTypes().get(i).getRegistryName());
            }
            tag.put("SpiritualRoots", nbt);
        }
        tag.putFloat("SpiritualMana", this.spiritualMana);
        tag.putInt("SpellCooldown", this.spellCooldown);
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

    public void setUsingSpell(@javax.annotation.Nullable ISpellType spell){
        entityData.set(USING_SPELL, Optional.ofNullable(spell));
    }

    public Optional<ISpellType> getUsingSpell(){
        return entityData.get(USING_SPELL);
    }

    protected IRealmType getDefaultRealm() {
        return RealmTypes.MORTALITY;
    }

    @Nullable
    protected SoundEvent getSpawnSound() {
        return null;
    }

    @Override
    public float getMana() {
        return this.spiritualMana;
    }

    @Override
    public void addMana(float amount) {
        this.spiritualMana = Mth.clamp(this.spiritualMana + amount, 0, this.getMaxMana());
    }

    @Override
    public boolean isManaFull() {
        return this.getMana() >= this.getMaxMana();
    }

    public float getMaxMana() {
        return this.getRealm().getSpiritualValue();
    }

    @Override
    public boolean isOnCooldown() {
        return this.spellCooldown > 0;
    }

    @Override
    public int getSpellLevel(ISpellType spell) {
        return learnedSpells.getOrDefault(spell, 0);
    }

    @Override
    public Set<ISpellType> getLearnedSpellTypes() {
        return learnedSpells.keySet();
    }

    @Override
    public Mob self() {
        return this;
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
