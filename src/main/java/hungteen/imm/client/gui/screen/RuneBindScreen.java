package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.client.gui.RecipeRenderManager;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.menu.RuneBindMenu;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
        if(this.menu.getCurrentBehavior() != null){
            final List<Pair<Ingredient, Slot>> list = new ArrayList<>();
            for(int i = 0; i < COLS; i++) {
                if(this.menu.validSlot(i + 1) && this.menu.inputContainer.getItem(i + 1).isEmpty()){
                    list.add(Pair.of(
                            Ingredient.of(this.menu.getCurrentBehavior().getFilterItems().get(i).get()),
                            this.menu.getSlot(i + 1)
                    ));
                }
            }
            this.recipeRenderManager.setRecipe(list);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.recipeRenderManager.render(this.minecraft, stack, this.leftPos, this.topPos, partialTicks);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        RenderHelper.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if(this.menu.getCurrentBehavior() != null){
            for(int i = 0; i < COLS; i++) {
                int tx = 32;
                if(i < this.menu.getCurrentBehavior().maxSlot()){
                    final ItemStack itemStack = this.menu.inputContainer.getItem(0);
                    if(itemStack.getItem() instanceof BehaviorRuneItem item){
                        if(item.hasFilter(itemStack, i)){
                            tx = 16;
                        } else {
                            tx = 0;
                        }
                    }
                }
                this.blit(stack, getLeftOffset(i), getTopOffset(i), tx, 240, 16, 16);
            }
        }
    }

    @Override
    protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
        super.renderTooltip(stack, mouseX, mouseY);
        this.recipeRenderManager.renderGhostRecipeTooltip(this.minecraft, stack, this.leftPos, this.topPos, mouseX, mouseY);
        if(this.menu.getCurrentBehavior() != null){
            for(int i = 0; i < COLS; i++) {
                final int x = getLeftOffset(i);
                final int y = getTopOffset(i);
                if(i < this.menu.getCurrentBehavior().maxSlot() && MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)){
                    this.renderComponentTooltip(stack, List.of(
                            this.menu.getCurrentBehavior().getFilterDesc(i).withStyle(ChatFormatting.GREEN)
                    ), mouseX, mouseY);
                }
            }
        }
    }

    protected int getLeftOffset(int id){
        return this.leftPos + 51 + id * 18;
    }

    protected int getTopOffset(int id){
        return this.topPos + 36;
    }

}
