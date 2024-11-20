package hungteen.imm.client.model.entity.spirit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.htlib.client.model.AnimatedEntityModel;
import hungteen.imm.common.entity.creature.spirit.MetalSpirit;
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
 * @create: 25.10.2023 22:50
 **/
public class MetalSpiritModel<T extends MetalSpirit> extends AnimatedEntityModel<T> {

    public static final AnimationDefinition MODEL_IDLE = AnimationDefinition.Builder.withLength(4f).looping()
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("body",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.posVec(0f, 0.2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.16f, KeyframeAnimations.posVec(0f, -0.2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("down",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.posVec(0f, 0.4f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.16f, KeyframeAnimations.posVec(0f, -0.4f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, 1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.posVec(0f, 0.6f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3.16f, KeyframeAnimations.posVec(0f, -0.6f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition MODEL_FOUND_ENEMY = AnimationDefinition.Builder.withLength(2f)
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.52f, KeyframeAnimations.posVec(0f, 2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.12f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.36f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("down",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.2f, KeyframeAnimations.posVec(0f, 1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.52f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.posVec(0f, 7f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.76f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.52f, KeyframeAnimations.degreeVec(-32.5f, 0f, 100f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.76f, KeyframeAnimations.degreeVec(-12.99f, -0.65f, 67.59f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.52f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("axe",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.posVec(0f, -7f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.52f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.76f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.52f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4f, KeyframeAnimations.posVec(0f, 0f, -4f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.68f, KeyframeAnimations.posVec(0f, -1f, -4f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.52f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.68f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.52f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition ATTACK = AnimationDefinition.Builder.withLength(1.24f)
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.posVec(0f, -2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.56f, KeyframeAnimations.posVec(0f, 2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.92f, KeyframeAnimations.posVec(0f, 2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.2f, KeyframeAnimations.degreeVec(-5f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(-20f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.56f, KeyframeAnimations.degreeVec(6f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.92f, KeyframeAnimations.degreeVec(6f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("down",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.posVec(0f, 1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.56f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.92f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.posVec(0.24f, -1.65f, -2.07f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.posVec(0f, 5f, -1f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.56f, KeyframeAnimations.posVec(-9f, -3f, -7f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.92f, KeyframeAnimations.posVec(-9f, -3f, -7f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(10.59f, 0.07f, 78.6f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(-14.41f, -9.8f, 33.63f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.56f, KeyframeAnimations.degreeVec(56.92f, -3.66f, 56.05f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.92f, KeyframeAnimations.degreeVec(56.92f, -3.66f, 56.05f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("axe",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.posVec(0.25f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.24f, KeyframeAnimations.posVec(1.86f, -1.29f, -2.15f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.posVec(4.09f, -1.69f, -4.93f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.posVec(6.69f, -2.5f, -5.7f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.posVec(2.47f, -3.53f, -1.73f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.52f, KeyframeAnimations.posVec(-1.94f, -2.69f, 2.57f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.56f, KeyframeAnimations.posVec(-1.75f, -2f, 4.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.92f, KeyframeAnimations.posVec(-1.75f, -2f, 4.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.08f, KeyframeAnimations.posVec(-2.28f, -1.94f, 3.33f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.16f, KeyframeAnimations.posVec(-1.17f, -1.35f, 2.1f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(12.5f, 0f, 75f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.32f, KeyframeAnimations.degreeVec(-11.81f, 4.96f, 39.83f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(-13.71f, 6.82f, 24.65f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.56f, KeyframeAnimations.degreeVec(57.5f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.92f, KeyframeAnimations.degreeVec(57.5f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition MODEL_IDLE2 = AnimationDefinition.Builder.withLength(1.24f).looping()
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.44f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.88f, KeyframeAnimations.posVec(0f, 2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("down",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.52f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.04f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("axe",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.52f, KeyframeAnimations.posVec(0f, -1f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.04f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    public static final AnimationDefinition MODEL_LOST_GOAL = AnimationDefinition.Builder.withLength(3f)
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.68f, KeyframeAnimations.posVec(0f, -2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.52f, KeyframeAnimations.posVec(0f, -3f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.posVec(0f, 2f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("total",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("down",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.64f, KeyframeAnimations.posVec(0f, -0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.posVec(4f, 1.79f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.64f, KeyframeAnimations.posVec(0f, 9f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.04f, KeyframeAnimations.posVec(0f, 12f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.48f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("left",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.degreeVec(5f, 0f, 70f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.28f, KeyframeAnimations.degreeVec(-55f, 0f, 60f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.64f, KeyframeAnimations.degreeVec(-120f, 0f, 120f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(-240f, 0f, 120f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.96f, KeyframeAnimations.degreeVec(-357f, 0f, 123.93f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(360f, 0f, 123.93f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.12f, KeyframeAnimations.degreeVec(240f, 0f, 120f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.24f, KeyframeAnimations.degreeVec(120f, 0f, 120f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.44f, KeyframeAnimations.degreeVec(-60f, 0f, 115.47f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.72f, KeyframeAnimations.degreeVec(-15f, 0f, 30f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.08f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("axe",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.08f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.2f, KeyframeAnimations.posVec(0f, -8f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.4f, KeyframeAnimations.posVec(0f, -3.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.48f, KeyframeAnimations.posVec(0f, -1.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.12f, KeyframeAnimations.posVec(-5f, -3.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.posVec(-5f, -3.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.posVec(-2f, -4.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.76f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(1.12f, KeyframeAnimations.posVec(0f, 0.5f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("right",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 62.5f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(3f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();
    private final ModelPart total;
    private final ModelPart head;

    public MetalSpiritModel(ModelPart root) {
        this.total = root.getChild("total");
        this.head = total.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition total = partdefinition.addOrReplaceChild("total", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = total.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(32, 26).addBox(-3.0F, -1.5F, -1.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-3.5F, -2.0F, -2.0F, 7.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition down = body.addOrReplaceChild("down", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -4.5F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left = total.addOrReplaceChild("left", CubeListBuilder.create().texOffs(32, 18).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -12.0F, -3.0F));

        PartDefinition axe = left.addOrReplaceChild("axe", CubeListBuilder.create().texOffs(28, 16).addBox(-0.5F, -25.0F, -0.5F, 1.0F, 35.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 30).addBox(-1.0F, 1.5F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -24.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(0.0F, -25.5F, -7.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition right = total.addOrReplaceChild("right", CubeListBuilder.create().texOffs(32, 10).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -7.0F, -2.0F));

        PartDefinition head = total.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -6.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.animate(entity.idlingAnimationState, MODEL_IDLE, ageInTicks);
        this.animate(entity.agressiveAnimationState, MODEL_IDLE2, ageInTicks);
        this.animate(entity.preAttackAnimationState, MODEL_FOUND_ENEMY, ageInTicks);
        this.animate(entity.preIdleAnimationState, MODEL_LOST_GOAL, ageInTicks);
        this.animate(entity.attackAnimationState, ATTACK, ageInTicks);
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