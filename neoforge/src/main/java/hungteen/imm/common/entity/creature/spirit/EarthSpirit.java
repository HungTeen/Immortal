package hungteen.imm.common.entity.creature.spirit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.IMMActivities;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.ai.IMMSensors;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.util.BehaviorUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/27 13:07
 **/
public class EarthSpirit extends ElementSpirit {

    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState eatAnimationState = new AnimationState();

    public EarthSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected Brain.Provider<EarthSpirit> brainProvider() {
        return Brain.provider(List.of(
                /* Nearest Living Entities Sensor */
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                /* Nearest Players Sensor */
                MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                /* Hurt By Sensor */
                MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY,
                /* MoveToTargetSink Behavior*/
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.WALK_TARGET,
                /* GoToWantedItem Behavior */
                MemoryModuleType.LOOK_TARGET,
                /* LookAnInteract Behavior */
                MemoryModuleType.INTERACTION_TARGET,
                /* Fight */
                MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN,
                /* Custom */
                IMMMemories.ELEMENT_AMETHYST.get()
        ), List.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.HURT_BY,
                IMMSensors.NEAREST_AMETHYST.get()
        ));
    }

    @Override
    protected Brain<EarthSpirit> makeBrain(Dynamic<?> dynamic) {
        Brain<EarthSpirit> brain = this.brainProvider().makeBrain(dynamic);
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new Swim(0.7F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
        ));
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                StartAttacking.create(l -> findNearestValidAttackTarget(this, WaterSpirit.class::isInstance)),
                new RunOne<>(ImmutableList.of(
                        Pair.of(RandomStroll.stroll(1), 1),
                        Pair.of(SetWalkTargetFromLookTarget.create(1, 3), 1),
                        Pair.of(SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                ))
        ));
        brain.addActivityAndRemoveMemoryWhenStopped(IMMActivities.MELEE_FIGHT.get(), 0, ImmutableList.of(
                StopAttackingIfTargetInvalid.create(),
                SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.1F),
                new RushAttack()
        ), MemoryModuleType.ATTACK_TARGET);
        brain.addActivityAndRemoveMemoryWhenStopped(IMMActivities.EAT.get(), 0, ImmutableList.of(
                createPath(),
                new EatAttack()
        ), IMMMemories.ELEMENT_AMETHYST.get());
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    @Override
    public Brain<EarthSpirit> getBrain() {
        return (Brain<EarthSpirit>) super.getBrain();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ElementSpirit.createAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ARMOR, 2)
                .add(Attributes.ARMOR_TOUGHNESS, 1)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                ;
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if(accessor.equals(CURRENT_ANIMATION)){
            this.eatAnimationState.stop();
            this.attackAnimationState.stop();
            switch (this.getCurrentAnimation()){
                case EAT -> {
                    this.eatAnimationState.startIfStopped(tickCount);
                }
                case RUSH -> {
                    this.attackAnimationState.startIfStopped(tickCount);
                }
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("earthSpiritBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        this.level().getProfiler().push("earthSpiritActivityUpdate");
        this.brain.setActiveActivityToFirstValid(ImmutableList.of(IMMActivities.EAT.get(), IMMActivities.MELEE_FIGHT.get(), Activity.IDLE));
        this.level().getProfiler().pop();
        super.customServerAiStep();
    }

    public void eat(ElementAmethyst amethyst){
        final float amount = ElementManager.getElementAmount(amethyst, Element.EARTH, false);
        ElementManager.addElementAmount(this, Element.EARTH, false, amount);
        ElementManager.addElementAmount(this, Element.SPIRIT, false, amount);
        this.heal(5);
        amethyst.breakAmethyst();
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        if (type == NeoForgeMod.WATER_TYPE.value()) {
            return false;
        }
        return super.canDrownInFluidType(type);
    }

    @Override
    public QiRootType getSpiritualRoot() {
        return QiRootTypes.EARTH;
    }

    @Override
    public Element getElement() {
        return Element.EARTH;
    }

    public static BehaviorControl<Mob> createPath() {
        return BehaviorBuilder.create((instance) -> instance.group(
                instance.registered(MemoryModuleType.WALK_TARGET),
                instance.registered(MemoryModuleType.LOOK_TARGET),
                instance.present(IMMMemories.ELEMENT_AMETHYST.get())
        ).apply(instance, (walkTarget, lookTarget, amethyst) -> (level, mob, time) -> {
            ElementAmethyst elementAmethyst = instance.get(amethyst);
            if (mob.closerThan(elementAmethyst, 2)) {
                walkTarget.erase();
            } else {
                lookTarget.set(new EntityTracker(elementAmethyst, true));
                walkTarget.set(new WalkTarget(new EntityTracker(elementAmethyst, false), 1.2F, 0));
            }

            return true;
        }));
    }

    static class EatAttack extends Behavior<EarthSpirit> {

        public EatAttack() {
            super(Map.of(
                    IMMMemories.ELEMENT_AMETHYST.get(), MemoryStatus.VALUE_PRESENT
            ), 60);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, EarthSpirit spirit) {
            final Optional<ElementAmethyst> opt = spirit.getBrain().getMemory(IMMMemories.ELEMENT_AMETHYST.get());
            return spirit.isIdle() && opt.isPresent() && spirit.closerThan(opt.get(), 2);
        }

        @Override
        protected boolean canStillUse(ServerLevel level, EarthSpirit spirit, long time) {
            return true;
        }

        @Override
        protected void start(ServerLevel level, EarthSpirit spirit, long time) {
            spirit.setCurrentAnimation(AnimationTypes.EAT);
        }

        @Override
        protected void tick(ServerLevel level, EarthSpirit spirit, long time) {
            spirit.getBrain().getMemory(IMMMemories.ELEMENT_AMETHYST.get()).ifPresent(target -> {
                if (spirit.atAnimationTick(25) && spirit.closerThan(target, 3)) {
                    spirit.eat(target);
                }
            });
        }

        @Override
        protected void stop(ServerLevel level, EarthSpirit spirit, long time) {
            spirit.setIdle();
        }
    }

    static class RushAttack extends Behavior<EarthSpirit> {

        public RushAttack() {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
            ), 60);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, EarthSpirit spirit) {
            return spirit.isIdle() && BehaviorUtil.getAttackTarget(spirit).isPresent() && spirit.isWithinMeleeAttackRange(BehaviorUtil.getAttackTarget(spirit).get());
        }

        @Override
        protected boolean canStillUse(ServerLevel level, EarthSpirit spirit, long time) {
            return true;
        }

        @Override
        protected void start(ServerLevel level, EarthSpirit spirit, long time) {
            spirit.setCurrentAnimation(AnimationTypes.RUSH);
        }

        @Override
        protected void tick(ServerLevel level, EarthSpirit spirit, long time) {
            BehaviorUtil.getAttackTarget(spirit).ifPresent(target -> {
                if (spirit.atAnimationTick(10) && spirit.isWithinMeleeAttackRange(target)) {
                    spirit.swing(InteractionHand.MAIN_HAND);
                    spirit.doHurtTarget(target);
                }
            });
        }

        @Override
        protected void stop(ServerLevel level, EarthSpirit spirit, long time) {
            spirit.setIdle();
        }
    }

}
