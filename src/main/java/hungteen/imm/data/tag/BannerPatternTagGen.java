package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTHolderTagsProvider;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.imm.common.misc.IMMBannerPatterns;
import hungteen.imm.common.tag.IMMBannerPatternTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/8 20:15
 */
public class BannerPatternTagGen extends HTHolderTagsProvider<BannerPattern> {

    public BannerPatternTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BlockHelper.banner(), Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(IMMBannerPatternTags.CONTINUOUS_MOUNTAIN).add(IMMBannerPatterns.CONTINUOUS_MOUNTAIN.get());
        this.tag(IMMBannerPatternTags.FLOWING_CLOUD).add(IMMBannerPatterns.FLOWING_CLOUD.get());
        this.tag(IMMBannerPatternTags.FOLDED_THUNDER).add(IMMBannerPatterns.FOLDED_THUNDER.get());
        this.tag(IMMBannerPatternTags.RHOMBUS).add(IMMBannerPatterns.RHOMBUS.get());
        this.tag(IMMBannerPatternTags.TALISMAN).add(IMMBannerPatterns.TALISMAN.get());
        this.tag(IMMBannerPatternTags.COILED_LOONG).add(IMMBannerPatterns.COILED_LOONG.get());
        this.tag(IMMBannerPatternTags.HOVERING_PHOENIX).add(IMMBannerPatterns.HOVERING_PHOENIX.get());
    }

}
