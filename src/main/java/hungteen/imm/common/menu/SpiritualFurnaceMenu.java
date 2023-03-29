package hungteen.imm.common.menu;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 10:48
 **/
public class SpiritualFurnaceMenu {
//        extends HTContainerMenu {
//
//    private final SpiritualFurnaceBlockEntity blockEntity;
//    private final ContainerData accessData;
//    private final ContainerLevelAccess accessLevel;
//    private final Player player;
//
//    public SpiritualFurnaceMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
//        this(id, inventory, new SimpleContainerData(3), buffer.readBlockPos());
//    }
//
//    public SpiritualFurnaceMenu(int id, Inventory inventory, ContainerData accessData, BlockPos pos) {
//        super(id, ImmortalMenus.SPIRITUAL_FURNACE.get());
//        this.accessData = accessData;
//        this.player = inventory.player;
//        this.accessLevel = ContainerLevelAccess.create(inventory.player.level, pos);
//        BlockEntity blockEntity = inventory.player.level.getBlockEntity(pos);
//        if(blockEntity instanceof SpiritualFurnaceBlockEntity){
//            this.blockEntity = (SpiritualFurnaceBlockEntity) blockEntity;
//        } else{
//            throw new RuntimeException("Invalid block entity !");
//        }
//
//        //TODO 火葫芦槽位
////        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 0, 91, 25){
////            @Override
////            public boolean mayPlace(@NotNull ItemStack stack) {
////                return stack.is(ImmortalItems.FLAME_GOURD.get());
////            }
////        });
//
//        for(int i = 0; i < 3; ++ i){
//            this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 1 + i, 73 + 18 * i, 62){
//                @Override
//                public boolean mayPlace(ItemStack stack) {
//                    return stack.is(ImmortalItemTags.SPIRITUAL_STONES);
//                }
//            });
//        }
//
//        this.addInventoryAndHotBar(inventory, 19, 108);
//
//        this.addDataSlots(this.accessData);
//    }
//
//    public int getFlameValue(){
//        return this.accessData.get(0);
//    }
//
//    public boolean triggered(){
//        return this.accessData.get(1) == 1;
//    }
//
//    public int getMaxValue(){
//        return this.accessData.get(2);
//    }
//
//    @Override
//    public ItemStack quickMoveStack(Player player, int slotId) {
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    public boolean stillValid(Player player) {
//        return true;
////        return stillValid(this.accessLevel, player, ImmortalBlocks.COPPER_SPIRITUAL_FURNACE.get());
//    }

}
