package hungteen.imm.common.network.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2023-02-25 22:23
 **/
public record EntityElementPacket(int entityId, Element element, boolean robust,
                                  float value) implements PlayToClientPacket {

    public static final CustomPacketPayload.Type<EntityElementPacket> TYPE = new CustomPacketPayload.Type<>(Util.prefix("entity_element"));
    public static final Codec<EntityElementPacket> CODEC = RecordCodecBuilder.<EntityElementPacket>mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("entityId").forGetter(EntityElementPacket::entityId),
            Element.CODEC.fieldOf("element").forGetter(EntityElementPacket::element),
            Codec.BOOL.fieldOf("robust").forGetter(EntityElementPacket::robust),
            Codec.FLOAT.fieldOf("value").forGetter(EntityElementPacket::value)
    ).apply(instance, EntityElementPacket::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityElementPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    @Override
    public void process(ClientPacketContext clientPacketContext) {
        Entity entity = clientPacketContext.player().level().getEntity(entityId);
        if (entity != null) {
            ElementManager.setElementAmount(entity, element, robust, value);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

//    public static class Handler {
//
//        /**
//         * Only Server sync to Client.
//         */
//        public static void onMessage(EntityElementPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(() -> {
//                PlayerHelper.getClientPlayer().map(Entity::level).ifPresent(level -> {
//                    final Entity entity = level.getEntity(message.entityId);
//                    final Elements element = Elements.valueOf(message.type);
//                    if(entity != null){
//                        ElementManager.setElementAmount(entity, element, message.robust, message.value);
//                    }
//                });
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }
}
