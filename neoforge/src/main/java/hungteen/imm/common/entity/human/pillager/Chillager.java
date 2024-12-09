package hungteen.imm.common.entity.human.pillager;

import com.mojang.serialization.Dynamic;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.IMMSounds;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.realm.MultiRealm;
import hungteen.imm.common.entity.ai.IMMActivities;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.entity.human.VillagerLikeEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/7 22:49
 **/
public class Chillager extends VillagerLikeEntity {

    public float targetMovement;
    public float capeRotate;

    public Chillager(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return HumanLikeEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.ARMOR, 6)
                ;
    }

    @Override
    protected Collection<QiRootType> getInitialRoots(ServerLevelAccessor accessor) {
        return List.of(QiRootTypes.WATER);
    }

    @Override
    public List<MultiRealm> getMultiRealms() {
        return List.of(RealmTypes.QI_REFINING);
    }

    @Override
    public void updateBrain(ServerLevel level) {
        this.getBrain().tick(level, this);
        this.getBrain().setActiveActivityToFirstValid(List.of(IMMActivities.RANGE_FIGHT.get(), Activity.IDLE));
    }

    @Override
    public Brain<Chillager> getBrain() {
        return (Brain<Chillager>)super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return ChillagerAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<Chillager> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

    @Override
    public boolean canTargetLiving(LivingEntity target) {
        return (target instanceof Player || target instanceof AbstractVillager) && super.canTargetLiving(target);
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IMMSounds.CHILLAGER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return IMMSounds.CHILLAGER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return IMMSounds.CHILLAGER_DEATH.get();
    }
}
