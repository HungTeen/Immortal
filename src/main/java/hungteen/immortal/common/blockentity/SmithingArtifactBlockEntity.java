package hungteen.immortal.common.blockentity;

import hungteen.htlib.blockentity.ItemHandlerBlockEntity;
import hungteen.htlib.menu.container.SimpleCraftingContainer;
import hungteen.immortal.api.interfaces.IArtifact;
import hungteen.immortal.common.menu.SmithingArtifactMenu;
import hungteen.immortal.common.recipe.ImmortalRecipes;
import hungteen.immortal.common.recipe.SmithingArtifactRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 21:44
 **/
public class SmithingArtifactBlockEntity extends ItemHandlerBlockEntity implements MenuProvider, IArtifact {

    public static final int MAX_PROGRESS_VALUE = 100;
    private static final int SIDE_LEN = 10;
    protected final ItemStackHandler itemHandler = new ItemStackHandler(25){
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            SmithingArtifactBlockEntity.this.updateRecipes();
        }
    };
    private float smithingProgress = 0;
    private int bestSmithingPoint = 0;
    private boolean direction = true;
    private float smithingSpeedMultiple = 1F;
    private int currentSmithingValue = 0;
    private int requireSmithingValue = 0;
    private int requireLevel = 0;
    private ItemStack result = ItemStack.EMPTY;

    public SmithingArtifactBlockEntity(BlockPos blockPos, BlockState state) {
        super(ImmortalBlockEntities.SMITHING_ARTIFACT.get(), blockPos, state);
    }

    public void onSmithing(){
        this.smithingProgress += Mth.clamp((direction ? 1 : -1 ) * this.smithingSpeedMultiple * 0.5F, 0, MAX_PROGRESS_VALUE);
        // change move direction.
        if(this.direction && this.smithingProgress >= MAX_PROGRESS_VALUE){
            this.direction = false;
        } else if(!this.direction && this.smithingProgress <= 0){
            this.direction = true;
        }
        this.update();
    }

    public void finishSmithing(){
        final int score = (int) (100 - (Math.abs(this.smithingProgress - this.bestSmithingPoint) * 200 / MAX_PROGRESS_VALUE));
        this.currentSmithingValue += score;
        if(this.currentSmithingValue >= this.requireSmithingValue) {
            for (int i = 0; i < this.itemHandler.getSlots(); ++i) {
                this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        this.itemHandler.setStackInSlot(12, this.result.copy());
        this.result = ItemStack.EMPTY;
        this.resetRecipe();
    }

    public void updateRecipes(){
        final SimpleCraftingContainer container = new SimpleCraftingContainer(5, 5);
        for(int i = 0; i < container.getContainerSize(); ++ i) {
            container.setItem(i, itemHandler.getStackInSlot(i).copy());
        }
        Optional<SmithingArtifactRecipe> opt = Objects.requireNonNull(level).getRecipeManager().getRecipeFor(ImmortalRecipes.SMITHING_ARTIFACT_RECIPE_TYPE.get(), container, level);
        if(opt.isPresent()){
            // recipe changed.
            if(this.result.isEmpty() || this.result.equals(opt.get().getResultItem(), true)){
                this.result = opt.get().getResultItem();
                this.resetRecipe();
                this.requireSmithingValue = opt.get().getSmithingValue();
                this.requireLevel = opt.get().getRequireLevel();
                this.smithingSpeedMultiple = opt.get().getSpeedMultiplier();
            }
        } else{
            if(! this.result.isEmpty()){
                this.result = ItemStack.EMPTY;
                this.resetRecipe();
            }
        }
    }

    public boolean hasSpiritualFlameNearby(){
        return true;
    }

    protected void chooseBestPoint(){
        this.bestSmithingPoint = Mth.clamp((int) (this.getRandom().nextDouble() * MAX_PROGRESS_VALUE), SIDE_LEN, MAX_PROGRESS_VALUE - SIDE_LEN);
    }

    protected void resetRecipe(){
        this.smithingProgress = 0;
        this.currentSmithingValue = 0;
        this.direction = true;
        this.chooseBestPoint();
        this.update();
    }

    public void update(){
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        this.setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("SmithingProgress")){
            this.smithingProgress = tag.getFloat("SmithingProgress");
        }
        if(tag.contains("BestSmithingPoint")){
            this.bestSmithingPoint = tag.getInt("BestSmithingPoint");
        }
        if(tag.contains("Direction")){
            this.direction = tag.getBoolean("Direction");
        }
        if(tag.contains("CurrentSmithingValue")){
            this.currentSmithingValue = tag.getInt("CurrentSmithingValue");
        }
        if(tag.contains("RequireSmithingValue")){
            this.requireSmithingValue = tag.getInt("RequireSmithingValue");
        }
        if(tag.contains("SmithingSpeedMultiple")){
            this.smithingSpeedMultiple = tag.getFloat("SmithingSpeedMultiple");
        }
        if(tag.contains("RequireLevel")){
            this.requireLevel = tag.getInt("RequireLevel");
        }
        if(tag.contains("ResultItem")){
            this.result = ItemStack.of(tag.getCompound("ResultItem"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("SmithingProgress", this.smithingProgress);
        tag.putInt("BestSmithingPoint", this.bestSmithingPoint);
        tag.putBoolean("Direction", this.direction);
        tag.putInt("CurrentSmithingValue", this.currentSmithingValue);
        tag.putInt("RequireSmithingValue", this.requireSmithingValue);
        tag.putFloat("SmithingSpeedMultiple", this.smithingSpeedMultiple);
        tag.putInt("RequireLevel", this.requireLevel);
        tag.put("ResultItem", this.result.serializeNBT());
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public int getArtifactLevel() {
        return this.getBlockState().getBlock() instanceof IArtifact ? ((IArtifact) this.getBlockState().getBlock()).getArtifactLevel() : 0;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SmithingArtifactMenu(id, inventory, this.getBlockPos());
    }
}
