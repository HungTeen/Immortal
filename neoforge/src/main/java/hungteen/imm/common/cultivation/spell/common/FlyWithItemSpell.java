package hungteen.imm.common.cultivation.spell.common;

import hungteen.imm.api.spell.InscriptionType;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/6 20:14
 */
public class FlyWithItemSpell extends SpellTypeImpl {

    public FlyWithItemSpell() {
        super("fly_with_item", property().maxLevel(2).mana(20).cd(1000));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        if(context.itemTrigger()){
            spawnFlyingItem(context.owner(), context.usingItem());
            return true;
        }
        final Optional<InteractionHand> handOpt = EntityUtil.getHandOpt(context.owner(), itemStack -> true);
        if(handOpt.isPresent()){
            final ItemStack stack = context.owner().getItemInHand(handOpt.get());
            spawnFlyingItem(context.owner(), stack);
            context.owner().swing(handOpt.get());
        }
        return false;
    }

    public void spawnFlyingItem(LivingEntity owner, ItemStack stack){
        final FlyingItemEntity flyingEntity = IMMEntities.FLYING_ITEM.get().create(owner.level());
        final ItemStack flyingItem = stack.copy();
        flyingItem.setCount(1);
        if(flyingEntity != null){
            flyingEntity.setPos(owner.position().add(owner.getLookAngle().normalize().scale(1)));
            flyingEntity.setItemStack(flyingItem);
            flyingEntity.setOwner(owner);
            owner.startRiding(flyingEntity);
            owner.level().addFreshEntity(flyingEntity);
            stack.shrink(1);
        }
    }

    public static float getFlyingCost(Entity entity){
        return EntityUtil.hasLearnedSpell(entity, SpellTypes.FLY_WITH_ITEM, 2) ? 1F : 2F;
    }

    @Override
    public InscriptionType getInscriptionType(int level) {
        return level == 1 ? InscriptionTypes.NONE : InscriptionTypes.ANY;
    }
}
