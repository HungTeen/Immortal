package hungteen.imm.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.world.entity.Entity;

/**
 * TODO 追加施加元素反应的实体。
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/1 16:22
 */
public interface IElementReaction extends ISimpleEntry {

    boolean match(Entity entity);

    void doReaction(Entity entity);

    void consume(Entity entity);

    int priority();
}
