package hungteen.immortal.common.impl;

import hungteen.immortal.common.RuneManager;
import hungteen.immortal.common.entity.ai.behavior.NearestTargetGoal;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.common.entity.golem.GolemEntity;
import hungteen.immortal.common.entity.golem.IronGolem;
import hungteen.immortal.utils.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.behavior.*;
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

    private static final List<RuneManager.IBehaviorRune> TYPES = new ArrayList<>();

    public static final RuneManager.IBehaviorRune MELEE_ATTACK = new BehaviorRune("melee_attack",
            (golem) -> new MeleeAttack(30)
    );

    public static final RuneManager.IBehaviorRune FIND_TARGET = new BehaviorRune("find_target",
            (golem) -> new NearestTargetGoal()
    );

    public static final RuneManager.IBehaviorRune MOVE_TO_TARGET = new BehaviorRune("move_to_target",
            (golem) -> new MoveToTargetSink()
    );

    public static final RuneManager.IBehaviorRune STOP_ATTACK_IF_TARGET_INVALID = new BehaviorRune("stop_attacking_if_target_invalid",
            (golem) -> new StopAttackingIfTargetInvalid<>()
    );

    public static final RuneManager.IBehaviorRune SET_WALK_TARGET_FROM_ATTACK_TARGET = new BehaviorRune("set_walk_target_from_attack_target",
            (golem) -> new SetWalkTargetFromAttackTargetIfTargetOutOfReach((livingEntity -> 0.5F))
    );

    public static class BehaviorRune implements RuneManager.IBehaviorRune {

        private final String name;
        private final Function<GolemEntity, Behavior<? super GolemEntity>> behaviorFunction;
        private Behavior<? super GolemEntity> behaviorCache;

        public static void register(){
            TYPES.forEach(RuneManager::registerBehaviorRune);
        }

        public BehaviorRune(String name, Function<GolemEntity, Behavior<? super GolemEntity>> behaviorFunction){
            this.name = name;
            this.behaviorFunction = behaviorFunction;
            TYPES.add(this);
        }

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("rune." + getModID() + ".behavior." + this.getName()).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD);
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
    }

}
