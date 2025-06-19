package hungteen.imm.client.gui.screen;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.TriggerCondition;
import hungteen.imm.client.gui.IScrollableScreen;
import hungteen.imm.client.gui.component.ScrollComponent;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.TriggerConditions;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.menu.InscriptionTableMenu;
import hungteen.imm.common.network.server.ServerInscriptionPacket;
import hungteen.imm.util.Colors;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-24 12:34
 **/
public class InscriptionTableScreen extends HTContainerScreen<InscriptionTableMenu> {

    private static final ResourceLocation TEXTURE = StringHelper.containerTexture(Util.id(), "inscription_table");
    private static final Component TITLE = TipUtil.gui("inscription_table");
    private static final int TOP_HEIGHT = InscriptionTableMenu.TOP_HEIGHT;
    private static final int BOTTOM_HEIGHT = InscriptionTableMenu.BOTTOM_HEIGHT;
    private static final int SMALL_WIDTH = InscriptionTableMenu.SMALL_WIDTH;
    private static final int LARGE_WIDTH = InscriptionTableMenu.LARGE_WIDTH;
    private static final int BOTTOM_WIDTH = InscriptionTableMenu.BOTTOM_WIDTH;
    private static final int GAP_HEIGHT = InscriptionTableMenu.GAP_HEIGHT;
    private static final int GAP_WIDTH = InscriptionTableMenu.GAP_WIDTH;
    private final ScrollComponent<Spell> spellScrollComponent;
    private final ScrollComponent<TriggerCondition> conditionScrollComponent;
    private final Player player;

    public InscriptionTableScreen(InscriptionTableMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = InscriptionTableMenu.getTotalHeight();
        this.imageWidth = InscriptionTableMenu.getTotalWidth();
        this.spellScrollComponent = new ScrollComponent<>(new InscriptionSpellScreen(this), 18, 18, 5, 3){
            @Override
            protected boolean onClick(Minecraft mc, Screen screen, Spell item, int slotId) {
                if (item == menu.getSelectedSpell()) {
                    menu.setSelectedSpell(null);
                } else {
                    menu.setSelectedSpell(item);
                }
                sendUpdatePacket();
                return true;
            }
        };
        this.conditionScrollComponent = new ScrollComponent<>(new InscriptionConditionScreen(this), 60, 16, 5, 1){
            @Override
            protected boolean onClick(Minecraft mc, Screen screen, TriggerCondition item, int slotId) {
                if (item == menu.getTriggerCondition()) {
                    menu.setTriggerCondition(null);
                } else {
                    menu.setTriggerCondition(item);
                }
                sendUpdatePacket();
                return true;
            }
        };
        this.player = inv.player;
    }

    public void sendUpdatePacket(){
        menu.updateSpellAndCondition(menu.getSelectedSpell(), menu.getTriggerCondition());
        NetworkHelper.sendToServer(new ServerInscriptionPacket(Optional.ofNullable(InscriptionTableScreen.this.menu.getSelectedSpell()), Optional.ofNullable(InscriptionTableScreen.this.menu.getTriggerCondition())));
    }

