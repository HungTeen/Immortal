package hungteen.immortal.common.blockentity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 09:30
 **/
public class SpiritualFurnaceBlockEntity {
//        extends ItemHandlerBlockEntity implements MenuProvider {
//
//    private static final int[] SLOTS_FOR_DIRECTIONS = new int[]{1, 2, 3};
//    /* first slot is for flame gourd, the rest are spiritual stone slots. */
//    protected final ItemStackHandler itemHandler = new ItemStackHandler(4);
//    protected final ContainerData accessData = new ContainerData() {
//        @Override
//        public int get(int id) {
//            switch (id) {
//                case 0: return SpiritualFurnaceBlockEntity.this.currentFlameValue;
//                case 1: return SpiritualFurnaceBlockEntity.this.triggered ? 1 : 0;
//                case 2: return SpiritualFurnaceBlockEntity.this.maxFlameValue;
//            }
//            Util.error("Unable to find suitable for Id");
//            return 0;
//        }
//
//        @Override
//        public void set(int id, int value) {
//            switch (id) {
//                case 0 -> SpiritualFurnaceBlockEntity.this.currentFlameValue = value;
//                case 1 -> SpiritualFurnaceBlockEntity.this.triggered = (value == 1);
//                case 2 -> SpiritualFurnaceBlockEntity.this.maxFlameValue = value;
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    };
//    private int currentFlameValue = 0;
//    private int maxFlameValue = 0;
//    private boolean triggered = false;
//
//    public SpiritualFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
//        super(ImmortalBlockEntities.SPIRITUAL_FURNACE.get(), blockPos, blockState);
//    }
//
//    public static void serverTick(Level level, BlockPos blockPos, BlockState state, SpiritualFurnaceBlockEntity blockEntity) {
//        if(blockEntity.triggered){
//            if(blockEntity.canSmelt()){
//                blockEntity.smelting(1);
//            } else{
//                blockEntity.stop();
//            }
//            blockEntity.setChanged();
//        }
//        if(state.getValue(SpiritualFurnace.LIT) ^ blockEntity.triggered){
//            level.setBlock(blockPos, state.setValue(SpiritualFurnace.LIT, blockEntity.triggered), 3);
//        }
//    }
//
//    public void start(){
//        this.triggered = true;
//        this.setChanged();
//    }
//
//    public void stop(){
//        this.triggered = false;
//        this.currentFlameValue = 0;
//        this.setChanged();
//    }
//
//    public void smelting(int consumeValue){
//        if(currentFlameValue >= consumeValue){
//            currentFlameValue -= consumeValue;
//        } else{
//            while(FlameGourd.getFlameAmount(getGourd()) > 0 && currentFlameValue < consumeValue){
//                FlameGourd.addFlameAmount(getGourd(), FlameGourd.getFlameLevel(getGourd()), -1);
//                currentFlameValue += getGourdFlameValue();
//                getFirstStone().ifPresent(stack -> {
//                    if(stack.is(ImmortalItemTags.SPIRITUAL_STONES_LEVEL_ONE)){
//                        currentFlameValue += 40;
//                    } else if(stack.is(ImmortalItemTags.SPIRITUAL_STONES_LEVEL_TWO)){
//                        currentFlameValue += 80;
//                    }
//                });
//            }
//            currentFlameValue -= consumeValue;
//            maxFlameValue = currentFlameValue;
//            if(currentFlameValue < 0){
//                this.stop();
//            }
//        }
//        this.setChanged();
//    }
//
//    public boolean canSmelt(){
//        return this.triggered && (this.currentFlameValue > 0 || FlameGourd.getFlameAmount(getGourd()) > 0);
//    }
//
//    public int getGourdFlameValue(){
//        return getSpiritualFlameValue(FlameGourd.getFlameLevel(getGourd()));
//    }
//
//    public static int getSpiritualFlameValue(int level){
//        return level * 100;
//    }
//
//    private ItemStack getGourd(){
//        return this.getItemHandler().getStackInSlot(0);
//    }
//
//    public Optional<ItemStack> getFirstStone(){
//        for(int i = 1; i < 4; ++ i){
//            if(this.getItemHandler().getStackInSlot(i).is(ImmortalItemTags.SPIRITUAL_STONES)){
//                return Optional.of(this.getItemHandler().getStackInSlot(i));
//            }
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public void load(CompoundTag tag) {
//        super.load(tag);
//        if(tag.contains("HasTriggered")){
//            this.triggered = tag.getBoolean("HasTriggered");
//        }
//        if(tag.contains("CurrentFlameValue")){
//            this.currentFlameValue = tag.getInt("CurrentFlameValue");
//        }
//        if(tag.contains("MaxFlameValue")){
//            this.maxFlameValue = tag.getInt("MaxFlameValue");
//        }
//    }
//
//    @Override
//    protected void saveAdditional(CompoundTag tag) {
//        super.saveAdditional(tag);
//        tag.putBoolean("HasTriggered", this.triggered);
//        tag.putInt("CurrentFlameValue", this.currentFlameValue);
//        tag.putInt("MaxFlameValue", this.maxFlameValue);
//    }
//
//    @Override
//    public ItemStackHandler getItemHandler() {
//        return itemHandler;
//    }
//
//    @Nullable
//    @Override
//    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
//        return new SpiritualFurnaceMenu(id, inventory, this.accessData, this.getBlockPos());
//    }
//
//    public IArtifactType getArtifactType() {
//        return this.getBlockState().getBlock() instanceof IArtifactBlock ? ((IArtifactBlock) this.getBlockState().getBlock()).getArtifactType(this.getBlockState()) : ArtifactTypes.EMPTY;
//    }
}
