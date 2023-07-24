package hungteen.imm.client.gui.screen.meditation;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.client.gui.IScrollableScreen;
import hungteen.imm.client.gui.component.ScrollComponent;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-24 21:40
 **/
public class SpellScreen extends MeditationScreen implements IScrollableScreen<ISpellType> {

    private static final ResourceLocation SPELL = Util.get().overlayTexture("spell_circle");
    private static final int CIRCLE_LEN = 128;
    private static final int MENU_HEIGHT = 129;
    private final ScrollComponent<ISpellType> scrollComponent;

    public SpellScreen() {
        super(MeditationTypes.SPELL);
        this.scrollComponent = new ScrollComponent<>(this, 16, 6, 5);
        this.imageWidth = CIRCLE_LEN << 1;
        this.imageHeight = MENU_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        this.scrollComponent.setOffset(this.leftPos + 15, this.topPos + 13);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.scrollComponent.mouseScrolled(delta);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        // Render Scroll Bar.
        if (this.scrollComponent.canScroll()) {
            final int len = (int)((106 - 19) * this.scrollComponent.getScrollPercent());
            graphics.blit(SPELL, this.leftPos + 107, this.topPos + 13 + len, 48, 128, 6, 19);
        } else {
            graphics.blit(SPELL, this.leftPos + 107, topPos + 13, 40, 128, 6, 19);
        }
        this.scrollComponent.renderItems(getMinecraft(), graphics);
        this.scrollComponent.renderTooltip(getMinecraft(), graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks) {
        super.renderBg(graphics, partialTicks);
        graphics.blit(SPELL, this.leftPos, this.topPos, CIRCLE_LEN, 0, CIRCLE_LEN, MENU_HEIGHT);
        RenderSystem.enableBlend();
        graphics.blit(SPELL, this.leftPos + CIRCLE_LEN, this.topPos, 0, 0, CIRCLE_LEN, CIRCLE_LEN);
    }

    @Override
    public boolean keyPressed(int key, int code, int p_96072_) {
        final InputConstants.Key mouseKey = InputConstants.getKey(key, code);
        return super.keyPressed(key, code, p_96072_);
    }

    @Override
    public List<ISpellType> getItems() {
        return SpellTypes.registry().getValues().stream()
                .filter(type -> PlayerUtil.hasLearnedSpell(getMinecraft().player, type)).toList();
    }

    @Override
    public void renderItem(Level level, GuiGraphics graphics, ISpellType item, int x, int y) {

    }

    @Override
    public void renderTooltip(Level level, GuiGraphics graphics, ISpellType item, int x, int y) {

    }
}
