package hungteen.imm.common.item.artifacts;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.entity.misc.SpiritualPearl;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Similar to {@link net.minecraft.world.item.EnderEyeItem}.
 *
 * @program Immortal
 * @author HungTeen
 * @create 2023-02-25 23:08
 **/
public class SpiritualPearlItem extends ArtifactItem {

    public SpiritualPearlItem() {
        super(RealmTypes.COMMON_ARTIFACT);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand hand) {
        if (!player.level().isClientSide) {
            spawnSpiritualPearl(player.level(), player, living, stack, hand);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            spawnSpiritualPearl(level, player, player, itemstack, hand);
            PlayerUtil.setIntegerData(player, IMMPlayerData.IntegerData.KNOW_SPIRITUAL_ROOTS, 1);
            return InteractionResultHolder.success(itemstack);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    private static void spawnSpiritualPearl(Level level, Player player, LivingEntity target, ItemStack itemStack, InteractionHand hand){
        SpiritualPearl pearl = new SpiritualPearl(level, player, target.getX(), target.getY(1D), target.getZ());
        level.gameEvent(GameEvent.PROJECTILE_SHOOT, pearl.position(), GameEvent.Context.of(player));
        pearl.setDeltaMovement(RandomHelper.vec3Range(target.getRandom(), 0.03F));
        level.addFreshEntity(pearl);
        pearl.setCheckTarget(target);
        if(player instanceof ServerPlayer serverPlayer){
//            SpiritualPearlTrigger.INSTANCE.trigger(serverPlayer, target, EntityUtil.getRoots(target, player).size());
        }

        LevelUtil.playSound(level, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, player.position(), 0.5F, 0.4F);

        if(! PlayerUtil.notConsume(player)){
            itemStack.shrink(1);
        }

        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        player.swing(hand, true);
    }

}
