package hungteen.imm.common.entity.undead;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.entity.IMMMob;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Collection;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-19 23:11
 **/
public abstract class UndeadEntity extends IMMMob implements Enemy {

    public UndeadEntity(EntityType<? extends UndeadEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void serverFinalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType) {
        super.serverFinalizeSpawn(accessor, difficultyInstance, spawnType);
        this.populateDefaultEquipmentSlots(accessor.getRandom(), difficultyInstance);
    }

    @Override
    protected Collection<QiRootType> getInitialRoots(ServerLevelAccessor accessor) {
        return CultivationManager.getQiRoots(accessor.getRandom());
    }

    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean flag = this.isSunSensitive() && this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        Item item = itemstack.getItem();
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.onEquippedItemBroken(item, EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.igniteForSeconds(8.0F);
                }
            }
        }

        super.aiStep();
    }

    protected boolean isSunSensitive() {
        return ! this.hasElement(Element.FIRE);
    }

}
