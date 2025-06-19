package hungteen.imm.common.cultivation.spell.fire;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.util.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.Optional;

/**
 * 燃烧手中的物品。
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-27 19:30
 **/
public class BurningSpell extends SpellTypeImpl {

    public BurningSpell() {
        super("burning", property(InscriptionTypes.ANY).qi(30).cd(200).maxLevel(2));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        boolean success = false;
        if(context.itemTrigger() && context.getEvent() instanceof BlockDropsEvent event){
            for (ItemEntity item : event.getDrops()) {
                Optional<ItemStack> burningResult = getBurningResult(context.level(), item.getItem(), context.spellLevel());
                if(burningResult.isPresent()){
                    success = true;
                    item.setItem(burningResult.get());
                }
            }
        } else {
            Optional<ItemStack> mainBurnResult = getBurningResult(context.level(), context.owner().getMainHandItem(), context.spellLevel());
            if(mainBurnResult.isPresent()){
                success = true;
                burn(context.owner(), context.owner().getMainHandItem(), mainBurnResult.get(), context);
            } else {
                Optional<ItemStack> offBurnResult = getBurningResult(context.level(), context.owner().getOffhandItem(), context.spellLevel());
                if(offBurnResult.isPresent()){
                    success = true;
                    burn(context.owner(), context.owner().getOffhandItem(), offBurnResult.get(), context);
                }
            }
        }
        if(success){
            context.owner().playSound(SoundEvents.BLAZE_BURN);
        } else {
            sendTip(context, "can_not_burn");
        }
        return success;
    }

    public void burn(LivingEntity owner, ItemStack origin, ItemStack result, SpellCastContext context){
        int count = Math.min(origin.getCount(), 2 * context.spellLevel());
        origin.shrink(count);
        result.setCount(count);
        EntityUtil.addItem(owner, result);
    }

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
