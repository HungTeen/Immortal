package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.entity.misc.SpiritualFlame;
import hungteen.imm.util.Constants;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 10:09
 **/
public interface RealmTypes {

    HTSimpleRegistry<IRealmType> TYPES = HTRegistryManager.createSimple(Util.prefix("realm"));

    /**
     * 无境界。
     */
    IRealmType NOT_IN_REALM = register(new RealmType("not_in_realm", 0, 0, 0, 0, CultivationTypes.MORTAL));

    /**
     * Default Player Realm.
     */
    IRealmType MORTALITY = register(new RealmType("mortality", 0, 0, 0, 10, CultivationTypes.MORTAL));

    /* 灵修 */

    IRealmType SPIRITUAL_LEVEL_1 = register(new RealmType("spiritual_level_1", 50, 100, 100, 100, CultivationTypes.SPIRITUAL));

    IRealmType SPIRITUAL_LEVEL_2 = register(new RealmType("spiritual_level_2", 100, 200, 125, 120, CultivationTypes.SPIRITUAL));

    IRealmType SPIRITUAL_LEVEL_3 = register(new RealmType("spiritual_level_3", 200, 300, 160, 150, CultivationTypes.SPIRITUAL));

    /* 妖修 */

    IRealmType MONSTER_LEVEL_1 = register(new RealmType("monster_level_1", 50, 120, 60, 75, CultivationTypes.MONSTER));

    IRealmType MONSTER_LEVEL_2 = register(new RealmType("monster_level_2", 120, 225, 80, 100, CultivationTypes.MONSTER));

    IRealmType MONSTER_LEVEL_3 = register(new RealmType("monster_level_3", 240, 325, 100, 125, CultivationTypes.MONSTER));

    /* 亡灵 */

    IRealmType UNDEAD_LEVEL_1 = register(new RealmType("undead_level_1", 80, 160, 50, 140, CultivationTypes.UNDEAD));
    IRealmType UNDEAD_LEVEL_2 = register(new RealmType("undead_level_2", 200, 325, 75, 180, CultivationTypes.UNDEAD));
    IRealmType UNDEAD_LEVEL_3 = register(new RealmType("undead_level_3", 400, 520, 175, 250, CultivationTypes.UNDEAD));

    /* 灵火 */

    IRealmType SPIRITUAL_FLAME_1 = create("spiritual_flame_1", 90, Constants.MAX_SPIRITUAL_FLAME_AMOUNT, CultivationTypes.SPIRITUAL);

//    IRealmType SPIRITUAL_FLAME_2 = register(new RealmType("spiritual_flame_2", 100, 200, 125, 120, CultivationTypes.SPIRITUAL));
//
//    IRealmType SPIRITUAL_FLAME_3 = register(new RealmType("spiritual_flame_3", 200, 300, 160, 150, CultivationTypes.SPIRITUAL));

    static MutableComponent getCategory(){
        return TipUtil.misc("realm");
    }

    static IHTSimpleRegistry<IRealmType> registry() {
        return TYPES;
    }

    static IRealmType register(IRealmType type) {
        return registry().register(type);
    }

    static IRealmType create(String name, int realmValue, int spiritualValue, ICultivationType cultivationType){
        return register(new RealmType(name, 0, realmValue, spiritualValue, 0, cultivationType));
    }

    record RealmType(String name, int maxCultivation, int realmValue, int spiritualValue, int baseConsciousness, ICultivationType cultivationType) implements IRealmType {

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
            return TipUtil.misc("realm." + getName());
        }

        @Override
        public int getRealmValue() {
            return realmValue();
        }

    }
}
