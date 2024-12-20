package hungteen.imm.client.gui.component;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.client.gui.GuiUtil;
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
 * @create 2023/7/14 15:54
 */
public class ScrollComponent<T> {

    private final IScrollableScreen<T> screen;
    private final int width;
    private final int height;
    private final int rows;
    private final int columns;
    private int widthInterval = 0;
    private int heightInterval = 0;
    private int leftPos = 0;
    private int topPos = 0;
    private int slotIdOffset = 0;
    private double scrollPercent;
    private int startIndex;

    public ScrollComponent(IScrollableScreen<T> screen, int len, int rows, int columns) {
        this(screen, len, len, rows, columns);
    }

    /**
     * Main Constructor.
     * @param screen       The component belongs to (Item list getter).
     * @param width        Square render width of each item.
     * @param height       Square render height of each item.
     * @param rows         How many rows to display items.
     * @param columns      How many columns to display items.
     */
    public ScrollComponent(IScrollableScreen<T> screen, int width, int height, int rows, int columns) {
        this.screen = screen;
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
    }

    public void setOffset(int leftPos, int topPos){
        this.leftPos = leftPos;
        this.topPos = topPos;
    }

    public void setInterval(int len){
        this.setInterval(len, len);
    }

    public void setInterval(int width, int height){
        this.widthInterval = width;
        this.heightInterval = height;
    }

    public void setSlotIdOffset(int slotIdOffset) {
        this.slotIdOffset = slotIdOffset;
    }

    public boolean mouseClicked(Minecraft mc, Screen screen, double mouseX, double mouseY, int key) {
        if(mc != null && mc.level != null && mc.player != null && mc.gameMode != null){
            final List<T> items = this.screen.getItems();
            for(int i = 0; i < this.rows; ++ i){
                for(int j = 0; j < this.columns; ++ j){
                    if(this.hovered(i, j, mouseX, mouseY)){
                        final int pos = this.getPos(i, j);
                        if(pos >= 0 && pos < items.size()){
                            // 对id进行偏移，预防冲突。
                            if(this.onClick(mc, screen, items.get(pos), pos + this.slotIdOffset)){
                                GuiUtil.playDownSound();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 点击每个槽位触发事件，发包到服务端。
     */
    protected boolean onClick(Minecraft mc, Screen screen, T item, int slotId){
        if(screen instanceof ContainerScreen containerScreen){
            if(containerScreen.getMenu().clickMenuButton(mc.player, slotId)){
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1.0F));
                mc.gameMode.handleInventoryButtonClick(containerScreen.getMenu().containerId, 0);
                return true;
            }
        }
        return false;
    }

    /**
     * 根据鼠标输入更新页面数据。
     */
    public boolean mouseScrolled(double delta) {
        if (this.canScroll()) {
            final int rows = this.getHiddenRows();
            final double f = delta / rows;
            this.scrollPercent = Mth.clamp(this.scrollPercent - f, 0.0F, 1.0F);
            this.startIndex = Math.round((float) (this.scrollPercent * rows)) * this.columns;
        }
        return true;
    }

    /**
     * 渲染每个槽位的Item。
     */
    public void renderItems(Minecraft mc, GuiGraphics graphics){
        final List<T> items = this.screen.getItems();
        // Render Recipes.
        if(! items.isEmpty() && mc != null && mc.level != null){
            for(int i = 0; i < this.rows; ++ i){
                for(int j = 0; j < this.columns; ++ j){
                    final int pos = this.getPos(i, j);
                    if(pos >= 0 && pos < items.size()){
                        final Pair<Integer, Integer> xy = getXY(i, j);
                        this.screen.renderItem(mc.level, graphics, items.get(pos), pos, xy.getFirst(), xy.getSecond());
                    }
                }
            }
        }
    }

    /**
     * 渲染每个槽位的Item的详细文本信息。
     */
    public void renderTooltip(Minecraft mc, GuiGraphics graphics, int mouseX, int mouseY) {
        if(mc != null && mc.level != null){
            for(int i = 0; i < this.rows; ++ i){
                for(int j = 0; j < this.columns; ++ j){
                    if(this.hovered(i, j, mouseX, mouseY)){
                        final List<T> items = this.screen.getItems();
                        final int pos = this.getPos(i, j);
                        if(pos >= 0 && pos < items.size()){
                            this.screen.renderTooltip(mc.level, graphics, items.get(pos), pos, mouseX, mouseY);
                        }
                    }
                }
            }
        }
    }

    /**
     * 滚轮能否滑动。
     */
    public boolean canScroll() {
        return this.getHiddenRows() > 0;
    }

    protected int getHiddenRows() {
        return (this.screen.getItems().size() + this.columns - 1) / this.columns - this.rows;
    }

    protected boolean hovered(int r, int c, double mouseX, double mouseY) {
        final Pair<Integer, Integer> pair = getXY(r, c);
        return pair.getFirst() <= mouseX && pair.getFirst() + this.width >= mouseX && pair.getSecond() <= mouseY && pair.getSecond() + this.height >= mouseY;
    }

    /**
     * 当前行和列对应的槽位在Items列表中的索引。
     */
    public int getPos(int r, int c){
        return this.startIndex + r * this.columns + c;
    }

    public Pair<Integer, Integer> getXY(int slotId){
        return getXY((slotId - this.startIndex) / getColumns(), (slotId - this.startIndex) % getColumns());
    }

    public Pair<Integer, Integer> getXY(int r, int c){
        return Pair.of(this.leftPos + c * (this.width + this.widthInterval), this.topPos + r * (this.height + this.heightInterval));
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public float getScrollPercent() {
        return (float) scrollPercent;
    }

    /**
     * 往下滑动了几行。
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * ID是否在当前页面。
     */
    public boolean inPage(int slotId){
        return slotId >= getStartIndex() && slotId < getStartIndex() + getRows() * getColumns();
    }
}
