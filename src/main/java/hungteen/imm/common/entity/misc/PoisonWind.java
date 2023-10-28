package hungteen.imm.common.entity.misc;

import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.records.Spell;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/27 19:14
 **/
public class PoisonWind extends ThrowableProjectile {

    public PoisonWind(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public PoisonWind(Level level, LivingEntity owner) {
        super(IMMEntities.POISON_WIND.get(), owner, level);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        this.level().addParticle(this.getTrailParticle(), getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (result.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult)result).getEntity())) {
            if (!this.level().isClientSide) {
                if(result instanceof EntityHitResult entityHitResult){
                    entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 1);
                }
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
                AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity) {
                    cloud.setOwner((LivingEntity)entity);
                }
                cloud.setRadius(3.0F);
                cloud.setDuration(100);
                cloud.addEffect(EffectHelper.viewEffect(MobEffects.POISON, 200, 2));
                if (!list.isEmpty()) {
                    for(LivingEntity livingentity : list) {
                        ElementManager.addElementAmount(livingentity, Elements.WOOD, false, 4, 8F);
                        double d0 = this.distanceToSqr(livingentity);
                        if (d0 < 16.0D) {
                            cloud.setPos(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                            break;
                        }
                    }
                }

//                this.level().levelEvent(2006, this.blockPosition(), this.isSilent() ? -1 : 1);
                this.level().addFreshEntity(cloud);
                this.discard();
            }
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }

}
