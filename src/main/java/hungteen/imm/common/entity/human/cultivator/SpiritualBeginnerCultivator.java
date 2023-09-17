package hungteen.imm.common.entity.human.cultivator;

import com.mojang.serialization.Dynamic;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.impl.registry.CultivationTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:16
 **/
public class SpiritualBeginnerCultivator extends Cultivator {

    public SpiritualBeginnerCultivator(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected SimpleContainer createInventory() {
        return new SimpleContainer(12);
    }

    @Override
    public void updateBrain(ServerLevel level) {
        this.getBrain().tick(level, this);
        SpiritualCultivatorAi.updateActivity(this);
    }

    @Override
    public Brain<SpiritualBeginnerCultivator> getBrain() {
        return (Brain<SpiritualBeginnerCultivator>)super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return SpiritualCultivatorAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<SpiritualBeginnerCultivator> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

    @Override
    public ICultivationType getCultivationType() {
        return CultivationTypes.SPIRITUAL;
    }

}
