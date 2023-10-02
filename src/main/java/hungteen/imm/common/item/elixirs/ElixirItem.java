package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 13:16
 **/
public abstract class ElixirItem extends Item {

//    private static final String ACCURACY = "Accuracy";
    private final int color;

    public ElixirItem(int color) {
        super(new Properties().stacksTo(16));
        this.color = color;
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
//                Accuracies accuracy = getAccuracy(stack);
//                if(Accuracies.hasUsage(accuracy)){
//                    eatElixir(level, livingEntity, stack, getAccuracy(stack));
//                } else if(accuracy == Accuracies.TRASH){
//                    eatTrash(level, livingEntity, stack);
//                } else if(accuracy == Accuracies.TOXIC){
//                    eatToxic(level, livingEntity, stack);
//                }
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

//    protected void eatTrash(Level level, LivingEntity livingEntity, ItemStack stack){
//
//    }
//
//    protected void eatToxic(Level level, LivingEntity livingEntity, ItemStack stack){
//
//    }

    /**
     * 判断服用丹药的结果。
     * @return False to explode, True to successfully eat elixir, absent to pass.
     */
    public Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack){
        final IRealmType realm = RealmManager.getEntityRealm( livingEntity);
        final ICultivationType cultivationType = realm.getCultivationType();
        var lowRealm = getLowestRealm(cultivationType);
        var highRealm = getHighestRealm(cultivationType);
        if(lowRealm.isEmpty() && highRealm.isEmpty()){
            return Optional.empty(); // 修炼类型不匹配，则食用无效。
        } else if(lowRealm.isPresent() && lowRealm.get().getRealmValue() > realm.getRealmValue()){
            return Optional.of(false); // 境界太低，爆体而亡。
        } else if(highRealm.isPresent() && highRealm.get().getRealmValue() < realm.getRealmValue()){
            return Optional.empty(); // 境界太高，食用无效。
        } else {
            return Optional.of(true); // 正好适合。
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(TipUtil.desc(this).withStyle(ChatFormatting.DARK_GRAY));
        PlayerHelper.getClientPlayer().ifPresent(player -> {
            final ICultivationType cultivationType = PlayerUtil.getCultivationType(player);
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
//        final Accuracies accuracy = getAccuracy(stack);
//        if(Accuracies.hasUsage(accuracy)){
//            final ResourceLocation location = ItemHelper.get().getKey(this);
//            components.add(Component.translatable("tooltip." + location.getNamespace() + "." + location.getPath(), getUsagesComponentArgs(accuracy)).withStyle(ChatFormatting.GREEN));
//        }
//        components.add(Component.translatable("tooltip.imm.elixir.accuracy", getAccuracyValue(stack) + "%").withStyle(accuracy.getFormats())
//                .append(Accuracies.getComponent(accuracy))
//        );
    }

    public boolean validCultivationType(ICultivationType cultivationType){
        return getLowestRealm(cultivationType).isPresent() || getHighestRealm(cultivationType).isPresent();
    }

    public Optional<IRealmType> getLowestRealm(ICultivationType cultivationType){
        return Optional.empty();
    }

    public Optional<IRealmType> getHighestRealm(ICultivationType cultivationType){
        return getLowestRealm(cultivationType);
    }

    public int getColor(int id){
        return id == 0 ? color : -1;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    public static IRealmType getRealm(LivingEntity livingEntity){
        return IMMAPI.get().getEntityRealm(livingEntity);
    }

//    public static void setAccuracy(ItemStack stack, int accuracy){
//        stack.getOrCreateTag().putInt(ACCURACY, accuracy);
//    }
//
//    public static int getAccuracyValue(ItemStack stack) {
//        return stack.getOrCreateTag().getInt(ACCURACY);
//    }
//
//    public static Accuracies getAccuracy(ItemStack stack) {
//        return getAccuracy(getAccuracyValue(stack));
//    }
//
//    public static Accuracies getAccuracy(int accuracy) {
//        List<Accuracies> list = Arrays.stream(Accuracies.values()).sorted(Comparator.comparingInt(Accuracies::getAccuracy)).toList();
//        for (Accuracies accuracies : list) {
//            if(accuracy <= accuracies.getAccuracy()){
//                return accuracies;
//            }
//        }
//        return Accuracies.MASTER;
//    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.getElixirRarity();
    }

    public abstract Rarity getElixirRarity();

    public enum Accuracies {
        TRASH(20, ChatFormatting.DARK_GRAY),
        TOXIC(40, ChatFormatting.DARK_GREEN),
        COMMON(60, ChatFormatting.GREEN),
        NICE(70, ChatFormatting.BLUE),
        EXCELLENT(80, ChatFormatting.DARK_BLUE),
        PERFECT(90, ChatFormatting.DARK_PURPLE),
        MASTER(100, ChatFormatting.GOLD);

        private final int accuracy;
        private final List<ChatFormatting> formats;

        public static MutableComponent getComponent(Accuracies accuracy){
            return Component.translatable("misc.imm.accuracy." + accuracy.toString().toLowerCase(Locale.ROOT)).withStyle(accuracy.getFormats());
        }

        public static boolean hasUsage(Accuracies accuracy){
            return accuracy != Accuracies.TRASH && accuracy != Accuracies.TOXIC;
        }

        Accuracies(int accuracy, ChatFormatting ... formats) {
            this.formats = List.of(formats);
            this.accuracy = accuracy;
        }

        public int getAccuracy() {
            return accuracy;
        }

        public ChatFormatting[] getFormats(){
            return this.formats.toArray(new ChatFormatting[0]);
        }
    }

}
