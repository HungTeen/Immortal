package hungteen.immortal.data.tag;

import hungteen.immortal.common.misc.ImmortalBannerPatterns;
import hungteen.immortal.common.tag.ImmortalBlockPatternTags;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.tags.BannerPatternTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 20:15
 */
public class BannerPatternTagGen extends BannerPatternTagsProvider {
    public BannerPatternTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ImmortalBlockPatternTags.CONTINUOUS_MOUNTAIN).add(ImmortalBannerPatterns.CONTINUOUS_MOUNTAIN.get());
        this.tag(ImmortalBlockPatternTags.FLOWING_CLOUD).add(ImmortalBannerPatterns.FLOWING_CLOUD.get());
        this.tag(ImmortalBlockPatternTags.FOLDED_THUNDER).add(ImmortalBannerPatterns.FOLDED_THUNDER.get());
        this.tag(ImmortalBlockPatternTags.RHOMBUS).add(ImmortalBannerPatterns.RHOMBUS.get());
    }

    @Override
    public String getName() {
        return "PPP HT";
    }
}
