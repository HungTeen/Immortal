package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.common.misc.IMMBannerPatterns;
import hungteen.imm.common.tag.IMMBannerPatternTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 20:15
 */
public class BannerPatternTagGen extends HTTagsProvider<BannerPattern> {

    public BannerPatternTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BlockHelper.banner());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.addTag(IMMBannerPatternTags.CONTINUOUS_MOUNTAIN, IMMBannerPatterns.CONTINUOUS_MOUNTAIN.getRegistryName());
        this.addTag(IMMBannerPatternTags.FLOWING_CLOUD, IMMBannerPatterns.FLOWING_CLOUD.getRegistryName());
        this.addTag(IMMBannerPatternTags.FOLDED_THUNDER, IMMBannerPatterns.FOLDED_THUNDER.getRegistryName());
        this.addTag(IMMBannerPatternTags.RHOMBUS, IMMBannerPatterns.RHOMBUS.getRegistryName());
        this.addTag(IMMBannerPatternTags.TALISMAN, IMMBannerPatterns.TALISMAN.getRegistryName());
        this.addTag(IMMBannerPatternTags.COILED_LOONG, IMMBannerPatterns.COILED_LOONG.getRegistryName());
        this.addTag(IMMBannerPatternTags.HOVERING_PHOENIX, IMMBannerPatterns.HOVERING_PHOENIX.getRegistryName());
    }

}
