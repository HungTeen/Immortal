package hungteen.imm.common;

import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:17
 **/
public class ElixirManager {

    /**
     * used in item model gen.
     */
    public static ResourceLocation getOuterLayer(Rarity rarity){
        return rarity == Rarity.RARE ? Util.prefix("item/elixir_heaven_layer") :
                rarity == Rarity.UNCOMMON ? Util.prefix("item/elixir_earth_layer") :
                        Util.prefix("item/elixir_human_layer");
    }

}
