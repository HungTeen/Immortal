package hungteen.imm.common.world.structure.structures;

import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * 浮屠塔用 Jigsaw 实现难点较多，比如无法解决旋转问题。<br>
 * 故而暂时改成了使用少量硬编码搭配结构方块加载的方式实现。<br>
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/24 14:51
 **/
public class SpiritPagoda {

//    ResourceKey<StructureTemplatePool> SPIRIT_PAGODA_LEVEL_1 = create("level_1");
//    ResourceKey<StructureTemplatePool> SPIRIT_PAGODA_LEVEL_2 = create("level_2");
//    ResourceKey<StructureTemplatePool> SPIRIT_PAGODA_LEVEL_3 = create("level_3");
//    ResourceKey<StructureTemplatePool> SPIRIT_PAGODA_LEVEL_4 = create("level_4");
//    ResourceKey<StructureTemplatePool>[] SPIRIT_PAGODA_LEVEL_5 = create("level_5", 4);
//    ResourceKey<StructureTemplatePool>[] SPIRIT_PAGODA_LEVEL_6 = create("level_6", 4);
//    ResourceKey<StructureTemplatePool>[] SPIRIT_PAGODA_LEVEL_7 = create("level_7", 4);
//
//    static void initPools(BootstrapContext<StructureTemplatePool> context) {
//        final HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
//        final Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
//
//        context.register(SPIRIT_PAGODA_LEVEL_1, new StructureTemplatePool(
//                empty, List.of(Pair.of(StructurePoolElement.legacy(name("level_1"))
//                                        .apply(StructureTemplatePool.Projection.RIGID), 2
//        ))));
//
//        context.register(SPIRIT_PAGODA_LEVEL_2, new StructureTemplatePool(
//                empty, List.of(Pair.of(StructurePoolElement.legacy(name("level_2"))
//                .apply(StructureTemplatePool.Projection.RIGID), 2
//        ))));
//
//        context.register(SPIRIT_PAGODA_LEVEL_3, new StructureTemplatePool(
//                empty, List.of(Pair.of(StructurePoolElement.legacy(name("level_3"))
//                .apply(StructureTemplatePool.Projection.RIGID), 2
//        ))));
//
//        context.register(SPIRIT_PAGODA_LEVEL_4, new StructureTemplatePool(
//                empty, List.of(Pair.of(StructurePoolElement.legacy(name("level_4"))
//                .apply(StructureTemplatePool.Projection.RIGID), 2
//        ))));
//
//        register(context, SPIRIT_PAGODA_LEVEL_5, "level_5", empty);
//
//        register(context, SPIRIT_PAGODA_LEVEL_6, "level_6", empty);
//
//        register(context, SPIRIT_PAGODA_LEVEL_7, "level_7", empty);
//    }
//
//    private static void register(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool>[] keys, String name, Holder<StructureTemplatePool> empty) {
//        for (int i = 0; i < keys.length; i++) {
//            context.register(keys[i], new StructureTemplatePool(
//                    empty, List.of(Pair.of(StructurePoolElement.legacy(name(name + "_" + (i + 1)))
//                    .apply(StructureTemplatePool.Projection.RIGID), 2
//            ))));
//        }
//    }
//
//
//    private static ResourceKey<StructureTemplatePool>[] create(String name, int count) {
//        ResourceKey<StructureTemplatePool>[] keys = new ResourceKey[count];
//        for (int i = 0; i < count; i++) {
//            keys[i] = create(name + "_" + (i + 1));
//        }
//        return keys;
//    }
//
//    private static ResourceKey<StructureTemplatePool> create(String name) {
//        return IMMPools.create("spirit_pagoda/" + name);
//    }
//
//    private static String name(String name) {
//        return Util.prefixName("spirit_pagoda/spirit_pagoda_" + name);
//    }

    public static List<PagodaPart> getParts(){
        List<PagodaPart> parts = new ArrayList<>();
        parts.add(new PagodaPart(1, true, 21, 7, 0));
        parts.add(new PagodaPart(2, true, 27, 8, 0));
        parts.add(new PagodaPart(3, true, 35, 10, 0));
        parts.add(new PagodaPart(4, true, 43, 12, 0));
        parts.add(new PagodaPart(5, false, 51, 14, 32));
        parts.add(new PagodaPart(6, false, 61, 17, 32));
        parts.add(new PagodaPart(7, false, 71, 23, 48));
        return parts;
    }

    /**
     * 浮屠塔的部件信息。
     * @param level 浮屠塔的层数。
     * @param single 当前层是不是只有一个结构方块。
     * @param length 浮屠塔当前俯视角八边形的长度。
     * @param height 浮屠塔当前的层高。
     * @param singleCoverSize 一个单元的覆盖面积。
     */
    public record PagodaPart(int level, boolean single, int length, int height, int singleCoverSize){

        public PartType[] getTypes(){
            if(single()){
                return new PartType[]{PartType.MAIN};
            } else {
                return PartType.values();
            }
        }

        public BlockPos getOffset(PartType type){
            BlockPos offset = new BlockPos(-length() >> 1, 0, -length() >> 1);
            switch (type){
                case LEFT -> offset = offset.offset(singleCoverSize(), 0, 0);
                case RIGHT -> offset = offset.offset(0, 0, singleCoverSize());
                case DIAGON -> offset = offset.offset(singleCoverSize(), 0, singleCoverSize());
            }
            return offset;
        }

        public ResourceLocation getTemplate(PartType type){
            if(single()){
                return Util.prefix("spirit_pagoda/spirit_pagoda_level_" + level());
            } else {
                return Util.prefix("spirit_pagoda/spirit_pagoda_level_" + level() + "_" + type.partId);
            }
        }
    }

    public enum PartType {

        MAIN(1),

        LEFT(2),

        RIGHT(3),

        DIAGON(4),

        ;

        private final int partId;

        PartType(int partId) {
            this.partId = partId;
        }
    }

}
