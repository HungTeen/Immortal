package hungteen.immortal.common.item.eixirs;

import hungteen.htlib.util.helper.ItemHelper;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IElixirItem;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.common.item.ItemTabs;
import hungteen.immortal.impl.Realms;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
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
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 13:16
 *
 * Another type of food.
 **/
public abstract class ElixirItem extends Item implements IElixirItem{

    private static final String ACCURACY = "Accuracy";
    private final Rarity elixirRarity;
    private final int color;

    public ElixirItem(Rarity rarity, int color) {
        super(new Properties().tab(ItemTabs.ELIXIRS));
        this.elixirRarity = rarity;
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
        level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
//        livingEntity.addEatEffect(stack, level, this);
        Optional<Boolean> canOpt = checkEating(level, livingEntity, stack);
        if(canOpt.isPresent()){
            if(canOpt.get()){
                Accuracies accuracy = getAccuracy(stack);
                if(Accuracies.hasUsage(accuracy)){
                    eatElixir(level, livingEntity, stack, getAccuracy(stack));
                } else if(accuracy == Accuracies.TRASH){
                    eatTrash(level, livingEntity, stack);
                } else if(accuracy == Accuracies.TOXIC){
                    eatToxic(level, livingEntity, stack);
                }
            } else{
                explode(level, livingEntity, stack);
            }
        } else{
            // Nothing Happen.
        }

        if (!(livingEntity instanceof Player) || !((Player) livingEntity).getAbilities().instabuild) {
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

    protected abstract void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy);

    protected void eatTrash(Level level, LivingEntity livingEntity, ItemStack stack){

    }

    protected void eatToxic(Level level, LivingEntity livingEntity, ItemStack stack){

    }

    /**
     * Return False to explode, True to successfully eat elixir, absent to pass.
     */
    protected abstract Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack);

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        final Accuracies accuracy = getAccuracy(stack);
        if(Accuracies.hasUsage(accuracy)){
            final ResourceLocation location = ItemHelper.getKey(this);
            components.add(Component.translatable("tooltip." + location.getNamespace() + "." + location.getPath(), getUsagesComponentArgs(accuracy)).withStyle(ChatFormatting.GREEN));
        }
        components.add(Component.translatable("tooltip.immortal.elixir.accuracy", getAccuracyValue(stack) + "%").withStyle(accuracy.getFormats())
                .append(Accuracies.getComponent(accuracy))
        );
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if(this.allowedIn(tab)){
            for (Accuracies value : Accuracies.values()) {
                if(tab == CreativeModeTab.TAB_SEARCH || value == Accuracies.COMMON || value == Accuracies.MASTER){
                    ItemStack stack = new ItemStack(this);
                    setAccuracy(stack, value.getAccuracy());
                    stacks.add(stack);
                }
            }
        }
    }

    protected List<Object> getUsagesComponentArgs(Accuracies accuracy){
        return List.of();
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

    public static IRealm getRealm(LivingEntity livingEntity){
        return ImmortalAPI.get().getEntityRealm(livingEntity);
    }

    public static void setAccuracy(ItemStack stack, int accuracy){
        stack.getOrCreateTag().putInt(ACCURACY, accuracy);
    }

    public static int getAccuracyValue(ItemStack stack) {
        return stack.getOrCreateTag().getInt(ACCURACY);
    }

    public static Accuracies getAccuracy(ItemStack stack) {
        return getAccuracy(getAccuracyValue(stack));
    }

    public static Accuracies getAccuracy(int accuracy) {
        List<Accuracies> list = Arrays.stream(Accuracies.values()).sorted(Comparator.comparingInt(Accuracies::getAccuracy)).toList();
        for (Accuracies accuracies : list) {
            if(accuracy <= accuracies.getAccuracy()){
                return accuracies;
            }
        }
        return Accuracies.MASTER;
    }

    protected static Function<IRealm, Optional<Boolean>> immortal(){
        return largeThan(Realms.MEDITATION_STAGE1);
    }

    protected static Function<IRealm, Optional<Boolean>> same(IRealm base){
        return realm -> {
            if(realm.getRealmValue() == base.getRealmValue()){
                return Optional.of(true);
            }
            return Optional.empty();
        };
    }

    protected static Function<IRealm, Optional<Boolean>> lessThan(IRealm base){
        return realm -> {
            if(realm.getRealmValue() <= base.getRealmValue()){
                return Optional.of(true);
            }
            return Optional.empty();
        };
    }

    protected static Function<IRealm, Optional<Boolean>> largeThan(IRealm base){
        return realm -> {
            if(realm.getRealmValue() >= base.getRealmValue()){
                return Optional.of(true);
            }
            return Optional.of(false);
        };
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.elixirRarity;
    }

    @Override
    public Rarity getElixirRarity() {
        return elixirRarity;
    }

    @Override
    public ItemStack getElixirItem() {
        return new ItemStack(this);
    }

    public enum Accuracies{
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
            return Component.translatable("misc.immortal.accuracy." + accuracy.toString().toLowerCase(Locale.ROOT)).withStyle(accuracy.getFormats());
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
