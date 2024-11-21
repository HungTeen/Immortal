package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/8 20:45
 */
public interface IMMBannerPatternTags {

    TagKey<BannerPattern> CONTINUOUS_MOUNTAIN = tag("continuous_mountain");
    TagKey<BannerPattern> FLOWING_CLOUD = tag("flowing_cloud");
    TagKey<BannerPattern> FOLDED_THUNDER = tag("folded_thunder");
    TagKey<BannerPattern> RHOMBUS = tag("rhombus");
    TagKey<BannerPattern> TALISMAN = tag("talisman");
    TagKey<BannerPattern> COILED_LOONG = tag("coiled_loong");
    TagKey<BannerPattern> HOVERING_PHOENIX = tag("hovering_phoenix");

    private static TagKey<BannerPattern> tag(String name){
        return create(Util.prefix(name));
    }
    
    private static TagKey<BannerPattern> create(ResourceLocation location) {
        return BlockHelper.banner().tag(location);
    }
}
