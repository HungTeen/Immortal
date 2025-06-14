package hungteen.imm.client.gui.screen.meditation;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.client.gui.component.HTButton;
import hungteen.imm.client.gui.overlay.CommonOverlay;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.network.server.ScreenOperationPacket;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.util.Colors;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-09 21:34
 **/
public class RestingScreen extends MeditationScreen {

    private static final ResourceLocation OVERLAY = CommonOverlay.OVERLAY;
    private static final int MANA_BAR_LEN = CommonOverlay.MANA_BAR_LEN;
    private static final int MANA_BAR_HEIGHT = CommonOverlay.MANA_BAR_HEIGHT;
    private static final int BUTTON_WIDTH = 106;
    private static final int BUTTON_HEIGHT = 28;
    private static final int BUTTON_DISTANCE = 40;
    private MeditationButton breakThroughOrMeditationButton;
    private MeditationButton spawnPointOrQuitButton;
    private MeditationButton quitButton;

    public RestingScreen() {
        super(MeditationType.RESTING);
    }

    @Override
    protected void init() {
        super.init();
        final int x = (this.width - BUTTON_WIDTH) >> 1;
        final int y = (this.height - BUTTON_HEIGHT) >> 1;
        this.breakThroughOrMeditationButton = new MeditationButton(Button.builder(TipUtil.gui("meditation.meditation"), (button) -> {
            NetworkHelper.sendToServer(new ScreenOperationPacket(isInSpiritWorld() ? ScreenOperationPacket.OperationType.BREAK_THROUGH : ScreenOperationPacket.OperationType.MEDITATE));
        }).pos(x, y - BUTTON_DISTANCE).tooltip(Tooltip.create(TipUtil.gui("meditation.meditation_button")))) {
            @Override
            public boolean isActive() {
                if(! PlayerUtil.sitOnCushion(ClientUtil.player())){
                    return false;
                }
                return CultivationManager.canBreakThrough(ClientUtil.player());
            }

            @Override
            public Component getMessage() {
                return isInSpiritWorld() ? TipUtil.gui("meditation.break_through") : super.getMessage();
            }

        };
        this.spawnPointOrQuitButton = new MeditationButton(Button.builder(TipUtil.gui("meditation.quit_meditation"), (button) -> {
            NetworkHelper.sendToServer(new ScreenOperationPacket(isInSpiritWorld() ? ScreenOperationPacket.OperationType.QUIT_MEDITATION : ScreenOperationPacket.OperationType.SET_SPAWN_POINT));
        }).pos(x, y).tooltip(Tooltip.create(TipUtil.gui("meditation.set_spawn_point_button")))){
            @Override
            public Component getMessage() {
                return isInSpiritWorld() ? super.getMessage() : TipUtil.gui("meditation.set_spawn_point");
            }
        };
        this.quitButton = new MeditationButton(Button.builder(TipUtil.gui("meditation.quit_resting"), (button) -> {
            this.sendWakeUp();
        }).pos(x, y + BUTTON_DISTANCE).tooltip(Tooltip.create(TipUtil.gui("meditation.quit_resting_button"))));
        this.addRenderableWidget(this.breakThroughOrMeditationButton);
        this.addRenderableWidget(this.spawnPointOrQuitButton);
        this.addRenderableWidget(this.quitButton);
        if(isInSpiritWorld()){
            this.breakThroughOrMeditationButton.setTooltip(Tooltip.create(TipUtil.gui("meditation.break_through_button")));
            this.spawnPointOrQuitButton.setTooltip(Tooltip.create(TipUtil.gui("meditation.quit_meditation_button")));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        final int x = (this.width >> 1) - 91;
        int y = this.height - 32 + 3;
        if(ClientUtil.player() != null){
            CommonOverlay.renderSpiritualMana(graphics, ClientUtil.player(), this.width, this.height, x, y);
            // Render Break Through Bar.
            if (CultivationManager.reachThreshold(ClientUtil.player())) {
                y -= 20;
                graphics.blit(OVERLAY, x, y, 0, 0, MANA_BAR_LEN, MANA_BAR_HEIGHT);
                final float progress = CultivationManager.getBreakThroughProgress(ClientUtil.player());
                final int backManaLen = Mth.floor((MANA_BAR_LEN - 2) * progress);
                graphics.blit(OVERLAY, x + 1, y + 1, 1, 16, backManaLen, MANA_BAR_HEIGHT - 1);
                final float scale = 1;
                final Component text = TipUtil.PERCENT.apply(progress);
                RenderUtil.renderCenterScaledText(graphics.pose(), text, (width >> 1), y - 6, scale, ColorHelper.GOLD.rgb(), ColorHelper.BLACK.rgb());
            }
        }
    }

    public boolean isInSpiritWorld() {
        return ClientUtil.playerOpt().map(player -> player.level().dimension().equals(IMMLevels.SPIRIT_WORLD)).orElse(false);
    }

    static class MeditationButton extends HTButton {

        protected MeditationButton(Builder builder) {
            super(builder.size(BUTTON_WIDTH, BUTTON_HEIGHT), RestingScreen.TEXTURE);
        }

        @Override
        protected int getColor(boolean active) {
            return active ? Colors.WORD : super.getColor(false);
        }

        @Override
        protected int getTextureY() {
            return !this.isActive() ? 228 : this.isHovered() ? 199 : 170;
        }
    }

}
