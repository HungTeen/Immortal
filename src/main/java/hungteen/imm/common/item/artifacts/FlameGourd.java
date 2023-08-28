package hungteen.imm.common.item.artifacts;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.entity.misc.SpiritualFlame;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.util.EntityUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

    public FlameGourd(IArtifactType artifactType) {
        super(artifactType);
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
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {
        if(stack.getItem() instanceof FlameGourd && player instanceof Player){
            final HitResult hitResult = EntityUtil.getHitResult((Player) player);
            final FlameGourd flameGourd = (FlameGourd) stack.getItem();
            if(hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof SpiritualFlame){
                final SpiritualFlame flame = (SpiritualFlame) ((EntityHitResult) hitResult).getEntity();
                // 等级不匹配 爆炸。
                if(flame.getFlameLevel() > flameGourd.getMaxFlameLevel()){
                    // TODO 更专业的爆炸
//                    if(! player.level.isClientSide){
//                        player.level.explode(flame, player.getX(), player.getY(), player.getZ(), 10, true, Explosion.BlockInteraction.DESTROY);
//                    }
                    ((Player) player).awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
                    stack.shrink(1);
//                    player.level.explode();
                } else{
                    absorbFlame(player, flame, stack, flameGourd);
                }
            }
        }
    }

    public static void absorbFlame(LivingEntity livingEntity, SpiritualFlame flame, ItemStack stack, FlameGourd flameGourd){
        if(EntityHelper.isServer(livingEntity)){
            final int speed = (flameGourd.getMaxFlameLevel() - flame.getFlameLevel()) * 2 + 1;
            addFlameAmount(stack, flame.getFlameLevel(), speed);
        } else{
            final int num = (flameGourd.getMaxFlameLevel() - flame.getFlameLevel()) / 2 + 1;
            ParticleHelper.spawnLineMovingParticle(livingEntity.level(), SpiritualFlame.getFlameParticleType(flame.getFlameLevel()), flame.getFlameCenter(), livingEntity.getEyePosition(), num, 0.1 * num, 0.1);
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
        components.add(Component.translatable("tooltip.imm.flame_gourd.flame_level", getFlameLevel(itemStack)).withStyle(ChatFormatting.YELLOW));
        components.add(Component.translatable("tooltip.imm.flame_gourd.flame_amount", getFlameAmount(itemStack)).withStyle(ChatFormatting.RED));
    }

//    @Override
//    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStacks) {
//        if(this.allowedIn(tab)){
//            ItemStack empty = new ItemStack(this);
//            itemStacks.add(empty);
//            ItemStack full = empty.copy();
//            setFlameAmount(full, MAX_FLAME_AMOUNT);
//            itemStacks.add(full);
//        }
//    }

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
        return ColorHelper.RED.rgb();
    }

    public int getMaxFlameLevel(){
        return 1;
        //TODO 灵火对应收集等级
//        return getArtifactType() <= 3 ? 3 :
//                        getArtifactType() <= 6 ? 6 :
//                                Constants.MAX_FLAME_LEVEL;
    }

}
