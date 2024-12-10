package hungteen.imm.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.client.render.level.ElementRenderer;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/27 15:20
 */
public class ElementOverlay {

    public static final ResourceLocation ELEMENTS = Util.get().guiTexture("elements");
    public static final int ELEMENT_INTERVAL = 10;
    public static final int ELEMENT_LEN = 9;
    private static final int ELEMENT_GUI_INTERVAL = 3;

    public static void renderElementOverBar(GuiGraphics graphics, DeltaTracker tracker) {
        if(ClientUtil.canRenderOverlay()){
            ClientUtil.push("renderElements");
            int topPos = graphics.guiHeight() - 59 - 12;
            if (! ClientUtil.mode().map(MultiPlayerGameMode::canHurtPlayer).orElse(false)) {
                topPos += 14;
            }
            final Entity entity = PlayerHelper.getClientPlayer().get();
            final Map<Element, Float> elements = ElementManager.getElementMap(entity);
            final List<Element> list = PlayerUtil.filterElements(ClientUtil.player(), Arrays.stream(Element.values()).toList());
            final int cnt = elements.size();
            final int barWidth = cnt * ELEMENT_LEN + (cnt - 1) * ELEMENT_GUI_INTERVAL;
            int startX = (graphics.guiWidth() - barWidth) >> 1;
            if (!elements.isEmpty()) {
                RenderHelper.push(graphics);
                RenderSystem.enableBlend();
                for (Element element : list) {
                    if (!elements.containsKey(element)) continue;
                    final float amount = elements.get(element);
                    final boolean robust = (elements.get(element) > 0);
                    final boolean warn = ElementRenderer.needWarn(entity, element, robust, Math.abs(amount));
                    if (!warn || ElementRenderer.notDisappear(entity)) {
                        graphics.blit(ELEMENTS, startX, topPos, ELEMENT_INTERVAL * element.ordinal(), 0, ELEMENT_LEN, ELEMENT_LEN);
                        if (robust && ElementRenderer.displayRobust(entity)) {
                            graphics.blit(ELEMENTS, startX, topPos, ELEMENT_INTERVAL * element.ordinal(), 10, ELEMENT_LEN, ELEMENT_LEN);
                        }
                    }
                    startX += ELEMENT_LEN + ELEMENT_GUI_INTERVAL;
                }
                RenderHelper.pop(graphics);
            }
            ClientUtil.pop();
        }
    }

}
