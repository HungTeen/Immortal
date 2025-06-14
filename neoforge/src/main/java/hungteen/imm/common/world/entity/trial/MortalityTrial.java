package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.world.entity.IMMDummyEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-01 17:09
 **/
public class MortalityTrial extends SurvivalTrial {

    public MortalityTrial(ServerPlayer trialPlayer, RealmType realmType, float difficulty) {
        super(IMMDummyEntities.MORTALITY_TRIAL, trialPlayer, realmType, difficulty);
    }

    public MortalityTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag trialTag) {
        super(dummyEntityType, level, trialTag);
    }

    @Override
    public IntProvider getSpawnInterval() {
        return UniformInt.of(80, 160);
    }

    @Override
    public List<SpawnEntry> getSpawnEntries() {
        return List.of(
                new SpawnEntry(EntityType.WITHER_SKELETON, 10, 6, 0F, 0),
                new SpawnEntry(EntityType.ENDERMITE, 6, 5, 0.2F, 0),
                new SpawnEntry(EntityType.PHANTOM, 6, 6, 0.4F, 0),
                new SpawnEntry(EntityType.ENDERMAN, 6, 4, 0.6F, 0),
                new SpawnEntry(EntityType.VEX, 5, 2, 0.75F, 0)
        );
    }

    @Override
    public double getWidth() {
        return 10;
    }

    @Override
    public int getTrialLength() {
        return 60;
    }

}
