package hungteen.imm.common.tag;

import hungteen.imm.util.Util;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/2 17:06
 */
public interface IMMBlockTags {

    /* Forge */

    TagKey<Block> METAL_ELEMENT_ATTACHED_BLOCKS = forgeTag("metal_element_attached_blocks");
    TagKey<Block> WOOD_ELEMENT_ATTACHED_BLOCKS = forgeTag("wood_element_attached_blocks");
    TagKey<Block> WATER_ELEMENT_ATTACHED_BLOCKS = forgeTag("water_element_attached_blocks");
    TagKey<Block> FIRE_ELEMENT_ATTACHED_BLOCKS = forgeTag("fire_element_attached_blocks");
    TagKey<Block> EARTH_ELEMENT_ATTACHED_BLOCKS = forgeTag("earth_element_attached_blocks");
    TagKey<Block> SPIRIT_ELEMENT_ATTACHED_BLOCKS = forgeTag("spirit_element_attached_blocks");


    TagKey<Block> SPIRITUAL_ORES = forgeTag("spiritual_ores");
    TagKey<Block> CINNABAR_ORES = forgeTag("ores/cinnabar");

    TagKey<Block> COMMON_ARTIFACTS = forgeTag("artifacts/common");
    TagKey<Block> MODERATE_ARTIFACTS = forgeTag("artifacts/moderate");
    TagKey<Block> ADVANCED_ARTIFACTS = forgeTag("artifacts/advanced");

    /* IMM */

    TagKey<Block> GOURDS = tag("gourds");
    TagKey<Block> COPPER_BLOCKS = tag("copper_blocks");
    TagKey<Block> COPPER_SLABS = tag("copper_slabs");
    TagKey<Block> FUNCTIONAL_COPPERS = tag("functional_coppers");
    TagKey<Block> COPPER_INTERFACES = tag("copper_interfaces");
    TagKey<Block> FURNACE_BLOCKS = tag("furnace_blocks");

    private static TagKey<Block> tag(String name){
        return BlockTags.create(Util.prefix(name));
    }

    private static TagKey<Block> forgeTag(String name){
        return BlockTags.create(Util.forge().prefix(name));
    }
}
