package hungteen.imm.client.model.entity.human.villager;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/13 20:15
 **/
public class VillagerAnimations {

    public static final AnimationDefinition VILLAGER_LIKE_MOCK = AnimationDefinition.Builder.withLength(1.04f)
            .addAnimation("head",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.2f, KeyframeAnimations.degreeVec(0f, 0f, -22.5f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.48f, KeyframeAnimations.degreeVec(0f, 0f, 10f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(0f, 0f, -15f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("nose",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("nose",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.28f, KeyframeAnimations.degreeVec(-35f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.36f, KeyframeAnimations.degreeVec(-34.07239773400033f, 8.537257632141063f, 12.379605396312854f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.44f, KeyframeAnimations.degreeVec(-31.232518541470036f, -16.665768674058164f, -25.311230053869274f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.52f, KeyframeAnimations.degreeVec(-30.00194985474611f, 5.289201262876359f, 13.151177566676377f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.6f, KeyframeAnimations.degreeVec(-22.743858184465026f, -12.413087426488364f, -25.357563883987602f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.72f, KeyframeAnimations.degreeVec(-18.781726789272888f, 0.26914809393494643f, 10.198706976604225f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.84f, KeyframeAnimations.degreeVec(-7.737029718065278f, -8.856558383824067f, -43.00843551474179f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.92f, KeyframeAnimations.degreeVec(-6.672156307842342f, -2.2874100973386335f, 4.112747864022769f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.04f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("nose",
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f),
                                    AnimationChannel.Interpolations.LINEAR))).build();
}
