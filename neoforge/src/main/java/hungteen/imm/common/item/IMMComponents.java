package hungteen.imm.common.item;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.util.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 14:57
 **/
public interface IMMComponents {

    HTVanillaRegistry<DataComponentType<?>> TYPES = HTRegistryManager.vanilla(Registries.DATA_COMPONENT_TYPE, Util.id());

    HTHolder<DataComponentType<ArtifactRank>> ARTIFACT_RANK = TYPES.register("artifact_rank", () -> DataComponentType.<ArtifactRank>builder()
            .persistent(ArtifactRank.CODEC)
            .networkSynchronized(ArtifactRank.STREAM_CODEC)
            .build()
    );

    static void initialize(IEventBus modBus){
        NeoHelper.initRegistry(TYPES, modBus);
    }
}
