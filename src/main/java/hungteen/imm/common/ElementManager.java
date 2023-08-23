package hungteen.imm.common;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.capability.entity.IMMEntityCapability;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.ReactionPacket;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/1 15:19
 */
public class ElementManager {

    public static final int ESCAPE_UPDATE_CD = 20;
    public static final int DISAPPEAR_WARN_CD = 50;
    public static final int DISAPPEAR_WARN_AMOUNT = 5;
    public static final int DISAPPEAR_CD = 5;
    public static final int DISPLAY_ROBUST_CD = 10;

    /**
     * 在世界更新末尾更新，仅服务端。
     */
    public static void tickElements(Entity entity) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
            // Update Misc.
            cap.update();
            // Element Reactions.
            final Iterator<IElementReaction> iterator = cap.getPossibleReactions().iterator();
            while (iterator.hasNext()) {
                final IElementReaction reaction = iterator.next();
                final float scale = reaction.match(entity);
                if (scale > 0) {
                    reaction.doReaction(entity, (float) (MathUtil.log2(scale + 1)));
                    reaction.consume(entity, scale);
                    if(! reaction.once()){
                        cap.setActiveScale(reaction, scale);
                    }
                    NetworkHandler.sendToNearByClient(entity.level(), entity.position(), 60, new ReactionPacket(entity.getId(), reaction));
                } else {
                    iterator.remove();
                }
            }
            // Decay Elements.
            for (int i = 0; i < 2; ++i) {
                final boolean robust = (i == 0);
                for (Elements element : Elements.values()) {
                    if (cap.hasElement(element, robust)) {
                        final float decayAmount = getDecayAmount(entity, element, robust);
                        cap.addElementAmount(element, robust, -decayAmount);
                    }
                }
            }
        });
    }

    public static float getDecayAmount(Entity entity, Elements element, boolean robust) {
        return element.getDecaySpeed();
    }

    /**
     * Server side only.
     */
    public static void ifActiveReaction(Entity entity, IElementReaction reaction, Consumer<Float> consumer) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
            if(cap.isActiveReaction(reaction)){
                consumer.accept(cap.getActiveScale(reaction));
            }
        });
    }

    /**
     * Server side only.
     */
    public static boolean isActiveReaction(Entity entity, IElementReaction reaction) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.isActiveReaction(reaction), false);
    }

    /**
     * Server side only.
     */
    public static float getActiveScale(Entity entity, IElementReaction reaction) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.getActiveScale(reaction), 0F);
    }

    public static void setElementAmount(Entity entity, Elements element, boolean robust, float value) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.setElementAmount(element, robust, value));
    }

    public static void addElementAmount(Entity entity, Elements element, boolean robust, float value) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.addElementAmount(element, robust, value));
    }

    public static float getElementAmount(Entity entity, Elements element, boolean robust) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.getElementAmount(element, robust), 0F);
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static float getAmount(Entity entity, Elements element, boolean mustRobust) {
        return EntityUtil.getCapabilityResult(entity, cap -> {
            return cap.getElementAmount(element, true) + (mustRobust ? 0 : cap.getElementAmount(element, false));
        }, 0F);
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static void consumeAmount(Entity entity, Elements element, boolean mustRobust, float amount) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
            final float robustAmount = cap.getElementAmount(element, true);
            if(robustAmount >= amount || mustRobust){
                cap.addElementAmount(element, true, -amount);
            } else {
                cap.setElementAmount(element, true, 0);
                cap.addElementAmount(element, false, robustAmount - amount);
            }
        });
    }

    public static boolean hasElement(Entity entity, Elements element, boolean robust) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.hasElement(element, robust), false);
    }

    public static void clearElements(Entity entity) {
        EntityUtil.getOptCapability(entity).ifPresent(IMMEntityCapability::clearElements);
    }

    public static Map<Elements, Float> getElements(Entity entity) {
        return EntityUtil.getCapabilityResult(entity, IMMEntityCapability::getElementMap, Map.of());
    }

    public static void clearActiveReactions(Entity entity) {
        EntityUtil.getOptCapability(entity).ifPresent(IMMEntityCapability::clearElements);
    }

    public static boolean displayRobust(Entity entity){
        return (entity.tickCount % (DISPLAY_ROBUST_CD << 1)) < DISPLAY_ROBUST_CD;
    }

    public static boolean notDisappear(Entity entity){
        return (entity.tickCount % (DISAPPEAR_CD << 1)) < DISAPPEAR_CD;
    }

    /**
     * Amount need below threshold and last time is less than threshold.
     */
    public static boolean needWarn(Entity entity, Elements element, boolean robust, float amount){
        return (amount < DISAPPEAR_WARN_AMOUNT) && (amount / getDecayAmount(entity, element, robust) < DISAPPEAR_WARN_CD);
    }

    public static boolean canSeeElements(Player player, Entity entity, double distanceSqr){
        return distanceSqr < 1000;
    }

    public static MutableComponent name(Elements element){
        return TipUtil.misc("element." + element.name().toLowerCase());
    }

}
