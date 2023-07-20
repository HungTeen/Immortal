package hungteen.imm.api.registry;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:42
 */
public interface ILearnRequirement {

    boolean check(Level level, Player player);

    IRequirementType<?> getType();

}