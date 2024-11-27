package hungteen.imm.common.entity;

import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.cultivation.*;
import hungteen.imm.api.enums.SpellUsageCategories;
import hungteen.imm.api.interfaces.IHasSpell;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-21 18:37
 **/
public abstract class IMMMob extends PathfinderMob implements IEntityWithComplexSpawn, IHasRoot, ICultivatable, IHasMana, IHasSpell {

    protected static final int MELEE_ATTACK_ID = 4;
    private static final EntityDataAccessor<RealmType> REALM = SynchedEntityData.defineId(IMMMob.class, IMMDataSerializers.REALM.get());
    private static final EntityDataAccessor<Optional<ISpellType>> USING_SPELL = SynchedEntityData.defineId(IMMMob.class, IMMDataSerializers.OPT_SPELL.get());
    protected static final EntityDataAccessor<Integer> CURRENT_ANIMATION = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Long> ANIMATION_START_TICK = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.LONG);
    private final Map<ISpellType, Integer> learnedSpells = new HashMap<>();
    private final Set<QiRootType> spiritualRoots = new HashSet<>();
    protected float spiritualMana;
    private int spellCooldown;

    public IMMMob(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(REALM, getDefaultRealm())
                .define(USING_SPELL, Optional.empty())
                .define(CURRENT_ANIMATION, AnimationTypes.IDLING.ordinal())
                .define(ANIMATION_START_TICK, 0L);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
        if (!accessor.isClientSide()) {
            Optional.ofNullable(this.getSpawnSound()).ifPresent(l -> this.playSound(l, this.getSoundVolume(), this.getVoicePitch()));
            this.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, data);
    }

    /**
     * 加入世界前的最后处理。
     */
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        this.spiritualRoots.addAll(this.createSpiritualRoots(accessor));
        createLearnSpells().forEach(spell -> this.learnedSpells.put(spell.spell(), spell.level()));
    }

    /**
     * 随机初始化灵根。
     */
    protected Collection<QiRootType> createSpiritualRoots(ServerLevelAccessor accessor) {
        return List.of();
    }

    protected List<Spell> createLearnSpells() {
        return List.of();
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
//        final int count = additionalData.readInt();
//        this.spiritualRoots.clear();
//        for (int i = 0; i < count; ++i) {
//            Holder<ISpiritualType> root = SpiritualTypes.registry().helper().getStreamCodec().decode(additionalData);
//            this.spiritualRoots.add(root.value());
//        }
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
//        buffer.writeInt(this.spiritualRoots.size());
//        this.spiritualRoots.forEach(type -> {
//            buffer.writeRegistryIdUnsafe(SpiritualTypes.registry().getRegistry(), type);
//        });
    }

    @Override
    public void tick() {
        super.tick();
        if (EntityHelper.isServer(this)) {
            if (this.spellCooldown > 0) {
                --this.spellCooldown;
            }
        }
    }

    @Override
    public boolean canUseSpell(ISpellType spell) {
        if (spell.getCategory() == SpellUsageCategories.PLAYER_ONLY) {
            return false;
        }
        if (spell.getCategory().requireEntityTarget()) {
            return EntityHelper.isEntityValid(this.getTarget());
        }
        return true;
    }

    @Override
    public void trigger(@NotNull Spell spell) {
        this.setUsingSpell(spell.spell());
        this.addMana(-spell.spell().getConsumeMana());
        this.setCoolDown(spell.spell().getCooldown());
        this.usedSpell(spell);
    }

    protected void usedSpell(@NotNull Spell spell) {

    }

    @Override
    public boolean canAttack(LivingEntity living) {
        if (super.canAttack(living)) {
            //TODO 不攻击高境界。
//            return RealmManager.getRealm(living);
            return true;
        }
        return false;
    }

    @Override
    public HTHitResult createHitResult() {
        return new HTHitResult(this.getTarget());
    }

    public void serverChange(int id) {
        this.level().broadcastEntityEvent(this, (byte) id);
    }

