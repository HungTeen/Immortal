package hungteen.imm.data.loot;

import hungteen.htlib.data.loot.HTLootTableGen;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/2 14:36
 **/
public class LootTableGen extends HTLootTableGen {

    public LootTableGen(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(EntityLootGen::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(BlockLootGen::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ChestLootGen::new, LootContextParamSets.CHEST)
        ));
    }

}
