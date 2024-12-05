package hungteen.imm.common.entity;

import com.mojang.serialization.Codec;
import hungteen.htlib.util.SimpleWeightedList;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmLevel;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.entity.Cultivatable;
import hungteen.imm.api.event.EntityRandomSpellEvent;
import hungteen.imm.api.event.EntityRealmEvent;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.*;
import hungteen.imm.common.cultivation.realm.MultiRealm;
import hungteen.imm.common.cultivation.spell.basic.ElementalMasterySpell;
import hungteen.imm.util.EventUtil;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-21 18:37
 **/
public abstract class IMMMob extends PathfinderMob implements IEntityWithComplexSpawn, Cultivatable {

    protected static final int MELEE_ATTACK_ID = 4;
    private static final EntityDataAccessor<RealmType> REALM = SynchedEntityData.defineId(IMMMob.class, IMMDataSerializers.REALM.get());
    private static final EntityDataAccessor<Optional<SpellType>> USING_SPELL = SynchedEntityData.defineId(IMMMob.class, IMMDataSerializers.OPT_SPELL.get());
    protected static final EntityDataAccessor<Integer> CURRENT_ANIMATION = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Long> ANIMATION_START_TICK = SynchedEntityData.defineId(IMMMob.class, EntityDataSerializers.LONG);
    private final Map<SpellType, Integer> learnedSpells = new HashMap<>();
    private final Set<QiRootType> qiRoots = new HashSet<>();
    protected float qiAmount;
    private int spellCooldown;

