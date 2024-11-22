package hungteen.imm.common.world.levelgen;

import hungteen.htlib.util.helper.impl.LevelHelper;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/29 15:05
 */
public interface IMMDimensionTypes {

    ResourceKey<DimensionType> EAST_WORLD = create("east_world");
    ResourceKey<DimensionType> SPIRIT_WORLD = create("spirit_world");

    ResourceLocation SPIRIT_WORLD_EFFECTS = Util.prefix("spirit_world");

    static void register(BootstrapContext<DimensionType> context){
        context.register(EAST_WORLD, new DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0D,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                0.0F,
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
        ));
        context.register(SPIRIT_WORLD, new DimensionType(
                OptionalLong.empty(),
                false,
                false,
                false,
                true,
                1.0D,
                false,
                false,
                0,
                256,
                256,
                BlockTags.INFINIBURN_END,
                SPIRIT_WORLD_EFFECTS,
                0.0F,
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
        ));
    }

    private static ResourceKey<DimensionType> create(String name) {
        return LevelHelper.type().createKey(Util.prefix(name));
    }
}
