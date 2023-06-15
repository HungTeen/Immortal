package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-24 23:11
 **/
public class IMMBiomeTags {

    public static final TagKey<Biome> HAS_TELEPORT_RUIN = tag("has_structure/teleport_ruin");
    public static final TagKey<Biome> HAS_TRADING_MARKET = tag("has_structure/overworld_trading_market");

    private static TagKey<Biome> tag(String name){
        return BiomeHelper.get().tag(Util.prefix(name));
    }
}
