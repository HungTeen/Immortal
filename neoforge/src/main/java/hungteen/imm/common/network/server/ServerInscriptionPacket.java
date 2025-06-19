package hungteen.imm.common.network.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.TriggerCondition;
import hungteen.imm.common.cultivation.TriggerConditions;
import hungteen.imm.common.menu.InscriptionTableMenu;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 23:59
 **/
public record ServerInscriptionPacket(Optional<Spell> spell, Optional<TriggerCondition> condition) implements PlayToServerPacket {

    public static final Type<ServerInscriptionPacket> TYPE = new Type<>(Util.prefix("server_inscription"));
    public static final Codec<ServerInscriptionPacket> CODEC = RecordCodecBuilder.<ServerInscriptionPacket>mapCodec(instance -> instance.group(
            Codec.optionalField("spell", Spell.CODEC, true).forGetter(ServerInscriptionPacket::spell),
            Codec.optionalField("condition", TriggerConditions.registry().byNameCodec(), true).forGetter(ServerInscriptionPacket::condition)
    ).apply(instance, ServerInscriptionPacket::new)).codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerInscriptionPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ServerPacketContext serverPacketContext) {
        ServerPlayer player = serverPacketContext.player();
        if(player.containerMenu instanceof InscriptionTableMenu menu){
            menu.updateSpellAndCondition(spell.orElse(null), condition.orElse(null));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
