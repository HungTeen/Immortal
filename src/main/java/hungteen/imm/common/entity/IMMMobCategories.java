package hungteen.imm.common.entity;

import hungteen.imm.util.Util;
import net.minecraft.world.entity.MobCategory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-23 22:57
 **/
public class IMMMobCategories {

    public static final MobCategory HUMAN = MobCategory.create(
            "human", Util.id(), 20, true, false, 128
    );

    public static final MobCategory GOLEM = MobCategory.create(
            "golem", Util.id(), 10, true, false, 128
    );

}
