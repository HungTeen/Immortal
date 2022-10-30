package hungteen.immortal.common.item.eixirs;

import hungteen.immortal.api.interfaces.IElixirItem;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.item.ItemTabs;
import hungteen.immortal.impl.Realms;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 13:16
 *
 * Another type of food.
 **/
public abstract class ElixirItem extends Item implements IElixirItem{

    private final Rarity elixirRarity;
    public ElixirItem(Rarity rarity) {
        super(new Properties().tab(ItemTabs.MATERIALS));
        this.elixirRarity = rarity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        player.startUsingItem(interactionHand);
        return InteractionResultHolder.consume(stack);
//        return super.use(level, player, interactionHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return eatBy(level, livingEntity, stack);
    }

    public ItemStack eatBy(Level level, LivingEntity livingEntity, ItemStack stack) {
        level.gameEvent(livingEntity, GameEvent.EAT, livingEntity.eyeBlockPosition());
        level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
//        livingEntity.addEatEffect(stack, level, this);
        if (!(livingEntity instanceof Player) || !((Player) livingEntity).getAbilities().instabuild) {
            stack.shrink(1);
        }
        livingEntity.gameEvent(GameEvent.EAT);

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    private static Function<IRealm, Optional<Boolean>> immortal(){
        return largeThan(Realms.MEDITATION_STAGE1);
    }

    private static Function<IRealm, Optional<Boolean>> lessThan(IRealm base){
        return realm -> {
            if(realm.getRealmValue() <= base.getRealmValue()){
                return Optional.of(true);
            }
            return Optional.empty();
        };
    }

    private static Function<IRealm, Optional<Boolean>> largeThan(IRealm base){
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
}
