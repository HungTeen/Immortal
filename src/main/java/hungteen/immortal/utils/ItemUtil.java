package hungteen.immortal.utils;

import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
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

    public static boolean isFood(ItemStack stack) {
        return stack.isEdible();
    }

    public static boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem;
    }

    public static boolean isArmor(ItemStack stack, EquipmentSlot slot) {
        return isArmor(stack) && ((ArmorItem) stack.getItem()).getSlot() == slot;
    }

    public static boolean isShield(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    public static boolean isMeleeWeapon(ItemStack stack) {
        return stack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE) || stack.is(ImmortalItemTags.MELEE_ATTACK_ITEMS);
    }

    public static boolean isRangeWeapon(ItemStack stack) {
        return stack.getItem() instanceof ProjectileWeaponItem || stack.is(ImmortalItemTags.RANGE_ATTACK_ITEMS);
    }

    public static ResourceLocation getLargeHeldLocation(Item item){
        return Util.suffix(ItemHelper.get().getKey(item), SUFFIX);
    }

    /**
     * {@link ImmortalMod#coreRegister()}
     */
//    public static void registerLargeHeldItems(){
//        List.of(
//                ImmortalItems.BRONZE_SWORD
//        ).forEach(ItemUtil::registerLargeHeldItem);
//    }

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
