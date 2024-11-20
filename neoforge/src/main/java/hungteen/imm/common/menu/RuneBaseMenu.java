package hungteen.imm.common.menu;

import hungteen.htlib.common.menu.HTContainerMenu;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.rune.RuneCategories;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-17 22:36
 **/
public abstract class RuneBaseMenu extends HTContainerMenu {

    protected static final int TAB_INDEX_OFFSET = 100;
    protected final ContainerLevelAccess access;
    protected final Level level;
    protected final Player player;

    public RuneBaseMenu(int id, @Nullable MenuType<?> menuType, Inventory inventory, ContainerLevelAccess access) {
        super(id, menuType);
        this.access = access;
        this.level = inventory.player.level();
        this.player = inventory.player;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.isValidTabIndex(id) && getFromIndex(id) != getRuneCategory()) {
            if(player instanceof ServerPlayer p){
                p.openMenu(getMenuProvider(this.getFromIndex(id)));
            }
        }
        return true;
    }


    public MenuProvider getMenuProvider(RuneCategories category) {
        return new SimpleMenuProvider((id, inventory, player) -> {
            return category == RuneCategories.CRAFT ? new RuneCraftingMenu(id, inventory, this.access) :
                    category == RuneCategories.GATE ? new RuneGateMenu(id, inventory, this.access) :
                    new RuneBindMenu(id, inventory, this.access);
        }, Component.empty());
    }

    public abstract RuneCategories getRuneCategory();

    public List<RuneCategories> getRuneCategories() {
        return Arrays.stream(RuneCategories.values()).toList();
    }

    public boolean isValidTabIndex(int pos) {
        return pos >= TAB_INDEX_OFFSET && pos < TAB_INDEX_OFFSET + getRuneCategories().size();
    }

    public int getTabOffset(int pos) {
        return pos + TAB_INDEX_OFFSET;
    }

    public RuneCategories getFromIndex(int pos) {
        return getRuneCategories().get(pos - TAB_INDEX_OFFSET);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, IMMBlocks.RUNE_WORK_BENCH.get());
    }

}
