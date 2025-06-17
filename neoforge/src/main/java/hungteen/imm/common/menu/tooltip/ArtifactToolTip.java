package hungteen.imm.common.menu.tooltip;

import hungteen.imm.common.codec.SpellInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-13 18:40
 **/
public class ArtifactToolTip implements TooltipComponent {

    private static final int HEIGHT = 18;
    private static final int WIDTH = 18;
    private final SpellInstance instance;

    public ArtifactToolTip(SpellInstance instance) {
        this.instance = instance;
    }

    public Component getSpellTitle(){
        return instance.spell().spell().getComponent().withStyle(ChatFormatting.YELLOW);
    }

    public Component getConditionTitle(){
        return instance.condition().getDescription();
    }

    public ResourceLocation getSpellTexture(){
        return instance.spell().spell().getSpellTexture();
    }

    public int getHeight(){
        return HEIGHT;
    }

    public int getWidth(){
        return WIDTH;
    }

}
