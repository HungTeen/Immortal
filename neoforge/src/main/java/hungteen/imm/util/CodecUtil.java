package hungteen.imm.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2024/11/19 15:23
 **/
public interface CodecUtil {

    static MapCodec<NonNullList<Ingredient>> readIngredientsCodec(){
        return Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((ingredients) -> {
            Ingredient[] aingredient = ingredients.toArray(Ingredient[]::new);
            if (aingredient.length == 0) {
                return DataResult.error(() -> "No ingredients for shapeless recipe");
            } else {
                return aingredient.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth() ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth())) : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
            }
        }, DataResult::success);
    }
}
