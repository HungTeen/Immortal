package hungteen.imm.client.model.entity.human;

import hungteen.imm.common.entity.human.cultivator.PlayerLikeEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-22 22:19
 **/
public class PlayerLikeModel<T extends PlayerLikeEntity> extends PlayerModel<T> {

    public PlayerLikeModel(ModelPart modelPart, boolean slim) {
        super(modelPart, slim);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation, boolean slim) {
        return LayerDefinition.create(PlayerModel.createMesh(cubeDeformation, slim), 64, 64);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

}
