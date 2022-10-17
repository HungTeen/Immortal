package hungteen.immortal;

import hungteen.htlib.util.Pair;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.tag.ImmortalItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Collections;
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

    public static boolean isElixirIngredient(ItemStack itemStack) {
        return itemStack.is(ImmortalItemTags.ELIXIR_INGREDIENTS);
    }

}
