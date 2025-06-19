package hungteen.imm.api.spell;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 22:58
 **/
public interface TalismanSpell extends SpellType {

    /**
     * @param level 法术等级。
     * @return 符箓物品。
     */
    ItemStack getTalismanItem(int level);

    /**
     * @return 符箓的吟唱时间。
     */
    default int getUseDuration(ItemStack stack, LivingEntity owner){
        return 30;
    }

    /**
     * @return 施法的灵根要求。
     */
    default List<QiRootType> requireQiRoots(){
        return List.of();
    }

    /**
     * @return 施法的元素要求。
     */
    default List<Element> requireElements(){
        return List.of();
    }

}
