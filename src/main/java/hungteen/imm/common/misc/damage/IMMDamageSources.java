package hungteen.imm.common.misc.damage;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-18 20:39
 **/
public class IMMDamageSources {

    public static DamageSource spiritualMana(Entity causingEntity){
        return source(causingEntity, IMMDamageTypes.SPIRITUAL_MANA, causingEntity, causingEntity);
    }

    public static DamageSource source(Entity entity, ResourceKey<DamageType> key, Entity causingEntity){
        return new DamageSource(type(entity, key), causingEntity);
    }

    public static DamageSource source(Entity entity, ResourceKey<DamageType> key, Entity causingEntity, Entity directEntity){
        return new DamageSource(type(entity, key), causingEntity, directEntity);
    }

    public static DamageSource source(Entity entity, ResourceKey<DamageType> key, Vec3 pos){
        return new DamageSource(type(entity, key), pos);
    }

    public static Holder<DamageType> type(Entity entity, ResourceKey<DamageType> key){
        return entity.damageSources().damageTypes.getHolderOrThrow(key);
    }

}
