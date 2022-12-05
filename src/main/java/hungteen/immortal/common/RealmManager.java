package hungteen.immortal.common;

import hungteen.htlib.util.Pair;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.impl.CultivationTypes;
import hungteen.immortal.impl.RealmTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-26 18:36
 **/
public class RealmManager {

    public static final Map<ICultivationType, List<IRealmType>> HUMAN_UPGRADE_LIST_MAP = new HashMap<>();
    public static final Map<ICultivationType, List<IRealmType>> MONSTER_UPGRADE_LIST_MAP = new HashMap<>();

    /**
     * {@link hungteen.immortal.ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void registerUpgradeList() {
        registerUpgradeListMap(CultivationTypes.ELIXIR, Arrays.asList(
                RealmTypes.MEDITATION_STAGE1,
                RealmTypes.MEDITATION_STAGE2,
                RealmTypes.MEDITATION_STAGE3,
                RealmTypes.MEDITATION_STAGE4,
                RealmTypes.MEDITATION_STAGE5,
                RealmTypes.MEDITATION_STAGE6,
                RealmTypes.MEDITATION_STAGE7,
                RealmTypes.MEDITATION_STAGE8,
                RealmTypes.MEDITATION_STAGE9,
                RealmTypes.MEDITATION_STAGE10,
                RealmTypes.FOUNDATION_BEGIN,
                RealmTypes.FOUNDATION_MEDIUM,
                RealmTypes.FOUNDATION_LATE,
                RealmTypes.FOUNDATION_FINISH
        ));
        registerUpgradeListMap(CultivationTypes.MONSTER, Arrays.asList(
                RealmTypes.MONSTER_STAGE0,
                RealmTypes.MONSTER_STAGE1,
                RealmTypes.MONSTER_STAGE2,
                RealmTypes.MONSTER_STAGE3,
                RealmTypes.MONSTER_STAGE4
//                Realms.MONSTER_STAGE5,
        ));
    }

    public static void getRealm(CompoundTag tag, String name, Consumer<IRealmType> consumer){
        if(tag.contains(name)){
            ImmortalAPI.get().realmRegistry().flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, tag.get(name))
                    .result()).ifPresent(consumer);
        }
    }

    public static Pair<IRealmType, Integer> updateRealm(ICultivationType type, IRealmType realm, int value) {
        List<IRealmType> list = getUpgradeList(type);
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

    public static List<IRealmType> getUpgradeList(ICultivationType type) {
        return HUMAN_UPGRADE_LIST_MAP.getOrDefault(type, List.of());
    }

    public static void registerUpgradeListMap(ICultivationType type, List<IRealmType> realmList) {
        HUMAN_UPGRADE_LIST_MAP.put(type, realmList);
    }

}
