package hungteen.imm.common.blockentity;

import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.impl.ArtifactTypes;
import hungteen.imm.common.menu.furnace.ElixirRoomMenu;
import hungteen.imm.common.recipe.ElixirRecipe;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:59
 **/
public class ElixirRoomBlockEntity extends FunctionalFurnaceBlockEntity {

    public static final MutableComponent TITLE = TipUtil.gui("elixir_room");
    private static final int[] SLOTS_FOR_DIRECTIONS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    /* the rest are spiritual stone slots. */
    protected final ItemStackHandler itemHandler = new ItemStackHandler(9){
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    protected final ContainerData accessData = new ContainerData() {
        @Override
        public int get(int id) {
            return switch (id) {
                case 0 -> ElixirRoomBlockEntity.this.start ? 1 : 0;
                case 1 -> ElixirRoomBlockEntity.this.smeltingTick;
                default -> 0;
            };
        }

        @Override
        public void set(int id, int value) {
            switch (id) {
                case 0 -> ElixirRoomBlockEntity.this.start = value == 1;
                case 1 -> ElixirRoomBlockEntity.this.smeltingTick = value;
                default -> {
                    Util.warn("Wrong id {}", id);
                }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    private ItemStack result = ItemStack.EMPTY;
    private int smeltingCD = 0;
    private int smeltingTick = 0;

    public ElixirRoomBlockEntity(BlockPos blockPos, BlockState state) {
        super(IMMBlockEntities.ELIXIR_ROOM.get(), blockPos, state);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, ElixirRoomBlockEntity blockEntity) {
        if(blockEntity.start){
            if(++ blockEntity.smeltingTick >= blockEntity.smeltingCD){
                blockEntity.onFinish();
            }
            blockEntity.setChanged();
        }
    }

    /**
     * Click white flame to start.
     */
    public void onStart(Level level) {
        this.start = true;
        this.smeltingTick = 0;
        this.getRecipeOpt().ifPresent(recipe -> {
            this.smeltingCD = recipe.getSmeltingCD();
            this.result = recipe.getResultItem(level.registryAccess()).copy();
        });
        for(int i = 0 ; i < this.itemHandler.getSlots() ; ++ i){
            this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
        this.update();
    }

    public void onFinish(){
        this.start = false;
        this.smeltingTick = 0;
        this.update();
    }

    private Optional<ElixirRecipe> getRecipeOpt(){
//        SimpleContainer container = new SimpleContainer(9);
//        for(int i = 0; i < this.itemHandler.getSlots(); ++ i){
//            container.setItem(i, this.itemHandler.getStackInSlot(i));
//        }
//        return level.getRecipeManager().getRecipeFor(ImmortalRecipes.ELIXIR_RECIPE_TYPE.get(), container, level);
        return Optional.empty();
    }

    public void explode(){
//        this.level.explode(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 10, true, Explosion.BlockInteraction.DESTROY);
//        getBelowBlockEntity().ifPresent(SpiritualFurnaceBlockEntity::stop);
//        this.reset();
    }

    public void reset(){
        this.smeltingTick = 0;
        this.smeltingCD = 0;
        this.update();
    }

    public void update(){
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        this.setChanged();
    }

    public boolean canStart(){
        return getBelowBlockEntity().isPresent() && getBelowBlockEntity().get().canSmelt();
    }

    public boolean isSmeltStart() {
        return start;
    }

    private Optional<SpiritualFurnaceBlockEntity> getBelowBlockEntity(){
        BlockEntity blockEntity = level.getBlockEntity(getBlockPos().below());
        if(blockEntity instanceof SpiritualFurnaceBlockEntity){
            return Optional.of(((SpiritualFurnaceBlockEntity) blockEntity));
        }
        return Optional.empty();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("SmeltingStart")){
            this.start = tag.getBoolean("SmeltingStart");
        }
        if(tag.contains("ElixirResult")){
            this.result = ItemStack.of(tag.getCompound("ElixirResult"));
        }
        if(tag.contains("SmeltingCD")){
            this.smeltingCD = tag.getInt("SmeltingCD");
        }
        if(tag.contains("SmeltingTick")){
            this.smeltingTick = tag.getInt("SmeltingTick");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("SmeltingStart", this.start);
        tag.put("ElixirResult", this.result.serializeNBT());
        tag.putInt("SmeltingCD", this.smeltingCD);
        tag.putInt("SmeltingTick", this.smeltingTick);
    }

    public ItemStack getResultItem(){
        return this.result;
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    protected Component getDefaultName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ElixirRoomMenu(id, inventory, this.accessData, this.getBlockPos());
    }

    public IArtifactType getArtifactType() {
        return this.getBlockState().getBlock() instanceof IArtifactBlock ? ((IArtifactBlock) this.getBlockState().getBlock()).getArtifactType(this.getBlockState()) : ArtifactTypes.EMPTY;
    }

}
