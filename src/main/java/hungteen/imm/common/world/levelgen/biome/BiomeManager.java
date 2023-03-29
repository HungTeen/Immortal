package hungteen.imm.common.world.levelgen.biome;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.IMMAPI;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-15 19:00
 **/
public class BiomeManager {

    /**
     * {@link ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerSpiritualBiomes(){
        List.of(
                Pair.of(Biomes.LUSH_CAVES, 15)
        ).forEach(pair -> {
            IMMAPI.get().registerBiomeSpiritualValue(pair.getFirst(), pair.getSecond());
        });
    }

}
