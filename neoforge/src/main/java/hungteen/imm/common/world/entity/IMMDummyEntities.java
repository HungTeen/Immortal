package hungteen.imm.common.world.entity;

import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.common.world.entity.HTLibDummyEntities;
import hungteen.imm.common.world.entity.trial.MortalityTrial;
import hungteen.imm.util.Util;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-01 17:10
 **/
public interface IMMDummyEntities {

    DummyEntityType<MortalityTrial> MORTALITY_TRIAL = register("mortality_trial", MortalityTrial::new);

    /**
     * Load the class.
     */
    static void initialize() {
    }

    private static <T extends DummyEntity> DummyEntityType<T> register(String name, DummyEntityType.Factory<T> factory){
        return HTLibDummyEntities.register(new DummyEntityType<>(Util.prefix(name), factory));
    }
}
