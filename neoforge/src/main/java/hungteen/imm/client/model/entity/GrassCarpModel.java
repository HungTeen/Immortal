package hungteen.imm.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.imm.common.entity.creature.GrassCarp;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:33
 **/
public class GrassCarpModel<T extends GrassCarp> extends EntityModel<T> {

    private final ModelPart total;
    private final ModelPart hair;
    private final ModelPart left;
    private final ModelPart right;

    public GrassCarpModel(ModelPart root) {
        this.total = root.getChild("total");
        this.hair = this.total.getChild("leaf");
        this.left = this.total.getChild("left");
        this.right = this.total.getChild("right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(1, 13).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition tails_r1 = total.addOrReplaceChild("tails_r1", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition tailn_r1 = total.addOrReplaceChild("tailn_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition leaf = total.addOrReplaceChild("leaf", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 12).addBox(-1.0F, -2.0F, -6.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 3.0F));

        PartDefinition tail = total.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, -1.5F, -0.7F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 4).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 5.0F));

        PartDefinition right = total.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(-3.0F, -3.0F, 0.0F));

        PartDefinition right_r1 = right.addOrReplaceChild("right_r1", CubeListBuilder.create().texOffs(18, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition left = total.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(3.0F, -3.0F, 0.0F));

        PartDefinition left_r1 = left.addOrReplaceChild("left_r1", CubeListBuilder.create().texOffs(18, 3).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //hair.
        this.hair.visible = ! entity.isBald();
        //swing.
        final float slow = 3.0F;
        this.left.zRot = (float) Math.sin(ageInTicks / slow);
        this.right.zRot = - (float) Math.sin(ageInTicks / slow);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int rgba) {
        total.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
