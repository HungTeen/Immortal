package hungteen.imm.common.entity.ai;

import com.google.common.collect.ImmutableSet;
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
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-17 10:54
 **/
public class IMMPoiTypes {

    private static final HTVanillaRegistry<PoiType> POI_TYPES = HTRegistryManager.vanilla(Registries.POINT_OF_INTEREST_TYPE, Util.id());

//    public static final RegistryObject<PoiType> ELIXIR_ROOM = POI_TYPES.initialize("elixir_room", () -> new PoiType(
//            getBlockStates(ImmortalBlocks.COPPER_ELIXIR_ROOM.get()), 1, 1
//    ));

    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(POI_TYPES, event);
    }

    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}

