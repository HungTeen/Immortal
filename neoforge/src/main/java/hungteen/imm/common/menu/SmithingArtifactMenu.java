package hungteen.imm.common.menu;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-13 21:51
 **/
public class SmithingArtifactMenu {
//        extends HTContainerMenu {
//
//    private final SmithingArtifactBlockEntity blockEntity;
//    private final ContainerLevelAccess accessLevel;
//    private final Player player;
//
//    public SmithingArtifactMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
//        this(id, inventory, buffer.readBlockPos());
//    }
//
//    public SmithingArtifactMenu(int id, Inventory inventory, BlockPos pos) {
//        super(id, ImmortalMenus.SMITHING_ARTIFACT.get());
//        this.player = inventory.player;
//        this.accessLevel = ContainerLevelAccess.create(inventory.player.level, pos);
//        BlockEntity blockEntity = inventory.player.level.getBlockEntity(pos);
//        if(blockEntity instanceof SmithingArtifactBlockEntity){
//            this.blockEntity = (SmithingArtifactBlockEntity) blockEntity;
//            this.blockEntity.update();
//        } else{
//            throw new RuntimeException("Invalid block entity !");
//        }
//
//        this.addInventories(51, 31, 5, 5, 0, (x, y, slotId) -> new SlotItemHandler(this.blockEntity.getItemHandler(), x, y, slotId) {
//            @Override
//            public boolean mayPlace(@NotNull ItemStack stack) {
//                return true;
//            }
//
//            @Override
//            public void setChanged() {
//                super.setChanged();
//                SmithingArtifactMenu.this.blockEntity.updateRecipes();
//            }
//        });
//
//        this.addInventoryAndHotBar(inventory, 15, 131);
//    }
//
//    @Override
//    public boolean stillValid(Player player) {
//        return true;
////        return stillValid(this.accessLevel, player, ImmortalBlocks.COPPER_SMITHING_ARTIFACT.get());
//    }
}
