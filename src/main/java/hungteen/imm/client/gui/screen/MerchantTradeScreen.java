package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.client.util.ClientHelper;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.client.gui.component.HTButton;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.menu.MerchantTradeMenu;
import hungteen.imm.util.Colors;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 14:15
 */
public class MerchantTradeScreen extends HTContainerScreen<MerchantTradeMenu> {

    private static final ResourceLocation TEXTURE = Util.get().containerTexture("trade");
    private static final Component TRADES_LABEL = TipUtil.gui("merchant.trades");
    private static final int MAX_BUTTON_COUNT = 7;
    private static final int BUTTON_WIDTH = 83;
    private static final int BUTTON_HEIGHT = 20;
    private final MerchantButton[] tradeButtons = new MerchantButton[MAX_BUTTON_COUNT];

    private int shopItem;
    private int scrollOff;

    public MerchantTradeScreen(MerchantTradeMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 294;
        this.imageHeight = 171;
    }

    @Override
    protected void init() {
        super.init();
        final int leftPos = this.leftPos + 11 + 5;
        int topPos = this.topPos + 16 + 2;
        for (int l = 0; l < MAX_BUTTON_COUNT; ++l) {
            this.tradeButtons[l] = this.addRenderableWidget(new MerchantButton(l, Button.builder(Component.empty(), b -> {
                if (b instanceof MerchantButton button) {
                    this.shopItem = button.getIndex() + this.scrollOff;
                    if (this.menu.clickMenuButton(this.minecraft.player, this.shopItem)) {
//                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                        this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, this.shopItem);
                    }
                }
            }).pos(leftPos, topPos)));
            topPos += BUTTON_HEIGHT;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Render Title.
        RenderUtil.renderCenterScaledText(graphics.pose(), TRADES_LABEL, 58, 10, 1F, Colors.WORD, ColorHelper.BLACK.rgb());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);

        final TradeOffers offers = this.menu.getTrades();
        if (!offers.isEmpty()) {
            // 选中的暂时卖完了。
            if (this.menu.isValidButtonIndex(this.shopItem) && !offers.get(this.shopItem).valid()) {
                graphics.blit(TEXTURE, this.leftPos + 83 + 99, this.topPos + 35, 0, 311.0F, 0.0F, 28, 21, 512, 256);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        final TradeOffers offers = this.menu.getTrades();
        if (!offers.isEmpty()) {
            final int leftPos = this.leftPos + 5;
            RenderHelper.setTexture(TEXTURE);
            this.renderScroller(graphics, offers.size());

            // 渲染交易项。
            for (int i = 0; i < offers.size(); ++i) {
                // 在窗口里。
                if (i >= this.scrollOff && i < this.scrollOff + MAX_BUTTON_COUNT) {
                    final TradeOffer offer = offers.get(i);
                    final MerchantButton button = tradeButtons[i - this.scrollOff];
                    final int leftX = button.getX();
                    final int topX = button.getY();

                    RenderHelper.push(graphics);
                    graphics.pose().translate(0, 0, 100);

                    // 渲染交易相关的物品。
                    final int y = topX + button.getItemOffsetY();
                    for (int j = 0; j < getCostSize(offer); ++j) {
                        final ItemStack itemStack = offer.getTradeEntry().costItems().get(j);
                        final int x = leftX + button.getCostOffsetX(j);
                        graphics.renderFakeItem(itemStack, x, y);
                        graphics.renderItemDecorations(this.font, itemStack, x, y);
                    }

                    for (int j = 0; j < getResultSize(offer); ++j) {
                        final ItemStack itemStack = offer.getTradeEntry().resultItems().get(j);
                        final int x = leftX + button.getResultOffsetX(j);
                        graphics.renderFakeItem(itemStack, x, y);
                        graphics.renderItemDecorations(this.font, itemStack, x, y);
                    }

                    // 渲染箭头。
                    graphics.blit(TEXTURE, leftX + 50, topX + 4, 0, offer.valid() ? 15F : 25.0F, 171.0F, 10, 9, 512, 256);

                    RenderHelper.pop(graphics);
                }
            }

            for (MerchantButton button : this.tradeButtons) {
                if (button.isHoveredOrFocused()) {
                    button.renderToolTip(graphics, mouseX, mouseY);
                }

                button.visible = button.index < this.menu.getTrades().size();
            }

            RenderSystem.enableDepthTest();
        }
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    protected void renderScroller(GuiGraphics graphics, int len) {
        final int i = len + 1 - MAX_BUTTON_COUNT;
        if (this.canScroll(len)) {
            final int j = 139 - (27 + (i - 1) * 139 / i);
            final int k = 1 + j / i + 139 / i;
            int i1 = Math.min(113, this.scrollOff * k);
            if (this.scrollOff == i - 1) {
                i1 = 113;
            }
            graphics.blit(TEXTURE, this.leftPos + 104, this.topPos + 18 + i1, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            graphics.blit(TEXTURE, this.leftPos + 104, this.topPos + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
        }
    }

    public boolean canScroll(int len) {
        return len > MAX_BUTTON_COUNT;
    }

    @Override
    public boolean mouseScrolled(double p_99127_, double p_99128_, double delta) {
        final int len = this.menu.getTrades().size();
        if (this.canScroll(len)) {
            this.scrollOff = Mth.clamp((int) ((double) this.scrollOff - delta), 0, len - MAX_BUTTON_COUNT);
        }
        return true;
    }

    protected int getCostSize(TradeOffer offer) {
        return Math.min(offer.getTradeEntry().costItems().size(), MerchantTradeMenu.COST_SIZE);
    }

    protected int getResultSize(TradeOffer offer) {
        return Math.min(offer.getTradeEntry().resultItems().size(), MerchantTradeMenu.RESULT_SIZE);
    }

    class MerchantButton extends HTButton {
        final int index;

        protected MerchantButton(int index, Builder builder) {
            super(builder.size(BUTTON_WIDTH, BUTTON_HEIGHT), TEXTURE);
            this.index = index;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int x, int y, float partialTicks) {
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            graphics.blit(TEXTURE, this.getX(), this.getY(), 0, this.getTextureX(), this.getTextureY(), this.getWidth(), this.getHeight(), 512, 256);
            int i = getFGColor();
            this.renderString(graphics, ClientHelper.font(), i | Mth.ceil(this.alpha * 255.0F) << 24);
        }

        protected int getCostOffsetX(int i) {
            return i == 0 ? 4 : 32;
        }

        protected int getResultOffsetX(int i) {
            return 64;
        }

        protected int getItemOffsetY() {
            return 2;
        }

        @Override
        protected int getTextureX() {
            return 295;
        }

        @Override
        protected int getTextureY() {
            return this.isHovered() ? 67 : 46;
        }

        public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
            final TradeOffers offers = MerchantTradeScreen.this.menu.getTrades();
            final int scrollOff = MerchantTradeScreen.this.scrollOff;
            if (this.isHovered && this.index + scrollOff < offers.size()) {
                final TradeOffer offer = offers.get(this.index + scrollOff);
                boolean hovered = false;
                for (int i = 0; i < getCostSize(offer); ++i) {
                    final int x = this.getX() + getCostOffsetX(i);
                    if (mouseX >= x && mouseX <= x + 16) {
                        final ItemStack itemStack = offer.getTradeEntry().costItems().get(i);
                        graphics.renderTooltip(MerchantTradeScreen.this.font, itemStack, mouseX, mouseY);
                        hovered = true;
                        break;
                    }
                }
                if (!hovered) {
                    for (int i = 0; i < getResultSize(offer); ++i) {
                        final int x = this.getX() + getResultOffsetX(i);
                        if (mouseX >= x && mouseX <= x + 16) {
                            final ItemStack itemStack = offer.getTradeEntry().resultItems().get(i);
                            graphics.renderTooltip(MerchantTradeScreen.this.font, itemStack, mouseX, mouseY);
                            hovered = true;
                            break;
                        }
                    }
                }
            }
        }
    }

}
