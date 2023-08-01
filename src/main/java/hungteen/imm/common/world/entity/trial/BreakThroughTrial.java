package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.common.world.raid.AbstractRaid;
import hungteen.imm.common.world.entity.IMMDummyEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:09
 **/
public class BreakThroughTrial extends AbstractRaid {

    private ServerPlayer trialPlayer;

    public BreakThroughTrial(ServerLevel serverLevel, ServerPlayer trialPlayer, IRaidComponent raidComponent) {
        super(IMMDummyEntities.BREAK_THROUGH_TRIAL, serverLevel, trialPlayer.position(), raidComponent);
        this.trialPlayer = trialPlayer;
    }

    public BreakThroughTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag raidTag) {
        super(dummyEntityType, level, raidTag);
        if(level instanceof ServerLevel serverLevel){
            serverLevel.getPlayerByUUID(raidTag.getUUID("TrialPlayer"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        if(this.trialPlayer != null){
            tag.putUUID("TrialPlayer", this.trialPlayer.getUUID());
        }
        return super.save(tag);
    }



}
