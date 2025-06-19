package hungteen.imm.common.cultivation.spell.talisman;

import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SummonTalismanSpell;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.talisman.RangeEffectTalismanEntity;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-28 20:38
 **/
public class WoodHealingSpell extends SummonTalismanSpell {

    public WoodHealingSpell() {
        super("wood_healing", property(InscriptionTypes.JOSS_PAPER).maxLevel(1).qi(40).cd(400));
    }

    @Override
    public ItemStack getTalismanItem(int level) {
        return new ItemStack(IMMItems.WOOD_HEALING_TALISMAN.get());
    }

    @Override
    public EntityType<? extends RangeEffectTalismanEntity> getEntityType() {
        return IMMEntities.WOOD_HEALING_TALISMAN.getEntityType();
    }

}
