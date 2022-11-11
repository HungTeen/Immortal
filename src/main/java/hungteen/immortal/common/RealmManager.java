package hungteen.immortal.common;

import hungteen.htlib.util.Pair;
import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.impl.CultivationTypes;
import hungteen.immortal.impl.Realms;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-26 18:36
 **/
public class RealmManager {

    public static final Map<ICultivationType, List<IRealm>> HUMAN_UPGRADE_LIST_MAP = new HashMap<>();
    public static final Map<ICultivationType, List<IRealm>> MONSTER_UPGRADE_LIST_MAP = new HashMap<>();

    /**
     * {@link hungteen.immortal.ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerUpgradeList() {
        registerUpgradeListMap(CultivationTypes.ELIXIR, Arrays.asList(
                Realms.MEDITATION_STAGE1,
                Realms.MEDITATION_STAGE2,
                Realms.MEDITATION_STAGE3,
                Realms.MEDITATION_STAGE4,
                Realms.MEDITATION_STAGE5,
                Realms.MEDITATION_STAGE6,
                Realms.MEDITATION_STAGE7,
                Realms.MEDITATION_STAGE8,
                Realms.MEDITATION_STAGE9,
                Realms.MEDITATION_STAGE10,
                Realms.FOUNDATION_BEGIN,
                Realms.FOUNDATION_MEDIUM,
                Realms.FOUNDATION_LATE,
                Realms.FOUNDATION_FINISH
        ));
        registerUpgradeListMap(CultivationTypes.MONSTER, Arrays.asList(
                Realms.MONSTER_STAGE0,
                Realms.MONSTER_STAGE1,
                Realms.MONSTER_STAGE2,
                Realms.MONSTER_STAGE3,
                Realms.MONSTER_STAGE4
//                Realms.MONSTER_STAGE5,
        ));
    }

    public static Pair<IRealm, Integer> updateRealm(ICultivationType type, IRealm realm, int value) {
        List<IRealm> list = getUpgradeList(type);
        if (value > realm.getCultivation()) {// 修为值大于当前境界的最大值，即可以尝试突破。
            for (int i = 0; i < list.size(); i++) {
                // 有瓶颈，则卡在此境界。
                if(list.get(i).getCultivation() >= realm.getCultivation() && value > list.get(i).getCultivation() && list.get(i).hasThreshold()) {
                    return Pair.of(list.get(i), list.get(i).getCultivation());
                }
                if (value <= list.get(i).getCultivation()) {
                    return Pair.of(list.get(i), value);
                }
            }
        } else{
            for (int i = 0; i < list.size(); i++) {
                if (value <= list.get(i).getCultivation()) {
                    return Pair.of(list.get(i), value);
                }
            }
        }
        return Pair.of(list.get(list.size() - 1), list.get(list.size() - 1).getCultivation());
    }

    public static List<IRealm> getUpgradeList(ICultivationType type) {
        return HUMAN_UPGRADE_LIST_MAP.getOrDefault(type, List.of());
    }

    public static void registerUpgradeListMap(ICultivationType type, List<IRealm> realmList) {
        HUMAN_UPGRADE_LIST_MAP.put(type, realmList);
    }

}
