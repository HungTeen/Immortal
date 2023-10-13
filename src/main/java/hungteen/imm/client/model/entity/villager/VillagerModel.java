package hungteen.imm.client.model.entity.villager;// Made with Blockbench 4.7.0
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.htlib.client.model.AnimatedEntityModel;
import hungteen.imm.client.model.entity.villager.VillagerAnimations;
import hungteen.imm.common.entity.human.VillagerLikeEntity;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class VillagerModel<T extends VillagerLikeEntity> extends AnimatedEntityModel<T> implements ArmedModel {

	private final ModelPart total;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public VillagerModel(ModelPart root) {
		this.total = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.leftArm = root.getChild("leftArm");
		this.rightArm = root.getChild("rightArm");
		this.leftLeg = root.getChild("leftLeg");
		this.rightLeg = root.getChild("rightLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 38).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(44, 22).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 2.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(44, 22).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 2.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		if (this.riding) {
			this.rightArm.xRot = (-(float)Math.PI / 5F);
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = (-(float)Math.PI / 5F);
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		} else {
			this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.rightLeg.yRot = 0.0F;
			this.rightLeg.zRot = 0.0F;
			this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.leftLeg.yRot = 0.0F;
			this.leftLeg.zRot = 0.0F;
		}

		switch (entity.getArmPose()){
			case ATTACKING -> {
				if (entity.getMainHandItem().isEmpty()) {
					AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, ageInTicks);
				} else {
					AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, ageInTicks);
				}
			}
			case SPELLCASTING -> {
				this.rightArm.z = 0.0F;
				this.rightArm.x = -5.0F;
				this.leftArm.z = 0.0F;
				this.leftArm.x = 5.0F;
				this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
				this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
				this.rightArm.zRot = 2.3561945F;
				this.leftArm.zRot = -2.3561945F;
				this.rightArm.yRot = 0.0F;
				this.leftArm.yRot = 0.0F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yRot = -0.1F + this.head.yRot;
				this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = -0.9424779F + this.head.xRot;
				this.leftArm.yRot = this.head.yRot - 0.4F;
				this.leftArm.zRot = ((float)Math.PI / 2F);
			}
			case CROSSBOW_HOLD -> {
				AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
			}
			case CROSSBOW_CHARGE -> {
				AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
			}
			case CELEBRATING -> {
				this.rightArm.z = 0.0F;
				this.rightArm.x = -5.0F;
				this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
				this.rightArm.zRot = 2.670354F;
				this.rightArm.yRot = 0.0F;
				this.leftArm.z = 0.0F;
				this.leftArm.x = 5.0F;
				this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.05F;
				this.leftArm.zRot = -2.3561945F;
				this.leftArm.yRot = 0.0F;
			}
		}

		this.animate(entity.mockAnimationState, VillagerAnimations.VILLAGER_LIKE_MOCK, ageInTicks, 1F);
	}

	@Override
	public ModelPart root() {
		return this.total;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.root().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack stack) {
		this.getArm(arm).translateAndRotate(stack);
	}

	private ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
	}
}