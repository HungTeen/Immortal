package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTHolderTagsProvider;
import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.registry.BannerPatternHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.misc.ImmortalBannerPatterns;
import hungteen.imm.common.tag.ImmortalBannerPatternTags;
import hungteen.imm.utils.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.tags.BannerPatternTags;
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
        super(output, provider, BannerPatternHelper.get(), Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ImmortalBannerPatternTags.CONTINUOUS_MOUNTAIN).add(ImmortalBannerPatterns.CONTINUOUS_MOUNTAIN.get());
        this.tag(ImmortalBannerPatternTags.FLOWING_CLOUD).add(ImmortalBannerPatterns.FLOWING_CLOUD.get());
        this.tag(ImmortalBannerPatternTags.FOLDED_THUNDER).add(ImmortalBannerPatterns.FOLDED_THUNDER.get());
        this.tag(ImmortalBannerPatternTags.RHOMBUS).add(ImmortalBannerPatterns.RHOMBUS.get());
    }

}
