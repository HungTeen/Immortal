package hungteen.immortal.common.item.artifacts;

import hungteen.htlib.util.ColorUtil;
import hungteen.htlib.util.ParticleUtil;
import hungteen.immortal.common.entity.misc.SpiritualFlame;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import hungteen.immortal.utils.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-08 09:42
 **/
public class FlameGourd extends ArtifactItem {

    private static final String COLLECTED_FLAME_AMOUNT = "CollectedFlameAmount";
    private static final String COLLECTED_FLAME_LEVEL = "CollectedFlameLevel";
    private static final int MAX_FLAME_AMOUNT = 500;

    public FlameGourd(int artifactLevel) {
        super(artifactLevel);
    }

    public FlameGourd(int artifactLevel, boolean isAncientArtifact) {
        super(artifactLevel, isAncientArtifact);
    }

    /**
     * {@link PlayerEventHandler#onTraceEntity(Player, EntityHitResult)}
     */
    public static void rightClickFlame(Player player, ItemStack stack){
        if(! isFlameFull(stack)){
            player.startUsingItem(InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if(stack.getItem() instanceof FlameGourd && player instanceof Player){
            final HitResult hitResult = PlayerEventHandler.getHitResult((Player) player);
            final FlameGourd flameGourd = (FlameGourd) stack.getItem();
            if(hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof SpiritualFlame){
                final SpiritualFlame flame = (SpiritualFlame) ((EntityHitResult) hitResult).getEntity();
                // 等级不匹配 爆炸。
                if(flame.getFlameLevel() > flameGourd.getMaxFlameLevel()){
                    // TODO 更专业的爆炸
                    if(! player.level.isClientSide){
                        player.level.explode(flame, player.getX(), player.getY(), player.getZ(), 10, true, Explosion.BlockInteraction.BREAK);
                    }
                    if (player instanceof Player) {
                        ((Player) player).awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
                    }
                    stack.shrink(1);
//                    player.level.explode();
                } else{
                    absorbFlame(player, flame, stack, flameGourd);
                }
            }
        }
    }

    public static void absorbFlame(LivingEntity livingEntity, SpiritualFlame flame, ItemStack stack, FlameGourd flameGourd){
        if(! livingEntity.level.isClientSide){
            final int speed = (flameGourd.getMaxFlameLevel() - flame.getFlameLevel()) * 2 + 1;
            addFlameAmount(stack, flame.getFlameLevel(), speed);
        } else{
            final int num = (flameGourd.getMaxFlameLevel() - flame.getFlameLevel()) / 2 + 1;
            ParticleUtil.spawnLineMovingParticle(livingEntity.level, SpiritualFlame.getFlameParticleType(flame.getFlameLevel()), flame.getFlameCenter(), livingEntity.getEyePosition(), num, 0.1 * num, 0.1);
//            final double distance = livingEntity.distanceTo(flame);
//            final int particleNum = Mth.ceil(distance);
//            for(int i = 0; i < particleNum; ++ i){
//                for(int j = 0; j < num; ++ j){
//                    final Vec3 pos = flame.getFlameCenter().add(livingEntity.getEyePosition().subtract(flame.getFlameCenter()).normalize().scale(Math.max(1, distance - 2) / particleNum * (i + 1))).add(MathUtil.getRandomVec3(livingEntity.getRandom(), 0.1 * num));
//                    final Vec3 speed = livingEntity.getEyePosition().subtract(flame.getFlameCenter()).normalize().scale(0.1);
//                    ParticleUtil.spawnParticles(livingEntity.level, SpiritualFlame.getFlameParticleType(flame.getFlameLevel()), pos, speed.x, speed.y, speed.z);
//                }
//            }
        }
    }

    public static int getFlameLevel(ItemStack stack){
        return stack.getOrCreateTag().getInt(COLLECTED_FLAME_LEVEL);
    }

    public static int getFlameAmount(ItemStack stack){
        return stack.getOrCreateTag().getInt(COLLECTED_FLAME_AMOUNT);
    }

    public static void addFlameAmount(ItemStack stack, int level, int amount){
        if(getFlameLevel(stack) > level){
            // 无效。
        } else if(getFlameLevel(stack) > level){
            // 清空低级火。
            setFlameLevel(stack, level);
            setFlameAmount(stack, 0);
        } else{
            setFlameAmount(stack, Mth.clamp(getFlameAmount(stack) + amount, 0, MAX_FLAME_AMOUNT));
        }
    }

    public static boolean isFlameFull(ItemStack stack){
        return getFlameAmount(stack) == MAX_FLAME_AMOUNT;
    }

    public static void setFlameAmount(ItemStack stack, int amount){
        stack.getOrCreateTag().putInt(COLLECTED_FLAME_AMOUNT, amount);
    }

    public static void setFlameLevel(ItemStack stack, int level){
        stack.getOrCreateTag().putInt(COLLECTED_FLAME_LEVEL, level);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1000000;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (getFlameAmount(stack) * 13.0F / MAX_FLAME_AMOUNT);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return ColorUtil.RED;
    }

    public int getMaxFlameLevel(){
        return getArtifactLevel() == 0 ? 1 :
                getArtifactLevel() <= 3 ? 3 :
                        getArtifactLevel() <= 6 ? 6 :
                                Constants.MAX_FLAME_LEVEL;
    }

}
