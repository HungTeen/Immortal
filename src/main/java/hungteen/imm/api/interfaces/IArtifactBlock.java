package hungteen.imm.api.interfaces;

import hungteen.imm.api.registry.IRealmType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 具有灵力的方块需要继承此接口。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-08 09:35
 **/
public interface IArtifactBlock extends IArtifactItem{

    /**
     * 获取法器类型。
     * @return type.
     */
    IRealmType getRealm(BlockState state);

}
