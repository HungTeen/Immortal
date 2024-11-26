package hungteen.imm.common.capability.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.impl.LevelHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.capability.HTPlayerData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.UnknownNullability;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/22 20:19
 **/
public class PlayerMiscData implements HTPlayerData {

    private IMMPosition lastPosBeforeSpiritWorld;

    public void setLastPosBeforeSpiritWorld(ResourceKey<Level> level, Vec3 pos) {
        this.lastPosBeforeSpiritWorld = new IMMPosition(level, pos);
    }

    public IMMPosition getLastPosBeforeSpiritWorld() {
        return lastPosBeforeSpiritWorld;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if(lastPosBeforeSpiritWorld != null){
            CodecHelper.encodeNbt(IMMPosition.CODEC, lastPosBeforeSpiritWorld)
                    .resultOrPartial(msg -> IMMAPI.logger().error(msg))
                    .ifPresent(nbt -> tag.put("lastPosBeforeSpiritWorld", nbt));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if(nbt.contains("lastPosBeforeSpiritWorld")){
            CodecHelper.parse(IMMPosition.CODEC, nbt.getCompound("lastPosBeforeSpiritWorld"))
                    .resultOrPartial(msg -> IMMAPI.logger().error(msg))
                    .ifPresent(pos -> lastPosBeforeSpiritWorld = pos);
        }
    }

    @Override
    public void syncToClient() {

    }

    @Override
    public boolean isServer() {
        return false;
    }


    public record IMMPosition(ResourceKey<Level> level, Vec3 pos) {

        public static final Codec<IMMPosition> CODEC = RecordCodecBuilder.<IMMPosition>mapCodec(instance -> instance.group(
                ResourceKey.codec(LevelHelper.get().resourceKey()).fieldOf("level").forGetter(IMMPosition::level),
                Vec3.CODEC.fieldOf("pos").forGetter(IMMPosition::pos)
        ).apply(instance, IMMPosition::new)).codec();

    }
}
