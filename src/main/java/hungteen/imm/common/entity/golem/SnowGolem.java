package hungteen.imm.common.entity.golem;

import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-23 21:49
 **/
public class SnowGolem extends GolemEntity {

    public SnowGolem(EntityType<? extends GolemEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .build();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if(this.level.getBiome(this.blockPosition()).is(BiomeTags.SNOW_GOLEM_MELTS)) {
                this.hurt(this.damageSources().onFire(), 1.0F);
            }
        }
    }

    public void performRangedAttack(LivingEntity p_29912_, float p_29913_) {
        Snowball snowball = new Snowball(this.level, this);
        double d0 = p_29912_.getEyeY() - (double)1.1F;
        double d1 = p_29912_.getX() - this.getX();
        double d2 = d0 - snowball.getY();
        double d3 = p_29912_.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(snowball);
    }

    @Override
    public int getRuneInventorySize() {
        return 9;
    }

    @Override
    public int getRangeAttackCD() {
        return 30;
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (0.75F * this.getEyeHeight()), (this.getBbWidth() * 0.4F));
    }

    @Override
    public Collection<ISpiritualType> getSpiritualTypes() {
        return List.of(SpiritualTypes.WATER);
    }

}
