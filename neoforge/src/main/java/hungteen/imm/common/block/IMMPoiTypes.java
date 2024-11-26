package hungteen.imm.common.block;

import com.google.common.collect.ImmutableSet;
import hungteen.htlib.api.registry.PTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;

import java.util.Set;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/25 10:06
 **/
public interface IMMPoiTypes {

    HTVanillaRegistry<PoiType> POI_TYPES = HTRegistryManager.vanilla(Registries.POINT_OF_INTEREST_TYPE, Util.id());

    PTHolder<PoiType> SPIRIT_ANCHOR = POI_TYPES.registerForHolder("spirit_anchor", () -> new PoiType(
            getBlockStates(IMMBlocks.SPIRIT_ANCHOR.get()), 1, 1
    ));

    //    public static final RegistryObject<PoiType> ELIXIR_ROOM = POI_TYPES.initialize("elixir_room", () -> new PoiType(
//            getBlockStates(ImmortalBlocks.COPPER_ELIXIR_ROOM.get()), 1, 1
//    ));

    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    static void initialize(IEventBus modBus){
        NeoHelper.initRegistry(POI_TYPES, modBus);
    }

}
