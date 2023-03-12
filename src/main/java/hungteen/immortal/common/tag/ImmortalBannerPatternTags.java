package hungteen.immortal.common.tag;

import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 20:45
 */
public class ImmortalBannerPatternTags {

    public static final TagKey<BannerPattern> CONTINUOUS_MOUNTAIN = tag("continuous_mountain");
    public static final TagKey<BannerPattern> FLOWING_CLOUD = tag("flowing_cloud");
    public static final TagKey<BannerPattern> FOLDED_THUNDER = tag("folded_thunder");
    public static final TagKey<BannerPattern> RHOMBUS = tag("rhombus");
    
    private static TagKey<BannerPattern> tag(String name){
        return create(Util.prefix(name));
    }
    
    private static TagKey<BannerPattern> create(ResourceLocation location) {
        return TagKey.create(Registry.BANNER_PATTERN_REGISTRY, location);
    }
}