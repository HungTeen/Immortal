package hungteen.imm.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.api.HTHitResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

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
    boolean checkActivate(LivingEntity owner, HTHitResult result, int level);

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
     * 法术是否能被触发，能则会被放置在法术轮盘上。
     * @return Whether can the spell be triggered.
     */
    boolean canTrigger();

    /**
     * 获取法术贴图位置、
     * @return Get the texture location for this spell.
     */
    ResourceLocation getSpellTexture();

}
