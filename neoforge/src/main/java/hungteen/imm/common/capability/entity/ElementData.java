package hungteen.imm.common.capability.entity;

import hungteen.imm.api.cultivation.Element;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:44
 **/
public class ElementData implements INBTSerializable<CompoundTag> {

    private final EnumMap<Element, Float> elementAmounts = new EnumMap<>(Element.class);
    private final EnumMap<Element, Long> lastUpdateTicks = new EnumMap<>(Element.class);

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        final CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            this.elementAmounts.forEach((type, value) -> {
                nbt.putFloat(type.toString(), value);
            });
            tag.put("ElementAmounts", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            this.lastUpdateTicks.forEach((type, value) -> {
                nbt.putLong(type.toString(), value);
            });
            tag.put("LastUpdateTicks", nbt);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains("ElementAmounts")) {
            this.elementAmounts.clear();
            final CompoundTag nbt = tag.getCompound("ElementAmounts");
            Arrays.stream(Element.values()).forEach(type -> {
                if (nbt.contains(type.toString())) {
                    this.elementAmounts.put(type, nbt.getFloat(type.toString()));
                }
            });
        }
        if (tag.contains("LastUpdateTicks")) {
            this.lastUpdateTicks.clear();
            final CompoundTag nbt = tag.getCompound("LastUpdateTicks");
            Arrays.stream(Element.values()).forEach(type -> {
                if (nbt.contains(type.toString())) {
                    this.lastUpdateTicks.put(type, nbt.getLong(type.toString()));
                }
            });
        }
    }

    public float getAmount(Element element) {
        return this.elementAmounts.getOrDefault(element, 0F);
    }

    public long getLastUpdateTick(Element element) {
        return this.lastUpdateTicks.getOrDefault(element, 0L);
    }

    public void setAmount(Element element, float value) {
        this.elementAmounts.put(element, value);
    }

    public void setLastUpdateTick(Element element, long lastTick) {
        this.lastUpdateTicks.put(element, lastTick);
    }

}
