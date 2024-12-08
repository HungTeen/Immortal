package hungteen.imm.common.entity;

import hungteen.imm.util.Util;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-23 22:57
 **/
public class IMMMobCategory {

    public static final EnumProxy<MobCategory> HUMAN = new EnumProxy<>(MobCategory.class, Util.prefixName("human"), 10, false, true, 128);

//    public static final MobCategory GOLEM = MobCategory.create(
//            "GOLEM", Util.prefixName("golem"), 10, true, false, 128
//    );

}
