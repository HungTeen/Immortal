package hungteen.imm.common;

import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.IMMItems;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 13:18
 **/
public class CommonRegister {

    /**
     * {@link IMMInitializer#setUp(FMLCommonSetupEvent)}
     */
    public static void registerCompostable() {
        BlockHelper.registerCompostable(0.3F, IMMBlocks.MULBERRY_LEAVES.get());
        BlockHelper.registerCompostable(0.3F, IMMBlocks.MULBERRY_SAPLING.get());
        BlockHelper.registerCompostable(0.65F, IMMItems.MULBERRY.get());
    }
    
}
