package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.api.interfaces.raid.IRaid;
import hungteen.htlib.api.interfaces.raid.IRaidType;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.impl.raid.RaidComponent;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.IRealmType;
import org.jetbrains.annotations.NotNull;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:41
 **/
public class BreakThroughRaid extends RaidComponent {

    public BreakThroughRaid(RaidSetting raidSettings) {
        super(raidSettings);
    }

    public boolean match(IRealmType realm, RealmStages stage){
        return true;
    }

    @Override
    public int getWaveCount(IRaid iRaid) {
        return 0;
    }

    @Override
    public @NotNull IWaveComponent getCurrentWave(IRaid iRaid, int i) {
        return null;
    }

    @Override
    public IRaidType<?> getType() {
        return null;
    }
}
