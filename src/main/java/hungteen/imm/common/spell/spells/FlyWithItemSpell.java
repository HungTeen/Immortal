package hungteen.imm.common.spell.spells;

import hungteen.imm.api.EntityBlockResult;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.common.spell.SpellTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 20:14
 */
public class FlyWithItemSpell extends SpellTypes.SpellType {

    public FlyWithItemSpell(SpellTypes.SpellProperties properties) {
        super("fly_with_item", properties);
    }

    @Override
    public boolean onActivate(LivingEntity owner, EntityBlockResult result, int level) {
        if(result.getEntity() instanceof ItemEntity itemEntity){
            FlyingItemEntity flyingItem = IMMEntities.FLYING_ITEM.get().create(owner.level());
            if(flyingItem != null){
                flyingItem.setDeltaMovement(itemEntity.getDeltaMovement());
                flyingItem.setPos(itemEntity.position());
                flyingItem.setItemStack(itemEntity.getItem());
                itemEntity.discard();
                owner.level().addFreshEntity(flyingItem);
                return true;
            }
        }
        return false;
    }

}
