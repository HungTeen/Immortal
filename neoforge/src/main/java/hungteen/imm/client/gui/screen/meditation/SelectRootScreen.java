package hungteen.imm.client.gui.screen.meditation;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.client.gui.screen.HTScreen;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.client.gui.IScrollableScreen;
import hungteen.imm.client.gui.component.HTConfirmButton;
import hungteen.imm.client.gui.component.ScrollComponent;
import hungteen.imm.client.gui.overlay.ElementOverlay;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.cultivation.QiManager;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.network.server.ServerSelectQiRootPacket;
import hungteen.imm.util.Colors;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-24 21:40
 **/
public class SelectRootScreen extends HTScreen implements IScrollableScreen<QiRootType> {

    private static final ResourceLocation TEXTURE = Util.get().guiTexture("qi_root");
    private static final MutableComponent SELECT_ROOT = TipUtil.gui("select_root.select");
    private static final MutableComponent ONLY_ONE_SPECIAL = TipUtil.gui("select_root.only_one_special");
    private static final MutableComponent CONFIRM = TipUtil.gui("select_root.confirm");
    private static final MutableComponent CONFIRM_BUTTON = TipUtil.gui("select_root.confirm_button");
    private static final MutableComponent NO_ROOT_SELECT = TipUtil.gui("select_root.no_root_selected");
    private static final MutableComponent TOO_MANY_ROOTS = TipUtil.gui("select_root.too_many_roots");
    private static final int LIVING_WIDTH = 46;
    private static final int LIVING_HEIGHT = 66;
    private static final int TITLE_WIDTH = 72;
    private static final int TITLE_HEIGHT = 23;
    private final ScrollComponent<QiRootType> scrollComponent;
    private final Set<QiRootType> selectedRoots = new HashSet<>();
    private HTConfirmButton confirmButton;
    private Component displayText;
    /**
     * 0 means no selection, negative means selected on spell circle, or circle list.
     */
    private boolean hasSpecialRoot = false;
    private int displayTick = 0;

    public SelectRootScreen() {
        this.imageWidth = 214;
        this.imageHeight = 129;
        this.scrollComponent = new ScrollComponent<>(this, 23, 23, 4, 3) {
            @Override
            protected boolean onClick(Minecraft mc, Screen screen, QiRootType root, int slotId) {
                select(root);
                return true;
            }
        };
    }

