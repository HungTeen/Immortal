package hungteen.imm.common.menu.tooltip;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.manuals.SecretManual;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 21:17
 **/
public class ManualToolTip implements TooltipComponent {

    public static final int TEXT_HEIGHT = 8;
    public static final int ICON_WIDTH = 10;
    public static final int SINGLE_HEIGHT = TEXT_HEIGHT + ICON_WIDTH;
    public static final int SINGLE_WIDTH = ICON_WIDTH + 4;

    private final SecretManual secretManual;

    public ManualToolTip(SecretManual secretManual){
        this.secretManual = secretManual;
    }

    public MutableComponent getManualTitle(){
        return secretManual.getManualTitle().withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD);
    }

    public MutableComponent getContentInfo(){
        return secretManual.getContentInfo().withStyle(ChatFormatting.GRAY);
    }

    public Optional<ResourceLocation> getTexture(){
        return secretManual.getTexture();
    }

}
