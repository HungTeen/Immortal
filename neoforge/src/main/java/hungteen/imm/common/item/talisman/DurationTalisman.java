package hungteen.imm.common.item.talisman;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.IMMSounds;
import hungteen.imm.common.cultivation.QiManager;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.ParticleUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 用于实现蓄力法术。
 *
 * @author HungTeen
 * @program Immortal
 * @create 2023-06-04 16:16
 **/
public abstract class DurationTalisman extends TalismanItem {

    public DurationTalisman() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.canUseTalisman(stack, player)) {
            player.startUsingItem(hand);
            if (player instanceof ServerPlayer) {
                PlayerHelper.playClientSound(player, IMMSounds.TALISMAN_USE.get());
            }
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    /**
     * 检查灵气是否足够。
     */
    @Override
    public boolean canUseTalisman(ItemStack stack, LivingEntity living) {
        float qiCost = getQiCost(stack, living);
        if(! QiManager.hasEnoughQi(living, qiCost)){
            if(living instanceof Player player){
                PlayerHelper.sendTipTo(player, noEnoughQiTip(qiCost));
            }
            return false;
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext level, List<Component> components, TooltipFlag tooltipFlag) {
        PlayerHelper.getClientPlayer().ifPresent(player -> {
            components.add(ItemUtil.desc(itemStack, getQiCost(itemStack, player), getCoolDown(itemStack, player)));
        });
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        // 中途也需要检查灵力是否充足。
        if(! level.isClientSide() && ! canUseTalisman(stack, livingEntity)){
            livingEntity.stopUsingItem();
        }
        if (level.isClientSide() && remainingUseDuration % 4 == 0) {
            for (int i = 0; i < 4; ++i) {
                Vec3 pos = ParticleUtil.getUsingItemPos(livingEntity);
                ParticleUtil.spawnClientParticles(level, IMMParticles.QI.get(), pos, 1, 0.1, 0.1);
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            if (!EntityUtil.notConsume(entity)) {
                stack.shrink(1);
            }
            QiManager.addQi(entity, -getQiCost(stack, entity));
            if (entity instanceof Player player) {
                PlayerUtil.setCoolDown(player, stack.getItem(), getCoolDown(stack, player));
            }
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return 30;
    }

    public abstract int getCoolDown(ItemStack stack, LivingEntity entity);

    public abstract float getQiCost(ItemStack stack, Entity entity);

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }
}
