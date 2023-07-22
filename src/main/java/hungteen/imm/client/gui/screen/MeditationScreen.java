package hungteen.imm.client.gui.screen;

import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.ScreenButtonPacket;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/21 16:56
 */
public class MeditationScreen extends Screen {

    private Button leaveBedButton;
    private int selectedIndex = -1;

    public MeditationScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        this.leaveBedButton = Button.builder(Component.translatable("multiplayer.stopSleeping"), (p_96074_) -> {
            this.sendWakeUp();
        }).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build();
        this.addRenderableWidget(this.leaveBedButton);
    }

    /**
     * {@link hungteen.imm.client.event.ClientEvents#tick(TickEvent.ClientTickEvent)}
     */
    public static void tickMeditation(){
        if (ClientUtil.screen() == null && ClientUtil.player() != null) {
            if(PlayerUtil.isSitInMeditation(ClientUtil.player())){
                ClientProxy.mc().setScreen(new MeditationScreen());
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.selectedIndex = Mth.clamp(this.selectedIndex + (delta > 0 ? 1 : -1), 0, MeditationTypes.values().length);
        return true;
    }

    @Override
    public void onClose() {
        this.sendWakeUp();
    }

    @Override
    public boolean keyPressed(int key, int p_96071_, int p_96072_) {
        // Press Escape to wake up.
        if (key == 256) {
            this.sendWakeUp();
        }
        return true;
    }

    private void sendWakeUp() {
        NetworkHandler.sendToServer(new ScreenButtonPacket(ScreenButtonPacket.Types.QUIT_MEDITATION));
    }

    public enum MeditationTypes {

        CULTIVATION,

        SPELL,

        ;
    }

}
