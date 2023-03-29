package hungteen.imm.common.blockentity;

import hungteen.imm.util.Util;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 17:01
 **/
public class IMMBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Util.id());

//    public static final RegistryObject<BlockEntityType<SpiritualFurnaceBlockEntity>> SPIRITUAL_FURNACE = BLOCK_ENTITY_TYPES.register("spiritual_furnace", () -> {
//        return BlockEntityType.Builder.of(SpiritualFurnaceBlockEntity::new, ImmortalBlocks.COPPER_SPIRITUAL_FURNACE.get()).build(null);
//    });
//
//    public static final RegistryObject<BlockEntityType<ElixirRoomBlockEntity>> ELIXIR_ROOM = BLOCK_ENTITY_TYPES.register("elixir_room", () -> {
//        return BlockEntityType.Builder.of(ElixirRoomBlockEntity::new, ImmortalBlocks.COPPER_ELIXIR_ROOM.get()).build(null);
//    });
//
//    public static final RegistryObject<BlockEntityType<SpiritualRoomBlockEntity>> SPIRITUAL_ROOM = BLOCK_ENTITY_TYPES.register("spiritual_room", () -> {
//        return BlockEntityType.Builder.of(SpiritualRoomBlockEntity::new, ImmortalBlocks.COPPER_SPIRITUAL_ROOM.get()).build(null);
//    });
//
//    public static final RegistryObject<BlockEntityType<SmithingArtifactBlockEntity>> SMITHING_ARTIFACT = BLOCK_ENTITY_TYPES.register("smithing_artifact", () -> {
//        return BlockEntityType.Builder.of(SmithingArtifactBlockEntity::new, ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()).build(null);
//    });

}
