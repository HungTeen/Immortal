package hungteen.immortal.common.item.eixirs;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IElixirItem;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.item.ItemTabs;
import hungteen.immortal.impl.Realms;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import java.util.stream.Collectors;

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

    public ElixirItem(Rarity rarity) {
        super(new Properties().tab(ItemTabs.ELIXIRS));
        this.elixirRarity = rarity;
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
        level.gameEvent(livingEntity, GameEvent.EAT, livingEntity.eyeBlockPosition());
        level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
//        livingEntity.addEatEffect(stack, level, this);
        Optional<Boolean> canOpt = checkEating(level, livingEntity, stack);
        if(canOpt.isPresent()){
            if(canOpt.get()){
                eatElixir(level, livingEntity, stack, getAccuracy(stack));
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

    /**
     * Return False to explode, True to successfully eat elixir, absent to pass.
     */
    protected abstract Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack);

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(new TranslatableComponent("tooltip." + this.getRegistryName().getNamespace() + "." + this.getRegistryName().getPath()).withStyle(ChatFormatting.GREEN));
        components.add(new TranslatableComponent("tooltip.immortal.elixir.accuracy", getAccuracyValue(stack) + "%").withStyle(ChatFormatting.AQUA));
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        super.fillItemCategory(tab, stacks);

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
        List<Accuracies> list = Arrays.stream(Accuracies.values()).sorted(Comparator.comparingInt(Accuracies::getAccuracy)).collect(Collectors.toList());
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
        TRASH(20),
        TOXIC(40),
        COMMON(60),
        NICE(80),
        PERFECT(90),
        MASTER(100);

        private final int accuracy;

        Accuracies(int accuracy){
            this.accuracy = accuracy;
        }

        public int getAccuracy() {
            return accuracy;
        }
    }
}
