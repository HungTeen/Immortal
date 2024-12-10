package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.world.entity.IMMDummyEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-01 17:09
 **/
public class MortalityTrial extends BreakThroughTrial {

    public MortalityTrial(ServerPlayer trialPlayer, RealmType realmType, float difficulty) {
        super(IMMDummyEntities.MORTALITY_TRIAL, trialPlayer, realmType, difficulty, 2000);
    }

    public MortalityTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag trialTag) {
        super(dummyEntityType, level, trialTag);
    }

    @Override
    public void tickTrial(ServerPlayer player) {

    }


}
