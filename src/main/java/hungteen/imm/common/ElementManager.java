package hungteen.imm.common;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.capability.entity.IMMEntityCapability;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.Entity;

import java.util.Iterator;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/1 15:19
 */
public class ElementManager {

    public static final int ESCAPE_UPDATE_CD = 20;

    /**
     * 在世界更新末尾更新，仅服务端。
     */
    public static void tickElements(Entity entity){
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
            cap.updatePossibleReactions();
            // Element Reactions.
            final Iterator<IElementReaction> iterator = cap.getPossibleReactions().iterator();
            while (iterator.hasNext()) {
                final IElementReaction reaction = iterator.next();
                if(reaction.match(entity)){
                    reaction.doReaction(entity);
                    reaction.consume(entity);
                } else {
                    iterator.remove();
                }
            }
            // Decay Elements.
            for(int i = 0; i < 2; ++ i){
                final boolean robust = (i == 0);
                for (Elements element : Elements.values()) {
                    if(cap.hasElement(element, robust)){
                        final float decayAmount = getDecayAmount(entity, element, robust);
                        cap.addElementAmount(element, robust, - decayAmount);
                    }
                }
            }
        });
    }

    public static float getDecayAmount(Entity entity, Elements element, boolean robust){
        return element.getDecaySpeed();
    }

    /**
     * Server side only.
     */
    public static boolean hasReaction(Entity entity, IElementReaction reaction){
        return EntityUtil.getCapabilityResult(entity, cap -> cap.hasReaction(reaction), false);
    }

    public static void setElementAmount(Entity entity, Elements element, boolean robust, float value){
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.setElementAmount(element, robust, value));
    }

    public static void addElementAmount(Entity entity, Elements element, boolean robust, float value){
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.addElementAmount(element, robust, value));
    }

    public static float getElementAmount(Entity entity, Elements element, boolean robust){
        return EntityUtil.getCapabilityResult(entity, cap -> cap.getElementAmount(element, robust), 0F);
    }

    public static boolean hasElement(Entity entity, Elements element, boolean robust){
        return EntityUtil.getCapabilityResult(entity, cap -> cap.hasElement(element, robust), false);
    }

    public static void clearElements(Entity entity){
        EntityUtil.getOptCapability(entity).ifPresent(IMMEntityCapability::clearElements);
    }

}
