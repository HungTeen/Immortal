package hungteen.imm.common.item.talismans;

import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/3 15:58
 */
public abstract class TalismanItem extends Item implements IArtifactItem {

    public static final String ACTIVATED = "activated";
    public static final ResourceLocation ACTIVATE_PROPERTY = Util.prefix(ACTIVATED);

    public TalismanItem(Properties properties) {
        super(properties);
    }

    public boolean canUse(ItemStack stack, Entity entity){
        return true;
    }

    public static void setActivated(ItemStack stack, boolean activated){
        stack.getOrCreateTag().putBoolean(ACTIVATED, activated);
    }

    public static boolean isActivated(ItemStack stack){
        return stack.getOrCreateTag().getBoolean(ACTIVATED);
    }

}
