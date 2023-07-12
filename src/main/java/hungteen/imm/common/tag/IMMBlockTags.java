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
public class IMMBlockTags {

    /* Forge */

    public static final TagKey<Block> SPIRITUAL_ORES = forgeTag("spiritual_ores");
    public static final TagKey<Block> CINNABAR_ORES = forgeTag("ores/cinnabar");

    /* IMM */

    public static final TagKey<Block> COPPER_BLOCKS = tag("copper_blocks");
    public static final TagKey<Block> COPPER_SLABS = tag("copper_slabs");
    public static final TagKey<Block> FUNCTIONAL_COPPERS = tag("functional_coppers");
    public static final TagKey<Block> FURNACE_BLOCKS = tag("furnace_blocks");

    private static TagKey<Block> tag(String name){
        return BlockTags.create(Util.prefix(name));
    }

    private static TagKey<Block> forgeTag(String name){
        return BlockTags.create(Util.forgePrefix(name));
    }
}
