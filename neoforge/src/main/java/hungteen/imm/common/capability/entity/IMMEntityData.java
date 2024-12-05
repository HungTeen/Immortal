package hungteen.imm.common.capability.entity;

import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.ElementReactions;
import hungteen.imm.common.network.client.EntityElementPacket;
import hungteen.imm.util.MathUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-02-25 21:59
 **/
public class IMMEntityData implements INBTSerializable<CompoundTag> {

    private static final Comparator<ElementReaction> COMPARATOR = (reaction1, reaction2) -> - (reaction1.priority() - reaction2.priority());
    private final Entity entity;
    private final ElementData robustElementData = new ElementData();
    private final ElementData weakElementData = new ElementData();
    private final Set<ElementReaction> possibleReactions = new TreeSet<>(COMPARATOR);
    private final Map<ElementReaction, Float> activeReactions = new HashMap<>();
    private Entity lastAttachedEntity = null;
    private float quenchBladeDamage = 0;
    private boolean dirty = false;

    public static IMMEntityData create(IAttachmentHolder holder){
        if(holder instanceof Entity entity){
            return new IMMEntityData(entity);
        }
        return new IMMEntityData();
    }

    public IMMEntityData(Entity entity) {
        this.entity = entity;
    }

    public IMMEntityData() {
        this(null);
        throw new RuntimeException("No entity provided.");
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        final CompoundTag tag = new CompoundTag();
        tag.put("RobustElementData", this.robustElementData.serializeNBT(provider));
        tag.put("WeakElementData", this.weakElementData.serializeNBT(provider));
        {
            CompoundTag nbt = new CompoundTag();
            possibleReactions.forEach(l -> {
                nbt.putBoolean(l.getRegistryName(), true);
            });
            tag.put("PossibleReactions", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            activeReactions.forEach((l, r) -> {
                nbt.putFloat(l.getRegistryName(), r);
            });
            tag.put("ActiveReactions", nbt);
        }
        tag.putBoolean("ElementDirty", this.dirty);
        if(this.lastAttachedEntity != null){
            tag.putInt("LastAttachedEntity", this.lastAttachedEntity.getId());
        }
        tag.putFloat("QuenchBladeDamage", this.quenchBladeDamage);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains("RobustElementData")) {
            this.robustElementData.deserializeNBT(provider, tag.getCompound("RobustElementData"));
        }
        if (tag.contains("WeakElementData")) {
            this.weakElementData.deserializeNBT(provider, tag.getCompound("WeakElementData"));
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
        if (tag.contains("ActiveReactions")) {
            activeReactions.clear();
            CompoundTag nbt = tag.getCompound("ActiveReactions");
            ElementReactions.registry().getValues().forEach(l -> {
                if (nbt.contains(l.getRegistryName())) {
                    activeReactions.put(l, nbt.getFloat(l.getRegistryName()));
                }
            });
        }
        if (tag.contains("ElementDirty")) {
            this.dirty = tag.getBoolean("ElementDirty");
        }
        if(tag.contains("LastAttachedEntity")){
            this.lastAttachedEntity = this.entity.level().getEntity(tag.getInt("LastAttachedEntity"));
        }
        if(tag.contains("QuenchBladeDamage")){
            this.quenchBladeDamage = tag.getFloat("QuenchBladeDamage");
        }
    }

    public void tick() {
        if (ElementManager.canReactionOn(entity)) {
            // Update Misc.
            this.update();
            // Element Reactions.
            final Iterator<ElementReaction> iterator = getPossibleReactions().iterator();
            while (iterator.hasNext()) {
                final ElementReaction reaction = iterator.next();
                final float scale = reaction.match(entity);
                if (scale > 0) {
                    reaction.doReaction(entity, (float) (MathUtil.log2(scale + 1)));
                    reaction.consume(entity, scale);
                    if (!reaction.once()) {
                        setActiveScale(reaction, scale);
                    }
//                            NetworkHelper.sendToClient(serverLevel, entity, entity.position(), 60, new ReactionPacket(entity.getId(), reaction));
                } else {
                    iterator.remove();
                }
            }
        }
        // Decay Elements.
        for (int i = 0; i < 2; ++i) {
            final boolean robust = (i == 0);
            for (Element element : Element.values()) {
                if (hasElement(element, robust)) {
                    final float decayAmount = ElementManager.getDecayAmount(entity, element, robust);
                    addElementAmount(element, robust, -decayAmount);
                }
            }
        }
    }


    /**
     * 更新下一步可能发生的元素反应。
     */
    public void update() {
        if (this.dirty) {
            this.possibleReactions.clear();
            this.possibleReactions.addAll(ElementReactions.registry().getValues().stream().filter(l -> l.match(this.entity) > 0).toList());
            this.dirty = false;
        }
        this.clearActiveReactions();
    }

    public Set<ElementReaction> getPossibleReactions() {
        return possibleReactions;
    }

    public boolean isActiveReaction(ElementReaction reaction) {
        return getActiveScale(reaction) > 0;
    }

    public float getActiveScale(ElementReaction reaction) {
        return activeReactions.getOrDefault(reaction, 0F);
    }

    public void setActiveScale(ElementReaction reaction, float scale) {
        activeReactions.put(reaction, scale);
    }

    public void clearActiveReactions() {
        activeReactions.clear();
    }

    public void clearElements() {
        for (Element element : Element.values()) {
            setElementAmount(element, true, 0);
            setElementAmount(element, false, 0);
        }
    }

    public void setElementAmount(Element element, boolean robust, float amount) {
        setElementAmount(element, robust, amount, true);
    }

    public void setElementAmount(Element element, boolean robust, float amount, boolean check) {
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

    public void addElementAmount(Element element, boolean robust, float value) {
        this.addElementAmount(element, robust, value, Float.MAX_VALUE);
    }

    public void addElementAmount(Element element, boolean robust, float value, float maxAmount) {
        this.checkValid(element, robust);
        final float result = Mth.clamp(getElementAmount(element, robust) + value, 0, maxAmount);
        this.setElementAmount(element, robust, result, false);
    }

    public float getElementAmount(Element element, boolean robust) {
        return robust ? this.robustElementData.getAmount(element) : this.weakElementData.getAmount(element);
    }

    /**
     * Negative float means weak, vise versa robust.
     * @return (Elements -> float)
     */
    public Map<Element, Float> getElementMap() {
        final Map<Element, Float> map = new EnumMap<>(Element.class);
        for (Element element : Element.values()) {
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

    private void setLastUpdateTick(Element element, boolean robust) {
        if (EntityHelper.isServer(entity)) {
            if (robust) {
                this.robustElementData.setLastUpdateTick(element, this.getTime());
            } else {
                this.weakElementData.setLastUpdateTick(element, this.getTime());
            }
        }
    }

    private long getLastUpdateTick(Element element, boolean robust) {
        return robust ? this.robustElementData.getLastUpdateTick(element) : this.weakElementData.getLastUpdateTick(element);
    }

    public boolean hasElement(Element element, boolean robust) {
        return getElementAmount(element, robust) > 0;
    }

    public void setLastAttachedEntity(Entity lastAttachedEntity) {
        this.lastAttachedEntity = lastAttachedEntity;
    }

    public Entity getLastAttachedEntity() {
        return lastAttachedEntity;
    }

    public void setQuenchBladeDamage(float quenchBladeDamage) {
        this.quenchBladeDamage = quenchBladeDamage;
    }

    public float getQuenchBladeDamage() {
        return quenchBladeDamage;
    }

    /**
     * 如果玩家一段时间不在该实体的区块内，可能会丢失对于这个元素的客户端信息。
     */
    public void checkValid(Element element, boolean robust) {
        if (EntityHelper.isServer(entity)) {
            if (this.getLastUpdateTick(element, robust) + ElementManager.SYNC_UPDATE_CD < this.entity.level().getGameTime()) {
                if (this.hasElement(element, robust)) {
                    this.setElementAmount(element, robust, 0, false);
                }
            }
        }
    }

    public void sendElementPacket(Element element, boolean robust, float amount) {
        if (EntityHelper.isServer(entity)) {
            NetworkHelper.sendToClientTrackingPlayerAndSelf(this.entity, new EntityElementPacket(this.entity.getId(), element, robust, amount));
        }
    }

    private long getTime() {
        return this.entity.level().getGameTime();
    }

}
