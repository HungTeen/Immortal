package hungteen.immortal.common.entity;

import com.google.common.collect.ImmutableSet;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 10:54
 **/
public class ImmortalPoiTypes {

    private static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Util.id());

    public static final RegistryObject<PoiType> ELIXIR_ROOM = POI_TYPES.register("elixir_room", () -> new PoiType(
            getBlockStates(ImmortalBlocks.COPPER_ELIXIR_ROOM.get()), 1, 1
    ));

    public static void register(IEventBus event){
        POI_TYPES.register(event);
    }

    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}

