package hungteen.immortal.utils;

import hungteen.htlib.util.Triple;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.immortal.api.interfaces.IHasRoot;
import hungteen.immortal.api.registry.ISpiritualType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:43
 **/
public class EntityUtil {

    public static boolean isMainHolding(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getMainHandItem());
    }

    public static boolean isOffHolding(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getOffhandItem());
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Entity entity){
        Collection<ISpiritualType> roots = new ArrayList<>();
        if(entity instanceof Player){
            roots = PlayerUtil.getSpiritualRoots((Player) entity);
        } else if(entity instanceof IHasRoot){
            roots = ((IHasRoot) entity).getSpiritualTypes();
        }
        return getRGBForSpiritual(roots);
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Collection<ISpiritualType> roots){
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
