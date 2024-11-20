package hungteen.imm.client.gui.screen.meditation;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.client.gui.screen.HTScreen;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.IMMClientProxy;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.common.block.WoolCushionBlock;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.network.ScreenButtonPacket;
import hungteen.imm.util.Colors;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/21 16:56
 */
public abstract class MeditationScreen extends HTScreen {

    protected static final ResourceLocation TEXTURE = Util.get().containerTexture("cultivation");
    private static final int SWITCH_BAR_WIDTH = 92;
    private static final int SWITCH_BAR_HEIGHT = 39;
    private static final int SWITCH_BAR_Y_OFFSET = 5;
    private static final int QUIT_BUTTON_WIDTH = 200;
    private static final int QUIT_TEXT_HEIGHT = 20;
    private MeditationTypes type;

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
     * {@link hungteen.imm.client.event.ClientEvents#tick(ClientTickEvent.Post)}
     */
    public static void tickMeditation(){
        if (ClientUtil.screen() == null && ClientUtil.player() != null) {
            if(PlayerUtil.isSitInMeditation(ClientUtil.player())){
                IMMClientProxy.mc().setScreen(new RestingScreen());
            }
        }
        if(ClientUtil.screen() instanceof MeditationScreen){
            if(! PlayerUtil.isSitInMeditation(ClientUtil.player())){
                IMMClientProxy.mc().setScreen(null);
            }
        }
    }

    public static boolean canRenderMeditation() {
        return IMMClientProxy.MC.screen instanceof MeditationScreen;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBg(graphics, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks){
        final int left = (this.width - SWITCH_BAR_WIDTH) >> 1;
        graphics.blit(TEXTURE, left, SWITCH_BAR_Y_OFFSET, 0, 130, SWITCH_BAR_WIDTH, SWITCH_BAR_HEIGHT);
        // Render Page Title.
        RenderUtil.renderCenterScaledText(graphics.pose(), this.type.getTitle(), this.width >> 1, SWITCH_BAR_Y_OFFSET + 4, 1F, Colors.WORD, ColorHelper.BLACK.rgb());
        int x = left + 14;
        final int y = SWITCH_BAR_Y_OFFSET + 19;
        // Render Switch Slots.
        for (MeditationTypes type : MeditationTypes.values()) {
            graphics.renderItem(type.getItem(), x, y);
            if(this.type == type){
                graphics.blit(TEXTURE, x - 3, y - 3, 110, 150, 22, 22);
            }
            x += 24;
        }
        // Render Switch Tips.
        final String keyA = ClientUtil.getKey(InputConstants.KEY_A).getDisplayName().getString();
        final String keyLeft = ClientUtil.getKey(InputConstants.KEY_LEFT).getDisplayName().getString();
        final String keyD = ClientUtil.getKey(InputConstants.KEY_D).getDisplayName().getString();
        final String keyRight = ClientUtil.getKey(InputConstants.KEY_RIGHT).getDisplayName().getString();
        final Component tip = TipUtil.gui("meditation.switch", keyA, keyLeft, keyD, keyRight);
        RenderUtil.renderCenterScaledText(graphics.pose(), tip, this.width >> 1, SWITCH_BAR_Y_OFFSET + 40, 0.6F, ColorHelper.WHITE.rgb(), ColorHelper.BLACK.rgb());
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

    protected void sendWakeUp() {
        NetworkHelper.sendToServer(new ScreenButtonPacket(ScreenButtonPacket.Types.QUIT_MEDITATION));
    }

    protected void switchScreen(int val){
        final int id = this.type.ordinal();
        final int nextId = (id + val + getLen()) % getLen();
        switchScreen(MeditationTypes.values()[nextId]);
    }

    protected void switchScreen(MeditationTypes type){
        switch (type){
            case CULTIVATION -> getMinecraft().setScreen(new CultivationScreen());
            case RESTING -> getMinecraft().setScreen(new RestingScreen());
            case SPELL -> getMinecraft().setScreen(new SpellScreen());
        }
    }

    protected int getLen(){
        return MeditationTypes.values().length;
    }

    public enum MeditationTypes {

        CULTIVATION(() -> new ItemStack(IMMItems.FIVE_FLOWERS_ELIXIR.get())),

        RESTING(() -> new ItemStack(BlockHelper.get().get(WoolCushionBlock.getWoolCushionLocation(DyeColor.RED)).get())),

        SPELL(() -> new ItemStack(IMMItems.SECRET_MANUAL.get())),

        ;

        private final Supplier<ItemStack> supplier;

        MeditationTypes(Supplier<ItemStack> supplier) {
            this.supplier = supplier;
        }

        public ItemStack getItem(){
            return supplier.get();
        }

        public Component getTitle(){
            return TipUtil.gui("meditation." + name().toLowerCase());
        }

    }

}
