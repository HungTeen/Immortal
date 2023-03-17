package hungteen.immortal.common.entity.human.cultivator;

import com.mojang.serialization.Dynamic;
import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.api.registry.IInventoryLootType;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.impl.CultivationTypes;
import hungteen.immortal.common.impl.registry.RealmTypes;
import hungteen.immortal.common.impl.registry.InventoryLootTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:16
 **/
public class SpiritualCultivator extends Cultivator {

    public SpiritualCultivator(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected SimpleContainer createInventory() {
        return new SimpleContainer(12);
    }

    @Override
    public void fillInventory() {
        if(this.getRealm() == RealmTypes.MORTALITY){
            this.fillInventoryWith(new ItemStack(Items.GOLDEN_APPLE), 1, 2);

            this.fillInventoryWith(new ItemStack(Items.ENDER_PEARL), 1, 3);

            this.fillInventoryWith(new ItemStack(Items.DIAMOND_SWORD), 1, 1);
            this.fillInventoryWith(new ItemStack(Items.BOW), 1, 2);

            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        }
    }

    @Override
    public void updateBrain(ServerLevel level) {
        this.getBrain().tick(level, this);
        SpiritualCultivatorAi.updateActivity(this);
    }

    @Override
    public Brain<SpiritualCultivator> getBrain() {
        return (Brain<SpiritualCultivator>)super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return SpiritualCultivatorAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<SpiritualCultivator> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

    @Override
    public ICultivationType getCultivationType() {
        return CultivationTypes.SPIRITUAL;
    }

    @Override
    public IInventoryLootType getInventoryLootType() {
        return InventoryLootTypes.SPIRITUAL;
    }
}
