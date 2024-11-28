package hungteen.imm.api.spell;

import hungteen.htlib.api.registry.SimpleEntry;
import net.minecraft.world.entity.Entity;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/1 16:22
 */
public interface ElementReaction extends SimpleEntry {

    /**
     * @param entity 该实体上能否发生元素反应。
     */
    float match(Entity entity);

    /**
     * 元素反应。
     * @param scale 非原始强度，经过换算。
     */
    void doReaction(Entity entity, float scale);

    /**
     * 根据反应比例消耗元素。
     * @param scale 原始比例。
     */
    void consume(Entity entity, float scale);

    /**
     * 一瞬间发生的元素反应可以不记录正在发生。
     */
    boolean once();

    /**
     * 值越大越优先反应。
     */
    int priority();
}
