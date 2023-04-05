package hungteen.imm.common.item.runes;

import hungteen.htlib.util.helper.CodecHelper;
import hungteen.imm.common.rune.behavior.IBehaviorRune;
import hungteen.imm.common.rune.filter.FilterRuneTypes;
import hungteen.imm.common.rune.filter.IFilterRune;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 22:36
 **/
public class BehaviorRuneItem extends RuneItem {

    private static final String FILTER_MAP = "FilterList";
    private static final String FILTER_ITEM = "FilterItem";
    private final IBehaviorRune behaviorRune;

    public BehaviorRuneItem(IBehaviorRune behaviorRune) {
        this.behaviorRune = behaviorRune;
    }

    public void setFilter(ItemStack stack, int id, IFilterRune filterRune){
        if(id >= 0 && id < getBehaviorRune().maxSlot()){
            CodecHelper.encodeNbt(FilterRuneTypes.getCodec(), filterRune)
                    .result().ifPresent(tag -> {
                        CompoundTag nbt = new CompoundTag();
                        if(stack.getOrCreateTag().contains(FILTER_MAP)){
                            nbt = stack.getOrCreateTag().getCompound(FILTER_MAP);
                        }
                        nbt.put(getFilterLabel(id), tag);
                        stack.getOrCreateTag().put(FILTER_MAP, nbt);
                    });
        }
    }

    public Map<Integer, IFilterRune> getFilterMap(ItemStack stack){
        final Map<Integer, IFilterRune> map = new HashMap<>();
        if(stack.getOrCreateTag().contains(FILTER_MAP)){
            final CompoundTag tag = stack.getOrCreateTag().getCompound(FILTER_MAP);
            for(int i = 0; i < behaviorRune.maxSlot(); ++ i){
                if(tag.contains(getFilterLabel(i))){
                    final CompoundTag nbt = tag.getCompound(getFilterLabel(i));
                    int id = i;
                    CodecHelper.parse(FilterRuneTypes.getCodec(), nbt)
                            .result().ifPresent(rune -> {
                                map.put(id, rune);
                            });
                }
            }
        }
        return map;
    }

    public boolean hasFilter(ItemStack stack, int id){
        return getFilterMap(stack).containsKey(id);
    }

    private static String getFilterLabel(int id){
        return FILTER_ITEM + "_" + id;
    }

    public IBehaviorRune getBehaviorRune() {
        return behaviorRune;
    }
}
