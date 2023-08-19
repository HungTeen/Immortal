package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 10:09
 **/
public class RealmTypes {

    private static final HTSimpleRegistry<IRealmType> TYPES = HTRegistryManager.createSimple(Util.prefix("realm"));

    /**
     * 无境界。
     */
    public static final IRealmType NOT_IN_REALM = register(new RealmType("not_in_realm", 0, 0, 0, 0, CultivationTypes.MORTAL));

    /**
     * Default Player Realm.
     */
    public static final IRealmType MORTALITY = register(new RealmType("mortality", 0, 0, 0, 10, CultivationTypes.MORTAL));

    /* 灵修 */

    public static final IRealmType SPIRITUAL_LEVEL_1 = register(new RealmType("spiritual_level_1", 50, 100, 100, 100, CultivationTypes.SPIRITUAL));

    public static final IRealmType SPIRITUAL_LEVEL_2 = register(new RealmType("spiritual_level_2", 100, 200, 125, 120, CultivationTypes.SPIRITUAL));

    public static final IRealmType SPIRITUAL_LEVEL_3 = register(new RealmType("spiritual_level_3", 200, 300, 160, 150, CultivationTypes.SPIRITUAL));

    /* 妖修 */

    public static final IRealmType MONSTER_LEVEL_1 = register(new RealmType("monster_level_1", 50, 100, 60, 75, CultivationTypes.MONSTER));

    public static final IRealmType MONSTER_LEVEL_2 = register(new RealmType("monster_level_2", 120, 210, 80, 100, CultivationTypes.MONSTER));

    public static final IRealmType MONSTER_LEVEL_3 = register(new RealmType("monster_level_3", 240, 320, 100, 125, CultivationTypes.MONSTER));

    /* 亡灵 */
    public static final IRealmType UNDEAD_LEVEL_1 = register(new RealmType("undead_level_1", 80, 160, 50, 140, CultivationTypes.UNDEAD));
    public static final IRealmType UNDEAD_LEVEL_2 = register(new RealmType("undead_level_2", 200, 325, 75, 180, CultivationTypes.UNDEAD));
    public static final IRealmType UNDEAD_LEVEL_3 = register(new RealmType("undead_level_3", 400, 550, 175, 250, CultivationTypes.UNDEAD));

    public static MutableComponent getCategory(){
        return TipUtil.misc("realm");
    }

    public static IHTSimpleRegistry<IRealmType> registry() {
        return TYPES;
    }

    public static IRealmType register(IRealmType type) {
        return registry().register(type);
    }

    public record RealmType(String name, int maxCultivation, int realmValue, int spiritualValue, int baseConsciousness, ICultivationType cultivationType) implements IRealmType {

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
