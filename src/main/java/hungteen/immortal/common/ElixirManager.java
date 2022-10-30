package hungteen.immortal.common;

import hungteen.htlib.util.ItemUtil;
import hungteen.htlib.util.Pair;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.enums.ElixirRarity;
import hungteen.immortal.api.interfaces.IElixirItem;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.common.tag.ImmortalItemTags;
import hungteen.immortal.utils.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;
import java.util.stream.Collectors;

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
        List.of(
                Pair.of(Items.SUNFLOWER, Map.of(
                        SpiritualRoots.FIRE, 10,
                        SpiritualRoots.WOOD, 10
                )),
                Pair.of(Items.LILAC, Map.of( //丁香。
                        SpiritualRoots.WOOD, 10
                )),
                Pair.of(Items.ROSE_BUSH, Map.of(
                        SpiritualRoots.WOOD, 10
                )),
                Pair.of(Items.PEONY, Map.of( //牡丹。
                        SpiritualRoots.WOOD, 10
                )),
                Pair.of(Items.SPORE_BLOSSOM, Map.of(
                        SpiritualRoots.WATER, 10
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
        return rarity == ElixirRarity.HEAVEN ? Util.prefix("item/elixir_heaven_layer") :
                rarity == ElixirRarity.EARTH ? Util.prefix("item/elixir_earth_layer") :
                        Util.prefix("item/elixir_human_layer");
    }

    public static boolean isElixirIngredient(ItemStack itemStack) {
        return itemStack.is(ImmortalItemTags.ELIXIR_INGREDIENTS) || ImmortalAPI.get().getElixirIngredients().contains(itemStack.getItem());
    }

}
