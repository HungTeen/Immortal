package hungteen.imm.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.htlib.util.helper.registry.RecipeHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
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

    public static String readGroup(JsonObject jsonObject){
        return GsonHelper.getAsString(jsonObject, "group", "");
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

    public static NonNullList<Ingredient> readIngredients(JsonObject jsonObject) {
        return RecipeHelper.readIngredients(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
    }

    public static void writeResultItem(JsonObject jsonObject, Item result, int count) {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", ItemHelper.get().getKey(result).toString());
        if (count > 1) {
            obj.addProperty("count", count);
        }
        jsonObject.add("result", obj);
    }

}
