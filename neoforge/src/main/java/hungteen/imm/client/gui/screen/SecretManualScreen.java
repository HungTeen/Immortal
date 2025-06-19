package hungteen.imm.client.gui.screen;

import hungteen.htlib.client.gui.screen.HTScreen;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.client.gui.GuiUtil;
import hungteen.imm.client.gui.IScrollableScreen;
import hungteen.imm.client.gui.component.HTBookMarkButton;
import hungteen.imm.client.gui.component.HTConfirmButton;
import hungteen.imm.client.gui.component.ScrollComponent;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.cultivation.manual.LearnSpellScroll;
import hungteen.imm.common.cultivation.manual.SecretManual;
import hungteen.imm.common.cultivation.manual.SecretScroll;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.network.server.ServerManualPacket;
import hungteen.imm.util.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/15 10:50
 **/
public class SecretManualScreen extends HTScreen implements IScrollableScreen<SecretManualScreen.ScrollText> {

    private static final ResourceLocation TEXTURE = Util.get().guiTexture("secret_manual");
    private static final MutableComponent LEARN = TipUtil.gui("secret_manual.learn");
    private static final MutableComponent LEARN_BUTTON = TipUtil.gui("secret_manual.learn_button");
    private static final MutableComponent PREV = TipUtil.gui("secret_manual.prev");
    private static final MutableComponent NEXT = TipUtil.gui("secret_manual.next");
    private static final MutableComponent REQUIREMENTS = TipUtil.gui("secret_manual.requirements");
    private static final MutableComponent DESCRIPTION = TipUtil.gui("secret_manual.description");
    private static final int LOGO_WIDTH = 24;
    private final SecretManual manual;
    private final InteractionHand hand;
    private final ScrollComponent<ScrollText> scrollComponent;
    private HTConfirmButton learnButton;
    private HTBookMarkButton prevButton;
    private HTBookMarkButton nextButton;
    private int page;

    public SecretManualScreen(SecretManual manual, int page, InteractionHand hand) {
        this.manual = manual;
        this.page = page;
        this.hand = hand;
        this.imageWidth = 188;
        this.imageHeight = 196;
        this.scrollComponent = new ScrollComponent<>(this, 60, 12, 10, 1);
    }