    @Override
    protected void init() {
        super.init();
        this.spellScrollComponent.setOffset(this.leftPos + 14, this.topPos + 11);
        this.spellScrollComponent.setInterval(1, 0);
        this.conditionScrollComponent.setOffset(this.leftPos + LARGE_WIDTH + SMALL_WIDTH + GAP_WIDTH * 2 + 12, this.topPos + 14);
        this.conditionScrollComponent.setInterval(0, 1);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if(mouseX < this.width >> 1) return this.spellScrollComponent.mouseScrolled(deltaX);
        return this.conditionScrollComponent.mouseScrolled(deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        if (!this.spellScrollComponent.mouseClicked(getMinecraft(), this, mouseX, mouseY, key)) {
            if (!this.conditionScrollComponent.mouseClicked(getMinecraft(), this, mouseX, mouseY, key)) {
                return super.mouseClicked(mouseX, mouseY, key);
            }
        }
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        RenderUtil.renderCenterScaledText(graphics.pose(), TITLE, this.width >> 1, this.topPos + 12, 1, Colors.WORD, ColorHelper.BLACK.rgb());
        this.spellScrollComponent.renderItems(getMinecraft(), graphics);
        this.conditionScrollComponent.renderItems(getMinecraft(), graphics);
        if(this.renderGhostItem()){
            RenderUtil.renderGhostItem(graphics, getGhostItem(), this.leftPos + this.menu.getSlotX(1), this.topPos + this.menu.getSlotY(1));
        }
        this.spellScrollComponent.renderTooltip(getMinecraft(), graphics, mouseX, mouseY);
        this.conditionScrollComponent.renderTooltip(getMinecraft(), graphics, mouseX, mouseY);
        this.renderTooltip(graphics, mouseX, mouseY);
        if(this.renderGhostItem() && this.minecraft != null && MathHelper.isInArea(mouseX, mouseY, this.leftPos + this.menu.getSlotX(1), this.topPos + this.menu.getSlotY(1), 16, 16)){
            ItemStack stack = getGhostItem();
            graphics.renderComponentTooltip(this.font, Screen.getTooltipFromItem(this.minecraft, stack), mouseX, mouseY, stack);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        //Dynamically render background.
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 74, 0, LARGE_WIDTH, TOP_HEIGHT);
        graphics.blit(TEXTURE, this.leftPos + LARGE_WIDTH + GAP_WIDTH, this.topPos, 0, 0, SMALL_WIDTH, TOP_HEIGHT);
        graphics.blit(TEXTURE, this.leftPos + LARGE_WIDTH + SMALL_WIDTH + GAP_WIDTH * 2, this.topPos, 74, 0, LARGE_WIDTH, TOP_HEIGHT);
        graphics.blit(TEXTURE, (this.width - BOTTOM_WIDTH) >> 1, this.topPos + TOP_HEIGHT + GAP_HEIGHT, 0, 124, BOTTOM_WIDTH, BOTTOM_HEIGHT);
        // render slots.
        for(int i = 0; i < this.spellScrollComponent.getRows(); ++ i){
            for(int j = 0; j < this.spellScrollComponent.getColumns(); ++ j){
                Pair<Integer, Integer> xy = this.spellScrollComponent.getXY(i, j);
                graphics.blit(TEXTURE, xy.getFirst(), xy.getSecond(), 0, 230, 20, 20);
            }
        }
        // render text bg.
        for(int i = 0; i < this.conditionScrollComponent.getRows(); ++ i){
            Pair<Integer, Integer> xy = this.conditionScrollComponent.getXY(i, 0);
            graphics.blit(TEXTURE, xy.getFirst(), xy.getSecond(), 20, 230, 61, 16);
        }
        // Render Scroll Bar.
        if (this.spellScrollComponent.canScroll()) {
            final int len = (int) ((90 - 19) * this.spellScrollComponent.getScrollPercent());
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 74, this.topPos + 11 + len, 0, 58, 6, 19);
        } else {
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 74, topPos + 11, 0, 58, 6, 19);
        }
        // Render Scroll Bar.
        if (this.conditionScrollComponent.canScroll()) {
            final int len = (int) ((90 - 19) * this.conditionScrollComponent.getScrollPercent());
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 74 + GAP_WIDTH * 2 + LARGE_WIDTH + SMALL_WIDTH, this.topPos + 11 + len, 0, 58, 6, 19);
        } else {
            graphics.blit(RenderUtil.WIDGETS, this.leftPos + 74 + GAP_WIDTH * 2 + LARGE_WIDTH + SMALL_WIDTH, topPos + 11, 0, 58, 6, 19);
        }
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
    }

    public boolean renderGhostItem(){
        return menu.getSelectedSpell() != null && !this.menu.getSlot(1).hasItem();
    }

    public ItemStack getGhostItem(){
        return new ItemStack(IMMItems.CINNABAR.get());
    }

    public static class InscriptionSpellScreen implements IScrollableScreen<Spell> {

        private final InscriptionTableScreen screen;

        public InscriptionSpellScreen(InscriptionTableScreen screen) {
            this.screen = screen;
        }

        @Override
        public List<Spell> getItems() {
            return screen.menu.spells;
        }

        @Override
        public void renderItem(Level level, GuiGraphics graphics, Spell item, int slotId, int x, int y) {
            graphics.blit(item.spell().getSpellTexture(), x + 1, y + 1, 0, 0, 16, 16, 16, 16);
            if(screen.menu.getSelectedSpell() == item){
                graphics.blit(RenderUtil.WIDGETS, x - 2, y - 2, 15, 58, 22, 22);
            }
        }

        @Override
        public void renderTooltip(Level level, GuiGraphics graphics, Spell item, int slotId, int x, int y) {
            List<Component> components = new ArrayList<>();
            components.addAll(SpellManager.getSpellTooltips(item));
            graphics.renderTooltip(screen.font, components, Optional.empty(), x, y);
        }
    }

    public static class InscriptionConditionScreen implements IScrollableScreen<TriggerCondition> {

        private final InscriptionTableScreen screen;

        public InscriptionConditionScreen(InscriptionTableScreen screen) {
            this.screen = screen;
        }

        @Override
        public List<TriggerCondition> getItems() {
            return screen.menu.conditions;
        }

        @Override
        public void renderItem(Level level, GuiGraphics graphics, TriggerCondition item, int slotId, int x, int y) {
            if(screen.menu.getTriggerCondition() == item){
                graphics.blit(TEXTURE, x, y, 81, 230, 61, 16);
            }
            RenderUtil.renderCenterScaledText(graphics.pose(), item.getComponent(), x + 30, y + 6, 0.7F, Colors.WORD, ColorHelper.BLACK.rgb());
        }

        @Override
        public void renderTooltip(Level level, GuiGraphics graphics, TriggerCondition item, int slotId, int x, int y) {
            List<Component> components = new ArrayList<>();
            components.add(item.getDescription());
            components.add(TriggerConditions.getSlotComponent(item));
            graphics.renderTooltip(screen.font, components, Optional.empty(), x, y);
        }
    }

}
