package hungteen.immortal.common.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.common.ai.ImmortalMemories;
import hungteen.immortal.utils.BehaviorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-24 22:27
 **/
public class BreakBoat extends Behavior<LivingEntity> {

    public BreakBoat() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, ImmortalMemories.NEAREST_BOAT.get(), MemoryStatus.VALUE_PRESENT), 10);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, LivingEntity entity, long time) {
        return getEntity(entity).isPresent() ;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity entity) {
        return true;
    }

    @Override
    protected void start(ServerLevel level, LivingEntity entity, long time) {
        BehaviorUtil.lookAtEntity(entity, getEntity(entity).get());
    }

    @Override
    protected void tick(ServerLevel level, LivingEntity entity, long time) {
    }

    @Override
    protected void stop(ServerLevel level, LivingEntity entity, long time) {
        entity.swing(InteractionHand.MAIN_HAND);
        getEntity(entity).ifPresent(target -> {
            if(target instanceof Boat){
                if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    for(int i = 0; i < 3; ++i) {
                        target.spawnAtLocation(((Boat) target).getBoatType().getPlanks());
                    }

                    for(int j = 0; j < 2; ++j) {
                        target.spawnAtLocation(Items.STICK);
                    }
                }
                target.hurt(EntityDamageSource.mobAttack(entity), 1000F);
            }
        });
        entity.getBrain().setMemory(ImmortalMemories.NEAREST_BOAT.get(), Optional.empty());
    }

    private Optional<Entity> getEntity(LivingEntity entity){
        return entity.getBrain().getMemory(ImmortalMemories.NEAREST_BOAT.get());
    }

}
