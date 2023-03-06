package hungteen.immortal.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.immortal.api.EntityBlockResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * 法术。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 12:30
 **/
public interface ISpellType extends ISimpleEntry {

    /**
     * 法术被触发。
     * @param owner who activated this spell.
     * @param result target.
     * @param level spell level.
     * @return activate successfully.
     */
    boolean onActivate(LivingEntity owner, EntityBlockResult result, int level);

    /**
     * 此法术有多少层。
     * @return How many levels does the spell have.
     */
    int getMaxLevel();

    /**
     * 施放此法术需要消耗多少灵力。
     * @return How many spiritual mana will cost, only consider the start stage.
     */
    int getConsumeMana();

    /**
     * 此法术的冷却时间。
     * @return Each releasing cool down.
     */
    int getCooldown();

    /**
     * 是否是被动法术，即放置在常驻法术轮盘上的法术。
     * @return Whether it is a spell that triggers passively.
     */
    boolean isPassiveSpell();

    /**
     * 学习此法术需要什么灵根。
     * @return How many types of roots required to learn this spell.
     */
    List<ISpiritualType> requireSpiritualRoots();

    /**
     * 获取法术贴图位置、
     * @return Get the texture location for this spell.
     */
    ResourceLocation getSpellTexture();

}
