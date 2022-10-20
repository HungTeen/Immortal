package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IHasRealm;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 10:09
 **/
public class Realms {

    private static final List<IRealm> TYPES = new ArrayList<>();

    public static final IRealm MORTALITY = new Realm("mortality", 0, 0, 10, true, true);

    /* 炼气 */

    public static final IRealm MEDITATION_STAGE1 = new Realm("meditation_stage1", 10, 1, 10);

    public static final IRealm MEDITATION_STAGE2 = new Realm("meditation_stage2", 30, 2, 15);

    public static final IRealm MEDITATION_STAGE3 = new Realm("meditation_stage3", 50, 3, 20);

    public static final IRealm MEDITATION_STAGE4 = new Realm("meditation_stage4", 75, 4, 30);

    public static final IRealm MEDITATION_STAGE5 = new Realm("meditation_stage5", 100, 5, 40);

    public static final IRealm MEDITATION_STAGE6 = new Realm("meditation_stage6", 125, 6, 50);

    public static final IRealm MEDITATION_STAGE7 = new Realm("meditation_stage7", 150, 7, 60);

    public static final IRealm MEDITATION_STAGE8 = new Realm("meditation_stage8", 180, 8, 70);

    public static final IRealm MEDITATION_STAGE9 = new Realm("meditation_stage9", 210, 9, 80);

    public static final IRealm MEDITATION_STAGE10 = new Realm("meditation_stage10", 250, 10, 100, true, true);

    /* 筑基 */

    public static final IRealm FOUNDATION_BEGIN = new Realm("foundation_begin", 300, 11, 125);
//
//    public static final IRealm FOUNDATION_MEDIUM = new Realm("foundation_medium", 1500, 13);
//
//    public static final IRealm FOUNDATION_LATE = new Realm("foundation_late", 2000, 16);
//
//    public static final IRealm FOUNDATION_FINISH = new Realm("foundation_finish", 2500, 19);

//    /* 结丹 */
//    public static final IRealm VIRTUOSO_STAGE = new Realm("virtuoso_stage", 5000, 20);
//
//    /* 元婴 */
//    public static final IRealm IMMORTALITY_STAGE = new Realm("immortality_stage", 10000, 30);
//
//    /* 化神 */
//    public static final IRealm INCARNATION_STAGE = new Realm("incarnation_stage", 20000, 40);

    /* 妖兽 */
//    public static final IRealm MONSTER_STAGE1 = new Realm("monster_stage1", 10, 8);
//
//    public static final IRealm MONSTER_STAGE2 = new Realm("monster_stage2", 30, 11);
//
//    public static final IRealm MONSTER_STAGE3 = new Realm("monster_stage3", 60, 15);
//
//    public static final IRealm MONSTER_STAGE4 = new Realm("monster_stage4", 100, 18);
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

    /* 亡灵 */
    public static final IRealm UNDEAD_LEVEL1 = new Realm("undead_level1", 80, 4, 25, false);
    public static final IRealm UNDEAD_LEVEL2 = new Realm("undead_level2", 200, 8, 50, false);

    public static class Realm implements IRealm {

        private final String name;
        private final int cultivation;
        private final int realm;
        private final int spiritualValue;
        private final boolean hasThreshold;
        private final boolean forHuman;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            TYPES.forEach(type -> ImmortalAPI.get().registerRealm(type));
        }

        public Realm(String name, int cultivation, int realm, int spiritualValue){
            this(name, cultivation, realm, spiritualValue, true);
        }

        public Realm(String name, int cultivation, int realm, int spiritualValue, boolean forHuman){
            this(name, cultivation, realm, spiritualValue, false, forHuman);
        }

        public Realm(String name, int cultivation, int realm, int spiritualValue, boolean hasThreshold, boolean forHuman) {
            this.name = name;
            this.cultivation = cultivation;
            this.realm = realm;
            this.spiritualValue = spiritualValue;
            this.hasThreshold = hasThreshold;
            this.forHuman = forHuman;
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
        public boolean forHuman() {
            return forHuman;
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
        public Component getComponent() {
            return new TranslatableComponent("misc." + getModID() +".realm." + getName());
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
