package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.common.misc.IMMBannerPatterns;
import hungteen.imm.common.tag.IMMBannerPatternTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/8 20:15
 */
public class BannerPatternTagGen extends HTTagsProvider<BannerPattern> {

    public BannerPatternTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper fileHelper) {
        super(output, provider, BlockHelper.banner(), Util.id(), fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(IMMBannerPatternTags.CONTINUOUS_MOUNTAIN).add(IMMBannerPatterns.CONTINUOUS_MOUNTAIN);
        this.tag(IMMBannerPatternTags.FLOWING_CLOUD).add(IMMBannerPatterns.FLOWING_CLOUD);
        this.tag(IMMBannerPatternTags.FOLDED_THUNDER).add(IMMBannerPatterns.FOLDED_THUNDER);
        this.tag(IMMBannerPatternTags.RHOMBUS).add(IMMBannerPatterns.RHOMBUS);
        this.tag(IMMBannerPatternTags.TALISMAN).add(IMMBannerPatterns.TALISMAN);
        this.tag(IMMBannerPatternTags.COILED_LOONG).add(IMMBannerPatterns.COILED_LOONG);
        this.tag(IMMBannerPatternTags.HOVERING_PHOENIX).add(IMMBannerPatterns.HOVERING_PHOENIX);
    }

}
