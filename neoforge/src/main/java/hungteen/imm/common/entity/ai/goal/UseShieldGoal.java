package hungteen.imm.common.entity.ai.goal;

import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ItemUtil;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-03 20:48
 **/
public class UseShieldGoal extends Goal {

    private static final int COOLDOWN = 20;
    private final Mob entity;
    private final IntProvider blockingTime;
    private InteractionHand usingHand;
    private int shieldUsingTick;
    private int cooldown;

    public UseShieldGoal(Mob entity){
        this(entity, UniformInt.of(20, 50));
    }

    public UseShieldGoal(Mob entity, IntProvider blockingTime) {
        this.entity = entity;
        this.blockingTime = blockingTime;
        this.cooldown = COOLDOWN;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if(this.cooldown <= 0){
            Optional<InteractionHand> itemInHand = EntityUtil.getHandOpt(entity, ItemUtil::canShieldBlock);
            if(itemInHand.isPresent()){
                // 被攻击的五秒内或者距离敌人很近。
                boolean aware = (this.entity.tickCount - this.entity.getLastHurtByMobTimestamp() <= 100);
                if(aware || (this.entity.getTarget() != null && this.entity.distanceTo(this.entity.getTarget()) < 4) || entity.getRandom().nextFloat() < 0.25F){
                    this.usingHand = itemInHand.get();
                    return true;
                } else {
                    this.cooldown = COOLDOWN / 2;
                }
            } else {
                this.cooldown = COOLDOWN * 2;
            }
        } else {
            --this.cooldown;
        }
        return false;
    }

    @Override
    public void start() {
        this.entity.startUsingItem(this.usingHand);
        this.shieldUsingTick = this.blockingTime.sample(entity.getRandom());
    }

    @Override
    public boolean canContinueToUse() {
        return this.usingHand != null && this.entity.getUsedItemHand() == this.usingHand && EntityUtil.checkHand(entity, this.usingHand, ItemUtil::canShieldBlock) && -- this.shieldUsingTick >= 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        } else {
            target = this.entity.getLastHurtByMob();
            if (target != null) {
                this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
        }
    }

    @Override
    public void stop() {
        if(this.entity.isUsingItem()){
            this.cooldown = COOLDOWN;
            this.entity.stopUsingItem();
        } else {
            // 被破盾了。
            this.cooldown = COOLDOWN * 5;
        }
        this.shieldUsingTick = 0;
    }
}
