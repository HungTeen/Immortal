package hungteen.immortal.common.blockentity;

import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.utils.Util;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 17:01
 **/
public class ImmortalBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Util.id());

    public static final RegistryObject<BlockEntityType<SpiritualFurnaceBlockEntity>> SPIRITUAL_FURNACE = BLOCK_ENTITY_TYPES.register("spiritual_furnace", () -> {
        return BlockEntityType.Builder.of(SpiritualFurnaceBlockEntity::new, ImmortalBlocks.SPIRITUAL_FURNACE.get()).build(null);
    });

    public static final RegistryObject<BlockEntityType<ElixirRoomBlockEntity>> ELIXIR_ROOM = BLOCK_ENTITY_TYPES.register("elixir_room", () -> {
        return BlockEntityType.Builder.of(ElixirRoomBlockEntity::new, ImmortalBlocks.ELIXIR_ROOM.get()).build(null);
    });

}
