package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-16 13:08
 **/
public interface IMMItemTags {

    /* Uniform */

    TagKey<Item> RICE_SEEDS = uniformTag("seeds/rice");
    TagKey<Item> JUTE_SEEDS = uniformTag("seeds/jute");
    TagKey<Item> JUTE = uniformTag("crops/jute");

    TagKey<Item> CINNABAR_GEMS = uniformTag("gems/cinnabar");
    TagKey<Item> CINNABAR_ORES = uniformTag("ores/cinnabar");

    /**
     * Determine whether brain of living can use this to melee attack.
     */
    TagKey<Item> MELEE_ATTACK_ITEMS = uniformTag("melee_attack_items");
    TagKey<Item> RANGE_ATTACK_ITEMS = uniformTag("range_attack_items");

    /* IMM */

    TagKey<Item> SPIRITUAL_STONES = tag("spiritual_stones");
    TagKey<Item> SPIRITUAL_STONES_LEVEL_ONE = tag("spiritual_stone/level_one_stones");
    //    TagKey<Item> SPIRITUAL_STONES_LEVEL_TWO = uniformTag("spiritual_stone/level_two_stones");

    TagKey<Item> COMMON_ARTIFACTS = tag("artifacts/common");
    TagKey<Item> MODERATE_ARTIFACTS = tag("artifacts/moderate");
    TagKey<Item> ADVANCED_ARTIFACTS = tag("artifacts/advanced");

    private static TagKey<Item> tag(String name){
        return ItemHelper.get().tag(Util.prefix(name));
    }

    private static TagKey<Item> uniformTag(String name){
        return ItemHelper.get().uniformTag(name);
    }

}
