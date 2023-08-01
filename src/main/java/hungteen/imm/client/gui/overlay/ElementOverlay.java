package hungteen.imm.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.common.ElementManager;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 15:20
 */
public class ElementOverlay implements IGuiOverlay {

    public static final IGuiOverlay INSTANCE = new ElementOverlay();
    public static final ResourceLocation ELEMENTS = Util.get().guiTexture("elements");
    public static final int ELEMENT_INTERVAL = 10;
    public static final int ELEMENT_LEN = 9;
    private static final int ELEMENT_GUI_INTERVAL = 3;

    /**
     * Above {@link Gui#renderSelectedItemName(GuiGraphics)}.
     */
    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        if(ClientUtil.canRenderOverlay()){
            ClientUtil.push("renderElements");
            int topPos = height - 59 - 12;
            if (! ClientUtil.mode().map(MultiPlayerGameMode::canHurtPlayer).orElse(false)) {
                topPos += 14;
            }
            final Entity entity = PlayerHelper.getClientPlayer().get();
            final Map<Elements, Float> elements = ElementManager.getElements(entity);
            final int cnt = elements.size();
            final int barWidth = cnt * ELEMENT_LEN + (cnt - 1) * ELEMENT_GUI_INTERVAL;
            int startX = (width - barWidth) >> 1;
            if (!elements.isEmpty()) {
                RenderHelper.push(graphics);
                RenderSystem.enableBlend();
                for (Elements element : Elements.values()) {
                    if (!elements.containsKey(element)) continue;
                    final float amount = elements.get(element);
                    final boolean robust = (elements.get(element) > 0);
                    final boolean warn = ElementManager.needWarn(entity, element, robust, Math.abs(amount));
                    if (!warn || ElementManager.notDisappear(entity)) {
                        graphics.blit(ELEMENTS, startX, topPos, ELEMENT_INTERVAL * element.ordinal(), 0, ELEMENT_LEN, ELEMENT_LEN);
                        if (robust && ElementManager.displayRobust(entity)) {
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
