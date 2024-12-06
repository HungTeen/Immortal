package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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

    public boolean canUseTalisman(ItemStack stack, LivingEntity living){
        return true;
    }

    public static Component noEnoughQiTip(float qiCost) {
        return TipUtil.info("talisman.no_enough_qi", String.format("%.1f", qiCost));
    }

}
