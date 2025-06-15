package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.realm.*;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-03 10:09
 **/
public interface RealmTypes {

    HTCustomRegistry<RealmType> TYPES = HTRegistryManager.custom(Util.prefix("realm"));

    /**
     * 无境界。
     */
    RealmType NOT_IN_REALM = create("not_in_realm", 0, 0, DummyPeriod.DUMMY, CultivationTypes.NONE, Style.EMPTY);

    /**
     * Default Player Realm.
     */
    RealmType MORTALITY = create("mortality", 0, 0, DummyPeriod.DUMMY, CultivationTypes.NONE, Style.EMPTY);

    /* 灵修 */

    MultiLevelRealm QI_REFINING = builder("qi_refining", RealmStep.values(13))
            .cultivation(5, 10, 20, 30, 45, 60, 80, 100, 125, 150, 180, 210, 250)
            .realmValue(8, 80)
            .health(MathUtil.sameArray(13, 2))
            .qi(MathUtil.uniformArray(13, 15, 75, true))
            .type(CultivationTypes.QI)
            .multiLevel();

    MultiPeriodRealm FOUNDATION = builder("foundation", RealmPeriod.values())
            .cultivation(300, 400, 500, 550)
            .health(MathUtil.sameArray(5, 5))
            .qi(100, 110, 120, 125)
            .realmValue(115, 175)
            .type(CultivationTypes.QI)
            .multiPeriod();

    MultiPeriodRealm CORE_SHAPING = builder("core_shaping", RealmPeriod.values())
            .cultivation(750, 900, 1050, 1200)
            .health(MathUtil.sameArray(5, 8))
            .qi(150, 160, 170, 175)
            .realmValue(215, 275)
            .type(CultivationTypes.QI)
            .multiPeriod();

    /* 妖修 */

    MultiLevelRealm YAOGUAI_LEVEL_1 = builder("yaoguai_level_1", RealmStep.values(10))
            .cultivation(10, 20, 40, 60, 80, 105, 135, 175, 205, 275)
            .realmValue(9, 90)
            .qi(MathUtil.uniformArray(10, 15, 60, true))
            .armor(MathUtil.sameArray(10, 2))
            .type(CultivationTypes.YAOGUAI)
            .multiLevel();

    MultiPeriodRealm YAOGUAI_LEVEL_2 = builder("yaoguai_level_2", RealmRank.values())
            .emptyCultivation()
            .realmValue(120, 150)
            .qi(70, 75, 80, 80)
            .armor(MathUtil.sameArray(10, 4))
            .type(CultivationTypes.YAOGUAI)
            .multiPeriod();

    MultiPeriodRealm YAOGUAI_LEVEL_3 = builder("yaoguai_level_3", RealmRank.values())
            .emptyCultivation()
            .realmValue(160, 190)
            .qi(95, 100, 110, 110)
            .armor(MathUtil.sameArray(10, 6))
            .type(CultivationTypes.YAOGUAI)
            .multiPeriod();

    MultiPeriodRealm YAOGUAI_LEVEL_4 = builder("yaoguai_level_4", RealmRank.values())
            .emptyCultivation()
            .realmValue(220, 250)
            .qi(130, 140, 150, 150)
            .armor(MathUtil.sameArray(10, 6))
            .type(CultivationTypes.YAOGUAI)
            .multiPeriod();

    /* 亡灵 */

    MultiPeriodRealm LOW_RANK_UNDEAD = builder("low_rank_undead", RealmPeriod.values())
            .emptyCultivation()
            .realmValue(25, 65)
            .qi(25, 35, 45, 50, 50)
            .type(CultivationTypes.UNDEAD)
            .multiPeriod();

    MultiPeriodRealm MID_RANK_UNDEAD = builder("mid_rank_undead", RealmPeriod.values())
            .emptyCultivation()
            .realmValue(105, 145)
            .qi(65, 75, 85, 90, 90)
            .type(CultivationTypes.UNDEAD)
            .multiPeriod();

    MultiPeriodRealm HIGH_RANK_UNDEAD = builder("high_rank_undead", RealmPeriod.values())
            .emptyCultivation()
            .realmValue(160, 188)
            .qi(105, 115, 125, 130, 130)
            .type(CultivationTypes.UNDEAD)
            .multiPeriod();

    /* 灵火 */

    RealmType SPIRITUAL_FLAME_1 = nonLiving("spiritual_flame_1", 90, CultivationTypes.QI);

//    RealmType SPIRITUAL_FLAME_2 = initialize(new RealmType("spiritual_flame_2", 100, 200, 125, 120, CultivationTypes.SPIRITUAL));
//
//    RealmType SPIRITUAL_FLAME_3 = initialize(new RealmType("spiritual_flame_3", 200, 300, 160, 150, CultivationTypes.SPIRITUAL));

    static MutableComponent getCategory() {
        return TipUtil.misc("realm");
    }

    static MutableComponent getStageComponent() {
        return TipUtil.misc("realm_stage");
    }

