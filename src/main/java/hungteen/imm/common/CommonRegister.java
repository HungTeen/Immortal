package hungteen.imm.common;

import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.BlockUtil;
import net.minecraft.world.item.*;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Arrays;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 13:18
 **/
public class CommonRegister {

    public static void fillCreativeTabs(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.BUILDING_BLOCKS){

        } else if(event.getTab() == CreativeModeTabs.COLORED_BLOCKS){
            BlockUtil.getWoolCushions().forEach(pair -> {
                event.accept(new ItemStack(pair.getSecond()));
            });
        } else if(event.getTab() == CreativeModeTabs.NATURAL_BLOCKS){
            BlockUtil.getGourds().forEach(pair -> {
                event.accept(new ItemStack(pair.getSecond()));
            });
            Arrays.asList(
                    IMMBlocks.MULBERRY_LEAVES, IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES, IMMBlocks.MULBERRY_SAPLING
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS){
            Arrays.asList(
                    IMMItems.MULBERRY
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            Arrays.asList(
                    IMMItems.CONTINUOUS_MOUNTAIN_PATTERN, IMMItems.FLOWING_CLOUD_PATTERN,
                    IMMItems.FOLDED_THUNDER_PATTERN, IMMItems.RHOMBUS_PATTERN
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        }
    }

    /**
     * {@link hungteen.imm.ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerCompostable() {
        BlockHelper.registerCompostable(0.3F, IMMBlocks.MULBERRY_LEAVES.get());
        BlockHelper.registerCompostable(0.3F, IMMBlocks.MULBERRY_SAPLING.get());
        BlockHelper.registerCompostable(0.65F, IMMItems.MULBERRY.get());
    }
    
}
