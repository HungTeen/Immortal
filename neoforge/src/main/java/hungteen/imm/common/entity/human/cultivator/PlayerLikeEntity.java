package hungteen.imm.common.entity.human.cultivator;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 18:11
 **/
public abstract class PlayerLikeEntity extends HumanLikeEntity implements TraceableEntity {

    private Player owner;
    private UUID ownerUUID;
    private int ownerId;
    private int missingTick = 0;
    private boolean isSlim;

    public PlayerLikeEntity(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public void updateBrain(ServerLevel level) {

    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.getOwner() == null){
                if(++ missingTick >= 20){
                    this.discard();
                    IMMAPI.logger().error("PlayerLikeEntity missing owner, discard entity.");
                }
            }
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        super.readSpawnData(additionalData);
        this.ownerId = additionalData.readInt();
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        super.writeSpawnData(buffer);
        buffer.writeInt(this.getOwner() == null ? 0 : this.getOwner().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, tag::getUUID, "owner", this::setOwnerUUID);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag::putUUID, "owner", this.getOwnerUUID());
    }

    public void setOwner(@javax.annotation.Nullable Player entity) {
        this.owner = entity;
        this.ownerUUID = entity == null ? null : entity.getUUID();
    }

    @Nullable
    @Override
    public Player getOwner() {
        if (this.owner == null){
            if(this.ownerUUID != null && this.level() instanceof ServerLevel) {
                Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
                if (entity instanceof Player player) {
                    this.owner = player;
                }
            }
            if(!(this.level() instanceof ServerLevel)){
                Entity entity = this.level().getEntity(this.ownerId);
                if (entity instanceof Player player) {
                    this.owner = player;
                }
            }
        }

        return this.owner;
    }

    public Optional<Player> getPlayerOpt(){
        return Optional.ofNullable(getOwner());
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setSlim(boolean slim) {
        isSlim = slim;
    }

    public boolean isSlim() {
        return isSlim;
    }

}
