package hungteen.imm.common.item.runes;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.rune.ICraftableRune;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-06 18:27
 **/
public abstract class RuneItem extends Item {

    public RuneItem() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);
        if(PlayerHelper.getClientPlayer().isPresent()){
            this.addDisplayComponents(stack, components);
            if(this.hasHideInfo(stack)){
                if(Util.getProxy().isShiftKeyDown()){
                    this.addHideComponents(stack, components);
                } else {
                    components.add(TipUtil.rune("shift_to_see_details").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
                }
            }
        }
    }

    public abstract ICraftableRune getRune();

    protected void addDisplayComponents(ItemStack stack, List<Component> components) {
    }

    protected void addHideComponents(ItemStack stack, List<Component> components) {
    }

    protected boolean hasHideInfo(ItemStack stack){
        return false;
    }

}
