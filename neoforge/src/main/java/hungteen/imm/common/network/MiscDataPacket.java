package hungteen.imm.common.network;

import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-26 18:48
 **/
public class MiscDataPacket implements PlayToClientPacket {

    private final int type;
    private final String data;
    private final float value;

    public MiscDataPacket(Types type, String data, float value) {
        this.type = type.ordinal();
        this.data = data;
        this.value = value;
    }

    public MiscDataPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.data = buffer.readUtf();
        this.value = buffer.readFloat();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.type);
        buffer.writeUtf(this.data);
        buffer.writeFloat(this.value);
    }

    @Override
    public void process(ClientPacketContext clientPacketContext) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }

//    public static class Handler {
//
//        /**
//         * Only Server sync to Client.
//         */
//        public static void onMessage(MiscDataPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(()->{
//                PlayerHelper.getClientPlayer().ifPresent(player -> {
//                    final Types type = Types.values()[message.type];
//                    switch (type) {
//                        case ADD_ROOT -> SpiritualTypes.registry().getValue(message.data).ifPresent(l -> PlayerUtil.addRoot(player, l));
//                        case REMOVE_ROOT -> SpiritualTypes.registry().getValue(message.data).ifPresent(l -> PlayerUtil.removeRoot(player, l));
//                        case CLEAR_ROOT -> PlayerUtil.clearSpiritualRoot(player);
//                        case EXPERIENCE -> PlayerUtil.setExperience(player, ExperienceTypes.valueOf(message.data), message.value);
//                        case REALM -> RealmTypes.registry().getValue(message.data).ifPresent(realm -> PlayerUtil.clientSetRealm(player, realm));
//                        case REALM_STAGE -> PlayerUtil.checkAndSetRealmStage(player, RealmStages.valueOf(message.data));
//                        case PREPARING_SPELL -> SpellTypes.registry().getValue(message.data).ifPresent(l -> PlayerUtil.setPreparingSpell(player, l));
//                        case CLEAR_PREPARING_SPELL -> PlayerUtil.setPreparingSpell(player, null);
//                    }
//                });
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }

    public enum Types{

        ADD_ROOT,
        REMOVE_ROOT,
        CLEAR_ROOT,
        EXPERIENCE,
        REALM,
        REALM_STAGE,
        PREPARING_SPELL,
        CLEAR_PREPARING_SPELL,
        ;
    }

}
