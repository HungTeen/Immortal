package hungteen.immortal.common.entity.human.cultivator;

import com.mojang.serialization.Dynamic;
import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.api.registry.IInventoryLootType;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.impl.CultivationTypes;
import hungteen.immortal.common.impl.registry.InventoryLootTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/1 14:58
 */
public class EmptyCultivator extends Cultivator {

    public EmptyCultivator(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected SimpleContainer createInventory() {
        return new SimpleContainer(18);
    }

    @Override
    public void fillInventory() {
        super.fillInventory();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (this.level.isClientSide) {
            return false;
        } else {
            if (flag && source.getEntity() instanceof LivingEntity) {
                EmptyCultivatorAi.wasHurtBy(this, (LivingEntity)source.getEntity());
            }
            return flag;
        }
    }

    @Override
    public void updateBrain(ServerLevel level) {
        this.getBrain().tick(level, this);
        EmptyCultivatorAi.updateActivity(this);
    }

    @Override
    public Brain<EmptyCultivator> getBrain() {
        return (Brain<EmptyCultivator>)super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return EmptyCultivatorAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<EmptyCultivator> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

    @Override
    public ICultivationType getCultivationType() {
        return CultivationTypes.EMPTY;
    }

    @Override
    public IInventoryLootType getInventoryLootType() {
        return InventoryLootTypes.VANILLA;
    }

}
