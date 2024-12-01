package hungteen.imm.common.entity.undead;

import com.mojang.serialization.Dynamic;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.realm.MultiRealm;
import hungteen.imm.common.entity.IMMMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Collection;
import java.util.List;

/**
 * Modify from {@link net.minecraft.world.entity.monster.Zombie}.
 *
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-19 23:04
 **/
public class QiZombie extends UndeadEntity {

    public QiZombie(EntityType<? extends UndeadEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return IMMMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected Brain.Provider<QiZombie> brainProvider() {
        return Brain.provider(getMemories(), getSensors());
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return QiZombieAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Collection<QiRootType> createRoots(ServerLevelAccessor accessor) {
        return CultivationManager.getQiRoots(accessor.getRandom());
    }

    @Override
    public List<MultiRealm> getMultiRealms() {
        return List.of(RealmTypes.LOW_RANK_UNDEAD, RealmTypes.MID_RANK_UNDEAD, RealmTypes.HIGH_RANK_UNDEAD);
    }
}
