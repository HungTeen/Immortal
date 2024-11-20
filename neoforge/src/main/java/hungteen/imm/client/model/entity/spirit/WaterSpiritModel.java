package hungteen.imm.client.model.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.imm.common.entity.creature.spirit.WaterSpirit;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/24 12:22
 **/
public class WaterSpiritModel<T extends WaterSpirit> extends EntityModel<T> {

    private final ModelPart total;
    private final ModelPart inner;
    private final ModelPart tentacles;
    private final ModelPart layer;

    public WaterSpiritModel(ModelPart root, boolean outer) {
        this.total = root.getChild("total");
        this.inner = total.getChild("inner");
        this.tentacles = total.getChild("tentacles");
        this.layer = total.getChild("layer");
        if(outer){
            this.inner.visible = false;
            this.tentacles.visible = false;
        } else {
            this.layer.visible = false;
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition tentacles = total.addOrReplaceChild("tentacles", CubeListBuilder.create().texOffs(32, 31).addBox(-3.991F, 0.022F, -3.991F, 7.982F, 12.985F, 7.982F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-4.991F, 0.015F, -4.992F, 9.983F, 7.985F, 9.983F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition layer = total.addOrReplaceChild("layer", CubeListBuilder.create().texOffs(38, 7).addBox(-5.008F, -6.016F, -5.008F, 10.016F, 6.016F, 10.016F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, 0.001F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition inner = total.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 35).addBox(-3.992F, -4.985F, -3.992F, 7.984F, 4.985F, 7.984F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgba) {
        total.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgba);
    }

}
