package hungteen.immortal.common;

import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.utils.BlockUtil;
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
                    ImmortalBlocks.MULBERRY_LEAVES, ImmortalBlocks.MULBERRY_LEAVES_WITH_MULBERRIES, ImmortalBlocks.MULBERRY_SAPLING
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS){
            Arrays.asList(
                    ImmortalItems.MULBERRY
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            Arrays.asList(
                    ImmortalItems.CONTINUOUS_MOUNTAIN_PATTERN, ImmortalItems.FLOWING_CLOUD_PATTERN,
                    ImmortalItems.FOLDED_THUNDER_PATTERN, ImmortalItems.RHOMBUS_PATTERN
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        }
    }

    /**
     * {@link hungteen.immortal.ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerCompostable() {
        BlockHelper.registerCompostable(0.3F, ImmortalBlocks.MULBERRY_LEAVES.get());
        BlockHelper.registerCompostable(0.3F, ImmortalBlocks.MULBERRY_SAPLING.get());
        BlockHelper.registerCompostable(0.65F, ImmortalItems.MULBERRY.get());
    }
    
}
