package hungteen.imm.common.tag;

import hungteen.imm.util.Util;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 13:08
 **/
public interface IMMItemTags {

    /* forge */

    TagKey<Item> RICE_SEEDS = forgeTag("seeds/rice");
    TagKey<Item> JUTE_SEEDS = forgeTag("seeds/jute");
    TagKey<Item> JUTE = forgeTag("crops/jute");
    TagKey<Item> SPIRITUAL_STONES = forgeTag("spiritual_stones");
    TagKey<Item> SPIRITUAL_STONES_LEVEL_ONE = forgeTag("spiritual_stone/level_one_stones");
//    TagKey<Item> SPIRITUAL_STONES_LEVEL_TWO = forgeTag("spiritual_stone/level_two_stones");
    TagKey<Item> COMMON_ARTIFACTS = forgeTag("artifacts/common");
    TagKey<Item> MODERATE_ARTIFACTS = forgeTag("artifacts/moderate");
    TagKey<Item> ADVANCED_ARTIFACTS = forgeTag("artifacts/advanced");

    TagKey<Item> CINNABAR_GEMS = forgeTag("gems/cinnabar");
    TagKey<Item> CINNABAR_ORES = forgeTag("ores/cinnabar");

    /* immortal */

    /**
     * Determine whether brain of living can use this to melee attack.
     */
    TagKey<Item> MELEE_ATTACK_ITEMS = tag("melee_attack_items");
    TagKey<Item> RANGE_ATTACK_ITEMS = tag("range_attack_items");

    private static TagKey<Item> tag(String name){
        return ItemTags.create(Util.prefix(name));
    }

    private static TagKey<Item> forgeTag(String name){
        return ItemTags.create(Util.forge().prefix(name));
    }

}
