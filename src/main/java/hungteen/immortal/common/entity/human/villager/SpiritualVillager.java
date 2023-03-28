package hungteen.immortal.common.entity.human.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import hungteen.immortal.common.entity.ai.ImmortalActivities;
import hungteen.immortal.common.entity.ai.behavior.BreakBoat;
import hungteen.immortal.common.entity.ai.behavior.villager.Mock;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.entity.human.VillagerLikeEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 10:50
 **/
public abstract class SpiritualVillager extends VillagerLikeEntity {

    public SpiritualVillager(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void refreshBrain(ServerLevel level) {
        Brain<SpiritualVillager> brain = this.getBrain();
        brain.stopAll(level, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    @Override
    public void updateBrain(ServerLevel level) {
        this.getBrain().tick(level, this);
    }

    protected void registerBrainGoals(Brain<SpiritualVillager> brain){
//        if (this.isBaby()) {
//            p_35425_.setSchedule(Schedule.VILLAGER_BABY);
//            p_35425_.addActivity(Activity.PLAY, VillagerGoalPackages.getPlayPackage(0.5F));
//        } else {
//            p_35425_.setSchedule(Schedule.VILLAGER_DEFAULT);
//            p_35425_.addActivityWithConditions(Activity.WORK, VillagerGoalPackages.getWorkPackage(villagerprofession, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
//        }
        final float speed = 0.5F;
        brain.addActivity(Activity.CORE, this.getCoreBehaviors());
//        brain.addActivityWithConditions(Activity.MEET, this.getMeetBehaviors(), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
//        brain.addActivity(Activity.REST, VillagerGoalPackages.getRestPackage(villagerprofession, 0.5F));
        brain.addActivity(Activity.IDLE, this.getIdleBehaviors(speed));
//        brain.addActivity(Activity.PANIC, VillagerGoalPackages.getPanicPackage(villagerprofession, 0.5F));
//        brain.addActivity(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage(villagerprofession, 0.5F));
//        brain.addActivity(Activity.RAID, VillagerGoalPackages.getRaidPackage(villagerprofession, 0.5F));
//        brain.addActivity(Activity.HIDE, VillagerGoalPackages.getHidePackage(villagerprofession, 0.5F));
        brain.addActivity(ImmortalActivities.MELEE_FIGHT.get(), this.getMeleeFightBehaviors(speed));
        brain.addActivity(ImmortalActivities.RANGE_FIGHT.get(), this.getRangeFightBehaviors(speed));
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
//        brain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }

    /**
     * Tht Core Behaviors that will always run. <br>
     * Swim <br>
     * InteractWithDoor(PATH, DOORS_TO_CLOSE) : Open doors in path. <br>
     * LookAtTargetSink(LOOK_TARGET) : Look at the target. <br>
     * BreakBoat(LOOK_TARGET, NEAREST_BOAT) : Break boat to avoid stuck ! <br>
     * MoveToTargetSink(CANT_REACH_WALK_TARGET_SINCE, PATH, WALK_TARGET) : Walk to target. <br>
     */
    public ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualVillager>>> getCoreBehaviors() {
        return ImmutableList.of(
                Pair.of(0, new Swim(0.8F)),
//                Pair.of(0, new InteractWithDoor()),
                Pair.of(0, new LookAtTargetSink(45, 90)),
                Pair.of(0, new BreakBoat()),
//                Pair.of(0, new VillagerPanicTrigger()),
//                Pair.of(0, new WakeUp()),
//                Pair.of(0, new ReactToBell()),
//                Pair.of(0, new SetRaidStatus()),
                Pair.of(1, new MoveToTargetSink())
//                Pair.of(3, new LookAndFollowTradingPlayerSink(p_24587_)),
//                Pair.of(5, new GoToWantedItem(p_24587_, false, 4)),
        );
    }

    /**
     * The Idle Behaviors that triggered when nothing to focus. <br>
     * Mock : perform mock action. <br>
     * SetLookAndInteract(LOOK_TARGET, INTERACTION_TARGET, NEAREST_VISIBLE_LIVING_ENTITIES) : Look at specific entity. <br>
     */
    public ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualVillager>>> getIdleBehaviors(float speed) {
        return ImmutableList.of(
//                Pair.of(2, new RunOne<>(ImmutableList.of(
//                        Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, p_24600_, 2), 2),
//                        Pair.of(new InteractWith<>(EntityType.VILLAGER, 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, p_24600_, 2), 1),
//                        Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, p_24600_, 2), 1),
//                        Pair.of(new VillageBoundRandomStroll(speed), 1),
//                        Pair.of(new SetWalkTargetFromLookTarget(speed, 2), 1)
//                        Pair.of(new JumpOnBed(p_24600_), 1),
//                        Pair.of(new DoNothing(30, 60), 1)
//                ))),
//                Pair.of(3, new GiveGiftToHero(100)),
                Pair.of(3, new Mock())
//                Pair.of(3, new SetLookAndInteract(EntityType.PLAYER, 4))
//                Pair.of(3, new ShowTradesToPlayer(400, 1600)),
//                Pair.of(3, new GateBehavior<>(
//                        ImmutableMap.of(),
//                        ImmutableSet.of(MemoryModuleType.BREED_TARGET),
//                        GateBehavior.OrderPolicy.ORDERED,
//                        GateBehavior.RunningPolicy.RUN_ONE,
//                        ImmutableList.of(Pair.of(new VillagerMakeLove(), 1)))
//                ), getFullLookBehavior(),
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualVillager>>> getMeleeFightBehaviors(float speed) {
        return ImmutableList.of(
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    /**
     * The Melee Fight Behaviors that triggered when there exist enemy. <br>
     */
    public ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualVillager>>> getRangeFightBehaviors(float speed) {
        return ImmutableList.of(
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    /**
     *
     */
    public ImmutableList<Pair<Integer, ? extends Behavior<? super SpiritualVillager>>> getMeetBehaviors() {
        return ImmutableList.of(
//                Pair.of(2, new RunOne<>(ImmutableList.of(
//                        Pair.of(new StrollAroundPoi(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2),
//                        Pair.of(new SocializeAtBell(), 2)
//                        ))
//                ),
//                Pair.of(10, new ShowTradesToPlayer(400, 1600)),
//                Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)),
//                Pair.of(2, new SetWalkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, p_24597_, 6, 100, 200)),
//                Pair.of(3, new GiveGiftToHero(100)),
//                Pair.of(3, new ValidateNearbyPoi((p_217493_) -> {
//                    return p_217493_.is(PoiTypes.MEETING);
//                    }, MemoryModuleType.MEETING_POINT)
//                ),
//                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    @Override
    public Brain<SpiritualVillager> getBrain() {
        return (Brain<SpiritualVillager>)super.getBrain();
    }

    @Override
    protected Brain<SpiritualVillager> makeBrain(Dynamic<?> dynamic) {
        Brain<SpiritualVillager> brain = this.brainProvider().makeBrain(dynamic);
        this.registerBrainGoals(brain);
        return brain;
    }

    @Override
    protected Brain.Provider<SpiritualVillager> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        } else {
            return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

}
