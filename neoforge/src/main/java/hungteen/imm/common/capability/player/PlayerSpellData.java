package hungteen.imm.common.capability.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/21 21:01
 **/
public class PlayerSpellData implements INBTSerializable<CompoundTag> {

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }
}
