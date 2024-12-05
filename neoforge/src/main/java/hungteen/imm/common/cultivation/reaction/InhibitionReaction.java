package hungteen.imm.common.cultivation.reaction;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.ElementReactions;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EffectUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;

/**
 * 相克反应。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:30
 **/
public abstract class InhibitionReaction extends ElementReactionImpl {

    private final Element ingredient;
    private final Element production;
    private final float mainAmount;
    private final float offAmount;
    private final boolean spawnAmethyst;

    public InhibitionReaction(String name, boolean once, Element main, float mainAmount, Element off, float percent) {
        this(name, once, 80, main, mainAmount, off, mainAmount * percent);
    }

    public InhibitionReaction(String name, boolean once, int priority, Element main, float mainAmount, Element off, float offAmount) {
        super(name, once, priority, List.of(new ElementEntry(main, false, mainAmount), new ElementEntry(off, false, offAmount)));
        this.ingredient = main;
        this.production = off;
        this.mainAmount = mainAmount;
        this.offAmount = offAmount;
        this.spawnAmethyst = (main == Element.EARTH || off == Element.EARTH);
    }

    @Override
    public void doReaction(Entity entity, float scale) {
        if (this.spawnAmethyst && canSpawnAmethyst(entity)) {
            this.spawnAmethyst(entity).ifPresent(elementAmethyst -> {
                final float multiplier = scale * (this.once() ? 1 : 20);
                ElementManager.addElementAmount(elementAmethyst, this.ingredient, true, this.mainAmount * multiplier);
                ElementManager.addElementAmount(elementAmethyst, this.production, true, this.offAmount * multiplier);
                entity.level().addFreshEntity(elementAmethyst);
            });
        }
    }

    /**
     * 固化不能跳跃。
     */
    public static void blockSolidificationJump(LivingEntity entity){
        if(entity.hasEffect(IMMEffects.SOLIDIFICATION.holder())){
            final Vec3 speed = entity.getDeltaMovement();
            entity.setDeltaMovement(speed.x(), Math.min(speed.y(), 0), speed.z());
        }
    }

    public static void breakSolidification(LivingIncomingDamageEvent event){
        if(event.getEntity().level() instanceof ServerLevel level) {
            if (event.getEntity().hasEffect(IMMEffects.SOLIDIFICATION.holder())) {
                BlockParticleOption particle = ParticleUtil.block(Blocks.DIRT.defaultBlockState());
                Vec3 pos = event.getEntity().getEyePosition();
                if (event.getAmount() >= event.getEntity().getMaxHealth() * 0.1F) {
                    event.getEntity().removeEffect(IMMEffects.SOLIDIFICATION.holder());
                    event.setAmount(event.getAmount() * 1.1F);
                    ParticleHelper.sendParticles(level, particle, pos.x(), pos.y(), pos.z(), 20, 0.15);
                    event.getEntity().playSound(SoundEvents.GRAVEL_BREAK);
                } else {
                    ParticleHelper.sendParticles(level, particle, pos.x(), pos.y(), pos.z(), 5, 0.15);
                }
            }
        }
    }

    public static void cut(LivingDamageEvent.Pre event){
        if(EntityHelper.isServer(event.getEntity()) && event.getEntity().level() instanceof ServerLevel serverLevel){
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.CUTTING, (reaction, scale) -> {
                int level = EffectUtil.getEffectLevel(event.getEntity(), IMMEffects.CUTTING.holder());
                if(ElementManager.hasElement(event.getEntity(), Element.WATER, false) && reaction instanceof CuttingReaction cuttingReaction){
                    float amount = Math.min(ElementManager.getAmount(event.getEntity(), Element.WATER, false), cuttingReaction.getWaterAmount() * scale);
                    scale -= amount / cuttingReaction.getWaterAmount();
                    ElementManager.consumeAmount(event.getEntity(), Element.WATER, false, amount);
                }
                event.setNewDamage(event.getOriginalDamage() + scale * Mth.sqrt(2F * (level + 1)));
                event.getEntity().addEffect(EffectHelper.viewEffect(IMMEffects.CUTTING.holder(), 200, Math.min(5, level)));
                ParticleUtil.spawnClientParticles(serverLevel, IMMParticles.METAL_DAMAGE.get(), event.getEntity().position(), 1, 0.1, 0);
            });
        }
    }

    /**
     * Server only.
     */
    public static void parasitism(Entity entity){
        ElementManager.ifActiveReaction(entity, ElementReactions.PARASITISM, (reaction, scale) -> {
            if(EntityUtil.hasMana(entity)){
                EntityUtil.addMana(entity, - scale * 2.5F);
            } else {
                if(entity.getRandom().nextFloat() < 0.1F){
                    entity.hurt(IMMDamageSources.elementReaction(entity), scale * 2F);
                }
            }
        });
    }
}
