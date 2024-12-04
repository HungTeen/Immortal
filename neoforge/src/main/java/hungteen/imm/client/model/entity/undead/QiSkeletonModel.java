package hungteen.imm.client.model.entity.undead;

import hungteen.imm.common.entity.undead.QiSkeleton;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;

/**
 * {@link net.minecraft.client.model.SkeletonModel}.
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 21:45
 **/
public class QiSkeletonModel<T extends QiSkeleton> extends SkeletonModel<T> {

    public QiSkeletonModel(ModelPart modelPart) {
        super(modelPart);
    }


}
