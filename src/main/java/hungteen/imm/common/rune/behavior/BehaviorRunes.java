package hungteen.imm.common.rune.behavior;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.entity.ai.behavior.golem.GolemFindLookTarget;
import hungteen.imm.common.entity.ai.behavior.golem.GolemFindNearestLivings;
import hungteen.imm.common.entity.ai.behavior.golem.GolemLookAtTarget;
import hungteen.imm.common.entity.golem.GolemEntity;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:02
 **/
public class BehaviorRunes {

    private static final HTSimpleRegistry<IBehaviorRune> BEHAVIOR_RUNES = HTRegistryManager.create(Util.prefix("behavior_runes"));

    public static final IBehaviorRune FIND_NEAREST_LIVINGS = register(
            new BehaviorRune("find_nearest_livings", GolemFindNearestLivings::new, List.of())
    );

    public static final IBehaviorRune FIND_LOOK_TARGET = register(
            new BehaviorRune("find_look_target", GolemFindLookTarget::new, List.of())
    );

    public static final IBehaviorRune LOOK_AT_TARGET = register(
            new BehaviorRune("look_at_target", GolemLookAtTarget::new, List.of())
    );

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

    public static IHTSimpleRegistry<IBehaviorRune> registry(){
        return BEHAVIOR_RUNES;
    }

    /**
     * {@link ImmortalMod#coreRegister()}
     */
    public static void register(){

    }

    public static IBehaviorRune register(IBehaviorRune rune){
        return BEHAVIOR_RUNES.register(rune);
    }

    public static class BehaviorRune implements IBehaviorRune {

        private final String name;
        private final IBehaviorFactory behaviorFunction;
        private final List<Class<?>> predicateClasses;
        private Behavior<? super GolemEntity> behaviorCache;

        public BehaviorRune(String name, IBehaviorFactory behaviorFunction, List<Class<?>> predicateClasses){
            this.name = name;
            this.behaviorFunction = behaviorFunction;
            this.predicateClasses = predicateClasses;
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
        public IBehaviorFactory getBehaviorFactory() {
            return this.behaviorFunction;
        }

        @Override
        public Map<MemoryModuleType<?>, MemoryStatus> requireMemoryStatus() {
            if(this.behaviorCache == null){
                this.behaviorCache = this.getBehaviorFactory().create(ItemStack.EMPTY);
            }
            return this.behaviorCache.entryCondition;
        }

        @Override
        public List<Class<?>> getPredicateClasses() {
            return this.predicateClasses;
        }
    }

}
