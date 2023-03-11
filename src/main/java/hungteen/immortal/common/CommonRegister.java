package hungteen.immortal.common;

import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.item.ImmortalItems;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 13:18
 **/
public class CommonRegister {

    /**
     * {@link hungteen.immortal.ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerCompostable() {
        BlockHelper.registerCompostable(0.3F, ImmortalBlocks.MULBERRY_LEAVES.get());
        BlockHelper.registerCompostable(0.3F, ImmortalBlocks.MULBERRY_SAPLING.get());
        BlockHelper.registerCompostable(0.65F, ImmortalItems.MULBERRY.get());
    }
    
}
