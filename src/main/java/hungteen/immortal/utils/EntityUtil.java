package hungteen.immortal.utils;

import hungteen.htlib.util.Triple;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.immortal.api.interfaces.IHasRoot;
import hungteen.immortal.api.registry.ISpiritualType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:43
 **/
public class EntityUtil {

    public static Triple<Float, Float, Float> getRGBForSpiritual(Entity entity){
        Collection<ISpiritualType> roots = new ArrayList<>();
        if(entity instanceof Player){
            roots = PlayerUtil.getSpiritualRoots((Player) entity);
        } else if(entity instanceof IHasRoot){
            roots = ((IHasRoot) entity).getSpiritualTypes();
        }
        int r = 0;
        int g = 0;
        int b = 0;
        for (ISpiritualType root : roots) {
            Triple<Integer, Integer, Integer> triple = ColorHelper.getRGB(root.getSpiritualColor());
            r += triple.getLeft();
            g += triple.getMid();
            b += triple.getRight();
        }
        final float multiply = 1F / 255 / roots.size();
        return Triple.of(r / multiply, g / multiply, b / multiply);
    }

    public static boolean isEntityValid(Entity entity) {
        return entity != null && entity.isAlive();
    }
}
