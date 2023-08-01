package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.api.interfaces.raid.ISpawnComponent;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMSpawnComponents {

    public static void register(BootstapContext<ISpawnComponent> context){

    }

    public static ResourceKey<ISpawnComponent> create(String name){
        return HTSpawnComponents.registry().createKey(Util.prefix(name));
    }
}
