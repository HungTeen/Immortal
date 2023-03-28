package hungteen.imm.common.capability.player;

import hungteen.imm.common.capability.CapabilityHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:05
 **/
public class PlayerCapProvider implements ICapabilitySerializable<CompoundTag> {

    private PlayerCapability playerCap;

    private LazyOptional<PlayerCapability> playerCapOpt = LazyOptional.of(this::create);

    public PlayerCapProvider(Player player){
        this.playerCapOpt.ifPresent(cap -> cap.init(player));
    }

    private @NotNull PlayerCapability create(){
        if(playerCap == null){
            playerCap = new PlayerCapability();
        }
        return playerCap;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == CapabilityHandler.PLAYER_CAP){
            return playerCapOpt.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return playerCap.get().saveToNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        playerCap.get().loadFromNBT(nbt);
    }
}
