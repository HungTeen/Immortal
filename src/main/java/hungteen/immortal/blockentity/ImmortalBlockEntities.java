package hungteen.immortal.blockentity;

import hungteen.immortal.utils.Util;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 17:01
 **/
public class ImmortalBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Util.id());

}
