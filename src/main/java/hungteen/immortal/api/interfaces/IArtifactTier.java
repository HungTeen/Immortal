package hungteen.immortal.api.interfaces;

import hungteen.immortal.api.registry.IArtifactType;
import net.minecraft.world.item.Tier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-14 15:43
 **/
public interface IArtifactTier extends Tier {

    /**
     * Addition attack damage value of the artifact tier.
     * @return attack damage.
     */
    float getAttackDamage();

    /**
     * Addition attack speed value of the artifact tier.
     * @return attack speed.
     */
    float getAttackSpeed();

    /**
     * Addition attack range value of the artifact tier.
     * @return attack range.
     */
    float getAttackRange();

    /**
     * Level of the artifact tier.
     * @return level.
     */
    IArtifactType getArtifactType();

    /**
     * Replace this method with {@link IArtifactTier#getAttackSpeed()} ()}
     * @return speed bonus.
     */
    @Override
    default float getSpeed(){
        return getAttackSpeed();
    }

    /**
     * Replace this method with {@link IArtifactTier#getAttackDamage()}
     * @return damage bonus.
     */
    @Override
    default float getAttackDamageBonus() {
        return getAttackDamage();
    }

    /**
     * No usage.
     */
    @Override
    default int getLevel(){
        return 0;
    }

    /**
     * No usage because Artifact Item can not enchant.
     * @return enchantment point.
     */
    @Override
    default int getEnchantmentValue(){
        return 0;
    }
}
