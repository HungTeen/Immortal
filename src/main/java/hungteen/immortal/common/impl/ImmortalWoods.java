package hungteen.immortal.common.impl;

import hungteen.htlib.common.WoodIntegrations;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.utils.Util;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/22 15:34
 */
public class ImmortalWoods {

    public static final WoodIntegrations.WoodIntegration MULBERRY = new WoodIntegrations.Builder(Util.prefix("mulberry")).build();

    /**
     * {@link ImmortalMod#coreRegister()}
     */
    public static void register(){
        woods().forEach(WoodIntegrations::registerWoodIntegration);
    }

    public static List<WoodIntegrations.WoodIntegration> woods(){
        return Arrays.asList(MULBERRY);
    }
}