    public IMMMob(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.xpReward = 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(REALM, RealmTypes.MORTALITY)
                .define(USING_SPELL, Optional.empty())
                .define(CURRENT_ANIMATION, AnimationTypes.IDLING.ordinal())
                .define(ANIMATION_START_TICK, 0L);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE)
                .add(IMMAttributes.MAX_QI_AMOUNT.holder())
                ;
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
        this.qiRoots.addAll(this.getInitialRoots(accessor));
        this.updateRealm(this.getInitialRealm(accessor, difficultyInstance));
        getInitialSpells(accessor, difficultyInstance, this.getRealm()).forEach(spell -> this.learnedSpells.put(spell.spell(), spell.level()));
    }

    /**
     * 随机初始化灵根。
     */
    protected Collection<QiRootType> getInitialRoots(ServerLevelAccessor accessor) {
        return List.of();
    }

    /**
     * 默认的境界随机实现，首先根据难度和权重随机选择一个初始大境界，小境界均匀随机。
     */
    public RealmType getInitialRealm(ServerLevelAccessor accessor, DifficultyInstance difficulty) {
        List<MultiRealm> multiRealms = getMultiRealms();
        List<Integer> weights = MathUtil.genLinearWeights(multiRealms.size(), calculateDifficulty(difficulty), 1.2);
        MultiRealm multiRealm = SimpleWeightedList.list(multiRealms, weights).getItem(accessor.getRandom()).orElseThrow();
        RealmType[] realms = multiRealm.getRealms();
        return realms[accessor.getRandom().nextInt(realms.length)];
    }

    public float calculateDifficulty(DifficultyInstance difficulty) {
        // TODO 更合理的难度设置。
        return (difficulty.getDifficulty().ordinal() - 1) * 1.0F / 2;
    }

    /**
     * @return 获取可能的大境界。
     */
    public List<MultiRealm> getMultiRealms() {
        return List.of();
    }

    public List<Spell> getInitialSpells(ServerLevelAccessor accessor, DifficultyInstance difficulty, RealmType realm) {
        List<Spell> spells = new ArrayList<>();
        Set<Element> elements = CultivationManager.getElements(this).collect(Collectors.toSet());
        for (Element element : elements) {
            spells.addAll(getRandomSpells(accessor.getRandom(), element, realm));
        }
        addSpecialSpells(accessor.getRandom(), elements, realm, spells);
        EventUtil.post(new EntityRandomSpellEvent(this, spells, elements, realm, random));
        return spells;
    }

    public List<Spell> getRandomSpells(RandomSource random, Element element, RealmType realm){
        return List.of();
    }

    public void addSpecialSpells(RandomSource random, Set<Element> elements, RealmType realm, List<Spell> spells){
        // 威压。
        if(realm.getRealmLevel() == RealmLevel.FOUNDATION){
            spells.add(Spell.create(SpellTypes.INTIMIDATION));
        }
        // 元素精通。
        for(Element element : elements){
            spells.add(ElementalMasterySpell.create(element, realm.getRealmLevel()));
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.qiRoots.clear();
        final int size = additionalData.readInt();
        for (int i = 0; i < size; ++i) {
            QiRootTypes.registry().getValue(additionalData.readResourceLocation()).ifPresent(this.qiRoots::add);
        }
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.qiRoots.size());
        this.qiRoots.forEach(root -> {
            buffer.writeResourceLocation(root.getLocation());
        });
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
    public boolean canUseSpell(SpellType spell) {
        if (spell.getCategory() == SpellUsageCategory.PLAYER_ONLY) {
            return false;
        }
        if (spell.getCategory().requireEntityTarget()) {
            return EntityHelper.isEntityValid(this.getTarget());
        }
        return true;
    }

    @Override
    public int getSpellUsePriority(SpellType spell) {
        if(spell.equals(SpellTypes.LAVA_BREATHING)){
            return this.isOnFire() ? VERY_HIGH : VERY_LOW;
        }
        return getCategoryPriority().getOrDefault(spell.getCategory(), 0);
    }

    @Override
    public void trigger(@NotNull Spell spell) {
        this.setUsingSpell(spell.spell());
        this.addQiAmount(-spell.spell().getConsumeMana());
        this.setCoolDown(spell.spell().getCooldown());
        this.usedSpell(spell);
    }

    protected void usedSpell(@NotNull Spell spell) {

    }

    @Override
    public HTHitResult createHitResult() {
        return new HTHitResult(this.getTarget());
    }

    /**
     * 没有敌对目标时才能骑乘载具。
     */
    @Override
    protected boolean canRide(Entity vehicle) {
        return super.canRide(vehicle) && (!(vehicle instanceof VehicleEntity) || this.getTarget() == null);
    }

    public void serverChange(int id) {
        this.level().broadcastEntityEvent(this, (byte) id);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, RealmTypes.registry().byNameCodec(), "EntityRealm", this::setRealm);
        NBTUtil.read(tag, SpellTypes.registry().byNameCodec(), "UsingSpell", this::setUsingSpell);
        NBTUtil.readMap(tag, SpellTypes.registry().byNameCodec(), Codec.INT, "LearnedSpells", map -> {
            this.learnedSpells.clear();
            this.learnedSpells.putAll(map);
        });
        NBTUtil.readList(tag, QiRootTypes.registry().byNameCodec(), "Roots", list -> {
            this.qiRoots.clear();
            this.qiRoots.addAll(list);
        });
        NBTUtil.read(tag, tag::getFloat, "QiAmount", l -> this.qiAmount = l);
        NBTUtil.read(tag, tag::getInt, "SpellCooldown", this::setCoolDown);
        NBTUtil.read(tag, tag::getInt, "CurrentAnimation", this::setCurrentAnimation);
        NBTUtil.read(tag, tag::getLong, "AnimationStartTick", this::setAnimationStartTick);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag, RealmTypes.registry().byNameCodec(), "EntityRealm", this.getRealm());
        this.getUsingSpell().ifPresent(spell -> {
            NBTUtil.write(tag, SpellTypes.registry().byNameCodec(), "UsingSpell", spell);
        });
        NBTUtil.writeMap(tag, SpellTypes.registry().byNameCodec(), Codec.INT, "LearnedSpells", this.learnedSpells);
        NBTUtil.writeList(tag, QiRootTypes.registry().byNameCodec(), "Roots", this.qiRoots);
        NBTUtil.write(tag::putFloat, "QiAmount", this.qiAmount);
        tag.putInt("SpellCooldown", this.spellCooldown);
        tag.putInt("CurrentAnimation", this.getAnimationIndex());
        tag.putLong("AnimationStartTick", this.getAnimationStartTick());
    }

    @Override
    public final void updateRealm(RealmType newRealm) {
        EntityRealmEvent.Pre preEvent = new EntityRealmEvent.Pre(this, getRealm(), newRealm);
        if (!EventUtil.post(preEvent)) {
            newRealm = preEvent.getAfterRealm();
            RealmManager.updateRealmAttributes(this, getRealm(), newRealm);
            onUpdateRealm(newRealm);
            this.setRealm(newRealm);
            EventUtil.post(new EntityRealmEvent.Post(this, getRealm(), newRealm));
        }
    }

    protected void onUpdateRealm(RealmType realm){

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

    public void setUsingSpell(@javax.annotation.Nullable SpellType spell) {
        entityData.set(USING_SPELL, Optional.ofNullable(spell));
    }

    public Optional<SpellType> getUsingSpell() {
        return entityData.get(USING_SPELL);
    }

    @Nullable
    protected SoundEvent getSpawnSound() {
        return null;
    }

    @Override
    public float getQiAmount() {
        return this.qiAmount;
    }

    @Override
    public void addQiAmount(float amount) {
        this.qiAmount = Mth.clamp(this.qiAmount + amount, 0, this.getMaxQiAmount());
    }

    @Override
    public boolean isQiFull() {
        return this.getQiAmount() >= this.getMaxQiAmount();
    }

    @Override
    public float getMaxQiAmount() {
        return (float) QiManager.getMaxQi(this);
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
    public int getSpellLevel(SpellType spell) {
        return learnedSpells.getOrDefault(spell, 0);
    }

    @Override
    public Set<SpellType> getLearnedSpellTypes() {
        return learnedSpells.keySet();
    }

    @Override
    public Mob self() {
        return this;
    }

    @Override
    public Set<QiRootType> getRoots() {
        return qiRoots;
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
