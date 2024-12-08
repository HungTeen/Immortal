package hungteen.imm.compat.minecraft;

import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.entity.IMMEntities;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:55
 **/
public class VanillaCultivationCompat {

    private static final Map<EntityType<?>, RealmType> DEFAULT_REALM_MAP = new HashMap<>();
    private static final Map<EntityType<?>, Float> KILL_XP_MAP = new HashMap<>();
    private static final Map<TagKey<DamageType>, RealmType> DAMAGE_REALM_MAP = new HashMap<>();

    public static void fillEntityMap(){
        addKillXp(IMMEntities.WANDERING_CULTIVATOR.get(), 1);

//        put(EntityType.ENDERMAN, RealmTypes.QI_REFINING, 0.4F);
//        put(EntityType.PIGLIN, RealmTypes.YAOGUAI_LEVEL_1, 0.5F);
//        put(EntityType.BLAZE, RealmTypes.YAOGUAI_LEVEL_1, 0.5F);
//        put(EntityType.HOGLIN, RealmTypes.YAOGUAI_LEVEL_2, 0.7F);
//        put(EntityType.RAVAGER, RealmTypes.YAOGUAI_LEVEL_3, 1F);
    }

    public static void put(EntityType<?> type, RealmType realm, float xp){
        addDefaultRealm(type, realm);
        addKillXp(type, xp);
    }

    public static void addDefaultRealm(EntityType<?> type, RealmType realm){
        DEFAULT_REALM_MAP.put(type, realm);
    }

    public static RealmType getDefaultRealm(EntityType<?> type, RealmType defaultRealm){
        return DEFAULT_REALM_MAP.getOrDefault(type, defaultRealm);
    }

    public static RealmType getDamageSourceRealm(DamageSource source){
        for (Map.Entry<TagKey<DamageType>, RealmType> entry : DAMAGE_REALM_MAP.entrySet()) {
            if(source.is(entry.getKey())){
                return entry.getValue();
            }
        }
        return RealmTypes.NOT_IN_REALM;
    }

    public static void addKillXp(EntityType<?> type, float value){
        KILL_XP_MAP.put(type, value);
    }

    public static float getKillXp(EntityType<?> type){
        return KILL_XP_MAP.getOrDefault(type, CultivationManager.DEFAULT_KILL_XP);
    }

}