    @Override
    protected void init() {
        super.init();
        this.scrollComponent.setOffset(this.leftPos + 95, this.topPos + 18);
        this.scrollComponent.setInterval(8, 4);
        this.confirmButton = new HTConfirmButton(Button.builder(CONFIRM, (button) -> {
            if(this.selectedRoots.isEmpty()){
                warn(NO_ROOT_SELECT, 40);
                return;
            }
            NetworkHelper.sendToServer(new ServerSelectQiRootPacket(this.selectedRoots.stream().toList()));
            ClientUtil.mc().setScreen(null);
        }).pos(this.leftPos + (this.imageWidth -  HTConfirmButton.WIDTH) / 2 , this.topPos + this.imageHeight + 2).tooltip(Tooltip.create(CONFIRM_BUTTON)));
        this.addRenderableWidget(this.confirmButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        return this.scrollComponent.mouseScrolled(deltaX);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.displayTick > 0){
            -- this.displayTick;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        // 渲染主体界面。
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        {
            // 渲染标题。
            final int x = this.leftPos + (this.imageWidth - TITLE_WIDTH) / 2;
            final int y = this.topPos - TITLE_HEIGHT - 2;
            graphics.blit(TEXTURE, x, y, 0, 152, 72, 23);
            RenderUtil.renderCenterScaledText(graphics.pose(), SELECT_ROOT, this.width >> 1, y + 6, 1.2F, Colors.WORD, ColorHelper.BLACK.rgb());
        }
        {
            // 渲染玩家。
            final int x = this.leftPos + 21 + (LIVING_WIDTH >> 1);
            final int y = this.topPos + 14 + LIVING_HEIGHT;
            RenderUtil.renderEntityInInventoryFollowsMouse(graphics, x, y, 28, x - mouseX, y - mouseY, ClientUtil.player());
        }
        // 渲染玩家灵根信息。
        this.renderInfo(graphics);
        // 渲染提示信息。
        if (this.displayTick > 0 && this.displayText != null) {
            graphics.drawCenteredString(font, this.displayText, this.width >> 1, this.topPos + this.imageHeight + 20, ColorHelper.RED.rgb());
        }
        // 渲染滚轮和各个灵根按钮。
        if (this.scrollComponent.canScroll()) {
            final int len = MathUtil.getBarLen(this.scrollComponent.getScrollPercent(), 106 - 19);
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 193, this.topPos + 13 + len, 8, 58, 6, 19);
        } else {
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 193, topPos + 13, 0, 58, 6, 19);
        }
        this.scrollComponent.renderItems(getMinecraft(), graphics);
        this.scrollComponent.renderTooltip(getMinecraft(), graphics, mouseX, mouseY);
    }

    private void renderInfo(GuiGraphics graphics) {
        int posX = this.leftPos + 11;
        int posY = this.topPos + 84;
        if(! this.selectedRoots.isEmpty()){
            final int len = this.selectedRoots.size();
            final int interval = (65 - len * ElementOverlay.ELEMENT_LEN) / (len + 1);
            posX += interval;
            for (QiRootType root : this.selectedRoots) {
                var pair = root.getTexturePos();
                graphics.blit(root.getTexture(), posX, posY, pair.getFirst(), pair.getSecond(), ElementOverlay.ELEMENT_LEN, ElementOverlay.ELEMENT_LEN);
                posX += interval + ElementOverlay.ELEMENT_LEN;
            }
            posY += 12;

        } else {
            RenderUtil.renderCenterScaledText(graphics.pose(), TipUtil.UNKNOWN, posX + 32, posY + 1, 0.8F, ColorHelper.RED.rgb(), ColorHelper.BLACK.rgb());
        }
    }

    @Override
    public boolean keyPressed(int key, int code, int p_96072_) {
        final InputConstants.Key mouseKey = InputConstants.getKey(key, code);
        return super.keyPressed(key, code, p_96072_);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        if (!this.scrollComponent.mouseClicked(getMinecraft(), this, mouseX, mouseY, key)) {
            return super.mouseClicked(mouseX, mouseY, key);
        }
        return true;
    }

    @Override
    public List<QiRootType> getItems() {
        return QiRootTypes.registry().getValues().stream().sorted(Comparator.comparingInt(QiRootType::getSortPriority)).toList();
    }

    @Override
    public void renderItem(Level level, GuiGraphics graphics, QiRootType root, int slotId, int x, int y) {
        graphics.blit(TEXTURE, x, y, 10, 134, 13, 13);
        var pair = root.getTexturePos();
        graphics.blit(root.getTexture(), x + 2, y + 2, pair.getFirst(), pair.getSecond(), ElementOverlay.ELEMENT_LEN, ElementOverlay.ELEMENT_LEN);
        if (this.selected(root)) {
            graphics.blit(TEXTURE, x - 4, y - 3, 72, 129, 22, 22);
        }
    }

    @Override
    public void renderTooltip(Level level, GuiGraphics graphics, QiRootType root, int slotId, int x, int y) {
        final List<Component> components = new ArrayList<>();
        components.add(root.getComponent().withStyle(ChatFormatting.BOLD));
        components.add(root.isSpecialRoot() ? TipUtil.misc("root.special") : TipUtil.misc("root.common"));
        graphics.renderTooltip(font, components, Optional.empty(), x, y);
    }

    public boolean selected(QiRootType rootType){
        return this.selectedRoots.contains(rootType);
    }

    public void select(QiRootType rootType){
        if(! selected(rootType)){
            if(hasSpecialRoot){
                warn(ONLY_ONE_SPECIAL.withStyle(ChatFormatting.RED), 40);
                return;
            }
            if(rootType.isSpecialRoot()){
                this.selectedRoots.clear();
                warn(ONLY_ONE_SPECIAL.withStyle(ChatFormatting.GREEN), 40);
                hasSpecialRoot = true;
            }
            if(this.selectedRoots.size() >= QiManager.MAX_ROOT_AMOUNT){
                warn(TOO_MANY_ROOTS.withStyle(ChatFormatting.RED), 40);
                return;
            }
            this.selectedRoots.add(rootType);
        } else {
            if(rootType.isSpecialRoot()){
                hasSpecialRoot = false;
            }
            this.selectedRoots.remove(rootType);
        }
    }

    private void warn(Component component, int time) {
        this.displayText = component;
        this.displayTick = time;
    }

}
