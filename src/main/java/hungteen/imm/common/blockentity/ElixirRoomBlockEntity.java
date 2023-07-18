package hungteen.imm.common.blockentity;

import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.impl.ArtifactTypes;
import hungteen.imm.common.menu.furnace.ElixirRoomMenu;
import hungteen.imm.common.recipe.ElixirRecipe;
import hungteen.imm.common.recipe.IMMRecipes;
import hungteen.imm.common.world.ElixirManager;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:59
 **/
public class ElixirRoomBlockEntity extends FunctionalFurnaceBlockEntity {

    public static final MutableComponent TITLE = TipUtil.gui("elixir_room");
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
        if(blockEntity.started()){
            if(blockEntity.canContinue()){
                if(++ blockEntity.smeltingTick >= blockEntity.smeltingCD){
                    blockEntity.onFinish();
                }
                blockEntity.setChanged();
            }
        }
    }

    /**
     * Click white flame to start.
     */
    @Override
    public void onStart(Level level) {
        super.onStart(level);
        this.getFurnaceOpt().ifPresent(furnace -> {
            this.smeltingTick = 0;
            this.updateResult();
            for(int i = 0 ; i < this.itemHandler.getSlots() ; ++ i){
                this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            }
            this.update();
        });
    }

    @Override
    public void onFinish(){
        super.onFinish();
        this.smeltingTick = 0;
        this.itemHandler.setStackInSlot(4, this.result.copy());
        this.result = ItemStack.EMPTY;
        this.update();
    }

    public void updateResult(){
        if(level instanceof ServerLevel serverLevel){
            final SimpleContainer container = new SimpleContainer(9);
            for(int i = 0; i < this.itemHandler.getSlots(); ++ i){
                container.setItem(i, this.itemHandler.getStackInSlot(i));
            }
            final Optional<ElixirRecipe> recipe = level.getRecipeManager().getRecipeFor(IMMRecipes.ELIXIR.get(), container, level);
            if(recipe.isPresent() && matchLevel(recipe.get())){
                // Locked recipe.
                this.result = recipe.get().assemble(container, level.registryAccess());
                this.smeltingCD = recipe.get().getSmeltingCD();
            } else {
                // Custom recipe.
                ElixirManager.getElixirResult(serverLevel, container).ifPresentOrElse(stack -> {
                    this.result = stack.copy();
                }, () -> {
                    this.elixirExplode();
                });
                this.smeltingCD = 100;
            }
        }
    }

    public List<ElixirRecipe> getAvailableRecipes(Level level){
        return level.getRecipeManager().getAllRecipesFor(IMMRecipes.ELIXIR.get()).stream().filter(this::matchLevel).toList();
    }

    public void elixirExplode(){
        if(level != null){
            this.level.explode(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 10, true, Level.ExplosionInteraction.BLOCK);
            getFurnaceOpt().ifPresent(SpiritualFurnaceBlockEntity::stop);
        }
    }

    /**
     * TODO Recipe level check.
     */
    public boolean matchLevel(ElixirRecipe recipe){
        return recipe.getRequireLevel() <= 1;
    }

    public boolean hasIngredient(){
        for(int i = 0; i < this.itemHandler.getSlots(); ++ i){
            if(! this.itemHandler.getStackInSlot(i).isEmpty()){
                return true;
            }
        }
        return false;
    }

    public int getSmeltingCD() {
        return smeltingCD;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
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
