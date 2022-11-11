package hungteen.immortal.common.block;

import hungteen.htlib.util.helper.BlockHelper;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.utils.Util;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Arrays;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-11 20:23
 **/
public class ImmortalWoodTypes {

    public static final WoodType MULBERRY = WoodType.create(Util.prefixName("mulberry"));

    /**
     * {@link ImmortalMod#coreRegister()}
     */
    public static void registerWoodTypes(){
        Arrays.asList(MULBERRY).forEach(BlockHelper::registerWoodType);
    }
}
