package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.util.Constants;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-03 10:09
 **/
public interface RealmTypes {

    HTCustomRegistry<IRealmType> TYPES = HTRegistryManager.custom(Util.prefix("realm"));

    /**
     * 无境界。
     */
    IRealmType NOT_IN_REALM = create("not_in_realm", 0, 0, 0, 0, CultivationTypes.MORTAL);

    /**
     * Default Player Realm.
     */
    IRealmType MORTALITY = create("mortality", 0, 0, 0, 10, CultivationTypes.MORTAL);

    /* 灵修 */

    IRealmType SPIRITUAL_LEVEL_1 = create("spiritual_level_1", 50, 100, 100, 100, CultivationTypes.SPIRITUAL);

    IRealmType SPIRITUAL_LEVEL_2 = create("spiritual_level_2", 100, 200, 125, 120, CultivationTypes.SPIRITUAL);

    IRealmType SPIRITUAL_LEVEL_3 = create("spiritual_level_3", 200, 300, 160, 150, CultivationTypes.SPIRITUAL);

    /* 妖修 */

    IRealmType MONSTER_LEVEL_1 = create("monster_level_1", 50, 120, 60, 75, CultivationTypes.MONSTER);

    IRealmType MONSTER_LEVEL_2 = create("monster_level_2", 120, 225, 80, 100, CultivationTypes.MONSTER);

    IRealmType MONSTER_LEVEL_3 = create("monster_level_3", 240, 325, 100, 125, CultivationTypes.MONSTER);

    /* 亡灵 */

    IRealmType UNDEAD_LEVEL_1 = create("undead_level_1", 80, 160, 50, 140, CultivationTypes.UNDEAD);
    IRealmType UNDEAD_LEVEL_2 = create("undead_level_2", 200, 325, 75, 180, CultivationTypes.UNDEAD);
    IRealmType UNDEAD_LEVEL_3 = create("undead_level_3", 400, 520, 175, 250, CultivationTypes.UNDEAD);

    /* 灵火 */

    IRealmType SPIRITUAL_FLAME_1 = nonLiving("spiritual_flame_1", 90, Constants.MAX_SPIRITUAL_FLAME_AMOUNT, CultivationTypes.SPIRITUAL);

//    IRealmType SPIRITUAL_FLAME_2 = initialize(new RealmType("spiritual_flame_2", 100, 200, 125, 120, CultivationTypes.SPIRITUAL));
//
//    IRealmType SPIRITUAL_FLAME_3 = initialize(new RealmType("spiritual_flame_3", 200, 300, 160, 150, CultivationTypes.SPIRITUAL));

    /* 法器 */

    IRealmType COMMON_ARTIFACT = artifact("common_artifact", 110, StringHelper.style().applyFormat(ChatFormatting.GREEN));
    IRealmType MODERATE_ARTIFACT = artifact("moderate_artifact", 220, StringHelper.style().applyFormat(ChatFormatting.AQUA));
    IRealmType ADVANCED_ARTIFACT = artifact("advanced_artifact", 330, StringHelper.style().applyFormat(ChatFormatting.BLUE));

    static MutableComponent getCategory(){
        return TipUtil.misc("realm");
    }

    static HTCustomRegistry<IRealmType> registry() {
        return TYPES;
    }

    static IRealmType register(IRealmType type) {
        return registry().register(type.getLocation(), type);
    }

    static IRealmType artifact(String name, int realmValue, Style style){
        return create(name, 0, realmValue, 0, 0, CultivationTypes.ARTIFACT, style);
    }

    static IRealmType nonLiving(String name, int realmValue, int spiritualValue, ICultivationType cultivationType){
        return create(name, 0, realmValue, spiritualValue, 0, cultivationType);
    }

    static IRealmType create(String name, int maxCultivation, int realmValue, int spiritValue, int baseConsciousness, ICultivationType type, Style style){
        return register(new RealmType(name, maxCultivation, realmValue, spiritValue, baseConsciousness, type, style));
    }

    static IRealmType create(String name, int maxCultivation, int realmValue, int spiritValue, int baseConsciousness, ICultivationType type){
        return register(new RealmType(name, maxCultivation, realmValue, spiritValue, baseConsciousness, type, Style.EMPTY));
    }

    record RealmType(String name, int maxCultivation, int realmValue, int spiritualValue, int baseConsciousness, ICultivationType cultivationType, Style style) implements IRealmType {

        @Override
        public int getSpiritualValue() {
            return spiritualValue();
        }

        @Override
        public int getBaseConsciousness() {
            return baseConsciousness();
        }

        @Override
        public ICultivationType getCultivationType() {
            return cultivationType();
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.misc("realm." + getName()).withStyle(style());
        }

        @Override
        public int getRealmValue() {
            return realmValue();
        }

    }
}
