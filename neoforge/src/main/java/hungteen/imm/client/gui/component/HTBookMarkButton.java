package hungteen.imm.client.gui.component;

import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.util.Colors;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/13 18:00
 **/
public class HTBookMarkButton extends HTButton {

    public static final int WIDTH = 52;
    public static final int HEIGHT = 30;

    public HTBookMarkButton(Builder builder) {
        super(builder.size(WIDTH, HEIGHT), RenderUtil.WIDGETS);
    }

    @Override
    protected int getColor(boolean active) {
        return active ? Colors.WORD : super.getColor(false);
    }

    @Override
    protected int getTextureY() {
        return !this.isActive() ? 0 : this.isHovered() ? 32 : 64;
    }

    @Override
    protected int getTextureX() {
        return 204;
    }
}
