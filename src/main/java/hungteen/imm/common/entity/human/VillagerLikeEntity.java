package hungteen.imm.common.entity.human;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-16 22:49
 **/
public abstract class VillagerLikeEntity extends HumanEntity {

    public static final int MOCK_ANIMATION_FLAG = 0;
    public AnimationState mockAnimationState = new AnimationState();

    public VillagerLikeEntity(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if(dataAccessor.equals(DATA_POSE)){
            if(this.getPose() == Pose.ROARING){
                this.playHurtSound(null);
                this.mockAnimationState.start(this.tickCount);
            } else {
                this.mockAnimationState.stop();
            }
        }
    }
}
