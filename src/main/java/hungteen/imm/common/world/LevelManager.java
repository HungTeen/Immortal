package hungteen.imm.common.world;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.ImmortalAPI;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-15 19:13
 **/
public class LevelManager {

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerSpiritualLevels(){
        List.of(
                Pair.of(Level.OVERWORLD, 0.2F),
                Pair.of(Level.NETHER, 0.25F),
                Pair.of(Level.END, 0.15F)
        ).forEach(pair -> {
            ImmortalAPI.get().registerLevelSpiritualRatio(pair.getFirst(), pair.getSecond());
        });
    }

}
