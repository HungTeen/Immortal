package hungteen.imm.common.world.entity;

import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.common.world.entity.HTDummyEntities;
import hungteen.imm.common.world.entity.trial.BreakThroughTrial;
import hungteen.imm.util.Util;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:10
 **/
public class IMMDummyEntities {

    public static final DummyEntityType<BreakThroughTrial> BREAK_THROUGH_TRIAL = register("break_through_trial", BreakThroughTrial::new);

    public static void init(){}

    private static <T extends DummyEntity> DummyEntityType<T> register(String name, DummyEntityType.Factory<T> factory){
        return HTDummyEntities.register(new DummyEntityType<>(Util.prefix(name), factory));
    }
}
