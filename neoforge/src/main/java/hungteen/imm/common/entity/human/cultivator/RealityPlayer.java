package hungteen.imm.common.entity.human.cultivator;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.common.world.levelgen.spiritworld.SpiritWorldDimension;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 19:07
 **/
public class RealityPlayer extends PlayerLikeEntity{

    private static final EntityDataAccessor<Boolean> SPIRIT = SynchedEntityData.defineId(RealityPlayer.class, EntityDataSerializers.BOOLEAN);

    public RealityPlayer(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
        this.setNoAi(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPIRIT, false);
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.tickCount % 20 == 0){
                if(isSpirit()) {
                    this.getPlayerOpt().ifPresent(p -> {
                        // 玩家不在精神领域时，会自动消失。
                        if (!p.level().dimension().equals(IMMLevels.SPIRIT_WORLD)) {
                            this.discard();
                        }
                    });
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(super.hurt(source, amount)){
            this.getPlayerOpt().ifPresent(p -> {
                if(isSpirit()) {
                    // 受到伤害时，玩家会被传送出精神领域。
                    if (p instanceof ServerPlayer serverPlayer) {
                        SpiritWorldDimension.teleportBackFromSpiritRegion(serverPlayer.serverLevel(), serverPlayer);
                    }
                } else {
                    // 提醒玩家。

                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("Spirit")){
            this.setSpirit(tag.getBoolean("Spirit"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Spirit", this.isSpirit());
    }

    @Override
    public Set<QiRootType> getRoots() {
        return this.getPlayerOpt().map(p -> new HashSet<>(PlayerUtil.getRoots(p))).orElse(new HashSet<>());
    }

    public boolean isSpirit() {
        return this.entityData.get(SPIRIT);
    }

    public void setSpirit(boolean spirit) {
        this.entityData.set(SPIRIT, spirit);
    }

}
