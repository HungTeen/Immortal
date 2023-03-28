package hungteen.imm.common.capability.entity;

import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-02-25 21:59
 **/
public class EntitySpiritualCapability implements ISpiritualCapability {

    private Entity entity;
    private final HashMap<ISpiritualType, Integer> elements = new HashMap<>();

    public void init(Entity entity){
        this.entity = entity;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            this.elements.forEach((type, value) -> {
                nbt.putInt(type.getRegistryName(), value);
            });
            tag.put("Elements", nbt);
        }
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains("Elements")){
            this.elements.clear();
            final CompoundTag nbt = tag.getCompound("Elements");
            SpiritualTypes.registry().getValues().forEach(type -> {
                if(nbt.contains(type.getRegistryName())){
                    this.elements.put(type, nbt.getInt(type.getRegistryName()));
                }
            });
        }
    }

    /**
     * 目前没有客户端同步的需求。
     */
    public void addElement(ISpiritualType spiritualType, int value){
        final int result = Math.max(getElement(spiritualType) + value, 0);
        this.elements.put(spiritualType, result);
    }

    public int getElement(ISpiritualType spiritualType){
        return this.elements.getOrDefault(spiritualType, 0);
    }

}