    static HTCustomRegistry<RealmType> registry() {
        return TYPES;
    }

    static RealmType register(RealmType type) {
        return registry().register(type.getLocation(), type);
    }

    static RealmType artifact(String name, int realmValue, Style style) {
        return create(name, 0, realmValue, DummyPeriod.DUMMY, CultivationTypes.ARTIFACT, style);
    }

    static RealmType nonLiving(String name, int realmValue, CultivationType cultivationType) {
        return create(name, 0, realmValue, DummyPeriod.DUMMY, cultivationType, Style.EMPTY);
    }

    static RealmBuilder builder(String name, RealmStage[] stages) {
        return new RealmBuilder(name, stages);
    }

    static RealmType create(String name, int maxCultivation, int realmValue, RealmStage stage, CultivationType type, Style style) {
        return register(new RealmTypeImpl(name, maxCultivation, realmValue, Map.of(), stage, type, style));
    }

    static RealmType create(String name, int maxCultivation, int realmValue, Map<Holder<Attribute>, AttributeModifier> modifierMap, RealmStage stage, CultivationType type, Style style) {
        return register(new RealmTypeImpl(name, maxCultivation, realmValue, modifierMap, stage, type, style));
    }

    record RealmTypeImpl(String name, int maxCultivation, int realmValue,
                         Map<Holder<Attribute>, AttributeModifier> modifierMap, RealmStage stage,
                         CultivationType cultivationType, Style style) implements RealmType {

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
            String stageName = getStage().getName();
            String fullName = name();
            String realmName = fullName;
            if(fullName.contains(stageName)){
                String replace = name().replace(getStage().getName(), "");
                realmName = replace.substring(0, replace.length() - 1);
            }
            return TipUtil.misc("realm." + realmName, stage().getComponent()).withStyle(style());
        }

        @Override
        public int getRealmValue() {
            return realmValue();
        }

        @Override
        public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
            return modifierMap();
        }

    }

    class RealmBuilder {

        public final String name;
        private final RealmStage[] stages;
        private int[] cultivations = new int[]{0};
        private int[] qis;
        private int[] healths;
        private int[] armors;
        private int minRealm = 0;
        private int maxRealm = 0;

        private CultivationType type = CultivationTypes.UNKNOWN;
        private Style style = Style.EMPTY;

        public RealmBuilder(String name, RealmStage[] stages) {
            this.name = name;
            this.stages = stages;
        }

        public RealmBuilder cultivation(int... cultivations) {
            this.cultivations = cultivations;
            return this;
        }

        public RealmBuilder health(int... healths) {
            this.healths = healths;
            return this;
        }

        public RealmBuilder qi(int... qis) {
            this.qis = qis;
            return this;
        }

        public RealmBuilder armor(int... armors) {
            this.armors = armors;
            return this;
        }

        public RealmBuilder realmValue(int minRealm, int maxRealm) {
            this.minRealm = minRealm;
            this.maxRealm = maxRealm;
            return this;
        }

        public RealmBuilder type(CultivationType type) {
            this.type = type;
            return this;
        }

        public RealmBuilder style(Style style) {
            this.style = style;
            return this;
        }

        public RealmBuilder emptyCultivation() {
            this.cultivations = new int[stages.length];
            Arrays.fill(cultivations, 0);
            return this;
        }

        public MultiLevelRealm multiLevel() {
            return createMultiRealm(MultiLevelRealm::new);
        }

        public MultiPeriodRealm multiPeriod() {
            return createMultiRealm(MultiPeriodRealm::new);
        }

        <T> T createMultiRealm(Function<RealmType[], T> constructor) {
            assert cultivations.length == stages.length;
            assert qis == null || qis.length == stages.length;
            assert healths == null || healths.length == stages.length;
            assert armors == null || armors.length == stages.length;
            RealmType[] types = new RealmType[cultivations.length];
            int[] realmValues = MathUtil.uniformArray(cultivations.length, minRealm, maxRealm, true);
            Map<Holder<Attribute>, AttributeModifier> modifierMap = new HashMap<>();
            for (int i = 0; i < cultivations.length; i++) {
                if(qis != null){
                    modifierMap.put(IMMAttributes.MAX_QI_AMOUNT.holder(), new AttributeModifier(RealmType.QI_MODIFIER, qis[i], AttributeModifier.Operation.ADD_VALUE));
                }
                if(healths != null) {
                    modifierMap.put(Attributes.MAX_HEALTH, new AttributeModifier(RealmType.HEALTH_MODIFIER, healths[i], AttributeModifier.Operation.ADD_VALUE));
                }
                if(armors != null) {
                    modifierMap.put(Attributes.ARMOR, new AttributeModifier(RealmType.ARMOR_MODIFIER, armors[i], AttributeModifier.Operation.ADD_VALUE));
                }
                types[i] = create(name + "_" + stages[i].name().toLowerCase(), cultivations[i], realmValues[i], modifierMap, stages[i], type, style);
            }
            return constructor.apply(types);
        }

    }

}
