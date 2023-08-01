package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.ExperienceTypes;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-26 18:48
 **/
public class MiscDataPacket {

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

    public static class Handler {

        /**
         * Only Server sync to Client.
         */
        public static void onMessage(MiscDataPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                PlayerHelper.getClientPlayer().ifPresent(player -> {
                    final Types type = Types.values()[message.type];
                    switch (type) {
                        case ADD_ROOT -> SpiritualTypes.registry().getValue(message.data).ifPresent(l -> PlayerUtil.addSpiritualRoot(player, l));
                        case REMOVE_ROOT -> SpiritualTypes.registry().getValue(message.data).ifPresent(l -> PlayerUtil.removeSpiritualRoot(player, l));
                        case CLEAR_ROOT -> PlayerUtil.clearSpiritualRoot(player);
                        case EXPERIENCE -> PlayerUtil.setExperience(player, ExperienceTypes.valueOf(message.data), message.value);
                        case CULTIVATION -> CultivationTypes.registry().getValue(message.data).ifPresent(l -> PlayerUtil.setCultivationType(player, l));
                        case REALM -> RealmTypes.registry().getValue(message.data).ifPresent(realm -> PlayerUtil.checkAndSetRealm(player, realm));
                        case REALM_STAGE -> PlayerUtil.checkAndSetRealmStage(player, RealmStages.valueOf(message.data));
                        case PREPARING_SPELL -> SpellTypes.registry().getValue(message.data).ifPresent(l -> PlayerUtil.setPreparingSpell(player, l));
                        case CLEAR_PREPARING_SPELL -> PlayerUtil.setPreparingSpell(player, null);
                    }
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public enum Types{

        ADD_ROOT,
        REMOVE_ROOT,
        CLEAR_ROOT,
        EXPERIENCE,
        CULTIVATION,
        REALM,
        REALM_STAGE,
        PREPARING_SPELL,
        CLEAR_PREPARING_SPELL,
        ;
    }

}
