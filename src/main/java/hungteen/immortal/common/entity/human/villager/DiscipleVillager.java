package hungteen.immortal.common.entity.human.villager;

import hungteen.immortal.common.entity.human.HumanEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 12:03
 **/
public class DiscipleVillager extends SpiritualVillager {

    public DiscipleVillager(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void fillInventory() {
        this.fillInventoryWith(new ItemStack(Items.BREAD), 1F, 3, 5);
        this.fillInventoryWith(new ItemStack(Items.GOLDEN_APPLE), 1F, 1, 2);

        this.fillInventoryWith(new ItemStack(Items.ENDER_PEARL), 0.8F, 1, 3);

    }

}
