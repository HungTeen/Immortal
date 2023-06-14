package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.menu.MerchantTradeMenu;
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
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
//        int i = this.menu.getTraderLevel();
//        if (i > 0 && i <= 5 && this.menu.showProgressBar()) {
//            Component component = this.title.copy().append(LEVEL_SEPARATOR).append(Component.translatable("merchant.level." + i));
//            int j = this.font.width(component);
//            int k = 49 + this.imageWidth / 2 - j / 2;
//            this.font.draw(p_99185_, component, (float)k, 6.0F, 4210752);
//        } else {
//            this.font.draw(p_99185_, this.title, (float)(49 + this.imageWidth / 2 - this.font.width(this.title) / 2), 6.0F, 4210752);
//        }
//
//        this.font.draw(p_99185_, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
//        int l = this.font.width(TRADES_LABEL);
//        this.font.draw(p_99185_, TRADES_LABEL, (float)(5 - l / 2 + 48), 6.0F, 4210752);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        RenderHelper.setTexture(TEXTURE);
        blit(stack, this.leftPos, this.topPos, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);

        final TradeOffers offers = this.menu.getTrades();
        if (!offers.isEmpty()) {
            // 选中的暂时卖完了。
            if (this.menu.isValidButtonIndex(this.shopItem) && !offers.get(this.shopItem).valid()) {
                blit(stack, this.leftPos + 83 + 99, this.topPos + 35, 0, 311.0F, 0.0F, 28, 21, 512, 256);
            }
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        final TradeOffers offers = this.menu.getTrades();
        if (!offers.isEmpty()) {
            final int leftPos = this.leftPos + 5;
            int topPos = this.topPos + 16 + 1;
            RenderHelper.setTexture(TEXTURE);
            this.renderScroller(stack, offers.size());

            // 渲染交易项。
            for (int i = 0; i < offers.size(); ++i) {
                if (i >= this.scrollOff && i < this.scrollOff + MAX_BUTTON_COUNT) {
                    final TradeOffer offer = offers.get(i);
                    stack.pushPose();
                    stack.translate(0, 0, 100F);

                    final int hOffset = topPos + 2;
                    for (int j = 0; j < offer.getTradeEntry().costItems().size(); ++j) {
                        final ItemStack itemStack = offer.getTradeEntry().costItems().get(j);
                        this.itemRenderer.renderAndDecorateFakeItem(stack, itemStack, leftPos + 35, hOffset);
                        this.itemRenderer.renderGuiItemDecorations(stack, this.font, itemStack, leftPos + 35, hOffset);
                    }

                    // 渲染箭头。
                    RenderHelper.setTexture(TEXTURE);
                    blit(stack, leftPos + 35 + 20, hOffset + 3, 0, offer.valid() ? 15F : 25.0F, 171.0F, 10, 9, 512, 256);

                    for (int j = 0; j < offer.getTradeEntry().resultItems().size(); ++j) {
                        final ItemStack itemStack = offer.getTradeEntry().resultItems().get(j);
                        this.itemRenderer.renderAndDecorateFakeItem(stack, itemStack, leftPos + 68, hOffset);
                        this.itemRenderer.renderGuiItemDecorations(stack, this.font, itemStack, leftPos + 68, hOffset);
                    }

                    stack.popPose();

                    topPos += 20;
                }
            }

            for (TradeOfferButton button : this.tradeButtons) {
                if (button.isHoveredOrFocused()) {
                    button.renderToolTip(stack, mouseX, mouseY);
                }

                button.visible = button.index < this.menu.getTrades().size();
            }

            RenderSystem.enableDepthTest();
        }
        this.renderTooltip(stack, mouseX, mouseY);
    }

    protected void renderScroller(PoseStack stack, int len) {
        final int i = len + 1 - MAX_BUTTON_COUNT;
        if (this.canScroll(len)) {
            final int j = 139 - (27 + (i - 1) * 139 / i);
            final int k = 1 + j / i + 139 / i;
            int i1 = Math.min(113, this.scrollOff * k);
            if (this.scrollOff == i - 1) {
                i1 = 113;
            }
            blit(stack, this.leftPos + 94, this.topPos + 18 + i1, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            blit(stack, this.leftPos + 94, this.topPos + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
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

        public void renderToolTip(PoseStack stack, int mouseX, int mouseY) {
            final TradeOffers offers = MerchantTradeScreen.this.menu.getTrades();
            final int scrollOff = MerchantTradeScreen.this.scrollOff;
            if (this.isHovered && offers.size() > this.index + scrollOff) {
                if (mouseX < this.getX() + 20) {
                    final ItemStack itemStack = offers.get(this.index + scrollOff).getTradeEntry().costItems().get(0);
                    MerchantTradeScreen.this.renderTooltip(stack, itemStack, mouseX, mouseY);
                } else if (mouseX < this.getX() + 50 && mouseX > this.getX() + 30) {
                    final ItemStack itemStack = offers.get(this.index + scrollOff).getTradeEntry().costItems().get(1);
                    if (!itemStack.isEmpty()) {
                        MerchantTradeScreen.this.renderTooltip(stack, itemStack, mouseX, mouseY);
                    }
                } else if (mouseX > this.getX() + 65) {
                    final ItemStack itemStack = offers.get(this.index + scrollOff).getTradeEntry().resultItems().get(0);
                    MerchantTradeScreen.this.renderTooltip(stack, itemStack, mouseX, mouseY);
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
