package hungteen.imm.compat.jade;

import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 15:31
 */
public class IMMBlockProvider implements IBlockComponentProvider {

    public static final IMMBlockProvider INSTANCE = new IMMBlockProvider();
    private static final ResourceLocation UID = Util.prefix("block");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
//        if(blockAccessor.getBlockState().is(IMMBlocks.GOURD_SCAFFOLD.get())){
//            iTooltip.add(Component.literal(blockAccessor.getBlockState().getValue(ScaffoldingBlock.DISTANCE) + ""));
//            iTooltip.add(Component.literal(blockAccessor.getBlockState().getValue(GourdScaffoldBlock.REACH_DISTANCE) + ""));
//        }
//        if(blockAccessor.getBlockState().is(Blocks.SCAFFOLDING)){
//            iTooltip.add(Component.literal(blockAccessor.getBlockState().getValue(ScaffoldingBlock.DISTANCE) + ""));
//        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

}
