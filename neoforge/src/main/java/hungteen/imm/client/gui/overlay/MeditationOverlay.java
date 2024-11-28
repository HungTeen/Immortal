package hungteen.imm.client.gui.overlay;

import hungteen.imm.client.gui.screen.meditation.MeditationScreen;
import hungteen.imm.client.util.ClientUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/27 15:42
 */
public class MeditationOverlay {

    private static final float MAX_FADE_TICK = 100;
    private static float FADE_TICK = 0;

    public static void renderMeditation(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if(MeditationScreen.canRenderMeditation()){
            ClientUtil.push("renderMeditation");
            if(FADE_TICK < MAX_FADE_TICK){
                FADE_TICK += deltaTracker.getGameTimeDeltaPartialTick(false);
            }
            float percent = Math.min(1F, FADE_TICK / MAX_FADE_TICK);

            final int color = (int)(220.0F * percent) << 24 | 1052704;
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), color);
            ClientUtil.pop();
        } else {
            FADE_TICK = 0;
        }
    }
}
