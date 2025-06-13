package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.world.entity.IMMDummyEntities;
import hungteen.imm.util.TipUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-01 17:09
 **/
public class MortalityTrial extends BreakThroughTrial {

    private static final MutableComponent TITLE = TipUtil.info("trial.mortality.title");

    public MortalityTrial(ServerPlayer trialPlayer, RealmType realmType, float difficulty) {
        super(IMMDummyEntities.MORTALITY_TRIAL, trialPlayer, realmType, difficulty);
    }

    public MortalityTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag trialTag) {
        super(dummyEntityType, level, trialTag);
    }

    @Override
    public void tickTrial(ServerPlayer player) {

    }

    @Override
    public ServerBossEvent createProgressBar() {
        return new ServerBossEvent(TITLE, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
    }

    @Override
    public int getTrialLength() {
        return 0;
    }


}
