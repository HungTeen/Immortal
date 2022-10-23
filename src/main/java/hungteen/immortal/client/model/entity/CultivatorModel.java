package hungteen.immortal.client.model.entity;

import hungteen.immortal.common.entity.human.Cultivator;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:19
 **/
public class CultivatorModel<T extends Cultivator> extends PlayerModel<T> {

    public CultivatorModel(ModelPart modelPart, boolean slim) {
        super(modelPart, slim);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation, boolean slim) {
        return LayerDefinition.create(PlayerModel.createMesh(cubeDeformation, slim), 64, 64);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

}
