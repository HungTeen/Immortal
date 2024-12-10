package hungteen.imm.client.gui.screen.meditation;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.client.gui.IScrollableScreen;
import hungteen.imm.client.gui.component.ScrollComponent;
import hungteen.imm.client.gui.overlay.ElementOverlay;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.KarmaManager;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.spell.conscious.SpiritEyeSpell;
import hungteen.imm.util.Colors;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/27 14:42
 */
public class CultivationScreen extends MeditationScreen implements IScrollableScreen<CultivationScreen.CultivationEntries> {

    private static final int LIVING_WIDTH = 46;
    private static final int LIVING_HEIGHT = 66;
    private static final int PROGRESS_BAR_WIDTH = 101;
    private static final int PROGRESS_BAR_HEIGHT = 5;
    private static final int KARMA_WIDTH = 40;
    private static final int KARMA_HEIGHT = 18;
    private final ScrollComponent<CultivationEntries> scrollComponent;

    public CultivationScreen() {
        super(MeditationType.CULTIVATION);
        this.imageWidth = 214;
        this.imageHeight = 129;
        this.scrollComponent = new ScrollComponent<>(this, 100, 20, 5, 1);
    }

    @Override
    protected void init() {
        super.init();
        this.scrollComponent.setOffset(this.leftPos + 85, this.topPos + 14);
        this.scrollComponent.setInterval(0, 1);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        return this.scrollComponent.mouseScrolled(deltaX);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        final int x = this.leftPos + 21 + (LIVING_WIDTH >> 1);
        final int y = this.topPos + 14 + LIVING_HEIGHT + 10;
        RenderUtil.renderEntityInInventoryFollowsMouse(graphics, x, y, 30, x - mouseX, y - mouseY, ClientUtil.player());
        this.renderInfo(graphics);
        // Render Scroll Bar.
        if (this.scrollComponent.canScroll()) {
            final int len = MathUtil.getBarLen(this.scrollComponent.getScrollPercent(), 106 - 19);
            graphics.blit(TEXTURE, this.leftPos + 193, this.topPos + 13 + len, 94, 150, 6, 19);
        } else {
            graphics.blit(TEXTURE, this.leftPos + 193, topPos + 13, 102, 150, 6, 19);
        }
        this.scrollComponent.renderItems(getMinecraft(), graphics);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks) {
        super.renderBg(graphics, partialTicks);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    private void renderInfo(GuiGraphics graphics) {
        final int posX = this.leftPos + 11;
        int posY = this.topPos + 84;
        this.renderSpiritualRoots(graphics, posX, posY);
        posY += 12;
        RenderUtil.renderCenterScaledText(graphics.pose(), PlayerUtil.getPlayerRealm(ClientUtil.player()).getComponent(), posX + 32, posY, 0.6F, Colors.WORD, ColorHelper.BLACK.rgb());
    }

    private void renderSpiritualRoots(GuiGraphics graphics, int posX, int posY) {
        if (SpiritEyeSpell.knowOwnSpiritRoots(ClientUtil.player())) {
            final List<QiRootType> roots = PlayerUtil.filterSpiritRoots(ClientUtil.player(), PlayerUtil.getRoots(ClientUtil.player()));
            final int len = roots.size();
            if(len == 0){
                RenderUtil.renderCenterScaledText(graphics.pose(), TipUtil.misc("no_spiritual_root"), posX + 32, posY + 1, 1F, ColorHelper.RED.rgb(), ColorHelper.BLACK.rgb());
            } else {
                final int interval = (65 - len * ElementOverlay.ELEMENT_LEN) / (len + 1);
                posX += interval;
                for (QiRootType root : roots) {
                    var pair = root.getTexturePos();
                    graphics.blit(root.getTexture(), posX, posY, pair.getFirst(), pair.getSecond(), ElementOverlay.ELEMENT_LEN, ElementOverlay.ELEMENT_LEN);
                    posX += interval + ElementOverlay.ELEMENT_LEN;
                }
            }
        } else {
            RenderUtil.renderCenterScaledText(graphics.pose(), QiRootTypes.getCategory().append(" : ").append(TipUtil.UNKNOWN), posX + 32, posY + 1, 1F, Colors.WORD, ColorHelper.BLACK.rgb());
        }
    }

    @Override
    public List<CultivationEntries> getItems() {
        return Arrays.stream(CultivationEntries.values()).filter(l -> {
            return PlayerHelper.getClientPlayer().map(l::canDisplay).orElse(false);
        }).toList();
    }

    @Override
    public void renderTooltip(Level level, GuiGraphics graphics, CultivationEntries item, int slotId, int x, int y) {

    }

    @Override
    public void renderItem(Level level, GuiGraphics graphics, CultivationEntries item, int slotId, int x, int y) {
        final Player player = ClientUtil.player();
        switch (item) {
            case ELIXIR_CULTIVATION -> {
                this.renderProgressWithText(graphics, CultivationManager.getExperienceComponent(ExperienceType.ELIXIR), x, y, PlayerUtil.getExperience(player, ExperienceType.ELIXIR), CultivationManager.getMaxExperience(player), false);
            }
            case FIGHT_CULTIVATION -> {
                this.renderProgressWithText(graphics, CultivationManager.getExperienceComponent(ExperienceType.FIGHTING), x, y, PlayerUtil.getExperience(player, ExperienceType.FIGHTING), CultivationManager.getMaxExperience(player), false);
            }
            case LEARN_CULTIVATION -> {
                this.renderProgressWithText(graphics, CultivationManager.getExperienceComponent(ExperienceType.SPELL), x, y, PlayerUtil.getExperience(player, ExperienceType.SPELL), CultivationManager.getMaxExperience(player), false);
            }
//            case MISSION_CULTIVATION -> {
//                this.renderProgressWithText(graphics, RealmManager.getExperienceComponent(ExperienceTypes.MISSION), x, y, PlayerUtil.getExperience(player, ExperienceTypes.MISSION), CultivationManager.getMaxExperience(player), false);
//            }
            case PERSONALITY_CULTIVATION -> {
                this.renderProgressWithText(graphics, CultivationManager.getExperienceComponent(ExperienceType.PERSONALITY), x, y, PlayerUtil.getExperience(player, ExperienceType.PERSONALITY), CultivationManager.getMaxExperience(player), false);
            }
            case CULTIVATION -> {
                this.renderProgressWithText(graphics, CultivationManager.getCultivation(), x, y, CultivationManager.getCultivation(player), CultivationManager.getMaxCultivation(player), false);
            }
            case KARMA -> {
                final float value = KarmaManager.calculateKarma(ClientUtil.player());
                final int width = MathUtil.getBarLen(value, KarmaManager.MAX_KARMA_VALUE, PROGRESS_BAR_WIDTH - 2);
                int now = 0;
                while(now + KARMA_WIDTH <= width){
                    graphics.blit(TEXTURE, x + now, y + 1, 133, 150, KARMA_WIDTH, KARMA_HEIGHT);
                    now += KARMA_WIDTH;
                }
                if(now < width){
                    graphics.blit(TEXTURE, x + now, y + 1, 133, 150, width - now, KARMA_HEIGHT);
                }
                this.renderProgressWithText(graphics, IMMPlayerData.FloatData.KARMA.getComponent(), x, y, value, KarmaManager.MAX_KARMA_VALUE, true);
            }
            case SPIRITUAL_MANA -> {
                this.renderProgressWithText(graphics, IMMPlayerData.FloatData.QI_AMOUNT.getComponent(), x, y, PlayerUtil.getQiAmount(player), (float) PlayerUtil.getMaxQi(player), false);
            }
            case BREAK_THROUGH_PROGRESS -> {
                this.renderProgressWithText(graphics, IMMPlayerData.FloatData.BREAK_THROUGH_PROGRESS.getComponent(), x, y, PlayerUtil.getFloatData(player, IMMPlayerData.FloatData.BREAK_THROUGH_PROGRESS), false);
            }
//            case EMP -> {
//                final int emp = PlayerUtil.getIntegerData(player, PlayerRangeIntegers.ELEMENTAL_MASTERY_POINTS);
//                this.renderProgressWithText(graphics, PlayerRangeIntegers.ELEMENTAL_MASTERY_POINTS.getComponent(), x, y, emp, emp + ElementalMasterySpell.calculateUsedEMP(player), false);
//            }
        }
    }

    private void renderProgressWithText(GuiGraphics graphics, MutableComponent title, int x, int y, float value, float maxValue, boolean karma) {
        this.renderProgress(graphics, x, y, value, maxValue, karma);
        RenderUtil.renderScaledText(graphics.pose(), title.append(": ").append(String.format("%.1f / %.1f", Math.min(maxValue, value), maxValue)), x, y + 4, 0.8F, Colors.WORD, ColorHelper.BLACK.rgb());
    }

    private void renderProgressWithText(GuiGraphics graphics, MutableComponent title, int x, int y, float percent, boolean karma) {
        this.renderProgress(graphics, x, y, percent, karma);
        RenderUtil.renderScaledText(graphics.pose(), title.append(": ").append(TipUtil.PERCENT.apply(percent)), x, y + 4, 0.8F, Colors.WORD, ColorHelper.BLACK.rgb());
    }

    private void renderProgress(GuiGraphics graphics, int x, int y, float value, float maxValue, boolean karma) {
        this.renderProgress(graphics, x, y, MathUtil.getBarLen(value, maxValue, PROGRESS_BAR_WIDTH - 2), karma);
    }

    private void renderProgress(GuiGraphics graphics, int x, int y, float percent, boolean karma) {
        this.renderProgress(graphics, x, y, MathUtil.getBarLen(percent, PROGRESS_BAR_WIDTH - 2), karma);
    }

    private void renderProgress(GuiGraphics graphics, int x, int y, int width, boolean karma) {
        graphics.blit(TEXTURE, x, y + 14, 94, karma ? 140 : 130, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        graphics.blit(TEXTURE, x + 1, y + 14 + 1, 95, karma ? 146 : 136, width, PROGRESS_BAR_HEIGHT - 2);
    }

    private static boolean isSpiritual(Player player){
        return PlayerUtil.getPlayerRealm(player).getCultivationType().requireQi();
    }

    public enum CultivationEntries {

        ELIXIR_CULTIVATION,

        FIGHT_CULTIVATION(CultivationScreen::isSpiritual),

        LEARN_CULTIVATION(CultivationScreen::isSpiritual),

//        MISSION_CULTIVATION,

        PERSONALITY_CULTIVATION(CultivationScreen::isSpiritual),

        CULTIVATION(CultivationScreen::isSpiritual),

        KARMA,

        SPIRITUAL_MANA(CultivationScreen::isSpiritual),

        BREAK_THROUGH_PROGRESS(CultivationScreen::isSpiritual),

//        EMP

        ;

        private final Predicate<Player> canDisplay;

        CultivationEntries() {
            this.canDisplay = JavaHelper::alwaysTrue;
        }

        CultivationEntries(Predicate<Player> canDisplay) {
            this.canDisplay = canDisplay;
        }

        public boolean canDisplay(Player player) {
            return canDisplay.test(player);
        }

    }

}
