package hungteen.imm.common.entity.creature.spirit;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.reaction.SummonSpiritReaction;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/8 20:59
 **/
public abstract class ElementSpirit extends IMMMob {

    private boolean attachedElement = false;
    private int realmLevel = 1;

    public ElementSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.addTargetGoals();
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1F, 80));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    protected void addTargetGoals(){
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, target -> {
            return ElementManager.hasElement(target, ElementManager.getTargetElement(getElement()), false);
        }));
        // 攻击其他 非同类和不携带同种强元素的生物。
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, target -> {
            return ! ElementManager.hasElement(target, getElement(), true) && !(target instanceof ElementSpirit spirit && spirit.getElement() == this.getElement());
        }));
    }

    public void updateRealm(){
        final float amount = ElementManager.getElementAmount(this, Element.SPIRIT, false);
        final int level = Mth.ceil(amount / SummonSpiritReaction.SPIRIT_COST);
        this.setRealmLevel(level);
        this.setRealm(getRealmByLevel(level));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(IMMAttributes.ELEMENT_DECAY_FACTOR.holder(), 0.1)
                ;
    }

    private static RealmType getRealmByLevel(int level) {
        switch (level) {
            case 1 -> {
                return RealmTypes.YAOGUAI_LEVEL_1.first();
            }
            case 2 -> {
                return RealmTypes.YAOGUAI_LEVEL_2.pre();
            }
            default -> {
                return RealmTypes.YAOGUAI_LEVEL_3.pre();
            }
        }
    }

    protected static Optional<? extends LivingEntity> findNearestValidAttackTarget(ElementSpirit spirit, Predicate<LivingEntity> hateSpiritPredicate) {
        final NearestVisibleLivingEntities nearestEntities = spirit.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        Optional<LivingEntity> result = nearestEntities.findClosest(hateSpiritPredicate);
        if(result.isEmpty()){
            result = nearestEntities.findClosest(target -> {
                return ElementManager.hasElement(target, ElementManager.getTargetElement(spirit.getElement()), false);
            });
            if(result.isEmpty()){
                result = nearestEntities.findClosest(target -> {
                    return ! ElementManager.hasElement(target, spirit.getElement(), true) && !(target instanceof ElementSpirit elementSpirit && elementSpirit.getElement() == spirit.getElement());
                });
            }
        }
        return result;
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(! this.attachedElement){
                final float amount = (float) RandomHelper.getMinMax(getRandom(), 5F, 30F);
                ElementManager.addElementAmount(this, Element.SPIRIT, false, amount);
                ElementManager.addElementAmount(this, getElement(), false, amount);
                this.attachedElement = true;
                this.updateRealm();
            }
            if(this.tickCount % 5 == 0){
                // No Support Element.
                if(! ElementManager.hasElement(this, Element.SPIRIT, false) || ! ElementManager.hasElement(this, getElement(), false)){
                    this.disappear();
                    this.discard();
                }
            }
        }
    }

    protected void disappear(){
        this.playSound(Objects.requireNonNull(this.getDeathSound()));
        ParticleUtil.spawnEntityParticle(this, ElementManager.getParticle(getElement()), 20, 0.04);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("AttachedElement")){
            this.attachedElement = tag.getBoolean("AttachedElement");
        }
        if(tag.contains("RealmLevel")){
            this.setRealmLevel(tag.getInt("RealmLevel"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("attachedElement", this.attachedElement);
        tag.putInt("RealmLevel", this.getRealmLevel());
    }

    public int getRealmLevel() {
        return realmLevel;
    }

    public void setRealmLevel(int realmLevel) {
        this.realmLevel = realmLevel;
    }

    public void setAttachedElement(boolean attachedElement) {
        this.attachedElement = attachedElement;
    }

    @Override
    protected Collection<QiRootType> getInitialRoots(ServerLevelAccessor accessor) {
        return List.of(QiRootTypes.SPIRIT, getSpiritualRoot());
    }

    public abstract QiRootType getSpiritualRoot();

    public abstract Element getElement();

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ALLAY_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ALLAY_HURT;
    }
}
