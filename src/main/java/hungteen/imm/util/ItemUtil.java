package hungteen.imm.util;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.common.rune.ICraftableRune;
import hungteen.imm.common.tag.IMMItemTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-10 20:44
 **/
public class ItemUtil {

    private static final String SUFFIX = "large_held";
    private static final List<RegistryObject<Item>> LARGE_HELD_ITEMS = new ArrayList<>();

    /**
     * {@link net.minecraft.world.item.enchantment.EnchantmentHelper#getEnchantments(ItemStack)}.
     */
    public static boolean hasEnchantment(ItemStack stack){
        return ! (stack.is(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantments(stack) : stack.getEnchantmentTags()).isEmpty();
    }
    public static ItemStack dyeArmor(Item item, List<DyeColor> colors){
        return DyeableLeatherItem.dyeArmor(new ItemStack(item), colors.stream().map(DyeItem::byColor).toList());
    }

    public static boolean isFood(ItemStack stack) {
        return stack.isEdible();
    }

    public static boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem;
    }

    public static boolean isArmor(ItemStack stack, EquipmentSlot slot) {
        return isArmor(stack) && ((ArmorItem) stack.getItem()).getEquipmentSlot() == slot;
    }

    public static boolean isShield(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    public static boolean isMeleeWeapon(ItemStack stack) {
        return stack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE) || stack.is(IMMItemTags.MELEE_ATTACK_ITEMS);
    }

    public static boolean isRangeWeapon(ItemStack stack) {
        return stack.getItem() instanceof ProjectileWeaponItem || stack.is(IMMItemTags.RANGE_ATTACK_ITEMS);
    }

    public static ResourceLocation getLargeHeldLocation(Item item){
        return StringHelper.suffix(ItemHelper.get().getKey(item), SUFFIX);
    }

    public static List<Pair<ICraftableRune, ItemStack>> getCraftableRunes(){
        return ItemHelper.get().filterValues(RuneItem.class::isInstance)
                .stream().map(RuneItem.class::cast)
                .map(item -> Pair.of(item.getRune(), new ItemStack(item))).toList();
    }

    public static Set<Map.Entry<ResourceKey<Item>, Item>> getBannerPatterns(){
        return Util.get().filterEntries(ItemHelper.get(), BannerPatternItem.class::isInstance);
    }

    public static List<Item> getSpawnEggs(){
        return Util.get().filterValues(ItemHelper.get(), SpawnEggItem.class::isInstance);
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
