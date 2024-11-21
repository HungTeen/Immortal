package hungteen.imm.common.blockentity;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-06 17:01
 **/
public class IMMBlockEntities {

    private static final HTVanillaRegistry<BlockEntityType<?>> TYPES = HTRegistryManager.vanilla(Registries.BLOCK_ENTITY_TYPE, Util.id());

    public static final HTHolder<BlockEntityType<SpiritualFurnaceBlockEntity>> SPIRITUAL_FURNACE = TYPES.register("spiritual_furnace", () -> {
        return BlockEntityType.Builder.of(SpiritualFurnaceBlockEntity::new, IMMBlocks.COPPER_FURNACE.get()).build(null);
    });

    public static final HTHolder<BlockEntityType<ElixirRoomBlockEntity>> ELIXIR_ROOM = TYPES.register("elixir_room", () -> {
        return BlockEntityType.Builder.of(ElixirRoomBlockEntity::new, IMMBlocks.COPPER_ELIXIR_ROOM.get()).build(null);
    });

//    public static final RegistryObject<BlockEntityType<SpiritualRoomBlockEntity>> SPIRITUAL_ROOM = BLOCK_ENTITY_TYPES.initialize("spiritual_room", () -> {
//        return BlockEntityType.Builder.of(SpiritualRoomBlockEntity::new, ImmortalBlocks.COPPER_SPIRITUAL_ROOM.get()).build(null);
//    });
//
//    public static final RegistryObject<BlockEntityType<SmithingArtifactBlockEntity>> SMITHING_ARTIFACT = BLOCK_ENTITY_TYPES.initialize("smithing_artifact", () -> {
//        return BlockEntityType.Builder.of(SmithingArtifactBlockEntity::new, ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()).build(null);
//    });

    public static void initialize(IEventBus modBus){
        NeoHelper.initRegistry(TYPES, modBus);
    }


}
