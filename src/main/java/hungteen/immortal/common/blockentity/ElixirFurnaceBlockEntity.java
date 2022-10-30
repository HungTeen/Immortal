package hungteen.immortal.common.blockentity;

import hungteen.immortal.ImmortalConfigs;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.ElixirManager;
import hungteen.immortal.common.menu.ElixirFurnaceMenu;
import hungteen.immortal.common.recipe.ElixirRecipe;
import hungteen.immortal.common.recipe.ImmortalRecipes;
import hungteen.immortal.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:59
 **/
public class ElixirFurnaceBlockEntity extends ItemHandlerBlockEntity implements MenuProvider {

    private static final int[] SLOTS_FOR_DIRECTIONS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    /* the rest are spiritual stone slots. */
    protected final ItemStackHandler itemHandler = new ItemStackHandler(9);
    private final Map<ISpiritualRoot, Integer> recipeMap = new HashMap<>();
    private final Map<ISpiritualRoot, Integer> spiritualMap = new HashMap<>();
    protected final ContainerData accessData = new ContainerData() {
        @Override
        public int get(int id) {
            switch (id) {
                case 0 : return ElixirFurnaceBlockEntity.this.smeltingTick;
                case 1 : return ElixirFurnaceBlockEntity.this.explodeTick;
                case 2 : return ElixirFurnaceBlockEntity.this.score;
            }
            Util.error("Unable to find suitable for Id");
            return 0;
        }

        @Override
        public void set(int id, int value) {
            switch (id) {
                case 0 -> ElixirFurnaceBlockEntity.this.smeltingTick = value;
                case 1 -> ElixirFurnaceBlockEntity.this.explodeTick = value;
                case 2 -> ElixirFurnaceBlockEntity.this.score = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    private ItemStack result = ItemStack.EMPTY;
    private SmeltingStates state = SmeltingStates.PREPARE_RECIPE;
    private int prepareCD = 0;
    private int smeltingCD = 0;
    private int ingredientLimit = 0;
    private int smeltingTick = 0;
    private int explodeTick = 0;
    private int score = 0;

    public ElixirFurnaceBlockEntity(BlockPos blockPos, BlockState state) {
        super(ImmortalBlockEntities.ELIXIR_FURNACE.get(), blockPos, state);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, ElixirFurnaceBlockEntity blockEntity) {
        /* 炸炉 */
        if(blockEntity.explodeTick >= ImmortalConfigs.getFurnaceExplodeCD()){
            blockEntity.explode();
        }
        switch (blockEntity.getSmeltingState()) {
            case PREPARE_RECIPE -> {
                if(blockEntity.receiveSpiritualFlame()){
                    /* Start Smelting */
                    if(blockEntity.hasRecipe()){
                        blockEntity.onSmeltStart();
                    } else{
                        ++ blockEntity.explodeTick;
                        blockEntity.setChanged();
                    }
                }
            }
            case PREPARE_INGREDIENTS -> {
                /* Must */
                if(blockEntity.hasRecipe()){
                    ++ blockEntity.smeltingTick;
                    blockEntity.updateSpirituals();
                    if(blockEntity.smeltingTick >= blockEntity.prepareCD){
                        blockEntity.onPrepareFinished();
                    }
                    blockEntity.setChanged();
                }
            }
            case SMELTING -> {
                /* Must */
                if(blockEntity.hasRecipe()){
                    ++ blockEntity.smeltingTick;
                    if(blockEntity.smeltingTick >= blockEntity.smeltingCD){
                        blockEntity.onSuccessSmelt();
                    }
                    blockEntity.setChanged();
                }
            }
        }
    }

    public void onSmeltStart() {
        this.explodeTick = 0;
        this.spiritualMap.clear();
        this.recipeMap.clear();
        this.getRecipeOpt().ifPresent(recipe -> {
            this.prepareCD = recipe.getPrepareCD();
            this.smeltingCD = recipe.getSmeltingCD();
            this.recipeMap.putAll(recipe.getSpiritualMap());
            this.result = recipe.getResultItem().copy();
        });
        for(int i = 0 ; i < this.itemHandler.getSlots() ; ++ i){
            this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
        this.setState(SmeltingStates.PREPARE_INGREDIENTS);
        this.update();
    }

    public void onPrepareFinished(){
        this.setState(SmeltingStates.SMELTING);
        this.update();
    }

    public void updateSpirituals() {
        boolean flag = false;
        for (int i = 0; i < itemHandler.getSlots(); ++i) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                ElixirManager.getElixirIngredient(itemHandler.getStackInSlot(i)).forEach(pair -> {
                    this.spiritualMap.put(pair.getFirst(), this.spiritualMap.getOrDefault(pair.getFirst(), 0) + pair.getSecond());
                });
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                flag = true;
            }
        }
        this.calculateDifference();
        if(flag){
            this.update();
        }
        this.setChanged();
    }

    public void calculateDifference(){
        Set<ISpiritualRoot> roots = new HashSet<>();
        roots.addAll(this.getRecipeMap().keySet());
        roots.addAll(this.getSpiritualMap().keySet());
        List<ISpiritualRoot> list = roots.stream().sorted(Comparator.comparingInt(ISpiritualRoot::getSortPriority)).toList();
        float sum = 0;
        boolean exceeded = false;
        for(int i = 0; i < list.size(); ++ i){
            final int origin = this.getRecipeMap().getOrDefault(list.get(i), 0);
            final int dif = origin - this.getSpiritualMap().getOrDefault(list.get(i), 0);
            exceeded |= (dif < 0);
            sum += dif * dif * 1F / origin / origin;
        }
        if(exceeded){
            ++ this.explodeTick;
        }
        this.score = Math.abs((int)(100F * sum / list.size()) - 100);
    }

    /**
     * Can continue smelting.
     */
    public boolean hasRecipe(){
        return getSmeltingState() == SmeltingStates.PREPARE_RECIPE ? getRecipeOpt().isPresent() : ! this.result.isEmpty();
    }

    private Optional<ElixirRecipe> getRecipeOpt(){
        SimpleContainer container = new SimpleContainer(9);
        for(int i = 0; i < this.itemHandler.getSlots(); ++ i){
            container.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        return level.getRecipeManager().getRecipeFor(ImmortalRecipes.ELIXIR_RECIPE_TYPE, container, level);
    }

    public void explode(){
        this.reset();
    }

    public void onSuccessSmelt(){
        ItemStack stack = this.result.copy();
        itemHandler.setStackInSlot(4, stack);
        this.reset();
    }

    public void reset(){
        this.smeltingTick = 0;
        this.prepareCD = 0;
        this.smeltingCD = 0;
        this.explodeTick = 0;
        this.setState(SmeltingStates.PREPARE_RECIPE);
        this.recipeMap.clear();
        this.spiritualMap.clear();
        this.update();
    }

    public void update(){
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        this.setChanged();
    }

    public boolean receiveSpiritualFlame(){
        return getBelowBlockEntity().isPresent() && getBelowBlockEntity().get().isConsumingFlame();
    }

    private Optional<SpiritualStoveBlockEntity> getBelowBlockEntity(){
        BlockEntity blockEntity = level.getBlockEntity(getBlockPos().below());
        if(blockEntity instanceof SpiritualStoveBlockEntity){
            return Optional.of(((SpiritualStoveBlockEntity) blockEntity));
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
        if(tag.contains("SmeltingState")){
            this.state = SmeltingStates.values()[tag.getInt("SmeltingState")];
        }
        if(tag.contains("ElixirResult")){
            this.result = ItemStack.of(tag.getCompound("ElixirResult"));
        }
        if(tag.contains("PrepareCD")){
            this.prepareCD = tag.getInt("PrepareCD");
        }
        if(tag.contains("SmeltingCD")){
            this.smeltingCD = tag.getInt("SmeltingCD");
        }
        if(tag.contains("SmeltingTick")){
            this.smeltingTick = tag.getInt("SmeltingTick");
        }
        if(tag.contains("ExplodeTick")){
            this.explodeTick = tag.getInt("ExplodeTick");
        }
        if(tag.contains("ElixirScore")){
            this.score = tag.getInt("ElixirScore");
        }
        if(tag.contains("IngredientLimit")){
            this.ingredientLimit = tag.getInt("IngredientLimit");
        }
        if(tag.contains("RecipeMap")){
            CompoundTag nbt = tag.getCompound("RecipeMap");
            int len = nbt.getInt("Len");
            this.recipeMap.clear();
            for(int i = 0;i < len; ++ i){
                final int value = nbt.getInt("Value" + i);
                ImmortalAPI.get().getSpiritualRoot(nbt.getString("Root" + i)).ifPresent(root -> {
                    this.recipeMap.put(root, value);
                });
            }
        }
        if(tag.contains("SpiritualMap")){
            CompoundTag nbt = tag.getCompound("SpiritualMap");
            int len = nbt.getInt("Len");
            this.spiritualMap.clear();
            for(int i = 0; i < len; ++ i){
                final int value = nbt.getInt("Value" + i);
                ImmortalAPI.get().getSpiritualRoot(nbt.getString("Root" + i)).ifPresent(root -> {
                    this.spiritualMap.put(root, value);
                });
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("SmeltingState", this.state.ordinal());
        tag.put("ElixirResult", this.result.serializeNBT());
        tag.putInt("PrepareCD", this.prepareCD);
        tag.putInt("SmeltingCD", this.smeltingCD);
        tag.putInt("SmeltingTick", this.smeltingTick);
        tag.putInt("ExplodeTick", this.explodeTick);
        tag.putInt("ElixirScore", this.score);
        tag.putInt("IngredientLimit", this.ingredientLimit);
        {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("Len", this.recipeMap.size());
            List<ISpiritualRoot> list = this.recipeMap.keySet().stream().toList();
            for(int i = 0; i < list.size(); ++ i){
                nbt.putString("Root" + i, list.get(i).getRegistryName());
                nbt.putInt("Value" + i, this.recipeMap.get(list.get(i)));
            }
            tag.put("RecipeMap", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("Len", this.spiritualMap.size());
            List<ISpiritualRoot> list = this.spiritualMap.keySet().stream().toList();
            for(int i = 0; i < list.size(); ++ i){
                nbt.putString("Root" + i, list.get(i).getRegistryName());
                nbt.putInt("Value" + i, this.spiritualMap.get(list.get(i)));
            }
            tag.put("SpiritualMap", nbt);
        }
    }

    public ItemStack getResultItem(){
        return this.result;
    }

    public Map<ISpiritualRoot, Integer> getRecipeMap() {
        return recipeMap;
    }

    public Map<ISpiritualRoot, Integer> getSpiritualMap() {
        return spiritualMap;
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public SmeltingStates getSmeltingState() {
        return state;
    }

    public void setState(SmeltingStates state) {
        this.state = state;
    }

    public int getIngredientLimit() {
        return ingredientLimit;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ElixirFurnaceMenu(id, inventory, this.accessData, this.getBlockPos());
    }

    public enum SmeltingStates {

        PREPARE_RECIPE,
        PREPARE_INGREDIENTS,
        SMELTING

    }

}
