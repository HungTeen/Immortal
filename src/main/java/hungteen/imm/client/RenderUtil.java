package hungteen.imm.client;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.util.ClientHelper;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.client.util.TextRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/10 15:47
 */
public class RenderUtil {

    public static void renderCenterScaledText(PoseStack stack, Component text, float x, float y, float scale, int color, int outlineColor){
        RenderHelper.renderCenterScaledText(stack, ClientHelper.font(), ClientProxy.mc().renderBuffers().bufferSource(), text, x, y, scale, color, outlineColor, TextRenderType.NORMAL, LightTexture.FULL_BRIGHT);
    }
}
