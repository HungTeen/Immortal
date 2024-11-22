package hungteen.imm.common.world.entity;

import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.util.helper.ColorHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/22 17:01
 **/
public class SpiritRegion extends DummyEntity {

    protected double width = 0.5D;
    protected Player owner;
    private UUID ownerUUID;
    private int noOwnerTime = 0;

    public SpiritRegion(ServerLevel level, Vec3 position, double width) {
        super(IMMDummyEntities.SPIRIT_REGION, level, position);
        this.width = width;
    }

    public SpiritRegion(DummyEntityType<?> entityType, Level level, CompoundTag tag) {
        super(entityType, level, tag);
    }

    @Override
    public void tick() {
        super.tick();
        // 如果没有主人，或者主人不在同一个维度，20 tick 后移除。
        if(this.getOwner() == null || ! this.getOwner().level().dimension().equals(this.getLevel().dimension())) {
            if(++ noOwnerTime > 20) {
                this.remove();
            }
        } else if(this.noOwnerTime != 0){
            this.noOwnerTime = 0;
        }
    }

    public void setOwner(Player owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.owner = owner;
        }
    }

    @Nullable
    public Player getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else if (this.ownerUUID != null && this.getLevel() instanceof ServerLevel serverlevel && serverlevel.getEntity(this.ownerUUID) instanceof Player player) {
            this.owner = player;
            return this.owner;
        } else {
            return null;
        }
    }

    public boolean ownedBy(Player player) {
        return player.getUUID().equals(this.ownerUUID);
    }

    @Override
    public boolean renderBorder() {
        return false;
    }

    @Override
    public int getBorderColor() {
        return ColorHelper.BORDER_AQUA.rgb();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Width")) {
            this.setWidth(tag.getDouble("Width"));
        }
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.owner = null;
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putDouble("Width", this.getWidth());
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        return super.save(tag);
    }

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

}
