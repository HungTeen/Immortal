package hungteen.imm.common.item;

import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/8 19:51
 */
public interface IMMBannerPatterns {

    ResourceKey<BannerPattern> CONTINUOUS_MOUNTAIN = create("continuous_mountain");
    ResourceKey<BannerPattern> FLOWING_CLOUD = create("flowing_cloud");
    ResourceKey<BannerPattern> FOLDED_THUNDER = create("folded_thunder");
    ResourceKey<BannerPattern> RHOMBUS = create("rhombus");
    ResourceKey<BannerPattern> TALISMAN = create("talisman");
    ResourceKey<BannerPattern> COILED_LOONG = create("coiled_loong");
    ResourceKey<BannerPattern> HOVERING_PHOENIX = create("hovering_phoenix");

    static void register(BootstrapContext<BannerPattern> context){
        register(context, CONTINUOUS_MOUNTAIN);
        register(context, FLOWING_CLOUD);
        register(context, FOLDED_THUNDER);
        register(context, RHOMBUS);
        register(context, TALISMAN);
        register(context, COILED_LOONG);
        register(context, HOVERING_PHOENIX);
    }

    private static void register(BootstrapContext<BannerPattern> context, ResourceKey<BannerPattern> key) {
        // TODO translate key.
        context.register(key, new BannerPattern(key.location(), key.location().getPath()));
    }

    static ResourceKey<BannerPattern> create(String name) {
        return BlockHelper.banner().createKey(Util.prefix(name));
    }

}
