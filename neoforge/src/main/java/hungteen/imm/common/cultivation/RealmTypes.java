package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.realm.DummyPeriod;
import hungteen.imm.common.cultivation.realm.MultiPeriodRealm;
import hungteen.imm.common.cultivation.realm.RealmPeriod;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.stream.Stream;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-03 10:09
 **/
public interface RealmTypes {

    HTCustomRegistry<RealmType> TYPES = HTRegistryManager.custom(Util.prefix("realm"));

    /**
     * 无境界。
     */
    RealmType NOT_IN_REALM = create("not_in_realm", 0, 0, 0, DummyPeriod.DUMMY, CultivationTypes.NONE, Style.EMPTY);

    /**
     * Default Player Realm.
     */
    RealmType MORTALITY = create("mortality", 0, 0, 1, DummyPeriod.DUMMY, CultivationTypes.NONE, Style.EMPTY);

    /* 灵修 */

    MultiPeriodRealm QI_REFINING = multiPeriod("qi_refining", 50, 100, 2, RealmPeriod.values(), CultivationTypes.SPIRITUAL, Style.EMPTY);

    MultiPeriodRealm FOUNDATION = multiPeriod("foundation", 50, 100, 3, RealmPeriod.values(), CultivationTypes.SPIRITUAL, Style.EMPTY);

    MultiPeriodRealm CORE_SHAPING = multiPeriod("core_shaping", 50, 100, 4, RealmPeriod.values(), CultivationTypes.SPIRITUAL, Style.EMPTY);

    /* 妖修 */

    RealmType YAOGUAI_LEVEL_1 = create("yaoguai_level_1", 50, 120, 2, DummyPeriod.DUMMY, CultivationTypes.YAOGUAI, Style.EMPTY);

    RealmType YAOGUAI_LEVEL_2 = create("yaoguai_level_2", 120, 225, 3, DummyPeriod.DUMMY, CultivationTypes.YAOGUAI, Style.EMPTY);

    RealmType YAOGUAI_LEVEL_3 = create("yaoguai_level_3", 240, 325, 3, DummyPeriod.DUMMY, CultivationTypes.YAOGUAI, Style.EMPTY);

    /* 亡灵 */

//    RealmType UNDEAD_LEVEL_1 = create("undead_level_1", 80, 160, 50, 140, CultivationTypes.UNDEAD);
//    RealmType UNDEAD_LEVEL_2 = create("undead_level_2", 200, 325, 75, 180, CultivationTypes.UNDEAD);
//    RealmType UNDEAD_LEVEL_3 = create("undead_level_3", 400, 520, 175, 250, CultivationTypes.UNDEAD);

    /* 灵火 */

    RealmType SPIRITUAL_FLAME_1 = nonLiving("spiritual_flame_1", 90, CultivationTypes.SPIRITUAL);

//    RealmType SPIRITUAL_FLAME_2 = initialize(new RealmType("spiritual_flame_2", 100, 200, 125, 120, CultivationTypes.SPIRITUAL));
//
//    RealmType SPIRITUAL_FLAME_3 = initialize(new RealmType("spiritual_flame_3", 200, 300, 160, 150, CultivationTypes.SPIRITUAL));

    /* 法器 */

    RealmType COMMON_ARTIFACT = artifact("common_artifact", 110, StringHelper.style().applyFormat(ChatFormatting.GREEN));
    RealmType MODERATE_ARTIFACT = artifact("moderate_artifact", 220, StringHelper.style().applyFormat(ChatFormatting.AQUA));
    RealmType ADVANCED_ARTIFACT = artifact("advanced_artifact", 330, StringHelper.style().applyFormat(ChatFormatting.BLUE));

    static MutableComponent getCategory(){
        return TipUtil.misc("realm");
    }

    static MutableComponent getStageComponent(){
        return TipUtil.misc("realm_stage");
    }

    static HTCustomRegistry<RealmType> registry() {
        return TYPES;
    }

    static RealmType register(RealmType type) {
        return registry().register(type.getLocation(), type);
    }

    static RealmType artifact(String name, int realmValue, Style style){
        return create(name, 0, realmValue, 0, DummyPeriod.DUMMY, CultivationTypes.ARTIFACT, style);
    }

    static RealmType nonLiving(String name, int realmValue, CultivationType cultivationType){
        return create(name, 0, realmValue, 0, DummyPeriod.DUMMY, cultivationType, Style.EMPTY);
    }

    static MultiPeriodRealm multiPeriod(String name, int maxCultivation, int realmValue, int realmRegionLevel, RealmStage[] stages, CultivationType type, Style style){
        return new MultiPeriodRealm(Stream.of(stages).map(stage -> {
            return create(name + "_" + stage.name().toLowerCase(), maxCultivation, realmValue, realmRegionLevel, stage, type, style);
        }).toArray(RealmType[]::new));
    }

    static RealmType create(String name, int maxCultivation, int realmValue, int realmRegionLevel, RealmStage stage, CultivationType type, Style style){
        return register(new RealmTypeImpl(name, maxCultivation, realmValue, realmRegionLevel, stage, type, style));
    }

    record RealmTypeImpl(String name, int maxCultivation, int realmValue, int realmRegionLevel, RealmStage stage, CultivationType cultivationType, Style style) implements RealmType {

        @Override
        public RealmStage getStage() {
            return stage();
        }

        @Override
        public CultivationType getCultivationType() {
            return cultivationType();
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            // 从注册名中截取出阶段名。
            String replace = name().replace(getStage().getName(), "");
            String substring = replace.substring(0, replace.length() - 1);
            return TipUtil.misc("realm." + substring, stage().getComponent()).withStyle(style());
        }

        @Override
        public int getRealmValue() {
            return realmValue();
        }

        @Override
        public int getRealmRegionLevel() {
            return realmRegionLevel();
        }

    }

}
