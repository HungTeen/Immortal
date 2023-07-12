package hungteen.imm.client.gui.screen;

import hungteen.htlib.client.gui.screen.HTContainerScreen;
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

    private static final int FLAME_ANIM_CD = 20;

    public FunctionalFurnaceScreen(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 230;
        this.imageWidth = 198;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        //Render Title.
//        RenderUtil.renderCenterScaledText(graphics.pose(), this.getTitle(), this.leftPos + 100, this.topPos + 20, 0.6F, ColorHelper.WHITE.rgb(), ColorHelper.BLACK.rgb());
//        if(this.menu.getElixirStates() != ElixirRoomBlockEntity.SmeltingStates.PREPARE_RECIPE){
//            final Component text = Component.translatable("gui.imm.elixir_furnace.remain_count", this.menu.getIngredientLimit());
//            RenderUtil.renderCenterScaledText(graphics.pose(), text, this.leftPos + 162, this.topPos + 70, 0.6F, Colors.WORD, ColorHelper.BLACK.rgb());
//
//            if(this.menu.getExplodeTick() > 0){
//                final Component warn = Component.translatable("gui.imm.elixir_furnace.explode");
//                RenderUtil.renderCenterScaledText(graphics.pose(), warn, this.leftPos + 160, this.topPos + 80, 0.8F, ColorHelper.DARK_RED.rgb(), ColorHelper.BLACK.rgb());
//            }
//        }
        // Render Flame.
//        if(this.menu.getElixirStates() == ElixirRoomBlockEntity.SmeltingStates.PREPARE_INGREDIENTS){
//            this.renderFlame(graphics, true);
//        }
        // Render Flame & Close Slot.
//        if(this.menu.getElixirStates() == ElixirRoomBlockEntity.SmeltingStates.SMELTING){
//            this.renderFlame(graphics, false);
//            graphics.blit(TEXTURE, this.leftPos + 73, this.topPos + 53, 200, 100, 52, 52);
//        }
        // Render Tooltip.
        this.renderTooltip(graphics, mouseX, mouseY);
        // Render Tooltip for Result Item.
//        if(! this.menu.getResultItem().isEmpty()){
//            if(MathHelper.isInArea(mouseX, mouseY, resultX, resultY, 16, 16)){
//                // Can not put the tooltip over JEI !!!
//                stack.translate(0, 0, 900);
//                this.renderTooltip(graphics, this.menu.getResultItem(), mouseX, mouseY);
//            }
//        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
//        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
    }

//    private void renderFlame(GuiGraphics graphics, boolean isWhite){
//        final int cd = isWhite ? FLAME_ANIM_CD * 2 : FLAME_ANIM_CD * 5;
//        final int tick = this.menu.getSmeltingTick() % cd;
//        final int len = MathHelper.getBarLen(tick, cd, 16) + 1;
//        graphics.blit(TEXTURE, this.leftPos + 82, this.topPos + 108 + 16 - len, 202, (isWhite ? 61 : 81) + 16 - len, 35, len);
//    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if(delta > 0 && this.menu.canSwitchToFurnaceMenu()){
            if(this.canClickInventoryButton(0)){
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1.0F));
                this.sendInventoryButtonClickPacket(0);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}
