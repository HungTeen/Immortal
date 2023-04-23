package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.client.gui.RecipeRenderManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.menu.RuneBindMenu;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-21 22:23
 **/
public class RuneBindScreen extends RuneBaseScreen<RuneBindMenu> {

    private static final ResourceLocation TEXTURE = StringHelper.containerTexture(Util.id(), "rune_bind_table");
    private static final int COLS = 5;
    private final RecipeRenderManager recipeRenderManager;

    public RuneBindScreen(RuneBindMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        screenContainer.setClientSlotUpdateListener(this::slotChanged);
        this.recipeRenderManager = new RecipeRenderManager();
    }

    private void slotChanged() {
        this.recipeRenderManager.clear();
        List<Pair<Ingredient, Slot>> list = new ArrayList<>();
        for(int i = 0; i < COLS; i++) {

        }
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        RenderHelper.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.recipeRenderManager.render(this.minecraft, stack, this.leftPos, this.topPos, partialTicks);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        super.renderLabels(stack, mouseX, mouseY);
//        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
//            final int x = getLeftOffset(i) + 8 - this.leftPos;
//            final int y = getTopOffset(i) + 5 - this.topPos;
//            final IFilterRuneType<?> type = this.menu.getGateTypes().get(i);
//            RenderHelper.drawCenteredScaledString(stack, this.font, type.getComponent().getString(), x, y, ColorHelper.BLACK, 0.75F);
//        }
//        if(this.menu.getValidStatus() > 0){
//            final int status = this.menu.getValidStatus();
//            final Component component = status == 1 ? TipUtil.gui("rune_conflict") :
//                    status == 2 ? TipUtil.gui("need_more_rune") :
//                            TipUtil.gui("need_less_rune");
//            RenderHelper.drawCenteredScaledString(stack, this.font, component.getString(), 115, 85, ColorHelper.DARK_RED, 0.8F);
//        }
    }

    @Override
    protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
        super.renderTooltip(stack, mouseX, mouseY);
//        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
//            final int x = getLeftOffset(i);
//            final int y = getTopOffset(i);
//            if (MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)) {
//                final IFilterRuneType<?> type = this.menu.getGateTypes().get(i);
//                this.renderComponentTooltip(stack, List.of(
//                        type.getComponent(), type.getDesc().withStyle(ChatFormatting.GREEN)
//                ), mouseX, mouseY);
//            }
//        }
    }

}
