package hungteen.immortal.api.interfaces;

import hungteen.immortal.api.registry.IArtifactType;
import net.minecraft.world.item.ItemStack;

/**
 * 具有灵力的物品需要继承此接口。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-08 09:35
 **/
public interface IArtifactItem {

    /**
     * 获取法器类型。
     * @return type.
     */
    IArtifactType getArtifactType(ItemStack stack);

}
