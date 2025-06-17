package hungteen.imm.common.menu.tooltip;

import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.impl.manuals.SecretScroll;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-27 21:17
 **/
public class ManualToolTip implements TooltipComponent {

    private final SecretManual secretManual;
    private final SecretScroll secretScroll;

    public ManualToolTip(SecretManual secretManual, SecretScroll secretScroll){
        this.secretManual = secretManual;
        this.secretScroll = secretScroll;
    }

    public Component getManualCategory(){
        return secretManual.category().orElse(Component.empty());
    }

    public MutableComponent getContentInfo(){
        return secretScroll.getContentInfo().withStyle(ChatFormatting.GRAY);
    }

    public Optional<ResourceLocation> getTexture(){
        return secretScroll.getRenderLogo();
    }

}
