package hungteen.imm.common.cultivation.spell.talisman;

import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SummonTalismanSpell;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.talisman.RangeEffectTalismanEntity;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 14:25
 */
public class SproutSpell extends SummonTalismanSpell {

    public SproutSpell() {
        super("sprout", property(InscriptionTypes.JOSS_PAPER).maxLevel(1).qi(60).cd(400));
    }

    @Override
    public EntityType<? extends RangeEffectTalismanEntity> getEntityType() {
        return IMMEntities.SPROUT_TALISMAN.getEntityType();
    }

    @Override
    public ItemStack getTalismanItem(int level) {
        return new ItemStack(IMMItems.SPROUT_TALISMAN.get());
    }
}
