package hungteen.imm.common.entity.human.pillager;

import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.human.HumanSettings;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.entity.human.setting.HumanSettingHelper;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/8 16:54
 **/
public class PillagerSettings extends HumanSettingHelper {

    public static void register(BootstrapContext<HumanSetting> context) {
        context.register(HumanSettings.CHILLAGER, new HumanSetting(
                predicate(IMMEntities.CHILLAGER.get(), RealmTypes.QI_REFINING),
                100,
                List.of(
                        single(multi(Items.COOKED_RABBIT, 16, 32)),
                        single(multi(Items.GOLDEN_APPLE, 1, 3)),
                        single(multi(Items.ENDER_PEARL, 2, 4)),
                        single(multi(IMMItems.FALLING_ICE_TALISMAN.get(), 8, 16))
                ),
                Optional.empty(),
                List.of()
        ));
    }
}
