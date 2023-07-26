package hungteen.imm.client.gui.screen.meditation;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.client.gui.screen.HTScreen;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.ScreenButtonPacket;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;

import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/21 16:56
 */
public abstract class MeditationScreen extends HTScreen {

    private static final ResourceLocation SLOTS = Util.get().containerTexture("meditation");
    private static final int QUIT_BUTTON_WIDTH = 200;
    private static final int QUIT_TEXT_HEIGHT = 20;
    private MeditationTypes type;
    private Button quitButton;

    public MeditationScreen(MeditationTypes type) {
        this.type = type;
    }

    @Override
    protected void init() {
        super.init();
//        this.quitButton = Button.builder(Component.translatable("multiplayer.stopSleeping"), (p_96074_) -> {
//            this.sendWakeUp();
//        }).bounds((this.width - QUIT_BUTTON_WIDTH) / 2, this.height - 40, QUIT_BUTTON_WIDTH, QUIT_BUTTON_HEIGHT).build();
//        this.addRenderableWidget(this.quitButton);
    }

    /**
     * {@link hungteen.imm.client.event.ClientEvents#tick(TickEvent.ClientTickEvent)}
     */
    public static void tickMeditation(){
        if (ClientUtil.screen() == null && ClientUtil.player() != null) {
            if(PlayerUtil.isSitInMeditation(ClientUtil.player())){
                ClientProxy.mc().setScreen(new RestingScreen());
            }
        }
        if(ClientUtil.screen() instanceof MeditationScreen){
            if(! PlayerUtil.isSitInMeditation(ClientUtil.player())){
                ClientProxy.mc().setScreen(null);
            }
        }
    }

    public static boolean canRenderMeditation() {
        return ClientProxy.MC.screen instanceof MeditationScreen;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBg(graphics, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks){

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        return super.mouseClicked(mouseX, mouseY, key);
    }

    @Override
    public void onClose() {
        this.sendWakeUp();
    }

    @Override
    public boolean keyPressed(int key, int code, int p_96072_) {
        // Press Escape to wake up.
        if (key == InputConstants.KEY_ESCAPE || key == InputConstants.KEY_E) {
            this.sendWakeUp();
        }
        // Previous page.
        if (key == InputConstants.KEY_LEFT || key == InputConstants.KEY_A) {
            switchScreen(-1);
        }
        // Next page.
        if (key == InputConstants.KEY_RIGHT || key == InputConstants.KEY_D) {
            switchScreen(1);
        }
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void sendWakeUp() {
        NetworkHandler.sendToServer(new ScreenButtonPacket(ScreenButtonPacket.Types.QUIT_MEDITATION));
    }

    protected void switchScreen(int val){
        final int id = this.type.ordinal();
        final int nextId = (id + val + getLen()) % getLen();
        switchScreen(MeditationTypes.values()[nextId]);
    }

    protected void switchScreen(MeditationTypes type){
        switch (type){
            case REST -> getMinecraft().setScreen(new RestingScreen());
            case SPELL -> getMinecraft().setScreen(new SpellScreen());
        }
    }

    protected int getLen(){
        return MeditationTypes.values().length;
    }

    public enum MeditationTypes {

        CULTIVATION(() -> new ItemStack(IMMItems.FIVE_FLOWERS_ELIXIR.get())),

        REST(() -> new ItemStack(Blocks.RED_BED)),

        SPELL(() -> new ItemStack(IMMItems.SECRET_MANUAL.get())),

//        BREAK_THROUGH(() -> new ItemStack(I))

        ;

        private final Supplier<ItemStack> supplier;

        MeditationTypes(Supplier<ItemStack> supplier) {
            this.supplier = supplier;
        }

        public ItemStack getItem(){
            return supplier.get();
        }

    }

}