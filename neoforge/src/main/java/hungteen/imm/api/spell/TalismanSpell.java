package hungteen.imm.api.spell;

import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 22:58
 **/
public interface TalismanSpell {

    /**
     * @param level 法术等级。
     * @return 符箓物品。
     */
    ItemStack getTalismanItem(int level);

}
