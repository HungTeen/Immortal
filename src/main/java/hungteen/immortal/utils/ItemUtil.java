package hungteen.immortal.utils;

import hungteen.htlib.util.helper.ItemHelper;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.item.ImmortalItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-10 20:44
 **/
public class ItemUtil {

    private static final String SUFFIX = "large_held";
    private static final List<RegistryObject<Item>> LARGE_HELD_ITEMS = new ArrayList<>();

    public static ResourceLocation getLargeHeldLocation(Item item){
        return Util.suffix(ItemHelper.getKey(item), SUFFIX);
    }

    /**
     * {@link ImmortalMod#coreRegister()}
     */
    public static void registerLargeHeldItems(){
        List.of(
                ImmortalItems.BRONZE_SWORD
        ).forEach(ItemUtil::registerLargeHeldItem);
    }

    /**
     * Register item so that it will change BakedModel.
     */
    public static void registerLargeHeldItem(RegistryObject<Item> item){
        if(! LARGE_HELD_ITEMS.contains(item)){
            LARGE_HELD_ITEMS.add(item);
        } else {
            Util.warn("Registered this item {} before", item.getKey().location());
        }
    }

    public static List<Item> getLargeHeldItems(){
        return LARGE_HELD_ITEMS.stream().map(RegistryObject::get).toList();
    }
}
