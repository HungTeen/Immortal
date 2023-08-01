package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.api.interfaces.raid.IPositionComponent;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMPositionComponents {

    public static void register(BootstapContext<IPositionComponent> context){

    }

    public static ResourceKey<IPositionComponent> create(String name){
        return HTPositionComponents.registry().createKey(Util.prefix(name));
    }
}
