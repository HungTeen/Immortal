package hungteen.imm.client.gui.component;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.client.gui.IScrollableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/14 15:54
 */
public class ScrollComponent<T> {

    private final IScrollableScreen<T> screen;
    private final int len;
    private final int rows;
    private final int columns;
    private int leftPos = 0;
    private int topPos = 0;
    private int slotIdOffset = 0;
    private double scrollPercent;
    private int startIndex;

    /**
     * Main Constructor.
     * @param screen       The component belongs to (Item list getter).
     * @param len          Square render length of each item.
     * @param rows         How many rows to display items.
     * @param columns      How many columns to display items.
     */
    public ScrollComponent(IScrollableScreen<T> screen, int len, int rows, int columns) {
        this.screen = screen;
        this.len = len;
        this.rows = rows;
        this.columns = columns;
    }

    public void setOffset(int leftPos, int topPos){
        this.leftPos = leftPos;
        this.topPos = topPos;
    }

    public void setSlotIdOffset(int slotIdOffset) {
        this.slotIdOffset = slotIdOffset;
    }

    public boolean mouseClicked(Minecraft mc, Screen screen, double mouseX, double mouseY, int key) {
        final double dx = mouseX - (this.leftPos + 82);
        final double dy = mouseY - (this.topPos + 108);
        if(mc != null && mc.level != null && mc.player != null && mc.gameMode != null){
            for(int i = 0; i < this.rows; ++ i){
                for(int j = 0; j < this.columns; ++ j){
                    if(this.hovered(i, j, mouseX, mouseY)){
                        final List<T> items = this.screen.getItems();
                        final int pos = this.getPos(i, j) + this.slotIdOffset;
                        if(pos >= 0 && pos < items.size()){
                            if(this.onClick(mc, screen, pos)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    protected boolean onClick(Minecraft mc, Screen screen, int slotId){
        if(screen instanceof ContainerScreen containerScreen){
            if(containerScreen.getMenu().clickMenuButton(mc.player, slotId)){
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1.0F));
                mc.gameMode.handleInventoryButtonClick(containerScreen.getMenu().containerId, 0);
                return true;
            }
        }
        return false;
    }

    public boolean mouseScrolled(double delta) {
        if (this.canScroll()) {
            final int rows = this.getHiddenRows();
            final double f = delta / rows;
            this.scrollPercent = Mth.clamp(this.scrollPercent - f, 0.0F, 1.0F);
            this.startIndex = Math.round((float) (this.scrollPercent * rows)) * this.columns;
        }
        return true;
    }

    public void renderItems(Minecraft mc, GuiGraphics graphics){
        final List<T> items = this.screen.getItems();
        // Render Recipes.
        if(! items.isEmpty() && mc != null && mc.level != null){
            for(int i = 0; i < this.rows; ++ i){
                for(int j = 0; j < this.columns; ++ j){
                    final int pos = this.getPos(i, j);
                    if(pos >= 0 && pos < items.size()){
                        final Pair<Integer, Integer> xy = getXY(i, j);
                        this.screen.renderItem(mc.level, graphics, items.get(pos), xy.getFirst(), xy.getSecond());
                    }
                }
            }
        }
    }

    public void renderTooltip(Minecraft mc, GuiGraphics graphics, int mouseX, int mouseY) {
        if(mc != null && mc.level != null){
            for(int i = 0; i < this.rows; ++ i){
                for(int j = 0; j < this.columns; ++ j){
                    if(this.hovered(i, j, mouseX, mouseY)){
                        final List<T> items = this.screen.getItems();
                        final int pos = this.getPos(i, j);
                        if(pos >= 0 && pos < items.size()){
                            final Pair<Integer, Integer> xy = getXY(i, j);
                            this.screen.renderTooltip(mc.level, graphics, items.get(pos), xy.getFirst(), xy.getSecond());
                        }
                    }
                }
            }
        }
    }

    public boolean canScroll() {
        return this.getHiddenRows() > 0;
    }

    protected int getHiddenRows() {
        return (this.screen.getItems().size() + this.columns - 1) / this.columns - this.rows;
    }

    protected boolean hovered(int r, int c, double mouseX, double mouseY) {
        final Pair<Integer, Integer> pair = getXY(r, c);
        return pair.getFirst() <= mouseX && pair.getFirst() + this.len >= mouseX && pair.getSecond() <= mouseY && pair.getSecond() + this.len >= mouseY;
    }

    public int getPos(int r, int c){
        return this.startIndex + r * this.columns + c;
    }

    public Pair<Integer, Integer> getXY(int r, int c){
        return Pair.of(this.leftPos + c * this.len, this.topPos + r * this.len);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public double getScrollPercent() {
        return scrollPercent;
    }

    public int getStartIndex() {
        return startIndex;
    }
}
