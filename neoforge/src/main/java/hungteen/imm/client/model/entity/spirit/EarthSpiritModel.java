package hungteen.imm.client.model.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.htlib.client.model.AnimatedEntityModel;
import hungteen.imm.common.entity.creature.spirit.EarthSpirit;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/27 13:11
 **/
public class EarthSpiritModel<T extends EarthSpirit> extends AnimatedEntityModel<T> {


    public static final AnimationDefinition MODEL_EAT = AnimationDefinition.Builder.withLength(2f)
            .addAnimation("body",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.posVec(0f, -3f, -1f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, -3f, -1f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.posVec(0f, -3f, -1f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("body",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(35f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(35f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.degreeVec(27.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("head",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(-45f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(-45f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.36f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("rleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(50f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(50f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(50f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("rdleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(-72.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(-72.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(-72.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("lleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(50f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(50f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(50f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("ldleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(-72.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(-72.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.56f, KeyframeAnimations.degreeVec(-72.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition MODEL_WALK = AnimationDefinition.Builder.withLength(3f).looping()
            .addAnimation("body",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.52f, KeyframeAnimations.degreeVec(0f, 0f, 7.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.96f, KeyframeAnimations.degreeVec(0f, 0f, -7.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("head",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(-0.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.degreeVec(1.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.28f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.degreeVec(-0.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.52f, KeyframeAnimations.degreeVec(1.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.64f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("rleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.72f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.2f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("lleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.72f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.2f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.72f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition MODEL_ATTACK = AnimationDefinition.Builder.withLength(3f)
            .addAnimation("body",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.posVec(0f, -1f, 1f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.posVec(0f, 2f, -8f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.64f, KeyframeAnimations.posVec(0f, -1f, -5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.posVec(0f, 0f, -5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("body",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.64f, KeyframeAnimations.degreeVec(22.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("rleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.degreeVec(25f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(21.42f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.68f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.16f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.68f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("rdleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.degreeVec(-42.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(-40.66f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("lleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.degreeVec(25f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(21.42f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(15f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.76f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("ldleg",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.degreeVec(-42.5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(-40.66f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.48f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    private final ModelPart total;

    public EarthSpiritModel(ModelPart root) {
        this.total = root.getChild("total");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = total.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -12.0F, -6.0F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-4.5F, -4.5F, -10.5F, 9.0F, 7.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 5.0F));

        PartDefinition rleg = body.addOrReplaceChild("rleg", CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, -1.0F, -1.5F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -11.0F, 0.5F));

        PartDefinition rdleg = rleg.addOrReplaceChild("rdleg", CubeListBuilder.create().texOffs(35, 33).addBox(-3.001F, -1.0F, -3.0F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition lleg = body.addOrReplaceChild("lleg", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -1.5F, 2.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -11.0F, 0.5F));

        PartDefinition ldleg = lleg.addOrReplaceChild("ldleg", CubeListBuilder.create().texOffs(32, 0).addBox(0.001F, -1.0F, -3.0F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if(entity.isIdle()) {
            this.animateWalk(MODEL_WALK, limbSwing, limbSwingAmount, 10.0F, 200.0F);
        }
        this.animate(entity.attackAnimationState, MODEL_ATTACK, ageInTicks);
        this.animate(entity.eatAnimationState, MODEL_EAT, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgba) {
        total.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgba);
    }

    @Override
    public ModelPart root() {
        return total;
    }
}