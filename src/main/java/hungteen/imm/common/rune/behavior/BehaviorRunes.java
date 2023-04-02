package hungteen.imm.common.rune.behavior;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:02
 **/
public class BehaviorRunes {

    private static final HTSimpleRegistry<IBehaviorRune> BEHAVIOR_RUNES = HTRegistryManager.create(Util.prefix("behavior_runes"));
    private static final List<IBehaviorRune> TYPES = new ArrayList<>();

    public static IHTSimpleRegistry<IBehaviorRune> registry(){
        return BEHAVIOR_RUNES;
    }

//    public static final RuneManager.IBehaviorRune MELEE_ATTACK = new BehaviorRune("melee_attack",
//            (golem) -> new MeleeAttack(30)
//    );
//
//    public static final RuneManager.IBehaviorRune FIND_TARGET = new BehaviorRune("find_target",
//            (golem) -> new NearestTargetGoal()
//    );
//
//    public static final RuneManager.IBehaviorRune MOVE_TO_TARGET = new BehaviorRune("move_to_target",
//            (golem) -> new MoveToTargetSink()
//    );
//
//    public static final RuneManager.IBehaviorRune STOP_ATTACK_IF_TARGET_INVALID = new BehaviorRune("stop_attacking_if_target_invalid",
//            (golem) -> new StopAttackingIfTargetInvalid<>()
//    );
//
//    public static final RuneManager.IBehaviorRune SET_WALK_TARGET_FROM_ATTACK_TARGET = new BehaviorRune("set_walk_target_from_attack_target",
//            (golem) -> new SetWalkTargetFromAttackTargetIfTargetOutOfReach((livingEntity -> 0.5F))
//    );

    public static class BehaviorRune implements IBehaviorRune {

        private final String name;
        private final Function<GolemEntity, Behavior<? super GolemEntity>> behaviorFunction;
        private Behavior<? super GolemEntity> behaviorCache;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            registry().register(TYPES);
        }

        public BehaviorRune(String name, Function<GolemEntity, Behavior<? super GolemEntity>> behaviorFunction){
            this.name = name;
            this.behaviorFunction = behaviorFunction;
            TYPES.add(this);
        }

        @Override
        public MutableComponent getComponent() {
            return IBehaviorRune.super.getComponent().withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD);
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
        public Function<GolemEntity, Behavior<? super GolemEntity>> getBehaviorFunction() {
            return this.behaviorFunction;
        }

        @Override
        public Map<MemoryModuleType<?>, MemoryStatus> requireMemoryStatus(Level level) {
            return Map.of();
//            if(level == null){
//                return Map.of();
//            } else{
//                if(this.behaviorCache == null){
//                    this.behaviorCache = this.getBehaviorFunction().apply(new IronGolem(ImmortalEntities.IRON_GOLEM.get(), level));
//                }
//                return this.behaviorCache.entryCondition;
//            }
        }

        @Override
        public List<Class<?>> getPredicateClasses() {
            return List.of();
        }
    }

}
