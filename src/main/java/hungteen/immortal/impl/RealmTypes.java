package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.utils.Util;
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

    private static final List<IRealmType> TYPES = new ArrayList<>();

    public static final IRealmType MORTALITY = new RealmType("mortality", 0, 0, 0, true);

    /* 炼气 */

    public static final IRealmType MEDITATION_STAGE1 = new RealmType("meditation_stage1", 10, 1, 10);

    public static final IRealmType MEDITATION_STAGE2 = new RealmType("meditation_stage2", 30, 2, 20);

    public static final IRealmType MEDITATION_STAGE3 = new RealmType("meditation_stage3", 50, 3, 30);

    public static final IRealmType MEDITATION_STAGE4 = new RealmType("meditation_stage4", 75, 4, 40);

    public static final IRealmType MEDITATION_STAGE5 = new RealmType("meditation_stage5", 100, 5, 50);

    public static final IRealmType MEDITATION_STAGE6 = new RealmType("meditation_stage6", 125, 6, 60);

    public static final IRealmType MEDITATION_STAGE7 = new RealmType("meditation_stage7", 150, 7, 75);

    public static final IRealmType MEDITATION_STAGE8 = new RealmType("meditation_stage8", 180, 8, 90);

    public static final IRealmType MEDITATION_STAGE9 = new RealmType("meditation_stage9", 210, 9, 105);

    public static final IRealmType MEDITATION_STAGE10 = new RealmType("meditation_stage10", 250, 10, 125, true);

    /* 筑基 */

    public static final IRealmType FOUNDATION_BEGIN = new RealmType("foundation_begin", 300, 11, 250);

    public static final IRealmType FOUNDATION_MEDIUM = new RealmType("foundation_medium", 400, 13, 280);

    public static final IRealmType FOUNDATION_LATE = new RealmType("foundation_late", 500, 16, 320);

    public static final IRealmType FOUNDATION_FINISH = new RealmType("foundation_finish", 600, 19, 360);

//    /* 结丹 */
//    public static final IRealm VIRTUOSO_STAGE = new Realm("virtuoso_stage", 5000, 20);
//
//    /* 元婴 */
//    public static final IRealm IMMORTALITY_STAGE = new Realm("immortality_stage", 10000, 30);
//
//    /* 化神 */
//    public static final IRealm INCARNATION_STAGE = new Realm("incarnation_stage", 20000, 40);

    /* 妖兽 */
    public static final IRealmType MONSTER_STAGE0 = new RealmType("monster_stage0", 100, 4, 50);

    public static final IRealmType MONSTER_STAGE1 = new RealmType("monster_stage1", 200, 9, 100);

    public static final IRealmType MONSTER_STAGE2 = new RealmType("monster_stage2", 300, 12, 200);

    public static final IRealmType MONSTER_STAGE3 = new RealmType("monster_stage3", 450, 15, 300);

    public static final IRealmType MONSTER_STAGE4 = new RealmType("monster_stage4", 600, 20, 400);

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

    /* 亡灵 */
    public static final IRealmType UNDEAD_LEVEL1 = new RealmType("undead_level1", 80, 4, 25, false);
    public static final IRealmType UNDEAD_LEVEL2 = new RealmType("undead_level2", 200, 8, 50, false);

    public static class RealmType implements IRealmType {

        private final String name;
        private final int cultivation;
        private final int realm;
        private final int spiritualValue;
        private final boolean hasThreshold;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            ImmortalAPI.get().realmRegistry().ifPresent(l -> l.register(TYPES));
        }

        public RealmType(String name, int cultivation, int realm, int spiritualValue){
            this(name, cultivation, realm, spiritualValue, false);
        }

        public RealmType(String name, int cultivation, int realm, int spiritualValue, boolean hasThreshold) {
            this.name = name;
            this.cultivation = cultivation;
            this.realm = realm;
            this.spiritualValue = spiritualValue;
            this.hasThreshold = hasThreshold;
            TYPES.add(this);
        }

        @Override
        public int getBaseSpiritualValue() {
            return spiritualValue;
        }

        @Override
        public boolean hasThreshold() {
            return hasThreshold;
        }

        @Override
        public String getName() {
            return name;
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
        public int getCultivation() {
            return cultivation;
        }

        @Override
        public int getRealmValue() {
            return realm;
        }

    }
}
