package hungteen.imm.compat.jade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.ScaffoldingBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 15:29
 */
@WailaPlugin
public class IMMJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(IMMEntityProvider.INSTANCE, Entity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        // initialize component providers, icon providers, callbacks, and config options here
        registration.registerEntityComponent(IMMEntityProvider.INSTANCE, Entity.class);
        registration.registerBlockComponent(IMMBlockProvider.INSTANCE, ScaffoldingBlock.class);
    }

}
