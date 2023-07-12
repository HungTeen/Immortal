package hungteen.imm.common.blockentity;

import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.util.Util;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 17:01
 **/
public class IMMBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Util.id());

    public static final RegistryObject<BlockEntityType<SpiritualFurnaceBlockEntity>> SPIRITUAL_FURNACE = TYPES.register("spiritual_furnace", () -> {
        return BlockEntityType.Builder.of(SpiritualFurnaceBlockEntity::new, IMMBlocks.COPPER_FURNACE.get()).build(null);
    });

    public static final RegistryObject<BlockEntityType<ElixirRoomBlockEntity>> ELIXIR_ROOM = TYPES.register("elixir_room", () -> {
        return BlockEntityType.Builder.of(ElixirRoomBlockEntity::new, IMMBlocks.COPPER_ELIXIR_ROOM.get()).build(null);
    });

//    public static final RegistryObject<BlockEntityType<SpiritualRoomBlockEntity>> SPIRITUAL_ROOM = BLOCK_ENTITY_TYPES.register("spiritual_room", () -> {
//        return BlockEntityType.Builder.of(SpiritualRoomBlockEntity::new, ImmortalBlocks.COPPER_SPIRITUAL_ROOM.get()).build(null);
//    });
//
//    public static final RegistryObject<BlockEntityType<SmithingArtifactBlockEntity>> SMITHING_ARTIFACT = BLOCK_ENTITY_TYPES.register("smithing_artifact", () -> {
//        return BlockEntityType.Builder.of(SmithingArtifactBlockEntity::new, ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get()).build(null);
//    });

    public static void register(IEventBus modBus){
        TYPES.register(modBus);
    }


}
