package hungteen.imm.common.misc.damage;

import hungteen.htlib.util.helper.impl.DamageHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-18 20:39
 **/
public interface IMMDamageSources {

    static DamageSource qi(Entity causingEntity){
        return DamageHelper.source(causingEntity, IMMDamageTypes.QI, causingEntity);
    }

    static DamageSource spiritualFlame(Entity causingEntity){
        return DamageHelper.source(causingEntity, IMMDamageTypes.SPIRITUAL_FLAME, causingEntity);
    }

    static DamageSource elementReaction(Entity sourceEntity){
        return noEntity(sourceEntity, IMMDamageTypes.ELEMENT_REACTION);
    }

    static DamageSource woodElement(Entity causingEntity){
        return woodElement(causingEntity, causingEntity);
    }

    static DamageSource woodElement(Entity causingEntity, Entity owner){
        return DamageHelper.source(causingEntity, IMMDamageTypes.WOOD_ELEMENT, owner, causingEntity);
    }

    static DamageSource waterElement(Entity causingEntity){
        return DamageHelper.source(causingEntity, IMMDamageTypes.WATER_ELEMENT, causingEntity);
    }

    static DamageSource waterElement(Entity causingEntity, Entity owner){
        return DamageHelper.source(causingEntity, IMMDamageTypes.WATER_ELEMENT, owner, causingEntity);
    }

    static DamageSource fireElement(Entity causingEntity){
        return fireElement(causingEntity, causingEntity);
    }

    static DamageSource fireElement(Entity causingEntity, Entity owner){
        return DamageHelper.source(causingEntity, IMMDamageTypes.FIRE_ELEMENT, owner, causingEntity);
    }

    static DamageSource noEntity(Entity source, ResourceKey<DamageType> damageType){
        return DamageHelper.source(source, damageType);
    }

}
