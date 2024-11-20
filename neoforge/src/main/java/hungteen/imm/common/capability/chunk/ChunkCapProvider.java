package hungteen.imm.common.capability.chunk;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/11 16:36
 */
public class ChunkCapProvider {
//        implements ICapabilitySerializable<CompoundTag> {
//
//    private ChunkCapability chunkCapability;
//    private LazyOptional<ChunkCapability> chunkCapOpt = LazyOptional.of(this::create);
//
//    public ChunkCapProvider(LevelChunk chunk) {
//        this.chunkCapOpt.ifPresent(cap -> cap.init(chunk));
//    }
//
//    private @NotNull ChunkCapability create() {
//        if (chunkCapability == null) {
//            chunkCapability = new ChunkCapability();
//        }
//        return chunkCapability;
//    }
//
//    @Override
//    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        return this.getCapability(cap);
//    }
//
//    @Override
//    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
//        if (cap == CapabilityHandler.CHUNK_CAP) {
//            return chunkCapOpt.cast();
//        }
//        return LazyOptional.empty();
//    }
//
//    @Override
//    public CompoundTag serializeNBT() {
//        return chunkCapability.serializeNBT();
//    }
//
//    @Override
//    public void deserializeNBT(CompoundTag nbt) {
//        chunkCapability.deserializeNBT(nbt);
//    }
}