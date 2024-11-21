package hungteen.imm.client.gui.screen.meditation;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.IMMClientProxy;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.client.event.handler.SpellHandler;
import hungteen.imm.client.gui.GuiUtil;
import hungteen.imm.client.gui.IScrollableScreen;
import hungteen.imm.client.gui.component.HTButton;
import hungteen.imm.client.gui.component.ScrollComponent;
import hungteen.imm.client.gui.overlay.SpellOverlay;
import hungteen.imm.common.network.SpellPacket;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-24 21:40
 **/
public class SpellScreen extends MeditationScreen implements IScrollableScreen<ISpellType> {

    private static final ResourceLocation SPELL_CIRCLE = SpellOverlay.TEXTURE;
    private static final int MENU_HEIGHT = 129;
    private static final int CIRCLE_LEN = SpellOverlay.CIRCLE_LEN;
    private static final int SPELL_SLOT_LEN = SpellOverlay.SPELL_SLOT_LEN;
    private static final int BUTTON_WIDTH = 38;
    private static final int BUTTON_HEIGHT = 28;
    private final ScrollComponent<ISpellType> scrollComponent;
    private ModeButton modeButton;
    private Component displayText;
    private int selectPos = 0; // 0 means no selection, negative means selected on spell circle, or circle list.
    private int displayTick = 0;

    public SpellScreen() {
        super(MeditationTypes.SPELL);
        this.scrollComponent = new ScrollComponent<>(this, 16, 6, 5) {
            @Override
            protected boolean onClick(Minecraft mc, Screen screen, ISpellType spell, int slotId) {
                if (spell.canPlaceOnCircle()) {
                    if (SpellScreen.this.noSelection() || SpellScreen.this.selectOnList()) {
                        SpellScreen.this.selectPos = slotId + 1; // 选择当前法术。
                    } else if (SpellScreen.this.selectOnCircle()) {
                        // 将选择的法术放在轮盘上。
                        final int circlePos = - SpellScreen.this.selectPos - 1;
                        setSpellAt(circlePos, spell);
                        SpellScreen.this.selectPos = 0; // reset.
                    }
                    return true;
                }
                SpellScreen.this.warn(TipUtil.gui("meditation.can_not_select"), 100);
                return false;
            }
        };
        this.imageWidth = CIRCLE_LEN << 1;
        this.imageHeight = MENU_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        this.scrollComponent.setOffset(this.leftPos + 15, this.topPos + 13);
        this.scrollComponent.setInterval(2);
        this.modeButton = new ModeButton(Button.builder(TipUtil.gui("meditation.mode"), (button) -> {
            if (SpellHandler.useDefaultCircle()) {
                button.setTooltip(Tooltip.create(TipUtil.gui("meditation.to_move_mode")));
            } else {
                button.setTooltip(Tooltip.create(TipUtil.gui("meditation.to_scroll_mode")));
            }
            SpellHandler.changeCircleMode();
            NetworkHelper.sendToServer(new SpellPacket(SpellPacket.SpellOption.CHANGE_CIRCLE_MODE, PlayerUtil.getSpellCircleMode(ClientUtil.player())));
        }).pos(this.leftPos - BUTTON_WIDTH, this.topPos).tooltip(Tooltip.create(
                TipUtil.gui("meditation." + (SpellHandler.useDefaultCircle() ? "to_scroll_mode" : "to_move_mode"))
        )));
        this.addRenderableWidget(this.modeButton);
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
        // Render Scroll Bar.
        if (this.scrollComponent.canScroll()) {
            final int len = (int) ((106 - 19) * this.scrollComponent.getScrollPercent());
            graphics.blit(SPELL_CIRCLE, this.leftPos + 107, this.topPos + 13 + len, 48, 128, 6, 19);
        } else {
            graphics.blit(SPELL_CIRCLE, this.leftPos + 107, topPos + 13, 40, 128, 6, 19);
        }
        // Render Spell List.
        this.scrollComponent.renderItems(getMinecraft(), graphics);
        // Render Selected Spell.
        if (this.selectOnList() && this.scrollComponent.inPage(this.selectPos - 1)) {
            final Pair<Integer, Integer> xy = this.scrollComponent.getXY(this.selectPos - 1);
            graphics.blit(SPELL_CIRCLE, xy.getFirst() - 3, xy.getSecond() - 3, 128, 136, 22, 22);
        }
        if (this.displayTick > 0 && this.displayText != null) {
            graphics.drawCenteredString(font, this.displayText, this.width >> 1, this.topPos + this.imageHeight + 10, ColorHelper.RED.rgb());
        }
        // Render Circle Spell Tooltip.
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; ++i) {
            final int x = SpellOverlay.getSlotX(leftPos + CIRCLE_LEN * 3 / 2, i);
            final int y = SpellOverlay.getSlotY(topPos + CIRCLE_LEN / 2, i);
            final ISpellType spell = PlayerUtil.getSpellAt(IMMClientProxy.MC.player, i);
            if (spell != null && MathHelper.isInArea(mouseX, mouseY, x, y, SPELL_SLOT_LEN, SPELL_SLOT_LEN)) {
                graphics.renderTooltip(font, spell.getComponent(), mouseX, mouseY);
            }
        }
        // Render Spell Tooltip.
        this.scrollComponent.renderTooltip(getMinecraft(), graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks) {
        super.renderBg(graphics, partialTicks);
        graphics.blit(SPELL_CIRCLE, this.leftPos, this.topPos, CIRCLE_LEN, 0, CIRCLE_LEN, MENU_HEIGHT);
        this.renderSpellCircle(graphics);
    }

