package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-24 23:11
 **/
public interface IMMBiomeTags {

    /* Forge */

    TagKey<Biome> HAS_BIRCH_TREE = tag("has_birch_tree");
    TagKey<Biome> HAS_JUNGLE_TREE = tag("has_jungle_tree");
    TagKey<Biome> HAS_DARK_OAK_TREE = tag("has_dark_oak_tree");
    TagKey<Biome> HAS_ACACIA_TREE = tag("has_acacia_tree");
    TagKey<Biome> HAS_SPRUCE_TREE = tag("has_spruce_tree");
    TagKey<Biome> HAS_CHERRY_TREE = tag("has_cherry_tree");

    /* IMM */

    TagKey<Biome> HAS_TELEPORT_RUIN = tag("has_structure/teleport_ruin");
    TagKey<Biome> HAS_PLAINS_TRADING_MARKET = tag("has_structure/plains_trading_market");

    private static TagKey<Biome> tag(String name){
        return BiomeHelper.get().tag(Util.prefix(name));
    }

    private static TagKey<Biome> forgeTag(String name){
        return BiomeHelper.get().tag(Util.forge().prefix(name));
    }
}
