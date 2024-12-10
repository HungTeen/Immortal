package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 18:18
 **/
public class QiRootCrystal extends HTEntity implements IEntityWithComplexSpawn {

    private QiRootType qiRootType;

    public QiRootCrystal(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickMove();
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceLocation(qiRootType.getLocation());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        ResourceLocation location = additionalData.readResourceLocation();
        QiRootTypes.registry().getValue(location).ifPresent(this::setQiRootType);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        NBTUtil.read(tag, QiRootTypes.registry().byNameCodec(), "qiRootType", this::setQiRootType);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.write(tag, QiRootTypes.registry().byNameCodec(), "qiRootType", this.getQiRootType());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public QiRootType getQiRootType() {
        return qiRootType;
    }

    public void setQiRootType(QiRootType qiRootType) {
        this.qiRootType = qiRootType;
    }
}
