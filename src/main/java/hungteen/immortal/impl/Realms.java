package hungteen.immortal.impl;

import hungteen.immortal.api.interfaces.IRealm;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 10:09
 **/
public class Realms {

//    private static final List<IRealm> TYPES = new ArrayList<>();

    public static final IRealm MORTALITY = new Realm("mortality", 0, 0);

    /* 炼气 */

    public static final IRealm MEDITATION_STAGE1 = new Realm("meditation_stage1", 10, 1);

    public static final IRealm MEDITATION_STAGE2 = new Realm("meditation_stage2", 30, 2);

    public static final IRealm MEDITATION_STAGE3 = new Realm("meditation_stage3", 60, 3);

    public static final IRealm MEDITATION_STAGE4 = new Realm("meditation_stage4", 100, 4);

    public static final IRealm MEDITATION_STAGE5 = new Realm("meditation_stage5", 150, 5);

    public static final IRealm MEDITATION_STAGE6 = new Realm("meditation_stage6", 210, 6);

    public static final IRealm MEDITATION_STAGE7 = new Realm("meditation_stage7", 300, 7);

    public static final IRealm MEDITATION_STAGE8 = new Realm("meditation_stage8", 400, 8);

    public static final IRealm MEDITATION_STAGE9 = new Realm("meditation_stage9", 500, 9);

    public static final IRealm MEDITATION_STAGE10 = new Realm("meditation_stage10", 600, 10);

    /* 筑基 */

    public static final IRealm FOUNDATION_BEGIN = new Realm("foundation_begin", 1000, 10);

    public static final IRealm FOUNDATION_MEDIUM = new Realm("foundation_medium", 1500, 13);

    public static final IRealm FOUNDATION_LATE = new Realm("foundation_late", 2000, 16);

    public static final IRealm FOUNDATION_FINISH = new Realm("foundation_finish", 2500, 19);

    /* 结丹 */
    public static final IRealm VIRTUOSO_STAGE = new Realm("virtuoso_stage", 5000, 20);

    /* 元婴 */
    public static final IRealm IMMORTALITY_STAGE = new Realm("immortality_stage", 10000, 30);

    /* 化神 */
    public static final IRealm INCARNATION_STAGE = new Realm("incarnation_stage", 20000, 40);

    /* 妖兽 */
    public static final IRealm MONSTER_STAGE1 = new Realm("monster_stage1", 10, 8);

    public static final IRealm MONSTER_STAGE2 = new Realm("monster_stage2", 30, 11);

    public static final IRealm MONSTER_STAGE3 = new Realm("monster_stage3", 60, 15);

    public static final IRealm MONSTER_STAGE4 = new Realm("monster_stage4", 100, 18);

    public static final IRealm MONSTER_STAGE5 = new Realm("monster_stage5", 150, 21);

    public static final IRealm MONSTER_STAGE6 = new Realm("monster_stage6", 210, 25);

    public static final IRealm MONSTER_STAGE7 = new Realm("monster_stage7", 300, 28);

    public static final IRealm MONSTER_STAGE8 = new Realm("monster_stage8", 400, 31);

    public static final IRealm MONSTER_STAGE9 = new Realm("monster_stage9", 500, 35);

    public static final IRealm MONSTER_STAGE10 = new Realm("monster_stage10", 600, 38);


    public record Realm(String name, int cultivation, int realm) implements IRealm {

//        public HumanRealm(String name, int cultivation, int realm){
//            this.name = name;
//            this.cultivation = cultivation;
//            this.realm = realm;
//        }

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
