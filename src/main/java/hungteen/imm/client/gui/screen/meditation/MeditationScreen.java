package hungteen.imm.client.gui.screen.meditation;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.client.gui.screen.HTScreen;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.ScreenButtonPacket;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
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
public class MeditationScreen extends HTScreen {

    private static final ResourceLocation SLOTS = Util.get().containerTexture("meditation");
    private static final int QUIT_BUTTON_WIDTH = 200;
    private static final int QUIT_TEXT_HEIGHT = 20;
    private static final int SWITCH_BAR_WIDTH = 125;
    private static final int SWITCH_BAR_HEIGHT = 75;
    private static final int SLOT_LEN = 26;
    private MeditationTypes type;
    private Button quitButton;

    public MeditationScreen(MeditationTypes  type) {
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
                ClientProxy.mc().setScreen(new MeditationScreen(MeditationTypes.REST));
            }
        }
        if(ClientUtil.screen() instanceof MeditationScreen){
            if(! PlayerUtil.isSitInMeditation(ClientUtil.player())){
                ClientProxy.mc().setScreen(null);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBg(graphics, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks){
        final float tick = PlayerUtil.getIntegerData(ClientUtil.player(), PlayerRangeIntegers.MEDITATE_TICK);
        float percent = tick / 100.0F;
        if (percent > 1.0F) {
            percent = 1.0F - (tick - 100.0F) / 10.0F;
        }

        final int color = (int)(220.0F * percent) << 24 | 1052704;
        graphics.fill(RenderType.guiOverlay(), 0, 0, this.width, this.height, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
//        final double dx = mouseX - (this.leftPos + 82);
//        final double dy = mouseY - (this.topPos + 108);
//        if (dx >= 0 && dy >= 0 && dx < FULL_FLAME_LEN && dy < FULL_FLAME_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, 1)) {
//            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
//            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
//            return true;
//        }
//        for (MeditationTypes type : MeditationTypes.values()) {
//            final boolean selected = type.ordinal() == this.selectedIndex;
//            graphics.blit(SLOTS, this.getSlotX(type.ordinal()), this.getSlotY(),  selected ? 26 : 0, 75, SLOT_LEN, SLOT_LEN);
//        }
        return super.mouseClicked(mouseX, mouseY, key);
    }

    @Override
    public void onClose() {
        this.sendWakeUp();
    }

    @Override
    public boolean keyPressed(int key, int code, int p_96072_) {
        final InputConstants.Key inputKey = InputConstants.getKey(key, code);
        // Press Escape to wake up.
        if (key == InputConstants.KEY_ESCAPE || ClientUtil.option().keyInventory.isActiveAndMatches(inputKey)) {
            this.sendWakeUp();
        }
        // Previous page.
        if (key == InputConstants.KEY_LEFT || ClientUtil.option().keyLeft.isActiveAndMatches(inputKey)) {
            switchScreen(-1);
        }
        // Next page.
        if (key == InputConstants.KEY_RIGHT || ClientUtil.option().keyRight.isActiveAndMatches(inputKey)) {
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

    private int getSlotX(int i){
        return (this.width - SWITCH_BAR_WIDTH) / 2 + i * 26 + (i + 1) * this.getInterval();
    }

    private int getSlotY(){
        return 20 + 22 + 13;
    }

    private int getInterval(){
        return (SWITCH_BAR_WIDTH - SLOT_LEN * this.getLen()) / (this.getLen() + 1);
    }

    private int getLen(){
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
