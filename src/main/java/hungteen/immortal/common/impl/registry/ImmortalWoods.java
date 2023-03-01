package hungteen.immortal.common.impl.registry;

import hungteen.htlib.common.WoodIntegrations;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.utils.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Arrays;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/22 15:34
 */
public class ImmortalWoods {

//    public static final WoodIntegrations.WoodIntegration MULBERRY = new WoodIntegrations.Builder(Util.prefix("mulberry"))
//            .updateWoodItem(WoodIntegrations.WoodSuits.WALL_SIGN, p -> new WallSignBlock(p, WoodType.ACACIA)).build();

    /**
     * {@link ImmortalMod#coreRegister()}
     */
    public static void register(){
        woods().forEach(WoodIntegrations::registerWoodIntegration);
    }

    public static List<WoodIntegrations.WoodIntegration> woods(){
        return List.of();
//        return Arrays.asList(MULBERRY);
    }
}
