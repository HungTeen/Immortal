package hungteen.immortal.common;

import hungteen.htlib.util.Pair;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IElixirType;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:17
 **/
public class ElixirManager {

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerElixirIngredients(){
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
                ))

        ).forEach(pair -> {
            ImmortalAPI.get().registerElixirIngredient(pair.getFirst(), pair.getSecond());
        });

    }

    public static int getElixirTypeId(@Nonnull IElixirType type){
        return ImmortalAPI.get().getElixirTypes().indexOf(type);
    }

    public static IElixirType getElixirTypeId(int id){
        return ImmortalAPI.get().getElixirTypes().get(id);
    }

    public static Collection<Pair<ISpiritualRoot, Integer>> getElixirIngredient(ItemStack stack){
        return ImmortalAPI.get().getElixirIngredient(stack.getItem()).entrySet().stream().map(entry -> {
            return Pair.of(entry.getKey(), entry.getValue());
        }).toList();
    }

    public static boolean isElixirIngredient(ItemStack itemStack) {
        return itemStack.is(ImmortalItemTags.ELIXIR_INGREDIENTS) || ImmortalAPI.get().getElixirIngredients().contains(itemStack.getItem());
    }

}
