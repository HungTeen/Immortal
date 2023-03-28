package hungteen.imm.common.entity.human.villager;

import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IInventoryLootType;
import hungteen.imm.common.entity.human.HumanEntity;
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
