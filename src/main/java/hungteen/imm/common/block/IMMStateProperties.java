package hungteen.imm.common.block;

import hungteen.htlib.util.helper.JavaHelper;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-30 20:21
 **/
public class IMMStateProperties {

    public static final IntegerProperty TELEPORT_ANCHOR_CHARGES = IntegerProperty.create("charges", 0, 4);
    public static final IntegerProperty REACH_DISTANCE = IntegerProperty.create("reach_distance", 0, 15);
    public static final DirectionProperty ORIGIN_FACING = DirectionProperty.create("origin_facing", JavaHelper::alwaysTrue);
    public static final DirectionProperty TARGET_FACING = DirectionProperty.create("target_facing", JavaHelper::alwaysTrue);

}
