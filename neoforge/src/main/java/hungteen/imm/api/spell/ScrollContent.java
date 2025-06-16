package hungteen.imm.api.spell;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 功法秘籍的内容。
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:26
 */
public interface ScrollContent {

    /**
     * @return 是否能学习此卷轴。
     */
    default boolean canLearn(Level level, Player player){
        return true;
    }

    /**
     * 学习此卷轴。
     */
    void learn(Player player);

    /**
     * @return 卷轴的详细描述。
     */
    MutableComponent getInfo();

    /**
     * @return 学习此卷轴获得的经验值。
     */
    default float getLearnXp(){
        return 1;
    }

    /**
     * @return 卷轴的类型。
     */
    ScrollType<?> getType();

    /**
     * @return 该卷轴的图标。
     */
    ResourceLocation getIcon();

}
