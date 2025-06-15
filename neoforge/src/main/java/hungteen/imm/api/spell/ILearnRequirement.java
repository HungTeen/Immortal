package hungteen.imm.api.spell;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:42
 */
public interface ILearnRequirement {

    /**
     * @return 是否满足学习要求。
     */
    boolean check(Level level, Player player);

    /**
     * 消耗玩家相应的资源。
     */
    void consume(Level level, Player player);

    /**
     * @param player 相对于玩家的学习要求。
     * @return 学习要求的文字。
     */
    MutableComponent getRequirementInfo(Player player);

    IRequirementType<?> getType();

}
