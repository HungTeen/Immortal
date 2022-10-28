package hungteen.immortal.common.blockentity;

import hungteen.immortal.api.registry.IElixirType;
import hungteen.immortal.common.ElixirManager;
import hungteen.immortal.common.menu.ElixirFurnaceMenu;
import hungteen.immortal.common.menu.SpiritualStoveMenu;
import hungteen.immortal.common.recipe.ImmortalRecipes;
import hungteen.immortal.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:59
 **/
public class ElixirFurnaceBlockEntity extends ContainerBlockEntity implements MenuProvider {

    private static final int[] SLOTS_FOR_DIRECTIONS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    /* the rest are spiritual stone slots. */
    protected NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    protected final ContainerData accessData = new ContainerData() {
        @Override
        public int get(int id) {
            switch (id) {
                case 0 : return state.ordinal();
                case 1 : return elixirType.map(ElixirManager::getElixirTypeId).orElse(-1);
            }
            Util.error("Unable to find suitable for Id");
            return 0;
        }

        @Override
        public void set(int id, int value) {
            switch (id) {
                case 0 -> state = ElixirStates.values()[value];
                case 1 -> elixirType = Optional.ofNullable(value == -1 ? null : ElixirManager.getElixirTypeId(value));
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    private ElixirStates state = ElixirStates.PREPARE_RECIPE;
    private Optional<IElixirType> elixirType = Optional.empty();

    public ElixirFurnaceBlockEntity(BlockPos blockPos, BlockState state) {
        super(ImmortalBlockEntities.ELIXIR_FURNACE.get(), blockPos, state);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, ElixirFurnaceBlockEntity blockEntity) {
        switch (blockEntity.getState()) {
            case PREPARE_RECIPE -> {
                blockEntity.updateElixirType();
            }
        }
    }

    public void updateElixirType(){
        level.getRecipeManager().getRecipeFor(ImmortalRecipes.ELIXIR_RECIPE_TYPE, this, level);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("ElixirState")){
            this.state = ElixirStates.values()[tag.getInt("ElixirState")];
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ElixirState", this.state.ordinal());
    }

    public Optional<IElixirType> getElixirType() {
        return elixirType;
    }

    public ElixirStates getState() {
        return state;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS_FOR_DIRECTIONS;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ElixirFurnaceMenu(id, inventory, this, this.accessData);
    }

    public enum ElixirStates {

        PREPARE_RECIPE,
        PREPARE_INGREDIENTS,
        SMELTING

    }

}