    public void renderSpellCircle(GuiGraphics graphics) {
        RenderSystem.enableBlend();
        graphics.blit(SPELL_CIRCLE, leftPos + CIRCLE_LEN, topPos, 0, 0, CIRCLE_LEN, CIRCLE_LEN);
        // Render Spell Slots.
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; ++i) {
            final boolean isSelected = (i + 1 + this.selectPos == 0); // selected this one.
            // Render the empty spell slot.
            final int x = SpellOverlay.getSlotX(leftPos + CIRCLE_LEN * 3 / 2, i);
            final int y = SpellOverlay.getSlotY(topPos + CIRCLE_LEN / 2, i);
            graphics.blit(SPELL_CIRCLE, x, y, isSelected ? 20 : 0, 128, SPELL_SLOT_LEN, SPELL_SLOT_LEN);
            // Render the spell texture.
            final ISpellType spell = PlayerUtil.getSpellAt(IMMClientProxy.MC.player, i);
            if (spell != null) {
                graphics.blit(spell.getSpellTexture(), x + 2, y + 2, 0, 0, 16, 16, 16, 16);
            }
            // Render Selected.
            if (isSelected) {
                graphics.blit(SPELL_CIRCLE, x - 1, y - 1, 128, 136, 22, 22);
            }
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
            for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; ++i) {
                final int x = SpellOverlay.getSlotX(leftPos + CIRCLE_LEN * 3 / 2, i);
                final int y = SpellOverlay.getSlotY(topPos + CIRCLE_LEN / 2, i);
                if (MathUtil.inSlotArea(mouseX, mouseY, x, y, SPELL_SLOT_LEN, SPELL_SLOT_LEN)) {
                    final ISpellType curSpell = PlayerUtil.getSpellAt(IMMClientProxy.MC.player, i);
                    if (this.noSelection()) {
                        this.selectPos = - i - 1; // selected this.
                    } else if (this.selectOnCircle()) {
                        // 交换轮盘上法术的位置
                        final int lastSlotId = - this.selectPos - 1;
                        final ISpellType oldSpell = PlayerUtil.getSpellAt(IMMClientProxy.MC.player, lastSlotId);
                        setSpellAt(i, oldSpell);
                        setSpellAt(lastSlotId, curSpell);
                        this.selectPos = 0; // reset.
                    } else {
                        final ISpellType spellType = getItems().get(this.selectPos - 1);
                        setSpellAt(i, spellType);
                        this.selectPos = 0; // reset.
                    }
                    GuiUtil.playDownSound();
                    return true;
                }
            }
            return super.mouseClicked(mouseX, mouseY, key);
        }
        return true;
    }

    @Override
    public List<ISpellType> getItems() {
        return SpellTypes.registry().getValues().stream()
                .filter(type -> PlayerUtil.hasLearnedSpell(getMinecraft().player, type))
                .sorted(Comparator.comparingInt(ISpellType::getPriority))
                .sorted(Comparator.comparingInt(ISpellType::getModPriority))
                .toList();
    }

    @Override
    public void renderItem(Level level, GuiGraphics graphics, ISpellType item, int id, int x, int y) {
        graphics.blit(item.getSpellTexture(), x, y, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void renderTooltip(Level level, GuiGraphics graphics, ISpellType item, int id, int x, int y) {
        final List<Component> components = new ArrayList<>();
        final int lvl = PlayerUtil.getSpellLevel(ClientUtil.player(), item);
        components.add(item.getComponent().append("(" + lvl + "/" + item.getMaxLevel() + ")").withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD));
        for (int i = 1; i <= lvl; ++i) {
            components.add(item.getSpellDesc(i).withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.ITALIC));
        }
        components.add(SpellManager.getCostComponent(item.getConsumeMana()).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE));
        components.add(SpellManager.getCDComponent(item.getCooldown()).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE));
        if (!item.canPlaceOnCircle()) {
            components.add(TipUtil.tooltip("can_not_select").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }
        graphics.renderTooltip(font, components, Optional.empty(), x, y);
    }

    public void setSpellAt(int pos, @Nullable ISpellType spell) {
        if (spell != null && spell.canPlaceOnCircle()) {
            NetworkHelper.sendToServer(new SpellPacket(spell, SpellPacket.SpellOption.SET_SPELL_ON_CIRCLE, pos));
        } else if(spell == null){
            NetworkHelper.sendToServer(new SpellPacket(SpellPacket.SpellOption.REMOVE_SPELL_ON_CIRCLE, pos));
        }
    }

    private void warn(Component component, int time) {
        this.displayText = component;
        this.displayTick = time;
    }

    private boolean noSelection() {
        return this.selectPos == 0;
    }

    private boolean selectOnList() {
        return this.selectPos > 0 && this.selectPos <= this.getItems().size();
    }

    private boolean selectOnCircle() {
        return this.selectPos >= -Constants.SPELL_CIRCLE_SIZE && this.selectPos < 0;
    }

    static class ModeButton extends HTButton {

        protected ModeButton(Builder builder) {
            super(builder.size(38, 18), RenderUtil.WIDGETS);
        }

        @Override
        protected int getColor(boolean active) {
            return active ? Colors.WORD : super.getColor(false);
        }

        @Override
        protected int getTextureY() {
            return !this.isActive() ? 0 : this.isHovered() ? 20 : 40;
        }
    }
}
