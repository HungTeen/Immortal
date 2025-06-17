package hungteen.imm.client.gui.screen.meditation;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.client.gui.screen.HTScreen;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.imm.client.event.ClientEvents;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.block.artifacts.WoolCushionBlock;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.network.server.ScreenOperationPacket;
import hungteen.imm.util.Colors;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/21 16:56
 */
public abstract class MeditationScreen extends HTScreen {

    protected static final ResourceLocation TEXTURE = Util.get().guiTexture("cultivation");
    private static final int SWITCH_BAR_WIDTH = 92;
    private static final int SWITCH_BAR_HEIGHT = 39;
    private static final int SWITCH_BAR_Y_OFFSET = 5;
    private static final int QUIT_BUTTON_WIDTH = 200;
    private static final int QUIT_TEXT_HEIGHT = 20;
    private final MeditationType type;

    public MeditationScreen(MeditationType type) {
        this.type = type;
    }

    @Override
    protected void init() {
        super.init();
    }

    /**
     * 当玩家在打坐时，打开对应的打坐界面。当玩家不在打坐时，关闭打坐界面。
     * {@link ClientEvents#tick(ClientTickEvent.Post)}
     */
    public static void tickMeditation(LocalPlayer player){
        if (ClientUtil.screen() == null) {
            if(PlayerUtil.isSitInMeditation(player)){
                ClientUtil.mc().setScreen(new RestingScreen());
            }
        }
        if(ClientUtil.screen() instanceof MeditationScreen){
            if(! PlayerUtil.isSitInMeditation(ClientUtil.player())){
                ClientUtil.mc().setScreen(null);
            }
        }
    }

    public static boolean canRenderMeditation() {
        return ClientUtil.screen() instanceof MeditationScreen;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderBg(graphics, partialTicks);
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks){
        final int left = (this.width - SWITCH_BAR_WIDTH) >> 1;
        graphics.blit(TEXTURE, left, SWITCH_BAR_Y_OFFSET, 0, 130, SWITCH_BAR_WIDTH, SWITCH_BAR_HEIGHT);
        // Render Page Title.
        RenderUtil.renderCenterScaledText(graphics.pose(), this.type.getTitle(), this.width >> 1, SWITCH_BAR_Y_OFFSET + 4, 1F, Colors.WORD, ColorHelper.BLACK.rgb());
        int x = left + 14;
        final int y = SWITCH_BAR_Y_OFFSET + 19;
        // Render Switch Slots.
        for (MeditationType type : MeditationType.values()) {
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
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

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
        NetworkHelper.sendToServer(new ScreenOperationPacket(ScreenOperationPacket.OperationType.QUIT_RESTING));
    }

    protected void switchScreen(int val){
        final int id = this.type.ordinal();
        final int nextId = (id + val + getLen()) % getLen();
        switchScreen(MeditationType.values()[nextId]);
    }

    protected void switchScreen(MeditationType type){
        switch (type){
            case CULTIVATION -> getMinecraft().setScreen(new CultivationScreen());
            case RESTING -> getMinecraft().setScreen(new RestingScreen());
            case SPELL -> getMinecraft().setScreen(new SpellScreen());
        }
    }

    protected int getLen(){
        return MeditationType.values().length;
    }

    public enum MeditationType {

        CULTIVATION(() -> new ItemStack(IMMItems.INSPIRATION_ELIXIR.get())),

        RESTING(() -> new ItemStack(BlockHelper.get().get(WoolCushionBlock.getWoolCushionLocation(DyeColor.RED)).get())),

        SPELL(() -> new ItemStack(IMMItems.SECRET_MANUAL.get())),

        ;

        private final Supplier<ItemStack> supplier;

        MeditationType(Supplier<ItemStack> supplier) {
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
