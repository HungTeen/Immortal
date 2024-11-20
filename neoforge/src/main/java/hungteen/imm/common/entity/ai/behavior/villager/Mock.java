package hungteen.imm.common.entity.ai.behavior.villager;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.human.VillagerLikeEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 22:52
 **/
public class Mock extends Behavior<VillagerLikeEntity> {

    private final float chance;

    public Mock() {
        this(0.001F);
    }

    public Mock(float chance) {
        super(ImmutableMap.of(), 100);
        this.chance = chance;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, VillagerLikeEntity villager, long time) {
        return true;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, VillagerLikeEntity villager) {
        return villager.getRandom().nextDouble() < this.chance;
    }

    @Override
    protected void start(ServerLevel level, VillagerLikeEntity villager, long time) {
        // villager roaring means mock.
        villager.setPose(Pose.ROARING);
    }

    @Override
    protected void tick(ServerLevel level, VillagerLikeEntity villager, long time) {
    }

    @Override
    protected void stop(ServerLevel level, VillagerLikeEntity villager, long time) {
        villager.setPose(Pose.STANDING);
    }
}
