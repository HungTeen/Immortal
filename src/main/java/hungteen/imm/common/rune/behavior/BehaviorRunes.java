package hungteen.imm.common.rune.behavior;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.entity.ai.behavior.golem.*;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.runes.info.FilterRuneItem;
import hungteen.imm.util.Util;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:02
 **/
public class BehaviorRunes {

    private static final HTSimpleRegistry<IBehaviorRune> BEHAVIOR_RUNES = HTRegistryManager.create(Util.prefix("behavior_runes"));

    public static final IBehaviorRune SENSE_NEAREST_LIVINGS = register(
            new BehaviorRune("sense_nearest_livings",
                    GolemSenseNearestLivings::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.NEAREST_LIVING_ENTITIES, ImmutableList.of(MemoryStatus.REGISTERED, MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, ImmutableList.of(MemoryStatus.REGISTERED, MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune SENSE_HURT_BY = register(
            new BehaviorRune("sense_hurt_by",
                    GolemSenseHurtBy::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.HURT_BY, ImmutableList.of(MemoryStatus.REGISTERED, MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.HURT_BY_ENTITY, ImmutableList.of(MemoryStatus.REGISTERED, MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune FIND_LOOK_TARGET = register(
            new BehaviorRune("find_look_target",
                    GolemFindLookTarget::new,
                    ImmutableList.of(
                            IMMItems.ENTITY_FILTER_RUNE::get
                    ),
                    () -> ImmutableMap.of(
                            MemoryModuleType.LOOK_TARGET, ImmutableList.of(MemoryStatus.VALUE_ABSENT, MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, ImmutableList.of(MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune LOOK_AT_TARGET = register(
            new BehaviorRune("look_at_target",
                    GolemLookAtTarget::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.LOOK_TARGET, ImmutableList.of(MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune FIND_ATTACK_TARGET = register(
            new BehaviorRune("find_attack_target",
                    GolemFindAttackTarget::new,
                    ImmutableList.of(
                            IMMItems.ENTITY_FILTER_RUNE::get
                    ),
                    () -> ImmutableMap.of(
                            MemoryModuleType.ATTACK_TARGET, ImmutableList.of(MemoryStatus.VALUE_ABSENT, MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, ImmutableList.of(MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune RETALIATE = register(
            new BehaviorRune("retaliate",
                    GolemRetaliate::new,
                    ImmutableList.of(
                            IMMItems.ENTITY_FILTER_RUNE::get
                    ),
                    () -> ImmutableMap.of(
                            MemoryModuleType.ATTACK_TARGET, ImmutableList.of(MemoryStatus.VALUE_ABSENT, MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.HURT_BY_ENTITY, ImmutableList.of(MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune MELEE_ATTACK = register(
            new BehaviorRune("melee_attack",
                    GolemMeleeAttack::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.LOOK_TARGET, ImmutableList.of(MemoryStatus.REGISTERED, MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.ATTACK_TARGET, ImmutableList.of(MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune SET_WALK_FROM_LOOK_TARGET = register(
            new BehaviorRune("set_walk_from_look_target",
                    GolemWalkToLookTarget::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.LOOK_TARGET, ImmutableList.of(MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.WALK_TARGET, ImmutableList.of(MemoryStatus.VALUE_ABSENT, MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune RANDOM_STROLL = register(
            new BehaviorRune("random_stroll",
                    GolemRandomStroll::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.WALK_TARGET, ImmutableList.of(MemoryStatus.VALUE_ABSENT, MemoryStatus.VALUE_PRESENT)
                    )
            )
    );

    public static final IBehaviorRune MOVE_TO_TARGET = register(
            new BehaviorRune("move_to_target",
                    GolemMoveToTarget::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.PATH, ImmutableList.of(MemoryStatus.VALUE_ABSENT, MemoryStatus.VALUE_PRESENT, MemoryStatus.VALUE_ABSENT),
                            MemoryModuleType.WALK_TARGET, ImmutableList.of(MemoryStatus.VALUE_PRESENT, MemoryStatus.VALUE_ABSENT)
                    )
            )
    );

    public static final IBehaviorRune STOP_ATTACK_IF_TARGET_INVALID = register(
            new BehaviorRune("stop_attack_if_target_invalid",
                    GolemStopAttackingIfTargetInvalid::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.ATTACK_TARGET, ImmutableList.of(MemoryStatus.VALUE_PRESENT, MemoryStatus.VALUE_ABSENT)
                    )
            )
    );

    public static final IBehaviorRune SET_WALK_FROM_ATTACK_TARGET = register(
            new BehaviorRune("set_walk_from_attack_target",
                    GolemSetWalkToAttackTarget::new,
                    ImmutableList.of(),
                    () -> ImmutableMap.of(
                            MemoryModuleType.WALK_TARGET, ImmutableList.of(MemoryStatus.REGISTERED, MemoryStatus.VALUE_PRESENT, MemoryStatus.VALUE_ABSENT),
                            MemoryModuleType.LOOK_TARGET, ImmutableList.of(MemoryStatus.REGISTERED, MemoryStatus.VALUE_PRESENT, MemoryStatus.VALUE_ABSENT),
                            MemoryModuleType.ATTACK_TARGET, ImmutableList.of(MemoryStatus.VALUE_PRESENT),
                            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, ImmutableList.of(MemoryStatus.REGISTERED)
                    )
            )
    );

    public static IHTSimpleRegistry<IBehaviorRune> registry() {
        return BEHAVIOR_RUNES;
    }

    /**
     * {@link ImmortalMod#coreRegister()}
     */
    public static void register() {

    }

    public static IBehaviorRune register(IBehaviorRune rune) {
        return BEHAVIOR_RUNES.register(rune);
    }

    public static class BehaviorRune implements IBehaviorRune {

        private final String name;
        private final IBehaviorFactory behaviorFunction;
        private final List<Supplier<FilterRuneItem<?>>> filterRunes;
        private final Supplier<Map<MemoryModuleType<?>, List<MemoryStatus>>> statusMapSupplier;

        public BehaviorRune(String name, IBehaviorFactory behaviorFunction, List<Supplier<FilterRuneItem<?>>> filterRunes, Supplier<Map<MemoryModuleType<?>, List<MemoryStatus>>> statusMapSupplier) {
            this.name = name;
            this.behaviorFunction = behaviorFunction;
            this.filterRunes = filterRunes;
            this.statusMapSupplier = statusMapSupplier;
        }

        @Override
        public boolean costAmethyst() {
            return true;
        }

        @Override
        public int requireMaterial() {
            return 2;
        }

        @Override
        public int requireRedStone() {
            return 4;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public IBehaviorFactory getBehaviorFactory() {
            return this.behaviorFunction;
        }

        @Override
        public Map<MemoryModuleType<?>, List<MemoryStatus>> requireMemoryStatus() {
            return this.statusMapSupplier.get();
        }

        @Override
        public List<Supplier<FilterRuneItem<?>>> getFilterItems() {
            return this.filterRunes;
        }
    }

}
