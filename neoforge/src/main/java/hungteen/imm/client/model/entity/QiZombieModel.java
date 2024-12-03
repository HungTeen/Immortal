package hungteen.imm.client.model.entity;

import hungteen.imm.common.entity.undead.QiZombie;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;

/**
 * {@link net.minecraft.client.model.AbstractZombieModel}.
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 21:45
 **/
public class QiZombieModel<T extends QiZombie> extends HumanoidModel<T> {

    public static LayerDefinition createBodyLayer(){
        return createBodyLayer(64, 64);
    }

    public static LayerDefinition createBodyLayer(int width, int height){
        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
//        PartDefinition partdefinition = meshDefinition.getRoot();
//        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(5.0F, 2.0F, 0.0F));
//        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE), PartPose.offset(1.9F, 12.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, width, height);
    }

    public QiZombieModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, entity.isAggressive(), this.attackTime, ageInTicks);
    }

}
