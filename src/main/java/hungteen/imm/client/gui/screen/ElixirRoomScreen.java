package hungteen.imm.client.gui.screen;

import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.common.blockentity.ElixirRoomBlockEntity;
import hungteen.imm.common.menu.ElixirRoomMenu;
import hungteen.imm.util.Colors;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:17
 **/
public class ElixirRoomScreen extends HTContainerScreen<ElixirRoomMenu> {

    private static final ResourceLocation TEXTURE = Util.get().containerTexture("elixir_room");
    private static final int FLAME_ANIM_CD = 20;
    private static final int COUNT_PER_PAGE = 5;
    private int currentPos = 0; //TODO Mouse Scroll.

    public ElixirRoomScreen(ElixirRoomMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 230;
        this.imageWidth = 198;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        //Render Title.
        RenderUtil.renderCenterScaledText(graphics.pose(), this.getTitle(), this.leftPos + 100, this.topPos + 20, 0.6F, ColorHelper.WHITE.rgb(), ColorHelper.BLACK.rgb());
        if(this.menu.getElixirStates() != ElixirRoomBlockEntity.SmeltingStates.PREPARE_RECIPE){
            final Component text = Component.translatable("gui.imm.elixir_furnace.remain_count", this.menu.getIngredientLimit());
            RenderUtil.renderCenterScaledText(graphics.pose(), text, this.leftPos + 162, this.topPos + 70, 0.6F, Colors.WORD, ColorHelper.BLACK.rgb());

            if(this.menu.getExplodeTick() > 0){
                final Component warn = Component.translatable("gui.imm.elixir_furnace.explode");
                RenderUtil.renderCenterScaledText(graphics.pose(), warn, this.leftPos + 160, this.topPos + 80, 0.8F, ColorHelper.DARK_RED.rgb(), ColorHelper.BLACK.rgb());
            }
        }
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
        // Render Flame.
        if(this.menu.getElixirStates() == ElixirRoomBlockEntity.SmeltingStates.PREPARE_INGREDIENTS){
            this.renderFlame(graphics, true);
        }
        // Render Flame & Close Slot.
        if(this.menu.getElixirStates() == ElixirRoomBlockEntity.SmeltingStates.SMELTING){
            this.renderFlame(graphics, false);
            graphics.blit(TEXTURE, this.leftPos + 73, this.topPos + 53, 200, 100, 52, 52);
        }
        // Render Spiritual Map.
        if(this.menu.getElixirStates() != ElixirRoomBlockEntity.SmeltingStates.PREPARE_RECIPE){
            this.renderSpiritualMap(graphics);

            if(this.menu.getExplodeTick() > 0){
                graphics.blit(TEXTURE, this.leftPos + 140, this.topPos + 93, 200, 155, 41, 5);
                final int len = MathHelper.getBarLen(this.menu.getExplodeTick(), 200, 39);
                graphics.blit(TEXTURE, this.leftPos + 141, this.topPos + 94, 201, 161, len, 3);
            }
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

    private void renderSpiritualMap(GuiGraphics graphics){
//        Set<ISpiritualType> roots = new HashSet<>();
//        roots.addAll(this.menu.getRecipeMap().keySet());
//        roots.addAll(this.menu.getSpiritualMap().keySet());
//        List<ISpiritualType> list = roots.stream().sorted(Comparator.comparingInt(ISpiritualType::getSortPriority)).toList();
//        for(int i = 0; i < Math.min(list.size(), COUNT_PER_PAGE); ++ i){
//            final int y = 18 + i * 17; // width = 34, height = 17.
//            final int x = 15;
//            final ISpiritualType root = list.get(i);
//
//            RenderHelper.setTexture(root.getResourceLocation());
//            this.blit(graphics, this.leftPos + x + 1, this.topPos + y + 1, root.getTexturePosition().getFirst(), root.getTexturePosition().getSecond(), SpiritualTypes.TEX_WIDTH, SpiritualTypes.TEX_WIDTH);
//
//            RenderHelper.setTexture(TEXTURE);
//            this.blit(graphics, this.leftPos + x + 1, this.topPos + y + 11, 218, 166, 31, 5);
//
//            final int recipeValue = this.menu.getRecipeMap().getOrDefault(root, 0);
//            final int currentValue = this.menu.getSpiritualMap().getOrDefault(root, 0);
//            final int len = Math.min(29, MathHelper.getBarLen(currentValue, recipeValue, 29));
//            this.blit(graphics, this.leftPos + x + 2, this.topPos + y + 12, 219, 172, len, 3);
//
//            final String currentText = currentValue + "";
//            final int color = (currentValue <= recipeValue) ? Colors.WORD : ColorHelper.RED; //7825750;
//            RenderHelper.drawCenteredScaledString(graphics, font, currentText, this.leftPos + x + 22, this.topPos + y + 5, color, 0.6F);
//        }
//
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

    private void renderFlame(GuiGraphics graphics, boolean isWhite){
        final int cd = isWhite ? FLAME_ANIM_CD * 2 : FLAME_ANIM_CD * 5;
        final int tick = this.menu.getSmeltingTick() % cd;
        final int len = MathHelper.getBarLen(tick, cd, 16) + 1;
        graphics.blit(TEXTURE, this.leftPos + 82, this.topPos + 108 + 16 - len, 202, (isWhite ? 61 : 81) + 16 - len, 35, len);
    }

}
