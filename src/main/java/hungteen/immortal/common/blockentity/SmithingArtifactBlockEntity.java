package hungteen.immortal.common.blockentity;

import hungteen.htlib.common.blockentity.ItemHandlerBlockEntity;
import hungteen.htlib.common.menu.container.SimpleCraftingContainer;
import hungteen.immortal.api.interfaces.IArtifact;
import hungteen.immortal.common.ArtifactManager;
import hungteen.immortal.common.menu.SmithingArtifactMenu;
import hungteen.immortal.common.recipe.ImmortalRecipes;
import hungteen.immortal.common.recipe.SmithingArtifactRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
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

    public static final MutableComponent TITLE = Component.translatable("gui.immortal.smithing_artifact.title");
    public static final int MAX_PROGRESS_VALUE = 100;
    private static final int SIDE_LEN = 10;
    protected final ItemStackHandler itemHandler = new ItemStackHandler(25){
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
        }
    };
    private int bestSmithingPoint = 0;
    private float smithingSpeedMultiple = 1F;
    private int currentSmithingValue = 0;
    private int requireSmithingValue = 0;
    private int requireLevel = 0;
    private ItemStack result = ItemStack.EMPTY;

    public SmithingArtifactBlockEntity(BlockPos blockPos, BlockState state) {
        super(ImmortalBlockEntities.SMITHING_ARTIFACT.get(), blockPos, state);
    }

    /**
     * Client side check.
     */
    public boolean canSmithing(){
        return ! this.result.isEmpty();
    }

    public void finishSmithing(Player player, float smithingProgress, boolean isMainHand){
        final float difference = Math.abs(smithingProgress - this.bestSmithingPoint) / MAX_PROGRESS_VALUE;
        final int score = Mth.floor((1 - difference) *  25);
        ItemStack stack = player.getItemInHand(isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        final int levelDif = ArtifactManager.getArtifactLevel(stack) - this.requireLevel;
        final int baseCost = 25;
        final int requireDurability = levelDif == 0 ? baseCost : levelDif > 0 ? baseCost / (1 + levelDif) : baseCost * (1 - levelDif);
        this.currentSmithingValue += score;
        stack.hurtAndBreak(requireDurability, player, p -> {
            p.broadcastBreakEvent(isMainHand ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        });
        // successfully smithing.
        if(this.currentSmithingValue >= this.requireSmithingValue) {
            for (int i = 0; i < this.itemHandler.getSlots(); ++i) {
                this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            }
            this.itemHandler.setStackInSlot(12, this.result.copy());
            this.result = ItemStack.EMPTY;
            this.resetRecipe();
        } else{
            this.chooseBestPoint();
        }
        this.update();
    }

    public void updateRecipes(){
        if(! this.level.isClientSide){
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
                    this.smithingSpeedMultiple = opt.get().getSpeedMultiple();
                }
            } else{
                if(! this.result.isEmpty()){
                    this.result = ItemStack.EMPTY;
                    this.resetRecipe();
                }
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
        this.currentSmithingValue = 0;
        this.chooseBestPoint();
        this.update();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public void update(){
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        this.setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("BestSmithingPoint")){
            this.bestSmithingPoint = tag.getInt("BestSmithingPoint");
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
        tag.putInt("BestSmithingPoint", this.bestSmithingPoint);
        tag.putInt("CurrentSmithingValue", this.currentSmithingValue);
        tag.putInt("RequireSmithingValue", this.requireSmithingValue);
        tag.putFloat("SmithingSpeedMultiple", this.smithingSpeedMultiple);
        tag.putInt("RequireLevel", this.requireLevel);
        tag.put("ResultItem", this.result.serializeNBT());
    }

    public float getSmithingSpeedMultiple() {
        return smithingSpeedMultiple;
    }

    public int getRequireLevel() {
        return requireLevel;
    }

    public int getBestSmithingPoint() {
        return bestSmithingPoint;
    }

    public int getRequireSmithingValue() {
        return requireSmithingValue;
    }

    public int getCurrentSmithingValue() {
        return currentSmithingValue;
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public int getArtifactLevel() {
        return this.getBlockState().getBlock() instanceof IArtifact ? ((IArtifact) this.getBlockState().getBlock()).getArtifactLevel() : 0;
    }

    @Override
    protected Component getDefaultName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SmithingArtifactMenu(id, inventory, this.getBlockPos());
    }
}
