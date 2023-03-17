package hungteen.immortal.common.blockentity;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:59
 **/
public class ElixirRoomBlockEntity {
//        extends ItemHandlerBlockEntity implements MenuProvider {
//
//    public static final MutableComponent TITLE = Component.translatable("gui.immortal.elixir_room.title");
//    private static final int[] SLOTS_FOR_DIRECTIONS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
//    /* the rest are spiritual stone slots. */
//    protected final ItemStackHandler itemHandler = new ItemStackHandler(9){
//        @Override
//        public int getSlotLimit(int slot) {
//            return 1;
//        }
//    };
//    private final Map<ISpiritualType, Integer> recipeMap = new HashMap<>();
//    private final Map<ISpiritualType, Integer> spiritualMap = new HashMap<>();
//    protected final ContainerData accessData = new ContainerData() {
//        @Override
//        public int get(int id) {
//            return switch (id) {
//                case 0 -> ElixirRoomBlockEntity.this.smeltingTick;
//                case 1 -> ElixirRoomBlockEntity.this.explodeTick;
//                case 2 -> ElixirRoomBlockEntity.this.score;
//                default -> 0;
//            };
//        }
//
//        @Override
//        public void set(int id, int value) {
//            switch (id) {
//                case 0 -> ElixirRoomBlockEntity.this.smeltingTick = value;
//                case 1 -> ElixirRoomBlockEntity.this.explodeTick = value;
//                case 2 -> ElixirRoomBlockEntity.this.score = value;
//                default -> {
//                    Util.warn("Wrong id {}", id);
//                }
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    };
//    private ItemStack result = ItemStack.EMPTY;
//    private SmeltingStates state = SmeltingStates.PREPARE_RECIPE;
//    private int prepareCD = 0;
//    private int smeltingCD = 0;
//    private int ingredientLimit = 0;
//    private int requireFlameLevel = 0;
//    private int smeltingTick = 0;
//    private int explodeTick = 0;
//    private int score = 0;
//    private boolean explode = false;
//
//    public ElixirRoomBlockEntity(BlockPos blockPos, BlockState state) {
//        super(ImmortalBlockEntities.ELIXIR_ROOM.get(), blockPos, state);
//    }
//
//    public static void serverTick(Level level, BlockPos blockPos, BlockState state, ElixirRoomBlockEntity blockEntity) {
//        blockEntity.explode = false;
//        /* 炸炉 */
//        if(blockEntity.explodeTick >= ImmortalConfigs.getFurnaceExplodeCD()){
//            blockEntity.explode();
//        }
//        /* Prepare Ingredients or Smelting */
//        if(blockEntity.isSmeltingState()){
//            if(! blockEntity.hasRecipe()){
//                Util.error("Why there is no recipe ?");
//                blockEntity.reset();
//            }
//            if(blockEntity.canSmelt()){
////                blockEntity.getBelowBlockEntity().ifPresent(e -> e.smelting(2));
//            } else{ // no flame when smelting.
//                blockEntity.explode = true;
//            }
//            blockEntity.setChanged(); // Save each tick.
//        }
//        switch (blockEntity.getSmeltingState()) {
//            case PREPARE_RECIPE -> {
//                if(blockEntity.canSmelt()){
//                    /* Start Smelting */
//                    if(blockEntity.hasRecipe()){
//                        blockEntity.onSmeltStart();
//                    } else{
//                        blockEntity.explode = true;
//                        blockEntity.setChanged();
//                    }
//                }
//            }
//            case PREPARE_INGREDIENTS -> {
//                ++ blockEntity.smeltingTick;
//                if(blockEntity.ingredientLimit <= 0 || blockEntity.smeltingTick >= blockEntity.prepareCD){
//                    blockEntity.onPrepareFinished();
//                }
//                blockEntity.updateSpirituals();
//            }
//            case SMELTING -> {
//                ++ blockEntity.smeltingTick;
//                if(blockEntity.smeltingTick >= blockEntity.smeltingCD){
//                    blockEntity.onSuccessSmelt();
//                }
//            }
//        }
//        if(blockEntity.explode){
//            ++ blockEntity.explodeTick;
//        } else{
//            -- blockEntity.explodeTick;
//        }
//    }
//
//    public void onSmeltStart() {
//        this.explodeTick = 0;
//        this.smeltingTick = 0;
//        this.spiritualMap.clear();
//        this.recipeMap.clear();
//        this.getRecipeOpt().ifPresent(recipe -> {
//            this.prepareCD = recipe.getPrepareCD();
//            this.smeltingCD = recipe.getSmeltingCD();
//            this.recipeMap.putAll(recipe.getSpiritualMap());
//            this.result = recipe.getResultItem().copy();
//            this.ingredientLimit = recipe.getIngredientLimit();
//            this.requireFlameLevel= recipe.getRequireFlameLevel();
//        });
//        for(int i = 0 ; i < this.itemHandler.getSlots() ; ++ i){
//            this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
//        }
//        this.setState(SmeltingStates.PREPARE_INGREDIENTS);
//        this.update();
//    }
//
//    public void onPrepareFinished(){
//        this.setState(SmeltingStates.SMELTING);
//        this.smeltingTick = 0;
//        this.update();
//    }
//
//    public void updateSpirituals() {
//        boolean flag = false;
//        for (int i = 0; i < itemHandler.getSlots(); ++i) {
//            if (!itemHandler.getStackInSlot(i).isEmpty() && this.ingredientLimit > 0) {
//                ElixirManager.getElixirIngredient(itemHandler.getStackInSlot(i)).forEach(pair -> {
//                    this.spiritualMap.put(pair.getFirst(), this.spiritualMap.getOrDefault(pair.getFirst(), 0) + pair.getSecond());
//                });
//                -- this.ingredientLimit;
//                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
//                flag = true;
//            }
//        }
//        if(flag){
//            this.calculateDifference();
//            this.update();
//        }
//        this.setChanged();
//    }
//
//    /**
//     * 计算准确度，以及判定辅料添加有没有导致炸炉。
//     */
//    public void calculateDifference(){
//        Set<ISpiritualType> roots = new HashSet<>();
//        roots.addAll(this.getRecipeMap().keySet());
//        roots.addAll(this.getSpiritualMap().keySet());
//        List<ISpiritualType> list = roots.stream().sorted(Comparator.comparingInt(ISpiritualType::getSortPriority)).toList();
//        if(list.size() > 0){
//            float sum = 0;
//            for (ISpiritualType root : list) {
//                final int origin = this.getRecipeMap().getOrDefault(root, 0);
//                final int dif = origin - this.getSpiritualMap().getOrDefault(root, 0);
//                if (dif < 0 || origin == 0) {
//                    // 炸炉。
//                    this.explode = true;
//                    this.score = 0;
//                    return;
//                }
//                sum += dif * dif * 1F / origin / origin;
//            }
//            this.score = Math.abs((int)(100F * sum / list.size()) - 100);
//        } else{
//            this.score = 100;
//        }
//        ElixirItem.setAccuracy(this.result, this.score);
//    }
//
//    /**
//     * Can continue smelting.
//     */
//    public boolean hasRecipe(){
//        return getSmeltingState() == SmeltingStates.PREPARE_RECIPE ? getRecipeOpt().isPresent() : ! this.result.isEmpty();
//    }
//
//    private Optional<ElixirRecipe> getRecipeOpt(){
//        SimpleContainer container = new SimpleContainer(9);
//        for(int i = 0; i < this.itemHandler.getSlots(); ++ i){
//            container.setItem(i, this.itemHandler.getStackInSlot(i));
//        }
//        return level.getRecipeManager().getRecipeFor(ImmortalRecipes.ELIXIR_RECIPE_TYPE.get(), container, level);
//    }
//
//    public boolean isSmeltingState() {
//        return this.getSmeltingState() == SmeltingStates.PREPARE_INGREDIENTS || this.getSmeltingState() == SmeltingStates.SMELTING;
//    }
//
//    public void explode(){
//        this.level.explode(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 10, true, Explosion.BlockInteraction.DESTROY);
//        getBelowBlockEntity().ifPresent(SpiritualFurnaceBlockEntity::stop);
//        this.reset();
//    }
//
//    public void onSuccessSmelt(){
//        ItemStack stack = this.result.copy();
//        ElixirItem.Accuracies accuracy = ElixirItem.getAccuracy(this.score);
//        ElixirItem.setAccuracy(stack, this.score);
//        itemHandler.setStackInSlot(4, stack);
//        getBelowBlockEntity().ifPresent(SpiritualFurnaceBlockEntity::stop);
//        this.reset();
//    }
//
//    public void reset(){
//        this.smeltingTick = 0;
//        this.prepareCD = 0;
//        this.smeltingCD = 0;
//        this.explodeTick = 0;
//        this.setState(SmeltingStates.PREPARE_RECIPE);
//        this.recipeMap.clear();
//        this.spiritualMap.clear();
//        this.update();
//    }
//
//    public void update(){
//        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
//        this.setChanged();
//    }
//
//    public boolean canSmelt(){
//        return getBelowBlockEntity().isPresent() && getBelowBlockEntity().get().canSmelt();
//    }
//
//    private Optional<SpiritualFurnaceBlockEntity> getBelowBlockEntity(){
//        BlockEntity blockEntity = level.getBlockEntity(getBlockPos().below());
//        if(blockEntity instanceof SpiritualFurnaceBlockEntity){
//            return Optional.of(((SpiritualFurnaceBlockEntity) blockEntity));
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public CompoundTag getUpdateTag() {
//        return saveWithoutMetadata();
//    }
//
//    @Override
//    public void load(CompoundTag tag) {
//        super.load(tag);
//        if(tag.contains("SmeltingState")){
//            this.state = SmeltingStates.values()[tag.getInt("SmeltingState")];
//        }
//        if(tag.contains("ElixirResult")){
//            this.result = ItemStack.of(tag.getCompound("ElixirResult"));
//        }
//        if(tag.contains("PrepareCD")){
//            this.prepareCD = tag.getInt("PrepareCD");
//        }
//        if(tag.contains("SmeltingCD")){
//            this.smeltingCD = tag.getInt("SmeltingCD");
//        }
//        if(tag.contains("SmeltingTick")){
//            this.smeltingTick = tag.getInt("SmeltingTick");
//        }
//        if(tag.contains("ExplodeTick")){
//            this.explodeTick = tag.getInt("ExplodeTick");
//        }
//        if(tag.contains("ElixirScore")){
//            this.score = tag.getInt("ElixirScore");
//        }
//        if(tag.contains("IngredientLimit")){
//            this.ingredientLimit = tag.getInt("IngredientLimit");
//        }
//        if(tag.contains("RequireFlameLevel")){
//            this.requireFlameLevel = tag.getInt("RequireFlameLevel");
//        }
//        if(tag.contains("RecipeMap")){
//            CompoundTag nbt = tag.getCompound("RecipeMap");
//            int len = nbt.getInt("Len");
//            this.recipeMap.clear();
//            for(int i = 0;i < len; ++ i){
//                final int value = nbt.getInt("Value" + i);
//                final int id = i;
//                ImmortalAPI.get().spiritualRegistry()
//                        .flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, nbt.get("Root" + id)).result())
//                        .ifPresent(type -> recipeMap.put(type, value));
//            }
//        }
//        if(tag.contains("SpiritualMap")){
//            CompoundTag nbt = tag.getCompound("SpiritualMap");
//            int len = nbt.getInt("Len");
//            this.spiritualMap.clear();
//            for(int i = 0; i < len; ++ i){
//                final int value = nbt.getInt("Value" + i);
//                final int id = i;
//                ImmortalAPI.get().spiritualRegistry()
//                        .flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, nbt.get("Root" + id)).result())
//                        .ifPresent(type -> spiritualMap.put(type, value));
//            }
//        }
//    }
//
//    @Override
//    protected void saveAdditional(CompoundTag tag) {
//        super.saveAdditional(tag);
//        tag.putInt("SmeltingState", this.state.ordinal());
//        tag.put("ElixirResult", this.result.serializeNBT());
//        tag.putInt("PrepareCD", this.prepareCD);
//        tag.putInt("SmeltingCD", this.smeltingCD);
//        tag.putInt("SmeltingTick", this.smeltingTick);
//        tag.putInt("ExplodeTick", this.explodeTick);
//        tag.putInt("ElixirScore", this.score);
//        tag.putInt("IngredientLimit", this.ingredientLimit);
//        tag.putInt("RequireFlameLevel", this.requireFlameLevel);
//        {
//            CompoundTag nbt = new CompoundTag();
//            nbt.putInt("Len", this.recipeMap.size());
//            List<ISpiritualType> list = this.recipeMap.keySet().stream().toList();
//            for(int i = 0; i < list.size(); ++ i){
//                nbt.putString("Root" + i, list.get(i).getRegistryName());
//                nbt.putInt("Value" + i, this.recipeMap.get(list.get(i)));
//            }
//            tag.put("RecipeMap", nbt);
//        }
//        {
//            CompoundTag nbt = new CompoundTag();
//            nbt.putInt("Len", this.spiritualMap.size());
//            List<ISpiritualType> list = this.spiritualMap.keySet().stream().toList();
//            for(int i = 0; i < list.size(); ++ i){
//                nbt.putString("Root" + i, list.get(i).getRegistryName());
//                nbt.putInt("Value" + i, this.spiritualMap.get(list.get(i)));
//            }
//            tag.put("SpiritualMap", nbt);
//        }
//    }
//
//    public ItemStack getResultItem(){
//        return this.result;
//    }
//
//    public Map<ISpiritualType, Integer> getRecipeMap() {
//        return recipeMap;
//    }
//
//    public Map<ISpiritualType, Integer> getSpiritualMap() {
//        return spiritualMap;
//    }
//
//    @Override
//    public ItemStackHandler getItemHandler() {
//        return this.itemHandler;
//    }
//
//    public SmeltingStates getSmeltingState() {
//        return state;
//    }
//
//    public void setState(SmeltingStates state) {
//        this.state = state;
//    }
//
//    public int getIngredientLimit() {
//        return ingredientLimit;
//    }
//
//    @Override
//    protected Component getDefaultName() {
//        return TITLE;
//    }
//
//    @Nullable
//    @Override
//    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
//        return new ElixirRoomMenu(id, inventory, this.accessData, this.getBlockPos());
//    }
//
//    public IArtifactType getArtifactType() {
//        return this.getBlockState().getBlock() instanceof IArtifactBlock ? ((IArtifactBlock) this.getBlockState().getBlock()).getArtifactType(this.getBlockState()) : ArtifactTypes.EMPTY;
//    }
//
//    public enum SmeltingStates {
//
//        PREPARE_RECIPE,
//        PREPARE_INGREDIENTS,
//        SMELTING
//
//    }

}
