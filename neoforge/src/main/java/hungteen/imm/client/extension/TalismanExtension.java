package hungteen.imm.client.extension;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/5 23:19
 **/
public class TalismanExtension implements IClientItemExtensions {

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUseItem().equals(itemInHand)) {
            applyTalismanTransform(poseStack, partialTick, arm, itemInHand, player);
            applyItemArmTransform(poseStack, arm, equipProcess);
            return true;
        }
        return false;
    }

    private static void applyTalismanTransform(PoseStack poseStack, float partialTick, HumanoidArm arm, ItemStack stack, Player player) {
        float f3 = 1.0F;
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(f3 * 0.6F * i, f3 * -0.5F, f3 * 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(i * f3 * 90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f3 * 25.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(i * f3 * 30.0F));
    }

    private static void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProg) {
        int i = hand == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float)i * 0.56F, -0.52F + equippedProg * -0.6F, -0.72F);
    }

}
