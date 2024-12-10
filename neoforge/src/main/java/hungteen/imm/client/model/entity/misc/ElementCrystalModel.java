package hungteen.imm.client.model.entity.misc;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ElementCrystalModel extends EntityModel<ElementAmethyst> {

	private final ModelPart total;
	private final ModelPart solid;
	private final ModelPart coat;
	private final ModelPart corner;

	public ElementCrystalModel(ModelPart root, boolean isSolid) {
		this.total = root.getChild("total");
		this.solid = total.getChild("solid");
		this.coat = total.getChild("coat");
		this.corner = total.getChild("corner");
		if(isSolid){
			this.coat.visible = false;
		} else {
			this.solid.visible = false;
			this.corner.visible = false;
		}
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition solid = total.addOrReplaceChild("solid", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition coat = total.addOrReplaceChild("coat", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(4.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition corner = total.addOrReplaceChild("corner", CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -9.0F, -4.25F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(ElementAmethyst entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgba) {
		this.total.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgba);
	}

}