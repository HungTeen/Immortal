package hungteen.imm.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hungteen.htlib.util.helper.registry.ItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-06 22:48
 **/
public class NBTUtil {

    /**
     * 返回的list类型为tag。
     */
    public static ListTag list(CompoundTag nbt, String name){
        return nbt.getList(name, Tag.TAG_COMPOUND);
    }

    public static ItemStack readResultItem(JsonObject jsonObject){
        return ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
    }

    public static void writeResultItem(JsonObject jsonObject, Item result, int count){
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("item", ItemHelper.get().getKey(result).toString());
        if (count > 1) {
            jsonobject.addProperty("count", count);
        }
    }

    public static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for(int i = 0; i < jsonArray.size(); ++i) {
            ingredients.add(Ingredient.fromJson(jsonArray.get(i)));
        }
        return ingredients;
    }

    public static void writeIngredients(JsonObject jsonObject, NonNullList<Ingredient> ingredients){
        JsonArray jsonarray = new JsonArray();
        for (Ingredient ingredient : ingredients) {
            jsonarray.add(ingredient.toJson());
        }
        jsonObject.add("ingredients", jsonarray);
    }

    public static NonNullList<Ingredient> readIngredients(FriendlyByteBuf buf){
        final int len = buf.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(len, Ingredient.EMPTY);
        for(int j = 0; j < ingredients.size(); ++j) {
            ingredients.set(j, Ingredient.fromNetwork(buf));
        }
        return ingredients;
    }

    public static void writeIngredients(FriendlyByteBuf buf, NonNullList<Ingredient> ingredients){
        buf.writeVarInt(ingredients.size());
        for(Ingredient ingredient : ingredients) {
            ingredient.toNetwork(buf);
        }
    }

}
