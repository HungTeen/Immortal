package hungteen.imm.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/14 16:10
 */
public interface IScrollableScreen<T> {

    List<T> getItems();

    void renderItem(Level level, GuiGraphics graphics, T item, int slotId, int x, int y);

    void renderTooltip(Level level, GuiGraphics graphics, T item, int slotId, int x, int y);
}
