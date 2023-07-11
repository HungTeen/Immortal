package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.menu.MerchantTradeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
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

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/villager2.png");
    private static final Component TRADES_LABEL = Component.translatable("gui.imm.merchant.trades");
    private static final int MAX_BUTTON_COUNT = 7;
    private static final int BUTTON_HEIGHT = 20;
    private final TradeOfferButton[] tradeButtons = new TradeOfferButton[MAX_BUTTON_COUNT];
    private int shopItem;
    private int scrollOff;

    public MerchantTradeScreen(MerchantTradeMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 276;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        final int leftPos = this.leftPos + 5;
        int topPos = this.topPos + 16 + 2;
        for (int l = 0; l < MAX_BUTTON_COUNT; ++l) {
            this.tradeButtons[l] = this.addRenderableWidget(new TradeOfferButton(leftPos, topPos, l, b -> {
                if (b instanceof TradeOfferButton button) {
                    this.shopItem = button.getIndex() + this.scrollOff;
                    if (this.menu.clickMenuButton(this.minecraft.player, this.shopItem)) {
//                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                        this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, this.shopItem);
                    }
                }
            }));
            topPos += BUTTON_HEIGHT;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        RenderUtil.renderCenterScaledText(graphics.pose(), TRADES_LABEL, 53, 6, 1F, 4210752, ColorHelper.BLACK.rgb());
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
                    RenderHelper.push(graphics);
                    graphics.pose().translate(0, 0, 100);

                    // 渲染钱。
                    final int hOffset = getItemOffsetY(i);
                    for (int j = 0; j < getCostSize(offer); ++ j) {
                        final ItemStack itemStack = offer.getTradeEntry().costItems().get(j);
                        graphics.renderFakeItem(itemStack, getCostOffsetX(j), hOffset);
                        graphics.renderItemDecorations(this.font, itemStack, getCostOffsetX(j), hOffset);
                    }

                    // 渲染箭头。
                    graphics.blit(TEXTURE, leftPos + 35 + 20, hOffset + 3, 0, offer.valid() ? 15F : 25.0F, 171.0F, 10, 9, 512, 256);

                    // 渲染商品。
                    for (int j = 0; j < getResultSize(offer); ++j) {
                        final ItemStack itemStack = offer.getTradeEntry().resultItems().get(j);
                        graphics.renderFakeItem(itemStack, getResultOffsetX(j), hOffset);
                        graphics.renderItemDecorations(this.font, itemStack, getResultOffsetX(j), hOffset);
                    }

                    RenderHelper.pop(graphics);
                }
            }

            for (TradeOfferButton button : this.tradeButtons) {
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
            graphics.blit(TEXTURE, this.leftPos + 94, this.topPos + 18 + i1, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            graphics.blit(TEXTURE, this.leftPos + 94, this.topPos + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
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

    protected int getItemOffsetY(int i){
        return this.topPos + 18 + 2 + 20 * i;
    }

    protected int getCostOffsetX(int i){
        return this.leftPos + 5 + 5 + 20 * i;
    }

    protected int getCostSize(TradeOffer offer) {
        return Math.min(offer.getTradeEntry().costItems().size(), MerchantTradeMenu.COST_SIZE);
    }

    protected int getResultOffsetX(int i){
        return this.leftPos + 6 + 68 + 20 * i;
    }

    protected int getResultSize(TradeOffer offer) {
        return Math.min(offer.getTradeEntry().resultItems().size(), MerchantTradeMenu.RESULT_SIZE);
    }

    class TradeOfferButton extends Button {
        final int index;

        public TradeOfferButton(int p_99205_, int p_99206_, int p_99207_, Button.OnPress p_99208_) {
            super(p_99205_, p_99206_, 89, 20, CommonComponents.EMPTY, p_99208_, DEFAULT_NARRATION);
            this.index = p_99207_;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
            final TradeOffers offers = MerchantTradeScreen.this.menu.getTrades();
            final int scrollOff = MerchantTradeScreen.this.scrollOff;
            if (this.isHovered && this.index + scrollOff < offers.size()) {
                final TradeOffer offer = offers.get(this.index + scrollOff);
                boolean hovered = false;
                for (int i = 0; i < getCostSize(offer); ++ i) {
                    final int x = getCostOffsetX(i);
                    if(mouseX >= x && mouseX <= x + 16){
                        final ItemStack itemStack = offer.getTradeEntry().costItems().get(i);
                        graphics.renderTooltip(MerchantTradeScreen.this.font, itemStack, mouseX, mouseY);
                        hovered = true;
                        break;
                    }
                }
                if(! hovered){
                    for (int i = 0; i < getResultSize(offer); ++i) {
                        final int x = getResultOffsetX(i);
                        if(mouseX >= x && mouseX <= x + 16){
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

//    private class TradeOfferButton extends HTButton {
//        final int index;
//
//        public TradeOfferButton(int p_99205_, int p_99206_, int p_99207_, HTButton.OnPress onPress) {
//            super(RenderHelper.WIDGETS, p_99205_, p_99206_, 89, 20, onPress);
//            this.index = p_99207_;
//            this.visible = false;
//        }
//
//        public int getIndex() {
//            return this.index;
//        }
//
//        public void renderToolTip(PoseStack stack, int p_99212_, int p_99213_) {
////            if (this.isHovered && CultivatorTradeScreen.this.menu.getTrades().size() > this.index + CultivatorTradeScreen.this.scrollOff) {
////                if (p_99212_ < this.x + 20) {
////                    ItemStack itemstack = CultivatorTradeScreen.this.menu.getTrades().get(this.index + CultivatorTradeScreen.this.scrollOff).getCostA();
////                    CultivatorTradeScreen.this.renderTooltip(stack, itemstack, p_99212_, p_99213_);
////                } else if (p_99212_ < this.x + 50 && p_99212_ > this.x + 30) {
////                    ItemStack itemstack2 = CultivatorTradeScreen.this.menu.getTrades().get(this.index + CultivatorTradeScreen.this.scrollOff).getCostB();
////                    if (!itemstack2.isEmpty()) {
////                        CultivatorTradeScreen.this.renderTooltip(stack, itemstack2, p_99212_, p_99213_);
////                    }
////                } else if (p_99212_ > this.x + 65) {
////                    ItemStack itemstack1 = CultivatorTradeScreen.this.menu.getTrades().get(this.index + CultivatorTradeScreen.this.scrollOff).getResult();
////                    CultivatorTradeScreen.this.renderTooltip(stack, itemstack1, p_99212_, p_99213_);
////                }
////            }
//
//        }
//
//        @Override
//        protected Pair<Integer, Integer> getButtonUV() {
//            return Pair.of(0, 0);
//        }
//
//        @Override
//        protected Pair<Integer, Integer> getButtonUVOffset() {
//            return Pair.of(0, 0);
//        }
//
//        @Override
//        protected void updateWidgetNarration(NarrationElementOutput output) {
//
//        }
//    }
    }
