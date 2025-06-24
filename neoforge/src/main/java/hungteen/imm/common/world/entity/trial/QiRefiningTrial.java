package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.entity.IMMEntities;
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
public class QiRefiningTrial extends SurvivalTrial {

    public QiRefiningTrial(ServerPlayer trialPlayer, RealmType realmType, float difficulty) {
        super(IMMDummyEntities.QI_REFINING_TRIAL, trialPlayer, realmType, difficulty);
    }

    public QiRefiningTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag trialTag) {
        super(dummyEntityType, level, trialTag);
    }

    @Override
    public IntProvider getSpawnInterval() {
        return UniformInt.of(80, 160);
    }

    @Override
    public List<SpawnEntry> getSpawnEntries() {
        return List.of(
                new SpawnEntry(EntityType.ENDERMAN, 6, 6, 0F, 0),
                new SpawnEntry(EntityType.PHANTOM, 6, 6, 0.35F, 0),
                new SpawnEntry(IMMEntities.QI_ZOMBIE.getEntityType(), 8, 6, 0.25F, 0),
                new SpawnEntry(IMMEntities.QI_SKELETON.getEntityType(), 8, 6, 0.5F, 0),
                new SpawnEntry(EntityType.EVOKER, 4, 2, 0.75F, 0),
                new SpawnEntry(EntityType.ILLUSIONER, 4, 2, 0.75F, 0)
        );
    }

    @Override
    public double getWidth() {
        return 12;
    }

    @Override
    public int getTrialLength() {
        return 6000;
    }

}
