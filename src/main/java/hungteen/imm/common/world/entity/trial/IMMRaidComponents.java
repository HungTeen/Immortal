package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.common.impl.raid.HTRaidComponents;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMRaidComponents {

    public static void register(BootstapContext<IRaidComponent> context){

    }

    public static ResourceKey<IRaidComponent> create(String name){
        return HTRaidComponents.registry().createKey(Util.prefix(name));
    }
}
