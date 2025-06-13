package hungteen.imm.common.world;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/13 16:30
 **/
public class IMMGameRules {

    public static final GameRules.Key<GameRules.BooleanValue> RANDOM_QI_ROOTS = GameRules.register("randomQiRoots", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static GameRules getGameRule(Level level){
        return level.getGameRules();
    }

    public static boolean randomQiRoots(Level level){
        return getGameRule(level).getBoolean(RANDOM_QI_ROOTS);
    }

    public static void init(){

    }
}
