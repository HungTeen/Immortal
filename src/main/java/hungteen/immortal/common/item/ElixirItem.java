package hungteen.immortal.common.item;

import hungteen.immortal.api.registry.IElixirType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 13:16
 *
 * Another type of food.
 **/
public class ElixirItem extends Item {

    private final IElixirType elixirType;

    public ElixirItem(IElixirType elixirType) {
        super(new Properties().tab(ItemTabs.MATERIALS));
        this.elixirType = elixirType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if(getElixirType(stack).isPresent()){
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        Optional<IElixirType> opt = getElixirType(stack);
        return opt.map(type -> eatBy(level, livingEntity, stack, type)).orElse(stack);
    }

    public static ItemStack eatBy(Level level, LivingEntity livingEntity, ItemStack stack, IElixirType type) {
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
        return getElixirType(stack).map(IElixirType::getEatDuration).orElse(0);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return getElixirType(stack).isPresent() ? UseAnim.EAT : UseAnim.NONE;
    }

    public static Optional<IElixirType> getElixirType(ItemStack stack) {
        return stack.getItem() instanceof ElixirItem ? Optional.ofNullable(((ElixirItem) stack.getItem()).getElixirType()) : Optional.empty();
    }

    public IElixirType getElixirType() {
        return elixirType;
    }
}
