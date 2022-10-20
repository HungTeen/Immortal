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

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Util.id());

    public static final RegistryObject<BlockEntityType<SpiritualStoveBlockEntity>> SPIRITUAL_STOVE = BLOCK_ENTITY_TYPES.register("spiritual_stove", () -> {
        return BlockEntityType.Builder.of(SpiritualStoveBlockEntity::new, ImmortalBlocks.SPIRITUAL_STOVE.get()).build(null);
    });
}
