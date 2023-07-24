package hungteen.imm.client.gui.screen.furnace;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.common.menu.furnace.FunctionalFurnaceMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-12 21:04
 **/
public class FunctionalFurnaceScreen<T extends FunctionalFurnaceMenu> extends HTContainerScreen<T> {

    public FunctionalFurnaceScreen(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
    }
    @Override
    public boolean keyPressed(int key, int code, int p_97767_) {
        final InputConstants.Key mouseKey = InputConstants.getKey(key, code);
        if(ClientUtil.option().keyPlayerList.isActiveAndMatches(mouseKey) && this.menu.canSwitchToFurnaceMenu() && this.canClickInventoryButton(0)){
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1.0F));
            this.sendInventoryButtonClickPacket(0);
            return true;
        }
        return super.keyPressed(key, code, p_97767_);
    }

}
