package hungteen.imm.common.impl.raid;

import hungteen.htlib.api.interfaces.raid.IPositionComponent;
import hungteen.htlib.common.impl.position.CenterAreaPosition;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMPositionComponents {

    public static ResourceKey<IPositionComponent> AROUND_20_BLOCKS = create("around_20_blocks");
    public static ResourceKey<IPositionComponent> AROUND_15_BLOCKS = create("around_15_blocks");

    public static void register(BootstapContext<IPositionComponent> context){
        context.register(AROUND_20_BLOCKS, new CenterAreaPosition(
                Vec3.ZERO,
                20,
                24,
                true,
                0,
                true
        ));
        context.register(AROUND_15_BLOCKS, new CenterAreaPosition(
                Vec3.ZERO,
                15,
                24,
                true,
                0,
                false
        ));
    }

    public static ResourceKey<IPositionComponent> create(String name){
        return HTPositionComponents.registry().createKey(Util.prefix(name));
    }
}
