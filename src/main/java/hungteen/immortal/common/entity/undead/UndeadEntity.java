package hungteen.immortal.common.entity.undead;

import hungteen.immortal.api.interfaces.IUndead;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.entity.ImmortalCreature;
import hungteen.immortal.impl.SpiritualTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 23:11
 **/
public abstract class UndeadEntity extends ImmortalCreature implements IUndead {

    public UndeadEntity(EntityType<? extends UndeadEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        return super.finalizeSpawn(levelAccessor, difficulty, spawnType, groupData, tag);
    }

    @Override
    public Collection<ISpiritualType> getSpiritualTypes() {
        return List.of(SpiritualTypes.DRUG);
    }
}
