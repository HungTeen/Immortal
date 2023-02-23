package hungteen.immortal.common.impl;

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

    public static final IRealmType SPIRITUAL_LEVEL_1 = new RealmType("spiritual_level_1", 10, 15, 10);

    public static final IRealmType SPIRITUAL_LEVEL_2 = new RealmType("spiritual_level_2", 10, 30, 10);

    public static final IRealmType SPIRITUAL_LEVEL_3 = new RealmType("spiritual_level_3", 10, 60, 10);

    public static final IRealmType SPIRITUAL_LEVEL_4 = new RealmType("spiritual_level_4", 10, 100, 10);

    public static final IRealmType SPIRITUAL_LEVEL_5 = new RealmType("spiritual_level_5", 10, 160, 10);

    public static final IRealmType SPIRITUAL_LEVEL_6 = new RealmType("spiritual_level_6", 10, 200, 10);

    public static final IRealmType SPIRITUAL_LEVEL_7 = new RealmType("spiritual_level_7", 10, 250, 10);

    public static final IRealmType SPIRITUAL_LEVEL_8 = new RealmType("spiritual_level_8", 10, 300, 10);

    public static final IRealmType SPIRITUAL_LEVEL_9 = new RealmType("spiritual_level_9", 10, 350, 10);

    public static final IRealmType SPIRITUAL_LEVEL_10 = new RealmType("spiritual_level_10", 10, 400, 10);

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
        public int requireCultivation() {
            return cultivation;
        }

        @Override
        public int getRealmValue() {
            return realm;
        }

    }
}
