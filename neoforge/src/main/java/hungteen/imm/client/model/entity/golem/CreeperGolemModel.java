package hungteen.imm.client.model.entity.golem;

import hungteen.imm.common.entity.golem.CreeperGolem;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-25 23:23
 **/
public class CreeperGolemModel extends CreeperModel<CreeperGolem> {
    public CreeperGolemModel(ModelPart part) {
        super(part);
    }

    public static LayerDefinition createBodyLayer() {
        return createBodyLayer(CubeDeformation.NONE);
    }
}
