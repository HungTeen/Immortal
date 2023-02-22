package hungteen.immortal.common;

import hungteen.htlib.util.Pair;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.tag.ImmortalItemTags;
import hungteen.immortal.common.impl.SpiritualTypes;
import hungteen.immortal.utils.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:17
 **/
public class ElixirManager {

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void init(){
        registerElixirIngredients();
    }

    private static void registerElixirIngredients(){
        /* Common Flowers */
        List.of(
                Items.BLUE_ORCHID, Items.ALLIUM, Items.AZURE_BLUET, Items.RED_TULIP, Items.ORANGE_TULIP, Items.WHITE_TULIP, Items.PINK_TULIP,
                Items.OXEYE_DAISY, Items.CORNFLOWER, Items.LILY_OF_THE_VALLEY
        ).forEach(item -> {
            ImmortalAPI.get().registerElixirIngredient(item, Map.of(
                    SpiritualTypes.WOOD, 1, SpiritualTypes.EARTH, 1
            ));
        });
        List.of(
                /* Flowers */
                Pair.of(Items.DANDELION, Map.of(
                        SpiritualTypes.WOOD, 1, SpiritualTypes.EARTH, 1, SpiritualTypes.WIND, 2
                )), // 蒲公英。
                Pair.of(Items.POPPY, Map.of(
                        SpiritualTypes.WOOD, 1, SpiritualTypes.EARTH, 1, SpiritualTypes.DRUG, 2
                )), // 虞美人。
                Pair.of(Items.BLUE_ORCHID, Map.of(
                        SpiritualTypes.WATER, 1, SpiritualTypes.EARTH, 1
                )), // 兰花。
                Pair.of(Items.WITHER_ROSE, Map.of(
                        SpiritualTypes.METAL, 3, SpiritualTypes.WOOD, -10, SpiritualTypes.WATER, -5, SpiritualTypes.FIRE, 5, SpiritualTypes.EARTH, 4, SpiritualTypes.DRUG, 6
                )),
                Pair.of(Items.SUNFLOWER, Map.of(
                        SpiritualTypes.FIRE, 10, SpiritualTypes.WOOD, 10
                )),
                Pair.of(Items.LILAC, Map.of(
                        SpiritualTypes.WOOD, 10
                )), //丁香。
                Pair.of(Items.ROSE_BUSH, Map.of(
                        SpiritualTypes.WOOD, 10
                )),
                Pair.of(Items.PEONY, Map.of(
                        SpiritualTypes.WOOD, 10
                )), // 牡丹。
                Pair.of(Items.SPORE_BLOSSOM, Map.of(
                        SpiritualTypes.WATER, 10
                )), // 孢子花。

                /* Bushes */
                Pair.of(Items.BROWN_MUSHROOM, Map.of(
                        SpiritualTypes.METAL, 1, SpiritualTypes.WOOD, -1, SpiritualTypes.EARTH, 2
                )),
                Pair.of(Items.RED_MUSHROOM, Map.of(
                        SpiritualTypes.METAL, 1, SpiritualTypes.WOOD, -2, SpiritualTypes.EARTH, 1, SpiritualTypes.DRUG, 2
                )),
                Pair.of(Items.CRIMSON_FUNGUS, Map.of(
                        SpiritualTypes.WOOD, -2, SpiritualTypes.WATER, -2, SpiritualTypes.FIRE, 3, SpiritualTypes.EARTH, 2
                )), // 绯红菌。
                Pair.of(Items.WARPED_FUNGUS, Map.of(
                        SpiritualTypes.WOOD, -2, SpiritualTypes.WATER, 3, SpiritualTypes.FIRE, -2, SpiritualTypes.EARTH, 2
                )), // 诡异菌。
                Pair.of(Items.SUGAR_CANE, Map.of(
                        SpiritualTypes.WOOD, 2, SpiritualTypes.WATER, 2, SpiritualTypes.EARTH, 1
                )),
                Pair.of(Items.KELP, Map.of(
                        SpiritualTypes.WOOD, 2, SpiritualTypes.WATER, -1
                )),
                Pair.of(Items.BAMBOO, Map.of(
                        SpiritualTypes.METAL, 2, SpiritualTypes.WOOD, 1, SpiritualTypes.EARTH, 1
                )),
                Pair.of(Items.CACTUS, Map.of(
                        SpiritualTypes.METAL, 3, SpiritualTypes.WOOD, -1, SpiritualTypes.WATER, -2, SpiritualTypes.FIRE, 2
                )),
                Pair.of(Items.TALL_GRASS, Map.of(
                        SpiritualTypes.WOOD, - 2, SpiritualTypes.EARTH, 3
                )),

                /* Stones */
                Pair.of(Items.COAL, Map.of(
                        SpiritualTypes.METAL, 2, SpiritualTypes.WOOD, -1, SpiritualTypes.FIRE, 2
                )),
                Pair.of(Items.COAL, Map.of(
                        SpiritualTypes.METAL, 2, SpiritualTypes.WOOD, -2, SpiritualTypes.FIRE, 3
                )),
                Pair.of(Items.COPPER_INGOT, Map.of(
                        SpiritualTypes.METAL, 2, SpiritualTypes.ELECTRIC, 1
                )),
                Pair.of(Items.IRON_INGOT, Map.of(
                        SpiritualTypes.METAL, 2, SpiritualTypes.EARTH, 1
                )),
                Pair.of(Items.GOLD_INGOT, Map.of(
                        SpiritualTypes.METAL, 3, SpiritualTypes.FIRE, 1
                )),
                Pair.of(Items.DIAMOND, Map.of(
                        SpiritualTypes.METAL, 2, SpiritualTypes.FIRE, 2
                ))

        ).forEach(pair -> {
            ImmortalAPI.get().registerElixirIngredient(pair.getFirst(), pair.getSecond());
        });

    }

    public static Collection<Pair<ISpiritualType, Integer>> getElixirIngredient(ItemStack stack){
        return ImmortalAPI.get().getElixirIngredient(stack.getItem()).entrySet().stream().map(entry -> {
            return Pair.of(entry.getKey(), entry.getValue());
        }).toList();
    }

    /**
     * used in item model gen.
     */
    public static ResourceLocation getOuterLayer(Rarity rarity){
        return rarity == Rarity.RARE ? Util.prefix("item/elixir_heaven_layer") :
                rarity == Rarity.UNCOMMON ? Util.prefix("item/elixir_earth_layer") :
                        Util.prefix("item/elixir_human_layer");
    }

    public static boolean isElixirIngredient(ItemStack itemStack) {
        return itemStack.is(ImmortalItemTags.ELIXIR_INGREDIENTS) || ImmortalAPI.get().getElixirIngredients().contains(itemStack.getItem());
    }

}
