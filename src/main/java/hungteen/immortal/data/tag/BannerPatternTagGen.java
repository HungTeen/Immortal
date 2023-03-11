package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.registry.BannerPatternHelper;
import hungteen.immortal.common.misc.ImmortalBannerPatterns;
import hungteen.immortal.common.tag.ImmortalBannerPatternTags;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 20:15
 */
public class BannerPatternTagGen extends HTTagsProvider<BannerPattern> {
    public BannerPatternTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, BannerPatternHelper.get(), Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ImmortalBannerPatternTags.CONTINUOUS_MOUNTAIN).add(ImmortalBannerPatterns.CONTINUOUS_MOUNTAIN.get());
        this.tag(ImmortalBannerPatternTags.FLOWING_CLOUD).add(ImmortalBannerPatterns.FLOWING_CLOUD.get());
        this.tag(ImmortalBannerPatternTags.FOLDED_THUNDER).add(ImmortalBannerPatterns.FOLDED_THUNDER.get());
        this.tag(ImmortalBannerPatternTags.RHOMBUS).add(ImmortalBannerPatterns.RHOMBUS.get());
    }

}
