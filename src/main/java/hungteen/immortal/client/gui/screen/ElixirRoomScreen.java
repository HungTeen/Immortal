package hungteen.immortal.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderUtil;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.ColorUtil;
import hungteen.htlib.util.MathUtil;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.blockentity.ElixirRoomBlockEntity;
import hungteen.immortal.common.menu.ElixirRoomMenu;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.utils.Colors;
import hungteen.immortal.utils.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:17
 **/
public class ElixirRoomScreen extends HTContainerScreen<ElixirRoomMenu> {

    private static final ResourceLocation TEXTURE = Util.prefix("textures/gui/container/elixir_room.png");
    private static final int FLAME_ANIM_CD = 20;
    private static final int COUNT_PER_PAGE = 5;
    private int currentPos = 0;

    public ElixirRoomScreen(ElixirRoomMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 230;
        this.imageWidth = 198;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        //Render Title.
        RenderUtil.drawCenteredScaledString(stack, font, this.getTitle().getString(), this.leftPos + 100, this.topPos + 20, ColorUtil.WHITE, 0.6F);
        if(this.menu.getElixirStates() != ElixirRoomBlockEntity.SmeltingStates.PREPARE_RECIPE){
            final String text = new TranslatableComponent("gui.immortal.elixir_furnace.remain_count", this.menu.getIngredientLimit()).getString();
            RenderUtil.drawCenteredScaledString(stack, font, text, this.leftPos + 162, this.topPos + 70, Colors.WORD, 0.6F);

            if(this.menu.getExplodeTick() > 0){
                final String warn = new TranslatableComponent("gui.immortal.elixir_furnace.explode").getString();
                RenderUtil.drawCenteredScaledString(stack, font, warn, this.leftPos + 160, this.topPos + 80, ColorUtil.DARK_RED, 0.8F);
            }
        }
        RenderUtil.setTexture(TEXTURE);
        //Render Result Item.
        final int resultX = this.leftPos + 142 + 11;
        final int resultY = this.topPos + 16 + 1;
        if(! this.menu.getResultItem().isEmpty()){
            this.blit(stack, this.leftPos + 142, this.topPos + 16, 200, 40, 38, 18);
            this.itemRenderer.renderAndDecorateItem(this.menu.getResultItem(), resultX, resultY);
            final String score = new TranslatableComponent("gui.immortal.elixir_furnace.score", this.menu.getScore()).withStyle(ChatFormatting.BOLD).getString();
            final int color =  this.menu.getScore() < 33 ? ColorUtil.RED : this.menu.getScore() < ColorUtil.YELLOW ? ColorUtil.YELLOW : ColorUtil.GREEN;
            RenderUtil.drawCenteredScaledString(stack, font, score, this.leftPos + 170, this.topPos + 40, color, 1F);

            RenderUtil.setTexture(TEXTURE);
            this.blit(stack, this.leftPos + 140, this.topPos + 50, 200, 155, 41, 5);
            final int len = MathUtil.getBarLen(this.menu.getScore(), 100, 39);
            this.blit(stack, this.leftPos + 141, this.topPos + 51, 201, 161, len, 3);

        } else{
            this.blit(stack, this.leftPos + 142, this.topPos + 16, 200, 0, 38, 18);
        }
        // Render Flame.
        if(this.menu.getElixirStates() == ElixirRoomBlockEntity.SmeltingStates.PREPARE_INGREDIENTS){
            this.renderFlame(stack, true);
        }
        // Render Flame & Close Slot.
        if(this.menu.getElixirStates() == ElixirRoomBlockEntity.SmeltingStates.SMELTING){
            this.renderFlame(stack, false);
            this.blit(stack, this.leftPos + 73, this.topPos + 53, 200, 100, 52, 52);
        }
        // Render Spiritual Map.
        if(this.menu.getElixirStates() != ElixirRoomBlockEntity.SmeltingStates.PREPARE_RECIPE){
            this.renderSpiritualMap(stack);

            if(this.menu.getExplodeTick() > 0){
                RenderUtil.setTexture(TEXTURE);
                this.blit(stack, this.leftPos + 140, this.topPos + 93, 200, 155, 41, 5);
                final int len = MathUtil.getBarLen(this.menu.getExplodeTick(), 200, 39);
                this.blit(stack, this.leftPos + 141, this.topPos + 94, 201, 161, len, 3);
            }
        }
        // Render Tooltip.
        this.renderTooltip(stack, mouseX, mouseY);
        // Render Tooltip for Result Item.
        if(! this.menu.getResultItem().isEmpty()){
            if(MathUtil.isInArea(mouseX, mouseY, resultX, resultY, 16, 16)){
                stack.pushPose();
                // Can not put the tooltip over JEI !!!
//                stack.translate(0, 0, 900);
                this.renderTooltip(stack, this.menu.getResultItem(), mouseX, mouseY);
                stack.popPose();
            }
        }
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderUtil.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(stack, partialTicks, mouseX, mouseY);
    }

