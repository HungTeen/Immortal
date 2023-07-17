package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 10:09
 **/
public class RealmTypes {

    private static final HTSimpleRegistry<IRealmType> REALM_TYPES = HTRegistryManager.createSimple(Util.prefix("realm"));
    private static final List<IRealmType> TYPES = new ArrayList<>();

    public static IHTSimpleRegistry<IRealmType> registry() {
        return REALM_TYPES;
    }

    /**
     * 无境界。
     */
    public static final IRealmType NOT_IN_REALM = new RealmType("not_in_realm", 0, 0, 0, 0, true, CultivationTypes.MORTAL);
    public static final IRealmType MORTALITY = new RealmType("mortality", 0, 0, 0, 0, true, CultivationTypes.MORTAL);

    public static final IRealmType SPIRITUAL_LEVEL_1 = new RealmType("spiritual_level_1", 50, 15, 10, 20, false, CultivationTypes.SPIRITUAL);

    public static final IRealmType SPIRITUAL_LEVEL_2 = new RealmType("spiritual_level_2", 100, 40, 30, 50, false, CultivationTypes.SPIRITUAL);

    public static final IRealmType SPIRITUAL_LEVEL_3 = new RealmType("spiritual_level_3", 200, 75, 50, 100, true, CultivationTypes.SPIRITUAL);

//    public static final IRealmType SPIRITUAL_LEVEL_4 = new RealmType("spiritual_level_4", 10, 100, 10);
//
//    public static final IRealmType SPIRITUAL_LEVEL_5 = new RealmType("spiritual_level_5", 10, 160, 10);
//
//    public static final IRealmType SPIRITUAL_LEVEL_6 = new RealmType("spiritual_level_6", 10, 200, 10);
//
//    public static final IRealmType SPIRITUAL_LEVEL_7 = new RealmType("spiritual_level_7", 10, 250, 10);
//
//    public static final IRealmType SPIRITUAL_LEVEL_8 = new RealmType("spiritual_level_8", 10, 300, 10);
//
//    public static final IRealmType SPIRITUAL_LEVEL_9 = new RealmType("spiritual_level_9", 10, 350, 10);
//
//    public static final IRealmType SPIRITUAL_LEVEL_10 = new RealmType("spiritual_level_10", 10, 400, 10);

    /* 妖兽 */
    public static final IRealmType MONSTER_LEVEL_1 = new RealmType("monster_level_1", 100, 25, 25, 60, false, CultivationTypes.MONSTER);

    public static final IRealmType MONSTER_LEVEL_2 = new RealmType("monster_level_2", 300, 80, 60, 120, false, CultivationTypes.MONSTER);

//    public static final IRealmType MONSTER_LEVEL_3 = new RealmType("monster_level_3", 300, 80, 50, 110, false, CultivationTypes.MONSTER);
//
//    public static final IRealmType MONSTER_STAGE3 = new RealmType("monster_stage3", 450, 15, 300);
//
//    public static final IRealmType MONSTER_STAGE4 = new RealmType("monster_stage4", 600, 20, 400);
//
//    public static final IRealm MONSTER_STAGE5 = new Realm("monster_stage5", 150, 21);
//
//    public static final IRealm MONSTER_STAGE6 = new Realm("monster_stage6", 210, 25);
//
//    public static final IRealm MONSTER_STAGE7 = new Realm("monster_stage7", 300, 28);
//
//    public static final IRealm MONSTER_STAGE8 = new Realm("monster_stage8", 400, 31);
//
//    public static final IRealm MONSTER_STAGE9 = new Realm("monster_stage9", 500, 35);
//
//    public static final IRealm MONSTER_STAGE10 = new Realm("monster_stage10", 600, 38);
//
//    /* 亡灵 */
//    public static final IRealmType UNDEAD_LEVEL1 = new RealmType("undead_level1", 80, 4, 25, false);
//    public static final IRealmType UNDEAD_LEVEL2 = new RealmType("undead_level2", 200, 8, 50, false);

    public record RealmType(String name, int requireCultivation, int realmValue, int spiritualValue, int spiritualLimit, boolean hasThreshold, ICultivationType cultivationType) implements IRealmType {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            IMMAPI.get().realmRegistry().ifPresent(l -> l.register(TYPES));
        }

        public RealmType(String name, int requireCultivation, int realmValue, int spiritualValue, int spiritualLimit, boolean hasThreshold, ICultivationType cultivationType) {
            this.name = name;
            this.requireCultivation = requireCultivation;
            this.realmValue = realmValue;
            this.spiritualValue = spiritualValue;
            this.spiritualLimit = spiritualLimit;
            this.hasThreshold = hasThreshold;
            this.cultivationType = cultivationType;
            TYPES.add(this);
        }

        @Override
        public int getBaseSpiritualValue() {
            return spiritualValue();
        }

        @Override
        public int getSpiritualValueLimit() {
            return spiritualLimit();
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
            return Component.translatable("misc." + getModID() +".realm." + getName());
        }

        @Override
        public int getRealmValue() {
            return realmValue();
        }

    }
}
