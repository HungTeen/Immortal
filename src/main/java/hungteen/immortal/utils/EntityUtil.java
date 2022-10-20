package hungteen.immortal.utils;

import net.minecraft.world.entity.Entity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:43
 **/
public class EntityUtil {

    public static boolean isEntityValid(Entity entity) {
        return entity != null && entity.isAlive();
    }
}
