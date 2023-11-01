package hungteen.imm.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.records.HTColor;
import hungteen.imm.common.entity.misc.Tornado;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/11/1 18:10
 **/
public class TornadoModel<T extends Tornado> extends EntityModel<T> {

    private final ModelPart total;
    private final ModelPart wind1;
    private final ModelPart wind2;
    private final ModelPart wind3;
    private final ModelPart wind4;
    private final ModelPart wind5;

    public TornadoModel(ModelPart root) {
        this.total = root.getChild("total");
        this.wind1 = total.getChild("bone");
        this.wind2 = total.getChild("bone2");
        this.wind3 = total.getChild("bone3");
        this.wind4 = total.getChild("bone4");
        this.wind5 = total.getChild("bone5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone = total.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -12.0F, -2.0F, 4.0F, 24.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition bone2 = total.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bone2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(56, 68).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 17.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.5F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition bone3 = total.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 68).addBox(-7.0F, -6.5F, -7.0F, 14.0F, 13.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.5F, 0.0F, -0.1309F, 0.3054F, 0.0F));

        PartDefinition bone4 = total.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = bone4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 37).addBox(-12.0F, -3.0F, -12.0F, 24.0F, 7.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -26.0F, 0.0F, 0.0436F, -0.3491F, 0.0F));

        PartDefinition bone5 = total.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(0.0F, -26.0F, 0.0F));

        PartDefinition cube_r5 = bone5.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 97).addBox(-13.0F, 21.0F, -13.0F, 26.0F, 5.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7418F, 0.0F));

        PartDefinition cube_r6 = bone5.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -8.0F, -14.0F, 28.0F, 9.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.wind1.yRot = (float) (ageInTicks * 0.2);
        this.wind2.yRot = (float) (ageInTicks * 0.3);
        this.wind3.yRot = (float) (ageInTicks * 0.35);
        this.wind4.yRot = (float) (ageInTicks * 0.25);
        this.wind5.yRot = (float) (ageInTicks * 0.1);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        total.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}