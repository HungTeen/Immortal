package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-18 12:49
 **/
public class ElementCrystal extends HTEntity {

    public ElementCrystal(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickMove(0.95F, 0.8F);
        if (EntityHelper.isServer(this) && (this.tickCount & 1) == 0) {
            if (!ElementManager.hasElement(this, Elements.EARTH, true)) {
                this.discard();
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.isRemoved()) {
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
            // 爆炸扩散元素。
            this.explode();
            return true;
        } else {
            return true;
        }
    }

    public void explode() {
        final AABB aabb = EntityHelper.getEntityAABB(this, 2F, 0.5F);
        EntityHelper.getPredicateEntities(this, aabb, Entity.class, target -> {
            return true;
        }).forEach(target -> {
            final double dx = target.getX() - this.getX();
            final double dz = target.getZ() - this.getZ();
            final double distance = dx * dx + dz * dz;
            final float percent = (float) (1 - distance / 8);
            if (percent > 0) {
                ElementManager.getElements(this).forEach((element, value) -> {
                    if (element != Elements.EARTH) {
                        ElementManager.addElementAmount(target, element, false, value * percent);
                    }
                });
            }
        });
        this.breakAmethyst();
    }

    public void breakAmethyst() {
        if (this.level() instanceof ServerLevel serverLevel) {
            ParticleHelper.spawnParticles(serverLevel, ParticleTypes.EXPLOSION, getX(), getY(0.5), getZ(), 2, 0);
            this.playSound(SoundEvents.AMETHYST_BLOCK_BREAK);
            this.discard();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }
}
