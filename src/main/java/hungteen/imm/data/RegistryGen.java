package hungteen.imm.data;

import hungteen.imm.utils.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 15:16
 **/
public class RegistryGen extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();

    private RegistryGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", Util.id()));
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
