package hungteen.immortal.common;

import hungteen.htlib.util.ItemUtil;
import hungteen.htlib.util.Pair;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IElixirItem;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.common.tag.ImmortalItemTags;
import hungteen.immortal.utils.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:17
 **/
public class ElixirManager {

    private static final List<IElixirItem> ELIXIRS = new ArrayList<>();

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void init(){
        ELIXIRS.clear();
        ELIXIRS.addAll(ItemUtil.getFilterItems(item -> item instanceof IElixirItem).stream().map(item -> (IElixirItem) item).toList());
        registerElixirIngredients();
    }

    private static void registerElixirIngredients(){
        /* Common Flowers */
        List.of(
                Items.BLUE_ORCHID, Items.ALLIUM, Items.AZURE_BLUET, Items.RED_TULIP, Items.ORANGE_TULIP, Items.WHITE_TULIP, Items.PINK_TULIP,
                Items.OXEYE_DAISY, Items.CORNFLOWER, Items.LILY_OF_THE_VALLEY
        ).forEach(item -> {
            ImmortalAPI.get().registerElixirIngredient(item, Map.of(
                    SpiritualRoots.WOOD, 1, SpiritualRoots.EARTH, 1
            ));
        });
        List.of(
                /* Flowers */
                Pair.of(Items.DANDELION, Map.of(
                        SpiritualRoots.WOOD, 1, SpiritualRoots.EARTH, 1, SpiritualRoots.WIND, 2
                )), // 蒲公英。
                Pair.of(Items.POPPY, Map.of(
                        SpiritualRoots.WOOD, 1, SpiritualRoots.EARTH, 1, SpiritualRoots.DRUG, 2
                )), // 虞美人。
                Pair.of(Items.BLUE_ORCHID, Map.of(
                        SpiritualRoots.WATER, 1, SpiritualRoots.EARTH, 1
                )), // 兰花。
                Pair.of(Items.WITHER_ROSE, Map.of(
                        SpiritualRoots.METAL, 3, SpiritualRoots.WOOD, -10, SpiritualRoots.WATER, -5, SpiritualRoots.FIRE, 5, SpiritualRoots.EARTH, 4, SpiritualRoots.DRUG, 6
                )),
                Pair.of(Items.SUNFLOWER, Map.of(
                        SpiritualRoots.FIRE, 10, SpiritualRoots.WOOD, 10
                )),
                Pair.of(Items.LILAC, Map.of(
                        SpiritualRoots.WOOD, 10
                )), //丁香。
                Pair.of(Items.ROSE_BUSH, Map.of(
                        SpiritualRoots.WOOD, 10
                )),
                Pair.of(Items.PEONY, Map.of(
                        SpiritualRoots.WOOD, 10
                )), // 牡丹。
                Pair.of(Items.SPORE_BLOSSOM, Map.of(
                        SpiritualRoots.WATER, 10
                )), // 孢子花。

                /* Bushes */
                Pair.of(Items.BROWN_MUSHROOM, Map.of(
                        SpiritualRoots.METAL, 1, SpiritualRoots.WOOD, -1, SpiritualRoots.EARTH, 2
                )),
                Pair.of(Items.RED_MUSHROOM, Map.of(
                        SpiritualRoots.METAL, 1, SpiritualRoots.WOOD, -2, SpiritualRoots.EARTH, 1, SpiritualRoots.DRUG, 2
                )),
                Pair.of(Items.CRIMSON_FUNGUS, Map.of(
                        SpiritualRoots.WOOD, -2, SpiritualRoots.WATER, -2, SpiritualRoots.FIRE, 3, SpiritualRoots.EARTH, 2
                )), // 绯红菌。
                Pair.of(Items.WARPED_FUNGUS, Map.of(
                        SpiritualRoots.WOOD, -2, SpiritualRoots.WATER, 3, SpiritualRoots.FIRE, -2, SpiritualRoots.EARTH, 2
                )), // 诡异菌。
                Pair.of(Items.SUGAR_CANE, Map.of(
                        SpiritualRoots.WOOD, 2, SpiritualRoots.WATER, 2, SpiritualRoots.EARTH, 1
                )),
                Pair.of(Items.KELP, Map.of(
                        SpiritualRoots.WOOD, 2, SpiritualRoots.WATER, -1
                )),
                Pair.of(Items.BAMBOO, Map.of(
                        SpiritualRoots.METAL, 2, SpiritualRoots.WOOD, 1, SpiritualRoots.EARTH, 1
                )),
                Pair.of(Items.CACTUS, Map.of(
                        SpiritualRoots.METAL, 3, SpiritualRoots.WOOD, -1, SpiritualRoots.WATER, -2, SpiritualRoots.FIRE, 2
                )),
                Pair.of(Items.TALL_GRASS, Map.of(
                        SpiritualRoots.WOOD, - 2, SpiritualRoots.EARTH, 3
                )),

                /* Stones */
                Pair.of(Items.COAL, Map.of(
                        SpiritualRoots.METAL, 2, SpiritualRoots.WOOD, -1, SpiritualRoots.FIRE, 2
                )),
                Pair.of(Items.COAL, Map.of(
                        SpiritualRoots.METAL, 2, SpiritualRoots.WOOD, -2, SpiritualRoots.FIRE, 3
                )),
                Pair.of(Items.COPPER_INGOT, Map.of(
                        SpiritualRoots.METAL, 2, SpiritualRoots.ELECTRIC, 1
                )),
                Pair.of(Items.IRON_INGOT, Map.of(
                        SpiritualRoots.METAL, 2, SpiritualRoots.EARTH, 1
                )),
                Pair.of(Items.GOLD_INGOT, Map.of(
                        SpiritualRoots.METAL, 3, SpiritualRoots.FIRE, 1
                )),
                Pair.of(Items.DIAMOND, Map.of(
                        SpiritualRoots.METAL, 2, SpiritualRoots.FIRE, 2
                ))

        ).forEach(pair -> {
            ImmortalAPI.get().registerElixirIngredient(pair.getFirst(), pair.getSecond());
        });

    }

    public static int getElixirTypeId(IElixirItem item){
        return ELIXIRS.indexOf(item);
    }

    public static IElixirItem getElixirTypeId(int id){
        return ELIXIRS.get(id);
    }

    public static Collection<Pair<ISpiritualRoot, Integer>> getElixirIngredient(ItemStack stack){
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
