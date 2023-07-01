package hungteen.imm.common.capability.entity;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.network.EntityElementPacket;
import hungteen.imm.common.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-02-25 21:59
 **/
public class IMMEntityCapability implements IIMMEntityCapability {

    private Entity entity;
    private final ElementData robustElementData = new ElementData();
    private final ElementData weakElementData = new ElementData();

    public void init(Entity entity){
        this.entity = entity;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.put("RobustElementData", this.robustElementData.serializeNBT());
        tag.put("WeakElementData", this.weakElementData.serializeNBT());
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains("RobustElementData")){
            this.robustElementData.deserializeNBT(tag.getCompound("RobustElementData"));
        }
        if(tag.contains("WeakElementData")){
            this.weakElementData.deserializeNBT(tag.getCompound("WeakElementData"));
        }
    }

    public void setElementValue(Elements element, boolean robust, float value){
       if(robust){
           this.robustElementData.setElementAmount(element, value);
       } else {
           this.weakElementData.setElementAmount(element, value);
       }
       this.sendElementPacket(element, robust);
    }

    public void addElement(Elements element, boolean robust, float value){
        final float result = Math.max(getElement(element, robust) + value, 0);
        this.setElement(element, robust, result);
    }

    public float getElementValue(Elements element, boolean robust){
        return robust ? this.robustElementData.getAmount(element) : this.weakElementData.getAmount(element);
    }

    public long getElementLastTick(Elements element, boolean robust){
        return robust ? this.robustElementData.getLastTick(element) : this.weakElementData.getLastTick(element);
    }

    public void checkValid(Elements element, boolean robust){
        if(! this.entity.level.isClientSide){
            if(this.getElementLastTick(element, robust) < this.entity.level.getGameTime()){
                this.setElement(element, robust, 0);
            }
        }
    }

    public void sendElementPacket(Elements element, boolean robust){
        if(! this.entity.level.isClientSide){
            NetworkHandler.sendToClientEntity(this.entity, new EntityElementPacket(this.entity.getId(), element, robust, get(element, robust), getLastTick(element, robust)));
        }
    }

    private static class ElementData {

        private final EnumMap<Elements, Float> elementAmounts = new EnumMap<>(Elements.class);
        private final EnumMap<Elements, Long> elementStartTicks = new EnumMap<>(Elements.class);

        public CompoundTag serializeNBT() {
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
                this.elementStartTicks.forEach((type, value) -> {
                    nbt.putLong(type.toString(), value);
                });
                tag.put("ElementLastTicks", nbt);
            }
            return tag;
        }

        public void deserializeNBT(CompoundTag tag) {
            if(tag.contains("ElementAmounts")){
                this.elementAmounts.clear();
                final CompoundTag nbt = tag.getCompound("ElementAmounts");
                Arrays.stream(Elements.values()).forEach(type -> {
                    if(nbt.contains(type.toString())){
                        this.elementAmounts.put(type, nbt.getFloat(type.toString()));
                    }
                });
            }
            if(tag.contains("ElementStartTicks")){
                this.elementStartTicks.clear();
                final CompoundTag nbt = tag.getCompound("ElementStartTicks");
                Arrays.stream(Elements.values()).forEach(type -> {
                    if(nbt.contains(type.toString())){
                        this.elementStartTicks.put(type, nbt.getLong(type.toString()));
                    }
                });
            }
        }

        public float getAmount(Elements element) {
            return this.elementAmounts.getOrDefault(element, 0F);
        }

        public long getLastTick(Elements element) {
            return this.elementStartTicks.getOrDefault(element, 0L);
        }

        public void setAmount(Elements element, float value) {
            this.elementAmounts.put(element, value);
        }

        public void setLastTick(Elements element, long lastTick) {
            this.elementStartTicks.put(element, lastTick);
        }

    }

}
