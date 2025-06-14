package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.BiomeHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-03-24 23:11
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

    TagKey<Biome> CAN_SPAWN_MOB = tag("can_spawn_mob");
    TagKey<Biome> HAS_TELEPORT_RUIN = tag("has_structure/teleport_ruin");
    TagKey<Biome> HAS_PLAINS_TRADING_MARKET = tag("has_structure/plains_trading_market");
    TagKey<Biome> HAS_SPIRITUAL_FLAME_ALTAR = tag("has_structure/spiritual_flame_altar");
    TagKey<Biome> HAS_SPIRIT_LAB = tag("has_structure/spirit_lab");

    private static TagKey<Biome> tag(String name){
        return BiomeHelper.get().tag(Util.prefix(name));
    }

    private static TagKey<Biome> forgeTag(String name){
        return BiomeHelper.get().tag(Util.neo().prefix(name));
    }
}
