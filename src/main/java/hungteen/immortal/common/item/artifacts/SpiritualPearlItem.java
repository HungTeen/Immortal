package hungteen.immortal.common.item.artifacts;

import hungteen.immortal.common.entity.misc.SpiritualPearl;
import hungteen.immortal.common.impl.registry.ArtifactTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;

/**
 * Similar to EnderEyeItem.
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-02-25 23:08
 **/
public class SpiritualPearlItem extends ArtifactItem{
    public SpiritualPearlItem() {
        super(ArtifactTypes.COMMON_ARTIFACT);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        final HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitresult.getType() != HitResult.Type.BLOCK) {
            player.startUsingItem(hand);
            if(! level.isClientSide){
                SpiritualPearl pearl = new SpiritualPearl(level, player.getX(), player.getY(0.6D), player.getZ());
                pearl.setItem(itemstack);
                level.gameEvent(GameEvent.PROJECTILE_SHOOT, pearl.position(), GameEvent.Context.of(player));
                level.addFreshEntity(pearl);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                player.swing(hand, true);
                return InteractionResultHolder.success(itemstack);
            }
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }
}
