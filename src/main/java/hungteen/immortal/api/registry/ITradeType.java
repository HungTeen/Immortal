package hungteen.immortal.api.registry;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.world.entity.Entity;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/16 10:55
 */
public interface ITradeType<T extends ITradeComponent> extends ISimpleEntry {

    /**
     * Get the method to codec trade.
     * @return Codec method.
     */
    Codec<T> codec();

}
