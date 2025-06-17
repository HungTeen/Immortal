package hungteen.imm.common.cultivation.spell.fire;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * 燃烧手中的物品。
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-27 19:30
 **/
public class BurningSpell extends SpellTypeImpl {

    public BurningSpell() {
        super("burning", property().mana(30).cd(200).maxLevel(2));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        return super.checkActivate(context);
    }

//    @Override
//    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
//        Optional<ItemStack> stack = getBurningResult(owner.level(), owner.getMainHandItem(), level);
//        if(stack.isPresent()){
//            owner.getMainHandItem().shrink(1);
//            EntityUtil.addItem(owner, stack.get());
//            owner.playSound(SoundEvents.BLAZE_BURN);
//            return true;
//        } else {
//            stack = getBurningResult(owner.level(), owner.getOffhandItem(), level);
//            if(stack.isPresent()){
//                owner.getOffhandItem().shrink(1);
//                EntityUtil.addItem(owner, stack.get());
//                owner.playSound(SoundEvents.BLAZE_BURN);
//                return true;
//            }
//        }
//        this.sendTip(owner, "can_not_burn");
//        return false;
//    }

    private static Optional<ItemStack> getBurningResult(Level level, ItemStack stack, int lvl){
        if(stack.is(Items.STICK)) {
            return Optional.of(new ItemStack(Items.TORCH));
        }
        Optional<ItemStack> result = getBurningResult(level, RecipeType.CAMPFIRE_COOKING, stack);
        if(result.isPresent()) {
            return result;
        }
        if(lvl > 1){
            result = getBurningResult(level, RecipeType.SMELTING, stack);
            if(result.isPresent()) {
                return result;
            }
            return getBurningResult(level, RecipeType.BLASTING, stack);
        }
        return Optional.empty();
    }

    private static <C extends RecipeInput, T extends Recipe<C>> Optional<ItemStack> getBurningResult(Level level, RecipeType<T> recipe, ItemStack stack){
        return level.getRecipeManager().getAllRecipesFor(recipe).stream().filter(r -> r.value().getIngredients().get(0).test(stack)).findAny().map(r -> r.value().getResultItem(level.registryAccess()).copy());
    }

}
