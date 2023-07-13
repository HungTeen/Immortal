package hungteen.imm.client.gui.screen.furnace;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.common.menu.furnace.ElixirRoomMenu;
import hungteen.imm.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:17
 **/
public class ElixirRoomScreen extends FunctionalFurnaceScreen<ElixirRoomMenu> {

    private static final ResourceLocation TEXTURE = Util.get().containerTexture("elixir_room");
    private static final int FULL_FLAME_HEIGHT = 16;
    private static final int FULL_FLAME_LEN = 35;
    private static final int COUNT_PER_PAGE = 5;
    private int currentPos = 0; //TODO Mouse Scroll.

    public ElixirRoomScreen(ElixirRoomMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 230;
        this.imageWidth = 198;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        final double dx = mouseX - (this.leftPos + 82);
        final double dy = mouseY - (this.topPos + 108);
        if (dx >= 0 && dy >= 0 && dx < FULL_FLAME_LEN && dy < FULL_FLAME_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, 1)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, key);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        //Render Title.
        RenderUtil.renderCenterScaledText(graphics.pose(), this.getTitle(), this.leftPos + 100, this.topPos + 20, 0.6F, ColorHelper.WHITE.rgb(), ColorHelper.BLACK.rgb());
//        if(this.menu.getElixirStates() != ElixirRoomBlockEntity.SmeltingStates.PREPARE_RECIPE){
//            final Component text = Component.translatable("gui.imm.elixir_furnace.remain_count", this.menu.getIngredientLimit());
//            RenderUtil.renderCenterScaledText(graphics.pose(), text, this.leftPos + 162, this.topPos + 70, 0.6F, Colors.WORD, ColorHelper.BLACK.rgb());
//
//            if(this.menu.getExplodeTick() > 0){
//                final Component warn = Component.translatable("gui.imm.elixir_furnace.explode");
//                RenderUtil.renderCenterScaledText(graphics.pose(), warn, this.leftPos + 160, this.topPos + 80, 0.8F, ColorHelper.DARK_RED.rgb(), ColorHelper.BLACK.rgb());
//            }
//        }
        //Render Result Item.
//        final int resultX = this.leftPos + 142 + 11;
//        final int resultY = this.topPos + 16 + 1;
//        if(! this.menu.getResultItem().isEmpty()){
//            this.blit(graphics, this.leftPos + 142, this.topPos + 16, 200, 40, 38, 18);
//            this.itemRenderer.renderAndDecorateItem(this.menu.getResultItem(), resultX, resultY);
//            final Component score = Component.translatable("gui.imm.elixir_furnace.score", this.menu.getScore()).withStyle(ChatFormatting.BOLD).getString();
//            final int color =  this.menu.getScore() < 33 ? ColorHelper.RED : this.menu.getScore() < ColorHelper.YELLOW ? ColorHelper.YELLOW : ColorHelper.GREEN;
//            RenderHelper.drawCenteredScaledString(graphics, font, score, this.leftPos + 170, this.topPos + 40, color, 1F);
//
//            RenderHelper.setTexture(TEXTURE);
//            this.blit(graphics, this.leftPos + 140, this.topPos + 50, 200, 155, 41, 5);
//            final int len = MathHelper.getBarLen(this.menu.getScore(), 100, 39);
//            this.blit(graphics, this.leftPos + 141, this.topPos + 51, 201, 161, len, 3);
//
//        } else{
//            this.blit(graphics, this.leftPos + 142, this.topPos + 16, 200, 0, 38, 18);
//        }
        this.renderFlame(graphics);
        // Close Slot.
        if(this.menu.started()){
            graphics.blit(TEXTURE, this.leftPos + 73, this.topPos + 53, 200, 100, 52, 52);
        }
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
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
    }

    private void renderRecipes(GuiGraphics graphics) {
//        /* Render Scroll Bar */
//        graphics.pushPose();
//        RenderHelper.setTexture(TEXTURE);
//        if (list.size() > COUNT_PER_PAGE) {
//            final int len = MathHelper.getBarLen(currentPos, list.size() - COUNT_PER_PAGE, 85 - 19);
//            this.blit(graphics, this.leftPos + 53, this.topPos + 18 + len, 208, 184, 6, 19);
//        } else {
//            this.blit(graphics, this.leftPos + 53, topPos + 18, 200, 184, 6, 19);
//        }
//        graphics.popPose();
    }

    private void renderFlame(GuiGraphics graphics) {
        if (this.menu.started() || this.menu.canStart()) {
            final boolean isWhite = !this.menu.started();
            final int len = isWhite ? FULL_FLAME_HEIGHT : MathHelper.getBarLen(this.menu.getSmeltingTick(), this.menu.getSmeltingCD(), FULL_FLAME_HEIGHT) + 1;
            graphics.blit(TEXTURE, this.leftPos + 82, this.topPos + 108 + FULL_FLAME_HEIGHT - len, 202, (isWhite ? 61 : 81) + FULL_FLAME_HEIGHT - len, FULL_FLAME_LEN, len);
        }
    }

}
