package hungteen.imm.common.item.runes;

import hungteen.imm.common.rune.behavior.IBehaviorRune;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 22:36
 **/
public class BehaviorRuneItem extends RuneItem {

    private static final String RUNE_LIST = "RuneList";
    private static final String RUNE_ITEM = "RuneItem";
    private final IBehaviorRune behaviorRune;

    public BehaviorRuneItem(IBehaviorRune behaviorRune) {
        this.behaviorRune = behaviorRune;
    }

    public List<ItemStack> getRuneItems(ItemStack stack){
        List<ItemStack> list = new ArrayList<>();
        if(stack.getOrCreateTag().contains(RUNE_LIST)){
            final CompoundTag tag = stack.getOrCreateTag().getCompound(RUNE_LIST);
            for(int i = 0; i < 9; ++ i){
                if(tag.contains(getRuneItemLabel(i))){
                    final CompoundTag nbt = tag.getCompound(getRuneItemLabel(i));
                    list.add(ItemStack.of(nbt));
                } else {
                    list.add(ItemStack.EMPTY);
                }
            }
        }
        return list;
    }

    private static String getRuneItemLabel(int id){
        return RUNE_ITEM + "_" + id;
    }

    public IBehaviorRune getBehaviorRune() {
        return behaviorRune;
    }
}
