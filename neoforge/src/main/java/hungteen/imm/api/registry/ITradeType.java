package hungteen.imm.api.registry;

import hungteen.htlib.api.registry.SimpleEntry;
import net.minecraft.server.level.ServerPlayer;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/16 10:55
 */
public interface ITradeType extends SimpleEntry {

    void openMenu(ServerPlayer player);

}
