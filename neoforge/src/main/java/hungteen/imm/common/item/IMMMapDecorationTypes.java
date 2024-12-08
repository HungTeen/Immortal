package hungteen.imm.common.item;

import hungteen.htlib.api.registry.PTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.neoforged.bus.api.IEventBus;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/7 16:15
 **/
public interface IMMMapDecorationTypes {

    HTVanillaRegistry<MapDecorationType> TYPES = HTRegistryManager.vanilla(Registries.MAP_DECORATION_TYPE, Util.id());

    PTHolder<MapDecorationType> TELEPORT_RUIN = register("teleport_ruin");

    static PTHolder<MapDecorationType> register(String name){
        return TYPES.registerForHolder(name, () -> new MapDecorationType(Util.prefix(name), true, -1, true, true));
    }

    static void initialize(IEventBus modBus){
        NeoHelper.initRegistry(TYPES, modBus);
    }
}
