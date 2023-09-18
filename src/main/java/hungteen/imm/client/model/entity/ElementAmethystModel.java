package hungteen.imm.client.model.entity;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ElementAmethystModel extends EntityModel<ElementAmethyst> {

	private final ModelPart total;
	private final ModelPart light;
	private final ModelPart cube;
	private final ModelPart coat;

	public ElementAmethystModel(ModelPart root) {
		this.total = root.getChild("total");
		this.light = this.total.getChild("light");
		this.cube = this.total.getChild("cube");
		this.coat = this.total.getChild("coat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition light = total.addOrReplaceChild("light", CubeListBuilder.create().texOffs(24, 14).addBox(-2.0F, -24.0F, 0.0F, 4.0F, 18.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(24, 10).addBox(0.0F, -24.0F, -2.0F, 0.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition cube = total.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 0.0F));

		PartDefinition coat = total.addOrReplaceChild("coat", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(ElementAmethyst entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.total.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void renderCube(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.cube.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void renderLight(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.cube.visible = false;
		this.total.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.cube.visible = true;
	}

}