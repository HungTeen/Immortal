package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/3 15:58
 */
public abstract class TalismanItem extends Item implements ArtifactItem {

    public static final ResourceLocation ACTIVATED = Util.prefix("activated");
    public static final ResourceLocation ACTIVATING = Util.prefix("activating");

    public TalismanItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Item.TooltipContext level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(ItemUtil.desc(itemStack));
    }

    public boolean canUse(ItemStack stack, Entity entity){
        return true;
    }

}
