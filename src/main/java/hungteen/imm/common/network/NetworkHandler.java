package hungteen.imm.common.network;

import hungteen.imm.util.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 22:14
 **/
public class NetworkHandler {

    private static int id = 0;

    private static SimpleChannel CHANNEL;

    public static void init() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(Util.prefix("networking"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        CHANNEL.registerMessage(getId(), PlaySoundPacket.class, PlaySoundPacket::encode, PlaySoundPacket::new, PlaySoundPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), SpellPacket.class, SpellPacket::encode, SpellPacket::new, SpellPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), EmptyClickPacket.class, EmptyClickPacket::encode, EmptyClickPacket::new, EmptyClickPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), IntegerDataPacket.class, IntegerDataPacket::encode, IntegerDataPacket::new, IntegerDataPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), FloatDataPacket.class, FloatDataPacket::encode, FloatDataPacket::new, FloatDataPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), MiscDataPacket.class, MiscDataPacket::encode, MiscDataPacket::new, MiscDataPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), TradeOffersPacket.class, TradeOffersPacket::encode, TradeOffersPacket::new, TradeOffersPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), SectRelationPacket.class, SectRelationPacket::encode, SectRelationPacket::new, SectRelationPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), EntityElementPacket.class, EntityElementPacket::encode, EntityElementPacket::new, EntityElementPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), ReactionPacket.class, ReactionPacket::encode, ReactionPacket::new, ReactionPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), SmithingPacket.class, SmithingPacket::encode, SmithingPacket::new, SmithingPacket.Handler::onMessage);
        CHANNEL.registerMessage(getId(), ScreenButtonPacket.class, ScreenButtonPacket::encode, ScreenButtonPacket::new, ScreenButtonPacket.Handler::onMessage);

    }

    public static <MSG> void sendToServer(MSG msg){
        CHANNEL.sendToServer(msg);
    }

    public static <MSG> void sendToClient(ServerPlayer serverPlayer, MSG msg){
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
    }

    public static <MSG> void sendToClientEntity(Entity entity, MSG msg){
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    public static <MSG> void sendToClientEntityAndSelf(Entity entity, MSG msg){
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }

    public static <MSG> void sendToNearByClient(Level world, Vec3 vec, double dis, MSG msg){
        CHANNEL.send(PacketDistributor.NEAR.with(() -> {
            return new PacketDistributor.TargetPoint(vec.x, vec.y, vec.z, dis, world.dimension());
        }), msg);
    }

    private static int getId(){
        return id ++;
    }

}
