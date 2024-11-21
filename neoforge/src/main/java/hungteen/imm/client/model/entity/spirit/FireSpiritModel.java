package hungteen.imm.client.model.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.imm.common.entity.creature.spirit.FireSpirit;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;

/**
 * @program Immortal
 * @author PangTeen
 * @create 25.10.2023 12:44
 **/
public class FireSpiritModel<T extends FireSpirit> extends EntityModel<T> {

    private final ModelPart total;
    private final ModelPart face;
    private final ModelPart outer;
    private final ModelPart middle;
    private final ModelPart inner;

    public FireSpiritModel(ModelPart root, boolean light) {
        super(RenderType::entityTranslucent);
        this.total = root.getChild("total");
        this.face = total.getChild("face");
        this.outer = total.getChild("outer");
        this.middle = total.getChild("middle");
        this.inner = total.getChild("inner");
//        if(light){
//            this.outer.visible = false;
//        } else {
//            this.face.visible = false;
//            this.middle.visible = false;
//            this.inner.visible = false;
//        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition inner = total.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition face = total.addOrReplaceChild("face", CubeListBuilder.create().texOffs(8, 8).addBox(-4.25F, -7.0F, -4.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(1.25F, -6.0F, -4.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition outer = total.addOrReplaceChild("outer", CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition middle = total.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(0, 28).addBox(-3.5F, -9.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgba) {
//        total.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, packedOverlay, red, green, blue, 0.5F);
        total.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, packedOverlay, rgba);
    }
}