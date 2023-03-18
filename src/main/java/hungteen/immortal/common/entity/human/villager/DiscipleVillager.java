package hungteen.immortal.common.entity.human.villager;

import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.api.registry.IInventoryLootType;
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
    public IInventoryLootType getInventoryLootType() {
        return null;
    }

    @Override
    public ICultivationType getCultivationType() {
        return null;
    }
}
