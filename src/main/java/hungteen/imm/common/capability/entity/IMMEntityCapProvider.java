package hungteen.imm.common.capability.entity;

import hungteen.imm.common.capability.CapabilityHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-02-25 22:01
 **/
public class IMMEntityCapProvider implements ICapabilitySerializable<CompoundTag> {

    private IMMEntityCapability entityCapability;
    private LazyOptional<IMMEntityCapability> entityCapOpt = LazyOptional.of(this::create);

    public IMMEntityCapProvider(Entity entity){
        this.entityCapOpt.ifPresent(cap -> cap.init(entity));
    }

    private @NotNull IMMEntityCapability create(){
        if(entityCapability == null){
            entityCapability = new IMMEntityCapability();
        }
        return entityCapability;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == CapabilityHandler.ENTITY_CAP){
            return entityCapOpt.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return entityCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        entityCapability.deserializeNBT(nbt);
    }
}
