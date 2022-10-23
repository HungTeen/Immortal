package hungteen.immortal.common.entity.human;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import hungteen.immortal.api.interfaces.IHuman;
import hungteen.immortal.common.ai.ImmortalSchedules;
import hungteen.immortal.common.entity.ImmortalGrowableCreature;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 18:23
 **/
public abstract class HumanEntity extends ImmortalGrowableCreature implements IHuman {

    @javax.annotation.Nullable
    private Player tradingPlayer;
    @javax.annotation.Nullable
    protected MerchantOffers offers;
    private final SimpleContainer inventory = new SimpleContainer(8);

    public HumanEntity(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setCanPickUpLoot(true);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData groupData, @org.jetbrains.annotations.Nullable CompoundTag compoundTag) {
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, compoundTag);
    }

    @Override
    protected Brain<HumanEntity> makeBrain(Dynamic<?> dynamic) {
        Brain<HumanEntity> brain = this.brainProvider().makeBrain(dynamic);
        this.registerBrainGoals(brain);
        return brain;
    }

    @Override
    protected Brain.Provider<HumanEntity> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

    protected ImmutableList<MemoryModuleType<?>> getMemoryModules() {
        return ImmutableList.of(
                /* Nearest Living Entities Sensor */
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                /* Nearest Players Sensor */
                MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                /* Nearest Item Sensor */
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                /* Nearest Bed Sensor */
                MemoryModuleType.NEAREST_BED,
                /* Hurt By Sensor */
                MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY,
                /* MoveToTargetSink Behavior*/
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.WALK_TARGET,
                /* InteractWithDoor Behavior */
                MemoryModuleType.DOORS_TO_CLOSE,
//                MemoryModuleType.HOME,
//                MemoryModuleType.JOB_SITE,
//                MemoryModuleType.POTENTIAL_JOB_SITE,
//                MemoryModuleType.MEETING_POINT,
                /* GoToWantedItem Behavior */
                MemoryModuleType.LOOK_TARGET,
                /* LookAnInteract Behavior */
                MemoryModuleType.INTERACTION_TARGET
//                MemoryModuleType.BREED_TARGET,
//                MemoryModuleType.SECONDARY_JOB_SITE,
//                MemoryModuleType.HIDING_PLACE,
//                MemoryModuleType.HEARD_BELL_TIME,
//                MemoryModuleType.LAST_SLEPT,
//                MemoryModuleType.LAST_WOKEN,
//                MemoryModuleType.LAST_WORKED_AT_POI,
//                MemoryModuleType.GOLEM_DETECTED_RECENTLY
        );
    }

    protected ImmutableList<SensorType<? extends Sensor<? super HumanEntity>>> getSensorModules() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.NEAREST_BED,
                SensorType.HURT_BY
        );
    }

    protected void registerBrainGoals(Brain<HumanEntity> brain) {
        brain.setSchedule(ImmortalSchedules.DAY_WORK.get());
        brain.addActivity(Activity.CORE, AI.getCorePackage(0.5F));
        brain.addActivity(Activity.IDLE, AI.getIdlePackage(0.5F));
        brain.addActivity(Activity.WORK, ImmutableList.of());
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.level.getProfiler().push("villagerBrain");
        this.getBrain().tick((ServerLevel) this.level, this);
        this.level.getProfiler().pop();
    }

    @Override
    public Brain<HumanEntity> getBrain() {
        return (Brain<HumanEntity>) super.getBrain();
    }

    @Override
    public Container getInventory() {
        return this.inventory;
    }

    @Override
    public void setTradingPlayer(@javax.annotation.Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Override
    @Nullable
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    @Override
    public MerchantOffers getOffers() {
        return null;
    }

    @Override
    public void overrideOffers(MerchantOffers p_45306_) {

    }

    @Override
    public void notifyTrade(MerchantOffer p_45305_) {

    }

    @Override
    public void notifyTradeUpdated(ItemStack p_45308_) {

    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int p_45309_) {

    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    @Override
    public boolean isClientSide() {
        return this.level.isClientSide;
    }

    private static class AI {
        public static ImmutableList<Pair<Integer, ? extends Behavior<? super HumanEntity>>> getCorePackage(float speedModifier) {
            return ImmutableList.of(
                    Pair.of(0, new Swim(0.8F)),
                    Pair.of(0, new InteractWithDoor()),
//                    Pair.of(0, new LookAtTargetSink(45, 90)),
//                    Pair.of(0, new VillagerPanicTrigger()),
//                    Pair.of(0, new WakeUp()),
//                    Pair.of(0, new ReactToBell()),
//                    Pair.of(0, new SetRaidStatus()),
//                    Pair.of(0, new ValidateNearbyPoi(p_24586_.getJobPoiType(), MemoryModuleType.JOB_SITE)),
//                    Pair.of(0, new ValidateNearbyPoi(p_24586_.getJobPoiType(), MemoryModuleType.POTENTIAL_JOB_SITE)),
                    Pair.of(1, new MoveToTargetSink()),
//                    Pair.of(2, new PoiCompetitorScan(p_24586_)),
//                    Pair.of(3, new LookAndFollowTradingPlayerSink(p_24587_)),
                    Pair.of(5, new GoToWantedItem(speedModifier, false, 8))
//                    Pair.of(6, new AcquirePoi(p_24586_.getJobPoiType(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())),
//                    Pair.of(7, new GoToPotentialJobSite(p_24587_)),
//                    Pair.of(8, new YieldJobSite(p_24587_)),
//                    Pair.of(10, new AcquirePoi(PoiType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))),
//                    Pair.of(10, new AcquirePoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14))),
//                    Pair.of(10, new AssignProfessionFromJobSite()),
//                    Pair.of(10, new ResetProfession())
            );
        }

        public static ImmutableList<Pair<Integer, ? extends Behavior<? super HumanEntity>>> getIdlePackage(float speedModifier) {
            return ImmutableList.of(
                    Pair.of(2, new RunOne<>(ImmutableList.of(
                                    Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), 2),
//                                    Pair.of(new InteractWith<>(EntityType.VILLAGER, 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, speedModifier, 2), 1),
                                    Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), 1),
                                    Pair.of(new VillageBoundRandomStroll(speedModifier), 1),
                                    Pair.of(new SetWalkTargetFromLookTarget(speedModifier, 2), 1),
                                    Pair.of(new JumpOnBed(speedModifier), 1),
                                    Pair.of(new DoNothing(30, 60), 1))
                            )
                    ),
//                    Pair.of(3, new GiveGiftToHero(100)),
                    Pair.of(3, new SetLookAndInteract(EntityType.PLAYER, 4)),
//                    Pair.of(3, new ShowTradesToPlayer(400, 1600)),
//                    Pair.of(3, new GateBehavior<>(
//                                    ImmutableMap.of(),
//                                    ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET),
//                                    GateBehavior.OrderPolicy.ORDERED,
//                                    GateBehavior.RunningPolicy.RUN_ONE,
//                                    ImmutableList.of(
//                                            Pair.of(new TradeWithVillager(), 1)
//                                    )
//                            )
//                    ),
//                    Pair.of(3, new GateBehavior<>(
//                                    ImmutableMap.of(),
//                                    ImmutableSet.of(MemoryModuleType.BREED_TARGET),
//                                    GateBehavior.OrderPolicy.ORDERED,
//                                    GateBehavior.RunningPolicy.RUN_ONE,
//                                    ImmutableList.of(
//                                            Pair.of(new VillagerMakeLove(), 1)
//                                    )
//                            )
//                    ),
                    getFullLookBehavior(),
                    Pair.of(99, new UpdateActivityFromSchedule())
            );
        }

        private static Pair<Integer, Behavior<LivingEntity>> getFullLookBehavior() {
            return Pair.of(5, new RunOne<>(ImmutableList.of(
                            Pair.of(new SetEntityLookTarget(EntityType.CAT, 8.0F), 8),
                            Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), 2),
                            Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2),
                            Pair.of(new SetEntityLookTarget(MobCategory.CREATURE, 8.0F), 1),
                            Pair.of(new SetEntityLookTarget(MobCategory.WATER_CREATURE, 8.0F), 1),
                            Pair.of(new SetEntityLookTarget(MobCategory.AXOLOTLS, 8.0F), 1),
                            Pair.of(new SetEntityLookTarget(MobCategory.UNDERGROUND_WATER_CREATURE, 8.0F), 1),
                            Pair.of(new SetEntityLookTarget(MobCategory.WATER_AMBIENT, 8.0F), 1),
                            Pair.of(new SetEntityLookTarget(MobCategory.MONSTER, 8.0F), 1),
                            Pair.of(new DoNothing(30, 60), 2)
                    ))
            );
        }
    }
}
