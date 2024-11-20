package hungteen.imm.client.model.entity.golem;// Made with Blockbench 4.7.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.model.AnimatedEntityModel;
import hungteen.imm.common.entity.golem.CopperGolem;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class CopperGolemModel<T extends CopperGolem> extends AnimatedEntityModel<T> implements ArmedModel {

	private final ModelPart total;
	private final ModelPart head;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public CopperGolemModel(ModelPart root) {
		this.total = root.getChild("total");
		this.head = this.total.getChild("head");
		this.rightArm = this.total.getChild("rightHand");
		this.leftArm = this.total.getChild("leftHand");
		this.rightLeg = this.total.getChild("rightLeg");
		this.leftLeg = this.total.getChild("leftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = total.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.5F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -1.5F, -6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(21, 13).addBox(-1.0F, -6.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-2.0F, -10.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 0.0F));

		PartDefinition body = total.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -5.0F, -2.5F, 8.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition leftLeg = total.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(22, 19).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(2.0F, -3.25F, 0.0F));

		PartDefinition rightLeg = total.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 23).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(-2.0F, -3.25F, 0.0F));

		PartDefinition leftHand = total.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(16, 28).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -7.0F, 0.0F));

		PartDefinition rightHand = total.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(26, 28).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		this.rightLeg.xRot = -1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.leftLeg.xRot = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
	}

	@Override
	public ModelPart root() {
		return this.total;
	}

	@Override
	public void translateToHand(HumanoidArm p_102108_, PoseStack p_102109_) {

	}
}