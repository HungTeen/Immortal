package hungteen.imm.api.artifact;

import net.minecraft.world.item.ItemStack;

/**
 * 具有灵力的物品需要继承此接口。
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-08 09:35
 **/
public interface ArtifactItem {

    /**
     * @return 获取法器等级。
     */
    ArtifactRank getArtifactRealm(ItemStack stack);

}
