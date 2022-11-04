package hungteen.immortal.utils;

import hungteen.htlib.util.ColorUtil;
import hungteen.htlib.util.Triple;
import hungteen.immortal.api.interfaces.IHasRoot;
import hungteen.immortal.api.registry.ISpiritualRoot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:43
 **/
public class EntityUtil {

    public static Triple<Float, Float, Float> getRGBForSpiritual(Entity entity){
        Collection<ISpiritualRoot> roots = new ArrayList<>();
        if(entity instanceof Player){
            roots = PlayerUtil.getSpiritualRoots((Player) entity);
        } else if(entity instanceof IHasRoot){
            roots = ((IHasRoot) entity).getSpiritualRoots();
        }
        int r = 0;
        int g = 0;
        int b = 0;
        for (ISpiritualRoot root : roots) {
            Triple<Integer, Integer, Integer> triple = ColorUtil.getRGB(root.getSpiritualColor());
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
