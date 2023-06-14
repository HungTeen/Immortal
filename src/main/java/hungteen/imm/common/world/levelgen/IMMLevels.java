package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-31 18:30
 **/
public class IMMLevels {

    public static final ResourceKey<Level> EAST_WORLD = create("east_world");

    private static ResourceKey<Level> create(String name) {
        return LevelHelper.get().createKey(Util.prefix(name));
    }

}
