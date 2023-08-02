package hungteen.imm.common.world.entity.trial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.api.interfaces.raid.IRaid;
import hungteen.htlib.api.interfaces.raid.IRaidType;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.impl.raid.RaidComponent;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.raid.IMMRaidHandler;
import hungteen.imm.common.impl.registry.RealmTypes;
import net.minecraft.core.Holder;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:41
 **/
public class BreakThroughRaid extends RaidComponent implements WeightedEntry {

    public static final Codec<BreakThroughRaid> CODEC = RecordCodecBuilder.<BreakThroughRaid>mapCodec(instance -> instance.group(
            RaidSetting.CODEC.fieldOf("setting").forGetter(BreakThroughRaid::getRaidSettings),
            HTWaveComponents.getCodec().listOf().fieldOf("waves").forGetter(BreakThroughRaid::getWaveComponents),
            RealmTypes.registry().byNameCodec().fieldOf("target_realm").forGetter(BreakThroughRaid::getTargetRealm),
            RealmStages.CODEC.fieldOf("target_stage").forGetter(BreakThroughRaid::getTargetStage),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 100).forGetter(BreakThroughRaid::weight),
            Codec.floatRange(0, 100).optionalFieldOf("min_difficulty", 0F).forGetter(BreakThroughRaid::getMinDifficulty),
            Codec.floatRange(0, 100).optionalFieldOf("max_difficulty", 100F).forGetter(BreakThroughRaid::getMaxDifficulty)
    ).apply(instance, BreakThroughRaid::new)).codec();

    private final List<Holder<IWaveComponent>> waveComponents;
    private final IRealmType targetRealm;
    private final RealmStages targetStage;
    private final int weight;
    private final float minDifficulty;
    private final float maxDifficulty;


    public BreakThroughRaid(RaidSetting raidSettings, List<Holder<IWaveComponent>> waveComponents, IRealmType targetRealm, RealmStages targetStage, int weight, float minDifficulty, float maxDifficulty) {
        super(raidSettings);
        this.waveComponents = waveComponents;
        this.targetRealm = targetRealm;
        this.targetStage = targetStage;
        this.weight = weight;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = maxDifficulty;
    }

    public boolean match(IRealmType realm, RealmStages stage, float difficulty) {
        return realm.equals(getTargetRealm()) && stage.equals(getTargetStage()) && difficulty >= getMinDifficulty() && difficulty <= getMaxDifficulty();
    }

    public int getWaveCount(IRaid raid) {
        return this.waveComponents.size();
    }

    public @NotNull IWaveComponent getCurrentWave(IRaid raid, int currentWave) {
        return this.waveComponents.get(currentWave).get();
    }

    public List<Holder<IWaveComponent>> getWaveComponents() {
        return this.waveComponents;
    }

    public IRealmType getTargetRealm() {
        return targetRealm;
    }

    public RealmStages getTargetStage() {
        return targetStage;
    }

    @Override
    public IRaidType<?> getType() {
        return IMMRaidHandler.BREAK_THROUGH_TRIAL;
    }

    public int weight() {
        return weight;
    }

    public float getMinDifficulty() {
        return minDifficulty;
    }

    public float getMaxDifficulty() {
        return maxDifficulty;
    }

    @Override
    public Weight getWeight() {
        return Weight.of(weight());
    }
}
