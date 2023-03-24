package hungteen.immortal.common.tag;

import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.immortal.utils.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-24 23:11
 **/
public class ImmortalBiomeTags {

    public static final TagKey<Biome> HAS_OVERWORLD_TRADING_MARKET = tag("has_structure/overworld_trading_market");

    private static TagKey<Biome> tag(String name){
        return BiomeHelper.get().tag(Util.prefix(name));
    }
}
