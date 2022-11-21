package hungteen.immortal.data.codec;

import hungteen.htlib.data.HTCodecGen;
import hungteen.immortal.utils.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;

import java.util.stream.StreamSupport;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 15:16
 **/
public class RegistryGen extends HTCodecGen {

    public RegistryGen(DataGenerator generator) {
        super(generator, Util.id());
    }

    @Override
    public void run(CachedOutput cache) {
        StreamSupport.stream(RegistryAccess.knownRegistries().spliterator(), false)
                .filter(r -> access().ownedRegistry(r.key()).isPresent())
                .forEach(data -> registerCap(cache, data));
    }

    @Override
    public String getName() {
        return this.modId + " misc registries";
    }
}
