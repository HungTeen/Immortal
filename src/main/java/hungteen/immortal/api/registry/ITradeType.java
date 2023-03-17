package hungteen.immortal.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.world.entity.Entity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/16 10:55
 */
public interface ITradeType extends ISimpleEntry {

    void deal(Entity solder, Entity customer);

}
