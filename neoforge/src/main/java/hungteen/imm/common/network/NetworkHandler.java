package hungteen.imm.common.network;

import hungteen.htlib.util.NeoHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.network.client.*;
import hungteen.imm.common.network.server.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-29 22:14
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                QiRootAndRealmPacket.TYPE,
                QiRootAndRealmPacket.STREAM_CODEC,
                NeoHelper.wrapClientHandler(QiRootAndRealmPacket::process)
        );
        registrar.playToClient(
                ExperienceChangePacket.TYPE,
                ExperienceChangePacket.STREAM_CODEC,
                NeoHelper.wrapClientHandler(ExperienceChangePacket::process)
        );
        registrar.playToClient(
                FloatDataPacket.TYPE,
                FloatDataPacket.STREAM_CODEC,
                NeoHelper.wrapClientHandler(FloatDataPacket::process)
        );
        registrar.playToClient(
                IntegerDataPacket.TYPE,
                IntegerDataPacket.STREAM_CODEC,
                NeoHelper.wrapClientHandler(IntegerDataPacket::process)
        );
        registrar.playToClient(
                ClientSpellPacket.TYPE,
                ClientSpellPacket.STREAM_CODEC,
                NeoHelper.wrapClientHandler(ClientSpellPacket::process)
        );
        registrar.playToClient(
                EntityElementPacket.TYPE,
                EntityElementPacket.STREAM_CODEC,
                NeoHelper.wrapClientHandler(EntityElementPacket::process)
        );
        registrar.playToClient(
                MiscDataPacket.TYPE,
                MiscDataPacket.STREAM_CODEC,
                NeoHelper.wrapClientHandler(MiscDataPacket::process)
        );

        registrar.playToServer(
                ScreenOperationPacket.TYPE,
                ScreenOperationPacket.STREAM_CODEC,
                NeoHelper.wrapServerHandler(ScreenOperationPacket::process)
        );
        registrar.playToServer(
                ServerSpellPacket.TYPE,
                ServerSpellPacket.STREAM_CODEC,
                NeoHelper.wrapServerHandler(ServerSpellPacket::process)
        );
        registrar.playToServer(
                ServerSelectQiRootPacket.TYPE,
                ServerSelectQiRootPacket.STREAM_CODEC,
                NeoHelper.wrapServerHandler(ServerSelectQiRootPacket::process)
        );
        registrar.playToServer(
                ServerManualPacket.TYPE,
                ServerManualPacket.STREAM_CODEC,
                NeoHelper.wrapServerHandler(ServerManualPacket::process)
        );
        registrar.playToServer(
                ServerInscriptionPacket.TYPE,
                ServerInscriptionPacket.STREAM_CODEC,
                NeoHelper.wrapServerHandler(ServerInscriptionPacket::process)
        );
//        registrar.playToServer(
//                EmptyClickPacket.TYPE,
//                EmptyClickPacket.STREAM_CODEC,
//                NeoHelper.wrapServerHandler(EmptyClickPacket::process)
//        );
    }

    public static void init() {
//        CHANNEL.registerMessage(getId(), MiscDataPacket.class, MiscDataPacket::encode, MiscDataPacket::new, MiscDataPacket.Handler::onMessage);
//        CHANNEL.registerMessage(getId(), TradeOffersPacket.class, TradeOffersPacket::encode, TradeOffersPacket::new, TradeOffersPacket.Handler::onMessage);
//        CHANNEL.registerMessage(getId(), SectRelationPacket.class, SectRelationPacket::encode, SectRelationPacket::new, SectRelationPacket.Handler::onMessage);
//        CHANNEL.registerMessage(getId(), EntityElementPacket.class, EntityElementPacket::encode, EntityElementPacket::new, EntityElementPacket.Handler::onMessage);
//        CHANNEL.registerMessage(getId(), ReactionPacket.class, ReactionPacket::encode, ReactionPacket::new, ReactionPacket.Handler::onMessage);
//        CHANNEL.registerMessage(getId(), SmithingPacket.class, SmithingPacket::encode, SmithingPacket::new, SmithingPacket.Handler::onMessage);

    }

}
