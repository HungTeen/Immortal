package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.registry.SoundHelper;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlaySoundPacket {

    private final BlockPos pos;
    private final SoundEvent soundEvent;
    private final SoundSource soundSource;

    public PlaySoundPacket(BlockPos pos, SoundEvent soundEvent, SoundSource soundSource) {
        this.pos = pos;
        this.soundEvent = soundEvent;
        this.soundSource = soundSource;
    }

    public PlaySoundPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.soundEvent = SoundHelper.get().read(buffer);
        this.soundSource = buffer.readEnum(SoundSource.class);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        SoundHelper.get().write(buffer, this.soundEvent);
        buffer.writeEnum(this.soundSource);
    }

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(PlaySoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                PlayerHelper.getClientPlayer().map(Entity::level).ifPresent(level -> {
                    level.playLocalSound(msg.pos, msg.soundEvent, msg.soundSource, 10.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F, false);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
