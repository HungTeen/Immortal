package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.api.interfaces.raid.IResultComponent;
import hungteen.htlib.common.impl.result.HTResultComponents;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMResultComponents {

    public static void register(BootstapContext<IResultComponent> context){

    }

    public static ResourceKey<IResultComponent> create(String name){
        return HTResultComponents.registry().createKey(Util.prefix(name));
    }
}
