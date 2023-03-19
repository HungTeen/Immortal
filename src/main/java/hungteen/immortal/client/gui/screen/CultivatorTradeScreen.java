package hungteen.immortal.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.immortal.common.impl.codec.HumanSettings;
import hungteen.immortal.common.menu.CultivatorTradeMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/18 14:15
 */
public class CultivatorTradeScreen extends HTContainerScreen<CultivatorTradeMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/villager2.png");
    private static final int MAX_BUTTON_COUNT = 7;
    private static final int BUTTON_HEIGHT = 20;
    private final TradeOfferButton[] tradeButtons = new TradeOfferButton[MAX_BUTTON_COUNT];
    private int shopItem;
    private int scrollOff;

    public CultivatorTradeScreen(CultivatorTradeMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 276;
    }

    @Override
    protected void init() {
        super.init();
        int k = this.topPos + 16 + 2;

        for(int l = 0; l < MAX_BUTTON_COUNT; ++ l) {
            this.tradeButtons[l] = this.addRenderableWidget(new TradeOfferButton(this.leftPos + 5, k, l, b -> {
                if (b instanceof TradeOfferButton button) {
                    this.shopItem = button.getIndex() + this.scrollOff;
                    this.postButtonClick();
                }

            }));
            k += BUTTON_HEIGHT;
        }
    }

    private void postButtonClick() {
//        this.menu.setSelectionHint(this.shopItem);
//        this.menu.tryMoveItems(this.shopItem);
        this.minecraft.getConnection().send(new ServerboundSelectTradePacket(this.shopItem));
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        List<HumanSettings.CommonTradeEntry> trades = this.menu.getTrades();
        if(! trades.isEmpty()){
            int k = this.topPos + 16 + 1;
            int l = this.leftPos + 5 + 5;
            RenderHelper.setTexture(TEXTURE);
            this.renderScroller(stack, trades.size());
            int i1 = 0;
            for (HumanSettings.CommonTradeEntry trade : trades) {
                if (this.canScroll(trades.size()) && (i1 < this.scrollOff || i1 >= MAX_BUTTON_COUNT + this.scrollOff)) {
                    ++i1;
                } else {
//                    ItemStack itemstack = merchantoffer.getBaseCostA();
//                    ItemStack itemstack1 = merchantoffer.getCostA();
//                    ItemStack itemstack2 = merchantoffer.getCostB();
//                    ItemStack itemstack3 = merchantoffer.getResult();
//                    this.itemRenderer.blitOffset = 100.0F;
//                    int j1 = k + 2;
//                    this.renderAndDecorateCostA(stack, itemstack1, itemstack, l, j1);
//                    if (!itemstack2.isEmpty()) {
//                        this.itemRenderer.renderAndDecorateFakeItem(itemstack2, i + 5 + 35, j1);
//                        this.itemRenderer.renderGuiItemDecorations(this.font, itemstack2, i + 5 + 35, j1);
//                    }
//
//                    this.renderButtonArrows(stack, merchantoffer, i, j1);
//                    this.itemRenderer.renderAndDecorateFakeItem(itemstack3, i + 5 + 68, j1);
//                    this.itemRenderer.renderGuiItemDecorations(this.font, itemstack3, i + 5 + 68, j1);
//                    this.itemRenderer.blitOffset = 0.0F;
                    k += 20;
                    ++i1;
                }
            }

            for(TradeOfferButton button : this.tradeButtons) {
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
        int i = len + 1 - MAX_BUTTON_COUNT;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int l = 113;
            int i1 = Math.min(113, this.scrollOff * k);
            if (this.scrollOff == i - 1) {
                i1 = 113;
            }

            blit(stack, this.leftPos + 94, this.topPos + 18 + i1, this.getBlitOffset(), 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            blit(stack, this.leftPos + 94, this.topPos + 18, this.getBlitOffset(), 6.0F, 199.0F, 6, 27, 512, 256);
        }

    }

    public boolean canScroll(int len) {
        return len > MAX_BUTTON_COUNT;
    }

    @Override
    public boolean mouseScrolled(double p_99127_, double p_99128_, double p_99129_) {
        int len = this.menu.getTrades().size();
        if (this.canScroll(len)) {
            this.scrollOff = Mth.clamp((int)((double)this.scrollOff - p_99129_), 0, len - MAX_BUTTON_COUNT);
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private class TradeOfferButton extends Button {
        final int index;

        public TradeOfferButton(int p_99205_, int p_99206_, int p_99207_, Button.OnPress p_99208_) {
            super(p_99205_, p_99206_, 89, 20, CommonComponents.EMPTY, p_99208_);
            this.index = p_99207_;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderToolTip(PoseStack stack, int p_99212_, int p_99213_) {
//            if (this.isHovered && CultivatorTradeScreen.this.menu.getTrades().size() > this.index + CultivatorTradeScreen.this.scrollOff) {
//                if (p_99212_ < this.x + 20) {
//                    ItemStack itemstack = CultivatorTradeScreen.this.menu.getTrades().get(this.index + CultivatorTradeScreen.this.scrollOff).getCostA();
//                    CultivatorTradeScreen.this.renderTooltip(stack, itemstack, p_99212_, p_99213_);
//                } else if (p_99212_ < this.x + 50 && p_99212_ > this.x + 30) {
//                    ItemStack itemstack2 = CultivatorTradeScreen.this.menu.getTrades().get(this.index + CultivatorTradeScreen.this.scrollOff).getCostB();
//                    if (!itemstack2.isEmpty()) {
//                        CultivatorTradeScreen.this.renderTooltip(stack, itemstack2, p_99212_, p_99213_);
//                    }
//                } else if (p_99212_ > this.x + 65) {
//                    ItemStack itemstack1 = CultivatorTradeScreen.this.menu.getTrades().get(this.index + CultivatorTradeScreen.this.scrollOff).getResult();
//                    CultivatorTradeScreen.this.renderTooltip(stack, itemstack1, p_99212_, p_99213_);
//                }
//            }

        }
    }
}
