package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/2 17:06
 */
public interface IMMBlockTags {

    /* Minecraft */

    /* Uniform */


    TagKey<Block> SPIRITUAL_ORES = uniformTag("spiritual_ores");
    TagKey<Block> CINNABAR_ORES = uniformTag("ores/cinnabar");

    TagKey<Block> GOURDS = uniformTag("gourds");
    TagKey<Block> COPPER_BLOCKS = uniformTag("copper_blocks");
    TagKey<Block> COPPER_SLABS = uniformTag("copper_slabs");

    /* IMM */

    TagKey<Block> METAL_ELEMENT_ATTACHED_BLOCKS = tag("metal_element_attached_blocks");
    TagKey<Block> WOOD_ELEMENT_ATTACHED_BLOCKS = tag("wood_element_attached_blocks");
    TagKey<Block> WATER_ELEMENT_ATTACHED_BLOCKS = tag("water_element_attached_blocks");
    TagKey<Block> FIRE_ELEMENT_ATTACHED_BLOCKS = tag("fire_element_attached_blocks");
    TagKey<Block> EARTH_ELEMENT_ATTACHED_BLOCKS = tag("earth_element_attached_blocks");
    TagKey<Block> SPIRIT_ELEMENT_ATTACHED_BLOCKS = tag("spirit_element_attached_blocks");

    TagKey<Block> COMMON_ARTIFACTS = tag("artifacts/common");
    TagKey<Block> MODERATE_ARTIFACTS = tag("artifacts/moderate");
    TagKey<Block> ADVANCED_ARTIFACTS = tag("artifacts/advanced");

    TagKey<Block> FUNCTIONAL_COPPERS = tag("functional_coppers");
    TagKey<Block> COPPER_INTERFACES = tag("copper_interfaces");
    TagKey<Block> FURNACE_BLOCKS = tag("furnace_blocks");

    private static TagKey<Block> tag(String name){
        return BlockHelper.get().tag(Util.prefix(name));
    }

    private static TagKey<Block> uniformTag(String name){
        return BlockHelper.get().uniformTag(name);
    }
}
