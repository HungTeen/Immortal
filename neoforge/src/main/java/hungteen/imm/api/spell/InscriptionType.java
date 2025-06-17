package hungteen.imm.api.spell;

import hungteen.htlib.api.registry.SimpleEntry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 法术能够绘制在哪些物品上。
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 9:58
 **/
public interface InscriptionType extends SimpleEntry {

    /**
     * 检查是否可以将符咒绘制在物品上。
     * @param living 施法者。
     * @param item   物品。
     * @return 返回 true 则表示可以绘制。
     */
    boolean compatWith(LivingEntity living, ItemStack item);

}
