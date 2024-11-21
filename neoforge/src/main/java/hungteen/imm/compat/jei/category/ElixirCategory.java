package hungteen.imm.compat.jei.category;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-04 12:56
 **/
public class ElixirCategory {
//    implements IRecipeCategory<ElixirRecipe>
// {
//
//    public static final RecipeType<ElixirRecipe> ELIXIR_RECIPE_TYPE = RecipeType.create(Util.id(), "elixir", ElixirRecipe.class);
//
//    private final Component title;
//    private final ICraftingGridHelper craftingGridHelper;
//    private final IDrawable background;
//    private final IDrawable icon;
//
//    public ElixirCategory(IGuiHelper helper) {
//        this.title = ElixirRoomBlockEntity.TITLE;
//        this.craftingGridHelper = helper.createCraftingGridHelper();
//        this.background = helper.createDrawable(IMMJEIUtil.CRAFTING_TABLE, 29, 16, 116, 54);
//        this.icon = helper.createDrawableItemStack(new ItemStack(IMMBlocks.COPPER_ELIXIR_ROOM.get()));
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayoutBuilder builder, ElixirRecipe recipe, IFocusGroup focuses) {
//        this.craftingGridHelper.createAndSetInputs(builder,  recipe.getIngredients().stream().map(ingredient -> Arrays.stream(ingredient.getItems()).toList()).toList(), 3, 3);
//        Util.getProxy().registryAccess().ifPresent(access -> {
//            this.craftingGridHelper.createAndSetOutputs(builder, List.of(recipe.getResultItem(access)));
//        });
//    }
//
//    @Override
//    public Component getTitle() {
//        return title;
//    }
//
////    @Override
////    public IDrawable getBackground() {
////        return background;
////    }
//
//    @Override
//    public IDrawable getIcon() {
//        return icon;
//    }
//
//    @Override
//    public RecipeType<ElixirRecipe> getRecipeType() {
//        return ELIXIR_RECIPE_TYPE;
//    }
}
