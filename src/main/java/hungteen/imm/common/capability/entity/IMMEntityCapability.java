package hungteen.imm.common.capability.entity;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.common.network.EntityElementPacket;
import hungteen.imm.common.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-02-25 21:59
 **/
public class IMMEntityCapability implements IIMMEntityCapability {

    private static final Comparator<IElementReaction> COMPARATOR = (reaction1, reaction2) -> -(reaction1.priority() - reaction2.priority());
    private Entity entity;
    private final ElementData robustElementData = new ElementData();
    private final ElementData weakElementData = new ElementData();
    private final Set<IElementReaction> possibleReactions = new TreeSet<>(COMPARATOR);
    private boolean dirty = false;

    public void init(Entity entity) {
        this.entity = entity;
    }

    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.put("RobustElementData", this.robustElementData.serializeNBT());
        tag.put("WeakElementData", this.weakElementData.serializeNBT());
        {
            CompoundTag nbt = new CompoundTag();
            possibleReactions.forEach(l -> {
                nbt.putBoolean(l.getRegistryName(), true);
            });
            tag.put("PossibleReactions", nbt);
        }
        tag.putBoolean("ElementDirty", this.dirty);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("RobustElementData")) {
            this.robustElementData.deserializeNBT(tag.getCompound("RobustElementData"));
        }
        if (tag.contains("WeakElementData")) {
            this.weakElementData.deserializeNBT(tag.getCompound("WeakElementData"));
        }
        if (tag.contains("PossibleReactions")) {
            possibleReactions.clear();
            CompoundTag nbt = tag.getCompound("PossibleReactions");
            ElementReactions.registry().getValues().forEach(l -> {
                if (nbt.contains(l.getRegistryName())) {
                    possibleReactions.add(l);
                }
            });
        }
        if (tag.contains("ElementDirty")) {
            this.dirty = tag.getBoolean("ElementDirty");
        }
    }

    public void updatePossibleReactions() {
        if (this.dirty) {
            this.possibleReactions.clear();
            this.possibleReactions.addAll(ElementReactions.registry().getValues().stream().filter(l -> l.match(this.entity)).toList());
            this.dirty = false;
        }
    }

    public Set<IElementReaction> getPossibleReactions() {
        return possibleReactions;
    }

    public boolean hasReaction(IElementReaction reaction) {
        return possibleReactions.contains(reaction);
    }

    public void clearElements() {
        for (Elements element : Elements.values()) {
            setElementAmount(element, true, 0);
            setElementAmount(element, false, 0);
        }
    }

    public void setElementAmount(Elements element, boolean robust, float amount) {
        setElementAmount(element, robust, amount, true);
    }

    public void setElementAmount(Elements element, boolean robust, float amount, boolean check) {
        final float oldAmount = getElementAmount(element, robust);
        if (check) {
            this.checkValid(element, robust);
        }
        if (robust) {
            this.robustElementData.setAmount(element, amount);
        } else {
            this.weakElementData.setAmount(element, amount);
        }
        if ((oldAmount == 0) ^ (amount == 0)) {
            this.dirty = true;
        }
        this.setLastUpdateTick(element, robust);
        this.sendElementPacket(element, robust, getElementAmount(element, robust));
    }

    public void addElementAmount(Elements element, boolean robust, float value) {
        this.checkValid(element, robust);
        final float result = Math.max(getElementAmount(element, robust) + value, 0);
        this.setElementAmount(element, robust, result, false);
    }

    public float getElementAmount(Elements element, boolean robust) {
        return robust ? this.robustElementData.getAmount(element) : this.weakElementData.getAmount(element);
    }

    /**
     * Negative float means weak, vise versa robust.
     * @return (Elements -> float)
     */
    public Map<Elements, Float> getElementMap() {
        final Map<Elements, Float> map = new EnumMap<>(Elements.class);
        for (Elements element : Elements.values()) {
            float amount = getElementAmount(element, true);
            if(amount > 0){
                map.put(element, amount);
            } else {
                amount = getElementAmount(element, false);
                if(amount > 0){
                    map.put(element, - amount);
                }
            }
        }
        return map;
    }

    private void setLastUpdateTick(Elements element, boolean robust) {
        if (EntityHelper.isServer(entity)) {
            if (robust) {
                this.robustElementData.setLastUpdateTick(element, this.getTime());
            } else {
                this.weakElementData.setLastUpdateTick(element, this.getTime());
            }
        }
    }

    private long getLastUpdateTick(Elements element, boolean robust) {
        return robust ? this.robustElementData.getLastUpdateTick(element) : this.weakElementData.getLastUpdateTick(element);
    }

    public boolean hasElement(Elements element, boolean robust) {
        return getElementAmount(element, robust) > 0;
    }

    public void checkValid(Elements element, boolean robust) {
        if (EntityHelper.isServer(entity)) {
            if (this.getLastUpdateTick(element, robust) + ElementManager.ESCAPE_UPDATE_CD < this.entity.level().getGameTime()) {
                if (this.hasElement(element, robust)) {
                    this.setElementAmount(element, robust, 0, false);
                }
            }
        }
    }

    public void sendElementPacket(Elements element, boolean robust, float amount) {
        if (EntityHelper.isServer(entity)) {
            NetworkHandler.sendToClientEntityAndSelf(this.entity, new EntityElementPacket(this.entity.getId(), element, robust, amount));
        }
    }

    private long getTime() {
        return this.entity.level().getGameTime();
    }

    private static class ElementData {

        private final EnumMap<Elements, Float> elementAmounts = new EnumMap<>(Elements.class);
        private final EnumMap<Elements, Long> lastUpdateTicks = new EnumMap<>(Elements.class);

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
                this.lastUpdateTicks.forEach((type, value) -> {
                    nbt.putLong(type.toString(), value);
                });
                tag.put("LastUpdateTicks", nbt);
            }
            return tag;
        }

        public void deserializeNBT(CompoundTag tag) {
            if (tag.contains("ElementAmounts")) {
                this.elementAmounts.clear();
                final CompoundTag nbt = tag.getCompound("ElementAmounts");
                Arrays.stream(Elements.values()).forEach(type -> {
                    if (nbt.contains(type.toString())) {
                        this.elementAmounts.put(type, nbt.getFloat(type.toString()));
                    }
                });
            }
            if (tag.contains("LastUpdateTicks")) {
                this.lastUpdateTicks.clear();
                final CompoundTag nbt = tag.getCompound("LastUpdateTicks");
                Arrays.stream(Elements.values()).forEach(type -> {
                    if (nbt.contains(type.toString())) {
                        this.lastUpdateTicks.put(type, nbt.getLong(type.toString()));
                    }
                });
            }
        }

        public float getAmount(Elements element) {
            return this.elementAmounts.getOrDefault(element, 0F);
        }

        public long getLastUpdateTick(Elements element) {
            return this.lastUpdateTicks.getOrDefault(element, 0L);
        }

        public void setAmount(Elements element, float value) {
            this.elementAmounts.put(element, value);
        }

        public void setLastUpdateTick(Elements element, long lastTick) {
            this.lastUpdateTicks.put(element, lastTick);
        }

    }

}
