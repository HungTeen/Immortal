package hungteen.imm.common.entity.creature.spirit;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.frog.FrogAi;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.Nullable;
import java.util.EnumSet;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/22 10:08
 **/
public class WaterSpirit extends ElementSpirit {

    public WaterSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 30, true);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1F, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1F, 80));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ElementSpirit.createAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FLYING_SPEED, 0.4D)
                ;
    }

    public static boolean isWaterSpiritRiding(Entity entity) {
        return entity.getPassengers().stream().anyMatch(WaterSpirit.class::isInstance);
    }

    /**
     * See drown damage handle in {@link LivingEntity#baseTick()}.
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        if(target instanceof LivingEntity living){
            this.startRiding(target);
            if(living.getAirSupply() <= 0){
                living.setAirSupply(living.getAirSupply() - 1);
            } else {
                living.setAirSupply(Math.max(0, living.getAirSupply() - RandomHelper.getMinMax(living.getRandom(), 20, 40)));
            }
            ParticleUtil.spawnEntityParticle(living, ParticleTypes.BUBBLE, 8, 0.1);
            if (living.getAirSupply() <= -19) {
                living.setAirSupply(0);
                living.hurt(living.damageSources().drown(), 3.0F);
            }
            return true;
        }
        return super.doHurtTarget(target);
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        if (type == ForgeMod.WATER_TYPE.get()) return false;
        return super.canDrownInFluidType(type);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    public ISpiritualType getSpiritualRoot() {
        return SpiritualTypes.WATER;
    }

    @Override
    public Elements getElement() {
        return Elements.WATER;
    }

}
