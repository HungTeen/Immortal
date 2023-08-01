package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMWaveComponents {

    public static void register(BootstapContext<IWaveComponent> context){

    }

    public static ResourceKey<IWaveComponent> create(String name){
        return HTWaveComponents.registry().createKey(Util.prefix(name));
    }
}
