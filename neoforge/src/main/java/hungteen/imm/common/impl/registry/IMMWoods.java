package hungteen.imm.common.impl.registry;

import hungteen.htlib.common.registry.suit.HTWoodSet;
import hungteen.imm.IMMInitializer;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/22 15:34
 */
public class IMMWoods {

//    public static final WoodIntegrations.WoodIntegration MULBERRY = new WoodIntegrations.Builder(Util.prefix("mulberry"))
//            .updateWoodItem(WoodIntegrations.WoodSuits.WALL_SIGN, p -> new WallSignBlock(p, WoodType.ACACIA)).build();

    /**
     * {@link IMMInitializer#coreRegister()}
     */
    public static void register(){
//        woods().forEach(TreeSuits::registerWoodIntegration);
    }

    public static List<HTWoodSet> woods(){
        return List.of();
//        return Arrays.asList(MULBERRY);
    }

}