    private void renderSpiritualMap(PoseStack stack){
        Set<ISpiritualRoot> roots = new HashSet<>();
        roots.addAll(this.menu.getRecipeMap().keySet());
        roots.addAll(this.menu.getSpiritualMap().keySet());
        List<ISpiritualRoot> list = roots.stream().sorted(Comparator.comparingInt(ISpiritualRoot::getSortPriority)).toList();
        for(int i = 0; i < Math.min(list.size(), COUNT_PER_PAGE); ++ i){
            final int y = 18 + i * 17; // width = 34, height = 17.
            final int x = 15;
            final ISpiritualRoot root = list.get(i);

            RenderUtil.setTexture(root.getResourceLocation());
            this.blit(stack, this.leftPos + x + 1, this.topPos + y + 1, root.getTexturePosition().getFirst(), root.getTexturePosition().getSecond(), SpiritualRoots.TEX_WIDTH, SpiritualRoots.TEX_WIDTH);

            RenderUtil.setTexture(TEXTURE);
            this.blit(stack, this.leftPos + x + 1, this.topPos + y + 11, 218, 166, 31, 5);

            final int recipeValue = this.menu.getRecipeMap().getOrDefault(root, 0);
            final int currentValue = this.menu.getSpiritualMap().getOrDefault(root, 0);
            final int len = Math.min(29, MathUtil.getBarLen(currentValue, recipeValue, 29));
            this.blit(stack, this.leftPos + x + 2, this.topPos + y + 12, 219, 172, len, 3);

            final String currentText = currentValue + "";
            final int color = (currentValue <= recipeValue) ? Colors.WORD : ColorUtil.RED; //7825750;
            RenderUtil.drawCenteredScaledString(stack, font, currentText, this.leftPos + x + 22, this.topPos + y + 5, color, 0.6F);
        }

        /* Render Scroll Bar */
        stack.pushPose();
        RenderUtil.setTexture(TEXTURE);
        if (list.size() > COUNT_PER_PAGE) {
            final int len = MathUtil.getBarLen(currentPos, list.size() - COUNT_PER_PAGE, 85 - 19);
            this.blit(stack, this.leftPos + 53, this.topPos + 18 + len, 208, 184, 6, 19);
        } else {
            this.blit(stack, this.leftPos + 53, topPos + 18, 200, 184, 6, 19);
        }
        stack.popPose();
    }

    private void renderFlame(PoseStack stack, boolean isWhite){
        final int cd = isWhite ? FLAME_ANIM_CD * 2 : FLAME_ANIM_CD * 5;
        final int tick = this.menu.getSmeltingTick() % cd;
        final int len = MathUtil.getBarLen(tick, cd, 16) + 1;
        RenderUtil.setTexture(TEXTURE);
        this.blit(stack, this.leftPos + 82, this.topPos + 108 + 16 - len, 202, (isWhite ? 61 : 81) + 16 - len, 35, len);
    }

}