    @Override
    protected void init() {
        super.init();
        this.scrollComponent.setOffset(this.leftPos + 15, this.topPos + 46);
        this.learnButton = new HTConfirmButton(Button.builder(LEARN, (button) -> {
            NetworkHelper.sendToServer(new ServerManualPacket(true, hand == InteractionHand.MAIN_HAND, page));
            GuiUtil.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
        }).pos(this.leftPos + (this.imageWidth -  HTConfirmButton.WIDTH) / 2 , this.topPos + this.imageHeight + 2).tooltip(Tooltip.create(LEARN_BUTTON))){
            @Override
            public boolean isActive() {
                if(getMinecraft().player != null) {
                    Optional<SecretScroll> scroll = getScroll();
                    if(scroll.isPresent()){
                        if(scroll.get().canLearn(getMinecraft().player.level(), getMinecraft().player)){
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        this.prevButton = new HTBookMarkButton(Button.builder(PREV, button -> {
            page = Math.clamp(page - 1, 0, manual.scrolls().size() - 1);
            NetworkHelper.sendToServer(new ServerManualPacket(false, hand == InteractionHand.MAIN_HAND, page));
            GuiUtil.playTurnPageSound();
        }).pos(this.leftPos - HTBookMarkButton.WIDTH + 10, this.topPos + 10)){
            @Override
            public boolean isActive() {
                return manual != null && manual.scrolls().size() > 1 && page > 0;
            }
        };
        this.nextButton = new HTBookMarkButton(Button.builder(NEXT, button -> {
            page = Math.clamp(page + 1, 0, manual.scrolls().size() - 1);
            NetworkHelper.sendToServer(new ServerManualPacket(false, hand == InteractionHand.MAIN_HAND, page));
            GuiUtil.playTurnPageSound();
        }).pos(this.leftPos - HTBookMarkButton.WIDTH + 10, this.topPos + 42)){
            @Override
            public boolean isActive() {
                return manual != null && manual.scrolls().size() > 1 && page < manual.scrolls().size() - 1;
            }
        };
        this.addRenderableWidget(this.learnButton);
        this.addRenderableWidget(this.prevButton);
        this.addRenderableWidget(this.nextButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        return this.scrollComponent.mouseScrolled(deltaX);
    }

    @Override
    public void tick() {
        super.tick();
        if(getMinecraft().player != null && ! getMinecraft().player.getItemInHand(hand).is(IMMItems.SECRET_MANUAL.get())){
            removed();
        } else if(manual == null){
            removed();
        } else {
            page = Math.clamp(page, 0, manual.scrolls().size() - 1);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        // 渲染主体界面。
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        SecretScroll scroll = manual.scrolls().get(page);
        Component title = scroll.getTitle();
        int headWidth = this.font.width(title);
        if(scroll.getRenderLogo().isPresent()) {
            headWidth += LOGO_WIDTH;
        }
        int leftX = this.leftPos + (this.imageWidth - headWidth) / 2;
        if(scroll.getRenderLogo().isPresent()){
            graphics.blit(TEXTURE, leftX, this.topPos + 15, 0, 196, 20, 20);
            graphics.blit(scroll.getRenderLogo().get(), leftX + 2, this.topPos + 15 + 2, 0, 0, 16, 16, 16, 16);
            // 学过的在法术上画个圈。
            if(getMinecraft().player != null && scroll.content() instanceof LearnSpellScroll spellScroll){
                if(PlayerUtil.hasLearnedSpell(getMinecraft().player, spellScroll.spellType(), spellScroll.level())){
                    graphics.blit(RenderUtil.WIDGETS, leftX - 1, this.topPos + 15 - 1, 15, 58, 22, 22);
                }
            }
            leftX += LOGO_WIDTH;
        }
        graphics.drawString(this.font, title, leftX, this.topPos + 22, Colors.WORD, false);
        // 渲染滚轮和各个灵根按钮。
        if (this.scrollComponent.canScroll()) {
            final int len = MathUtil.getBarLen(this.scrollComponent.getScrollPercent(), 106 - 19);
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 167, this.topPos + 46 + len, 8, 58, 6, 19);
        } else {
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 167, topPos + 46, 0, 58, 6, 19);
        }
        this.scrollComponent.renderItems(getMinecraft(), graphics);
        // 渲染页码
        if(manual.scrolls().size() > 1){
            RenderUtil.renderCenterScaledText(graphics.pose(), Component.literal(String.format("%d/%d", page + 1, manual.scrolls().size())), this.width >> 1, this.topPos + this.imageHeight - 16, 1F, Colors.WORD, ColorHelper.BLACK.rgb());
        }
    }

    @Override
    public List<ScrollText> getItems() {
        List<ScrollText> entries = new ArrayList<>();
        Optional<SecretScroll> scroll = getScroll();
        if(scroll.isPresent() && getMinecraft().player != null){
            // 前置条件。
            if(! scroll.get().requirements().isEmpty()){
                splitByRow(REQUIREMENTS).forEach(t -> entries.add(text(t, Colors.WORD)));
            }
            scroll.get().getRequirementInfo(getMinecraft().player).forEach(c -> {
                splitByRow(c).forEach(t -> entries.add(text(t)));
            });
            // 描述。
            splitByRow(DESCRIPTION).forEach(t -> entries.add(text(t, Colors.WORD)));
            splitByRow(scroll.get().getContentInfo()).forEach(t -> entries.add(text(t)));
        }
        return entries;
    }

    @Override
    public void renderItem(Level level, GuiGraphics graphics, ScrollText item, int slotId, int x, int y) {
        graphics.drawString(this.font, item.text(), x, y, 0, false);
    }

    @Override
    public void renderTooltip(Level level, GuiGraphics graphics, ScrollText item, int slotId, int x, int y) {

    }

    public List<FormattedCharSequence> splitByRow(FormattedText text){
        return this.font.split(text, 150);
    }

    public Optional<SecretScroll> getScroll(){
        if(manual != null) {
            page = Math.clamp(page, 0, manual.scrolls().size() - 1);
            return Optional.of(manual.scrolls().get(page));
        }
        return Optional.empty();
    }

    public static ScrollText text(FormattedCharSequence text, int color) {
        return new ScrollText(text, color);
    }

    public static ScrollText text(FormattedCharSequence text) {
        return text(text, ColorHelper.BLACK.rgb());
    }

    public record ScrollText(FormattedCharSequence text, int color) {

    }
}
