package hungteen.imm.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/13 9:32
 */
public class RecipeUtil {

    public static void writeGroup(JsonObject jsonObject, String group){
        if (!group.isEmpty()) {
            jsonObject.addProperty("group", group);
        }
    }

    public static void writeIngredients(JsonObject jsonObject, List<Ingredient> ingredients) {
        JsonArray jsonarray = new JsonArray();
        Iterator var3 = ingredients.iterator();

        while(var3.hasNext()) {
            Ingredient ingredient = (Ingredient)var3.next();
            jsonarray.add(ingredient.toJson());
        }

        jsonObject.add("ingredients", jsonarray);
    }

}
