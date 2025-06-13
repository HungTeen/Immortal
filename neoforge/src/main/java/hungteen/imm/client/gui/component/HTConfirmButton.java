package hungteen.imm.client.gui.component;

import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.util.Colors;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/13 18:00
 **/
public class HTConfirmButton extends HTButton {

    public static final int WIDTH = 38;
    public static final int HEIGHT = 18;

    public HTConfirmButton(Builder builder) {
        super(builder.size(WIDTH, HEIGHT), RenderUtil.WIDGETS);
    }

    @Override
    protected int getColor(boolean active) {
        return active ? Colors.WORD : super.getColor(false);
    }

    @Override
    protected int getTextureY() {
        return !this.isActive() ? 0 : this.isHovered() ? 20 : 40;
    }
}
