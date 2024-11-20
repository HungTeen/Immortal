package hungteen.imm.common.spell.spells.common;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.spell.spells.SpellType;
import hungteen.imm.util.EntityUtil;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 20:14
 */
public class FlyWithItemSpell extends SpellType {

    public FlyWithItemSpell() {
        super("fly_with_item", properties().maxLevel(3).mana(20).cd(1000));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        final ItemStack stack = EntityUtil.getItemInHand(owner, itemStack -> {
            if(level == 1) return itemStack.is(ItemTags.SWORDS);
            return true;
        });
        if(! stack.isEmpty()){
            final FlyingItemEntity flyingEntity = IMMEntities.FLYING_ITEM.get().create(owner.level());
            final ItemStack flyingItem = stack.copy();
            flyingItem.setCount(1);
            if(flyingEntity != null){
                flyingEntity.setPos(owner.position().add(owner.getLookAngle().normalize().scale(1)));
                flyingEntity.setItemStack(flyingItem);
                flyingEntity.setThrower(owner);
                owner.startRiding(flyingEntity);
                owner.level().addFreshEntity(flyingEntity);
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    public static float getFlyingCost(Entity entity){
        return EntityUtil.hasLearnedSpell(entity, SpellTypes.FLY_WITH_ITEM, 2) ? 1F : 2F;
    }

}
