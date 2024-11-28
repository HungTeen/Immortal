package hungteen.imm.api.spell;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:26
 */
public interface IManualContent {

    default boolean canLearn(Level level, Player player){
        return true;
    }

    void learn(Player player);

    MutableComponent getManualTitle();

    MutableComponent getInfo();

    IManualType<?> getType();

    ResourceLocation getTexture();
}
