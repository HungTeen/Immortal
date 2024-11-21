package hungteen.imm.common.item.runes;

import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.impl.BrainHelper;
import hungteen.imm.common.rune.behavior.IBehaviorRune;
import hungteen.imm.common.rune.filter.FilterRuneTypes;
import hungteen.imm.common.rune.filter.IFilterRune;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-02 22:36
 **/
public class BehaviorRuneItem extends RuneItem {

    private static final String FILTER_MAP = "FilterList";
    private static final String FILTER_ITEM = "FilterItem";
    private final IBehaviorRune behaviorRune;

    public BehaviorRuneItem(IBehaviorRune behaviorRune) {
        this.behaviorRune = behaviorRune;
    }

    public void setFilter(ItemStack stack, int id, IFilterRune filterRune){
        if(id >= 0 && id < getRune().maxSlot()){
            CodecHelper.encodeNbt(FilterRuneTypes.getCodec(), filterRune)
                    .result().ifPresent(tag -> {
                        CompoundTag nbt = new CompoundTag();
//                        if(stack.getOrCreateTag().contains(FILTER_MAP)){
//                            nbt = stack.getOrCreateTag().getCompound(FILTER_MAP);
//                        }
                        nbt.put(getFilterLabel(id), tag);
//                        stack.getOrCreateTag().put(FILTER_MAP, nbt);
                    });
        }
    }

    public Map<Integer, IFilterRune> getFilterMap(ItemStack stack){
        final Map<Integer, IFilterRune> map = new HashMap<>();
//        if(stack.getOrCreateTag().contains(FILTER_MAP)){
//            final CompoundTag tag = stack.getOrCreateTag().getCompound(FILTER_MAP);
//            for(int i = 0; i < behaviorRune.maxSlot(); ++ i){
//                if(tag.contains(getFilterLabel(i))){
//                    final CompoundTag nbt = tag.getCompound(getFilterLabel(i));
//                    int id = i;
//                    CodecHelper.parse(FilterRuneTypes.getCodec(), nbt)
//                            .result().ifPresent(rune -> {
//                                map.put(id, rune);
//                            });
//                }
//            }
//        }
        return map;
    }

    public boolean hasFilter(ItemStack stack, int id){
        return getFilterMap(stack).containsKey(id);
    }

    public Optional<IFilterRune> getFilter(ItemStack stack, int id){
        return Optional.ofNullable(getFilterMap(stack).getOrDefault(id, null));
    }

    @Override
    public String getDescriptionId() {
        return StringHelper.langKey("item", Util.id(), "behavior_rune");
    }

    @Override
    protected void addDisplayComponents(ItemStack stack, List<Component> components) {
        components.add(getRune().getComponent());
    }

    @Override
    protected void addHideComponents(ItemStack stack, List<Component> components) {
        List<Component> memoryComponents = getMemoryComponents();
        if(! memoryComponents.isEmpty()){
            components.add(TipUtil.rune("require_memories"));
            components.addAll(memoryComponents);
        }
        if(getRune().maxSlot() > 0){
            components.add(TipUtil.rune("filters"));
            for(int i = 0; i < getRune().maxSlot(); ++ i){
                final MutableComponent component = getRune().getFilterDesc(i).withStyle(ChatFormatting.YELLOW);
                component.append(" : ");
                this.getFilter(stack, i).ifPresentOrElse(rune -> {
                    component.append(rune.getFilterText());
                }, () -> {
                    component.append(TipUtil.rune("default_filter"));
                });
                components.add(component);
            }
        }
    }

    @Override
    protected boolean hasHideInfo(ItemStack stack) {
        return true;
    }

    public List<Component> getMemoryComponents(){
        List<Component> components = new ArrayList<>();
        getRune().requireMemoryStatus().forEach((memory, statusList) -> {
            components.add(getStatusText(memory, statusList));
        });
        return components;
    }

    public Component getStatusText(MemoryModuleType<?> type, List<MemoryStatus> status){
            final MutableComponent component = getMemoryComponent(type);
            if(! status.isEmpty()){
                component.append(" : ");
                component.append(getStatusComponent(status.get(0)));
                for(int i = 1; i < status.size(); i++){
                    component.append(" -> ");
                    component.append(getStatusComponent(status.get(i)));
                }
            }
            return component;
    }

    public MutableComponent getStatusComponent(MemoryStatus status) {
        return TipUtil.rune("status." + status.toString().toLowerCase(Locale.ROOT)).withStyle(
                status == MemoryStatus.REGISTERED ? ChatFormatting.YELLOW :
                status == MemoryStatus.VALUE_ABSENT ? ChatFormatting.RED :
                        ChatFormatting.GREEN
        );
    }

    public MutableComponent getMemoryComponent(MemoryModuleType<?> type) {
        final ResourceLocation location = BrainHelper.memory().getKey(type);
        return Component.translatable("rune." + location.getNamespace() + ".memory." + location.getPath()).withStyle(ChatFormatting.DARK_PURPLE);
    }

    private static String getFilterLabel(int id){
        return FILTER_ITEM + "_" + id;
    }

    @Override
    public IBehaviorRune getRune() {
        return behaviorRune;
    }
}
