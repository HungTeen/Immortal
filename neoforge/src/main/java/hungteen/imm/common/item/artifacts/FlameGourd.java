package hungteen.imm.common.item.artifacts;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.entity.misc.SpiritualFlame;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-08 09:42
 **/
public class FlameGourd extends ArtifactItem {

    private static final String COLLECTED_FLAME_AMOUNT = "CollectedFlameAmount";
    private static final String COLLECTED_FLAME_LEVEL = "CollectedFlameLevel";
    private static final int MAX_FLAME_AMOUNT = 500;

    public FlameGourd(RealmType artifactType) {
        super(artifactType);
    }

    /**
     * 火葫芦收集灵火。
     * {@link PlayerEventHandler#onTraceEntity(Player, InteractionHand, EntityHitResult)}
     */
    public static InteractionResult collectSpiritualFlame(Player player, InteractionHand hand, Entity target){
        final ItemStack stack = player.getItemInHand(hand);
        if(target instanceof SpiritualFlame flame && stack.getItem() instanceof FlameGourd gourd){
            if(canStoreFlame(stack, gourd, flame)){
                if(! isFlameFull(stack)){
                    player.startUsingItem(hand);
                } else {
                    PlayerHelper.sendTipTo(player, TipUtil.info("gourd_is_full"));
                }
                return InteractionResult.SUCCESS;
            } else {
                PlayerHelper.sendTipTo(player, TipUtil.info("gourd_level"));
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {
        if(stack.getItem() instanceof FlameGourd gourd){
            final HitResult hitResult = EntityUtil.getHitResult(player, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
            if(hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof SpiritualFlame flame){
                if(canStoreFlame(stack, gourd, flame)){
                    if(! isFlameFull(stack)) {
                        absorbFlame(player, flame, stack, gourd);
                    } else {
                        player.stopUsingItem();
                    }
                } else {
                    player.stopUsingItem();
                }
            }
        }
    }

    public static void absorbFlame(LivingEntity livingEntity, SpiritualFlame flame, ItemStack stack, FlameGourd flameGourd){
        if(EntityHelper.isServer(livingEntity)){
            addFlameAmount(stack, flame.getFlameLevel(), 5);
            flame.addQiAmount(-5);
            LevelUtil.playSound(livingEntity.level(), SoundEvents.LAVA_POP, SoundSource.AMBIENT, livingEntity.position());
        } else {
            ParticleHelper.spawnLineMovingParticle(livingEntity.level(), SpiritualFlame.getFlameParticleType(flame.getFlameLevel()), flame.getEyePosition(), livingEntity.getEyePosition(), 2, 0.1, 0.1);
        }
    }

    public static ItemStack createFlameGourd(float percent){
        ItemStack stack = new ItemStack(IMMItems.FLAME_GOURD.get());
        setFlameAmount(stack, (int) (MAX_FLAME_AMOUNT * percent));
        setFlameLevel(stack, 1);
        return stack;
    }

    public static int getFlameLevel(ItemStack stack){
        return 0;
//        return stack.getOrCreateTag().getInt(COLLECTED_FLAME_LEVEL);
    }

    public static int getFlameAmount(ItemStack stack){
        return 0;
//        return stack.getOrCreateTag().getInt(COLLECTED_FLAME_AMOUNT);
    }

    public static void addFlameAmount(ItemStack stack, int level, int amount){
        // 清空之前不同等级的火。
        if(getFlameLevel(stack) != level){
            setFlameLevel(stack, level);
            setFlameAmount(stack, amount);
        } else{
            setFlameAmount(stack, getFlameAmount(stack) + amount);
        }
    }

    /**
     * 火葫芦的等级不能低于灵火等级。
     */
    public static boolean canStoreFlame(ItemStack stack, FlameGourd gourd, SpiritualFlame flame){
        return canStoreFlame(gourd.getArtifactRealm(stack), flame.getRealm());
    }

    public static boolean canStoreFlame(RealmType artifactType, RealmType realmType){
        return artifactType.getRealmValue() >= realmType.getRealmValue();
    }

    public static boolean isFlameFull(ItemStack stack){
        return getFlameAmount(stack) == MAX_FLAME_AMOUNT;
    }

    public static void setFlameAmount(ItemStack stack, int amount){
//        stack.getOrCreateTag().putInt(COLLECTED_FLAME_AMOUNT, Mth.clamp(amount, 0, MAX_FLAME_AMOUNT));
    }

    public static void setFlameLevel(ItemStack stack, int level){
//        stack.getOrCreateTag().putInt(COLLECTED_FLAME_LEVEL, level);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);
        components.add(Component.translatable("tooltip.imm.flame_gourd.flame_level", getFlameLevel(itemStack)).withStyle(ChatFormatting.YELLOW));
        components.add(Component.translatable("tooltip.imm.flame_gourd.flame_amount", getFlameAmount(itemStack)).withStyle(ChatFormatting.RED));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
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

}