//    @Override
//    public int getBaseExperienceReward() {
//        final float realmXp = CultivationManager.getStageRequiredCultivation(getRealm(), getRealmStage()) * 0.05F;
//        return (int) (realmXp + this.xpReward);
//    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("EntityRealm")) {
            CodecHelper.parse(RealmTypes.registry().byNameCodec(), tag.get("EntityRealm"))
                    .result().ifPresentOrElse(this::setRealm, () -> this.setRealm(this.getDefaultRealm()));
        }
        if (tag.contains("UsingSpell")) {
            SpellTypes.registry().getValue(tag.getString("UsingSpell")).ifPresent(this::setUsingSpell);
        }
        if (tag.contains("LearnedSpells")) {
            this.learnedSpells.clear();
            final ListTag list = NBTUtil.list(tag, "LearnedSpells");
            for (int i = 0; i < list.size(); ++i) {
                final CompoundTag nbt = list.getCompound(i);
                if (nbt.contains("Spell") && nbt.contains("Level")) {
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
            for (int i = 0; i < count; ++i) {
                QiRootTypes.registry().getValue(nbt.getString("Root_" + i)).ifPresent(this.spiritualRoots::add);
            }
        }
        if (tag.contains("SpiritualMana")) {
            this.spiritualMana = tag.getFloat("SpiritualMana");
        }
        if (tag.contains("SpellCooldown")) {
            this.spellCooldown = tag.getInt("SpellCooldown");
        }
        if (tag.contains("CurrentAnimation")) {
            this.setCurrentAnimation(tag.getInt("CurrentAnimation"));
        }
        if (tag.contains("AnimationStartTick")) {
            this.setAnimationStartTick(tag.getLong("AnimationStartTick"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getRealm() != null) {
            CodecHelper.encodeNbt(RealmTypes.registry().byNameCodec(), this.getRealm())
                    .result().ifPresent(nbt -> tag.put("EntityRealm", nbt));
        }
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
            for (int i = 0; i < this.spiritualRoots.size(); ++i) {
                nbt.putString("Root_" + i, this.getSpiritualTypes().get(i).getRegistryName());
            }
            tag.put("SpiritualRoots", nbt);
        }
        tag.putFloat("SpiritualMana", this.spiritualMana);
        tag.putInt("SpellCooldown", this.spellCooldown);
        tag.putInt("CurrentAnimation", this.getAnimationIndex());
        tag.putLong("AnimationStartTick", this.getAnimationStartTick());
    }

    public void setRealm(RealmType realm) {
        entityData.set(REALM, realm);
    }

    @Override
    public RealmType getRealm() {
        return entityData.get(REALM);
    }

    public boolean inAnimationRange(int from, int to) {
        return getAnimationStartTick() + from <= this.level().getGameTime() && getAnimationStartTick() + to >= this.level().getGameTime();
    }

    public boolean atAnimationTick(int tick) {
        return getAnimationStartTick() + tick == this.level().getGameTime();
    }

    public boolean afterAnimationTick(int pos) {
        return getAnimationStartTick() + pos <= level().getGameTime();
    }

    public AnimationTypes getCurrentAnimation() {
        return AnimationTypes.values()[Math.min(getAnimationIndex(), AnimationTypes.values().length - 1)];
    }

    private int getAnimationIndex() {
        return entityData.get(CURRENT_ANIMATION);
    }

    public void setCurrentAnimation(AnimationTypes animation) {
        setCurrentAnimation(animation.ordinal());
        this.setAnimationStartTick(this.level().getGameTime());
    }

    private void setCurrentAnimation(int animation) {
        entityData.set(CURRENT_ANIMATION, animation);
    }

    public long getAnimationStartTick() {
        return entityData.get(ANIMATION_START_TICK);
    }

    public void setAnimationStartTick(long tick) {
        entityData.set(ANIMATION_START_TICK, tick);
    }

    public boolean isIdle() {
        return this.getCurrentAnimation() == AnimationTypes.IDLING;
    }

    public void setIdle() {
        this.setCurrentAnimation(AnimationTypes.IDLING);
    }

    public void setUsingSpell(@javax.annotation.Nullable ISpellType spell) {
        entityData.set(USING_SPELL, Optional.ofNullable(spell));
    }

    public Optional<ISpellType> getUsingSpell() {
        return entityData.get(USING_SPELL);
    }

    /**
     * 初始境界。
     */
    public RealmType getDefaultRealm() {
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

    @Override
    public float getMaxMana() {
        return (float) EntityUtil.getMaxQi(this);
    }

    @Override
    public boolean isOnCoolDown() {
        return this.spellCooldown > 0;
    }

    @Override
    public void setCoolDown(int cd) {
        this.spellCooldown = cd;
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
    public List<QiRootType> getSpiritualTypes() {
        return spiritualRoots.stream().toList();
    }

    public enum AnimationTypes {

        IDLING,
        IDLING_1,
        IDLING_2,

        ROAR,

        HEAL,

        FLAP,

        SHOOT,

        SWING,

        RUSH,

        EAT,

        /**
         * 闲置和攻击之间的过渡。
         */
        IDLE_TO_ATTACK,

        /**
         * 攻击和闲置之间的过渡。
         */
        ATTACK_TO_IDLE,
    }
}
