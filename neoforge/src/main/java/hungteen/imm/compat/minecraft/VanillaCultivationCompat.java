package hungteen.imm.compat.minecraft;

import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.RealmManager;
import hungteen.imm.common.cultivation.RealmTypes;
import net.minecraft.world.entity.EntityType;

/**
 * 兼容一些修仙设定和数值。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:55
 **/
public class VanillaCultivationCompat {

    public static void fillEntityMap(){
        put(EntityType.ENDERMAN, 0.3F);
        put(EntityType.ENDERMITE, 0.25F);
        put(EntityType.PHANTOM, 0.3F);
        put(EntityType.WITHER_SKELETON, 0.3F);
        put(EntityType.VEX, 0.4F);
        put(EntityType.EVOKER, RealmTypes.QI_REFINING.level(8));
        put(EntityType.ILLUSIONER, RealmTypes.QI_REFINING.level(11));
        put(EntityType.RAVAGER, RealmTypes.YAOGUAI_LEVEL_1.level(5));
        put(EntityType.IRON_GOLEM, RealmTypes.YAOGUAI_LEVEL_1.level(6));
        put(EntityType.SNIFFER, RealmTypes.YAOGUAI_LEVEL_1.level(8));
        put(EntityType.WARDEN, RealmTypes.YAOGUAI_LEVEL_3.pre());
        put(EntityType.WITHER, RealmTypes.YAOGUAI_LEVEL_4.pre());
        put(EntityType.ENDER_DRAGON, RealmTypes.YAOGUAI_LEVEL_4.pre());
    }

    public static void put(EntityType<?> type, RealmType realm, float xp){
        put(type, realm);
        put(type, xp);
    }

    public static void put(EntityType<?> type, float xp){
        CultivationManager.addKillXp(type, xp);
    }

    public static void put(EntityType<?> type, RealmType realm){
        RealmManager.addDefaultRealm(type, realm);
    }

}
