package hungteen.imm.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.world.entity.Entity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/1 16:22
 */
public interface IElementReactionType extends ISimpleEntry {

    boolean match(Entity entity);

    void reaction(Entity entity);

    void consume(Entity entity);
}
