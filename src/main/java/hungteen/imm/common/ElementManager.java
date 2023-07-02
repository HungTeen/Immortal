package hungteen.imm.common;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.capability.entity.IMMEntityCapability;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Iterator;
import java.util.Map;

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
            cap.updatePossibleReactions();
            // Element Reactions.
            final Iterator<IElementReaction> iterator = cap.getPossibleReactions().iterator();
            while (iterator.hasNext()) {
                final IElementReaction reaction = iterator.next();
                if (reaction.match(entity)) {
                    reaction.doReaction(entity);
                    reaction.consume(entity);
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
    public static boolean hasReaction(Entity entity, IElementReaction reaction) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.hasReaction(reaction), false);
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

    public static boolean hasElement(Entity entity, Elements element, boolean robust) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.hasElement(element, robust), false);
    }

    public static void clearElements(Entity entity) {
        EntityUtil.getOptCapability(entity).ifPresent(IMMEntityCapability::clearElements);
    }

    public static Map<Elements, Float> getElements(Entity entity) {
        return EntityUtil.getCapabilityResult(entity, IMMEntityCapability::getElementMap, Map.of());
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

}
