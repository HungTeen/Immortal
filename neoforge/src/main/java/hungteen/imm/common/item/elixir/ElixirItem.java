package hungteen.imm.common.item.elixir;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.RealmManager;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * 灵丹妙药。
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-16 13:16
 **/
public abstract class ElixirItem extends Item {

    private final int color;

    public ElixirItem(Properties properties, int color) {
        super(properties.stacksTo(16));
        this.color = color;
    }

    public static Properties withRarity(Rarity rarity){
        return new Item.Properties().rarity(rarity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return eatBy(level, livingEntity, stack);
    }

    public ItemStack eatBy(Level level, LivingEntity livingEntity, ItemStack stack) {
        level.gameEvent(livingEntity, GameEvent.EAT, livingEntity.getEyePosition());
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
        Optional<Boolean> canOpt = checkEating(level, livingEntity, stack);
        if(canOpt.isPresent()){
            if(canOpt.get()){
                eatElixir(level, livingEntity, stack);
            } else{
                explode(level, livingEntity, stack);
            }
        } else{
            // Nothing Happen.
        }

        if (! EntityUtil.notConsume(livingEntity)) {
            stack.shrink(1);
        }
        livingEntity.gameEvent(GameEvent.EAT);

        return stack;
    }

    /**
     * 爆体而亡。
     */
    protected void explode(Level level, LivingEntity livingEntity, ItemStack stack){

    }

    protected abstract void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack);

    /**
     * 判断服用丹药的结果。<br>
     * 修炼类型不匹配，则食用无效；境界太低，爆体而亡；境界太高，食用无效。
     * @return False to explode, True to successfully eat elixir, absent to pass.
     */
    public Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack){
        final RealmType realm = RealmManager.getRealm(livingEntity);
        final CultivationType cultivationType = realm.getCultivationType();
        var lowRealm = getLowestRealm(cultivationType);
        var highRealm = getHighestRealm(cultivationType);
        if(lowRealm.isEmpty() && highRealm.isEmpty()){
            return Optional.empty();
        } else if(lowRealm.isPresent() && lowRealm.get().getRealmValue() > realm.getRealmValue()){
            return Optional.of(false);
        } else if(highRealm.isPresent() && highRealm.get().getRealmValue() < realm.getRealmValue()){
            return Optional.of(false);
        } else {
            return Optional.of(true);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(TipUtil.desc(this).withStyle(ChatFormatting.DARK_GRAY));
        PlayerHelper.getClientPlayer().ifPresent(player -> {
            final CultivationType cultivationType = PlayerUtil.getCultivationType(player);
            var low = getLowestRealm(cultivationType);
            var high = getHighestRealm(cultivationType);
            if(low.isPresent() && high.isPresent()){
                if(low.get() == high.get()){
                    components.add(TipUtil.tooltip("elixir.match_equal_realm", low.get().getComponent().getString()));
                } else {
                    components.add(TipUtil.tooltip("elixir.match_range_realm", low.get().getComponent().getString(), high.get().getComponent().getString()));
                }
            } else if(low.isPresent()){
                components.add(TipUtil.tooltip("elixir.match_higher_realm", low.get().getComponent().getString()));
            } else if(high.isPresent()){
                components.add(TipUtil.tooltip("elixir.match_lower_realm", high.get().getComponent().getString()));
            } else {
                components.add(TipUtil.tooltip("elixir.realm_not_match", cultivationType.getComponent().getString()));
            }
        });
    }

    public boolean validCultivationType(CultivationType cultivationType){
        return getLowestRealm(cultivationType).isPresent() || getHighestRealm(cultivationType).isPresent();
    }

    public Optional<RealmType> getLowestRealm(CultivationType cultivationType){
        return Optional.empty();
    }

    public Optional<RealmType> getHighestRealm(CultivationType cultivationType){
        return Optional.empty();
    }

    public int getColor(ItemStack stack, int id){
        return id == 0 ? FastColor.ARGB32.opaque(color) : -1;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

}
