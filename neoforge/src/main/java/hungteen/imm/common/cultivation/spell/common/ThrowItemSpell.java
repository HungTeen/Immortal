package hungteen.imm.common.cultivation.spell.common;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.entity.misc.ThrowingItemEntity;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/17 16:48
 */
public class ThrowItemSpell extends SpellTypeImpl {

    public ThrowItemSpell() {
        super("throw_item", properties(SpellUsageCategory.ATTACK_TARGET).maxLevel(1).mana(25).cd(80));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if (EntityUtil.hasItemInHand(owner)) {
            final ItemStack throwItem = EntityUtil.getItemInHand(owner, JavaHelper::alwaysTrue);
            final ThrowingItemEntity projectile = new ThrowingItemEntity(owner, owner.level());
            projectile.setItem(throwItem.copy());
            if(owner instanceof Player || result.getEntity() == null){
                projectile.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0.0F, 3F, 1.0F);
            } else {
                EntityUtil.shootProjectile(projectile, result.getEntity().position().subtract(owner.getEyePosition()), 1.2F, 2F);
            }
            if(owner.level().addFreshEntity(projectile)){
                if(! EntityUtil.notConsume(owner)){
                    throwItem.shrink(1);
                }
                LevelUtil.playSound(owner.level(), SoundEvents.TRIDENT_THROW.value(), SoundSource.NEUTRAL, projectile.position(), 1F, 1F);
                return true;
            } else {
                this.sendTip(owner, "throw_failed");
                IMMAPI.logger().error("Failed to throw item entity");
            }
        } else {
            this.sendTip(owner, "no_item_in_hands");
        }
        return false;
    }

}
