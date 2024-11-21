package hungteen.imm.client.model.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.imm.common.entity.creature.spirit.WoodSpirit;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/27 12:55
 **/
public class WoodSpiritModel<T extends WoodSpirit> extends EntityModel<T> {

    private final ModelPart total;
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart tornado;
    private final ModelPart upTornado;
    private final ModelPart midTornado;
    private final ModelPart downTornado;

    public WoodSpiritModel(ModelPart root) {
        this.total = root.getChild("total");
        this.head = total.getChild("head");
        this.hat = head.getChild("propeller");
        this.tornado = total.getChild("tornado");
        this.upTornado = tornado.getChild("tornado0");
        this.midTornado = tornado.getChild("tornado1");
        this.downTornado = tornado.getChild("tornado2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition tornado = total.addOrReplaceChild("tornado", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tornado2 = tornado.addOrReplaceChild("tornado2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = tornado2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 38).addBox(-3.0F, -4.5F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition tornado1 = tornado.addOrReplaceChild("tornado1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = tornado1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 37).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition tornado0 = tornado.addOrReplaceChild("tornado0", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = tornado0.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 20).addBox(-6.0F, -2.5F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition head = total.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 20).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition leafl_r1 = head.addOrReplaceChild("leafl_r1", CubeListBuilder.create().texOffs(0, 5).addBox(0.0F, -6.25F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.5236F, 0.48F, -1.309F));

        PartDefinition leaff_r1 = head.addOrReplaceChild("leaff_r1", CubeListBuilder.create().texOffs(8, 4).addBox(-2.0F, -4.5F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 1.2217F, 0.0F, 0.0F));

        PartDefinition leafr_r1 = head.addOrReplaceChild("leafr_r1", CubeListBuilder.create().texOffs(0, -1).addBox(0.0F, -6.25F, -2.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.5236F, -0.48F, 1.309F));

        PartDefinition propeller = head.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(-4, 0).addBox(-10.0F, -16.5F, -10.0F, 20.0F, 0.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.upTornado.yRot = (float) (ageInTicks * 0.15);
        this.midTornado.yRot = (float) (ageInTicks * 0.3);
        this.downTornado.yRot = (float) (ageInTicks * 0.1);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgba) {
        total.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgba);
    }
}
