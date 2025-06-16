package hungteen.imm.api.artifact;

import net.minecraft.world.item.Tier;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-14 15:43
 **/
public interface ArtifactTier extends Tier {

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
    ArtifactRank getArtifactRealm();

    /**
     * Replace this method with {@link ArtifactTier#getAttackSpeed()} ()}
     * @return speed bonus.
     */
    @Override
    default float getSpeed(){
        return getAttackSpeed();
    }

    /**
     * Replace this method with {@link ArtifactTier#getAttackDamage()}
     * @return damage bonus.
     */
    @Override
    default float getAttackDamageBonus() {
        return getAttackDamage();
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
