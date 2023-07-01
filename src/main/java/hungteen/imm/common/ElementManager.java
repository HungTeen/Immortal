package hungteen.imm.common;

import hungteen.imm.api.enums.Elements;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.Entity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/1 15:19
 */
public class ElementManager {

    public static final int UPDATE_CD = 20;

    /**
     * Shrinking elements.
     */
    public static void tickElements(Entity entity){

    }

    public static void setEntityElement(Entity entity, Elements element, boolean robust, float value){
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.setElement(element, robust, value));
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.setElement(element, robust, value));
    }

    public static void addEntityElement(Entity entity, Elements element, boolean robust, float value){
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.addElement(element, robust, value));
    }

    public static float getEntityElement(Entity entity, Elements element, boolean robust){
        return EntityUtil.getCapabilityResult(entity, cap -> cap.getElement(element, robust), 0F);
    }

}
