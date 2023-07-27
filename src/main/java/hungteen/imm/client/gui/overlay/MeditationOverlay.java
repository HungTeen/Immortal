package hungteen.imm.client.gui.overlay;

import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.gui.screen.meditation.MeditationScreen;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 15:42
 */
public class MeditationOverlay implements IGuiOverlay {

    public static final IGuiOverlay INSTANCE = new MeditationOverlay();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if(MeditationScreen.canRenderMeditation()){
            ClientUtil.push("renderMeditation");
            final float tick = PlayerUtil.getIntegerData(ClientUtil.player(), PlayerRangeIntegers.MEDITATE_TICK);
            float percent = tick / 100.0F;
            if (percent > 1.0F) {
                percent = 1.0F - (tick - 100.0F) / 10.0F;
            }

            final int color = (int)(220.0F * percent) << 24 | 1052704;
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, screenWidth, screenHeight, color);
            ClientUtil.pop();
        }
    }
}
