package hungteen.imm.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 22:50
 **/
public class ImmortalWidget extends GuiComponent implements Renderable, GuiEventListener, NarratableEntry {

    protected final ResourceLocation resourceLocation;
    protected int width;
    protected int height;
    public int x;
    public int y;

    public ImmortalWidget(int x, int y, int height, int width, ResourceLocation resourceLocation) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.resourceLocation = resourceLocation;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public NarrationPriority narrationPriority() {
        return null;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }

}
