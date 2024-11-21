package hungteen.imm.common.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-29 22:46
 **/
public record SpellPacket(Optional<ISpellType> spell, SpellOption option, long num) implements PlayToClientPacket {

    public static final Type<SpellPacket> TYPE = new Type<>(Util.prefix("spell"));
    public static final Codec<SpellPacket> CODEC = RecordCodecBuilder.<SpellPacket>mapCodec(instance -> instance.group(
            Codec.optionalField("spell", SpellTypes.registry().byNameCodec(), true).forGetter(SpellPacket::spell),
            SpellOption.CODEC.fieldOf("option").forGetter(SpellPacket::option),
            Codec.LONG.fieldOf("num").forGetter(SpellPacket::num)
    ).apply(instance, SpellPacket::new)).codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public SpellPacket(SpellOption option) {
        this(option, 0);
    }

    public SpellPacket(SpellOption option, long num) {
        this(Optional.empty(), option, num);
    }

    public SpellPacket(ISpellType spell, SpellOption option, long num) {
        this(Optional.of(spell), option, num);
    }

    @Override
    public void process(ClientPacketContext clientPacketContext) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

//    public static class Handler {
//        public static void onMessage(SpellPacket message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(() -> {
//                // S -> C.
//                if (ctx.get().getDirection().getOriginationSide() == LogicalSide.SERVER) {
//                    SpellTypes.registry().getValue(message.type).ifPresent(spell -> {
//                        PlayerHelper.getClientPlayer().ifPresent(player -> {
//                            switch (message.option) {
//                                case LEARN -> PlayerUtil.learnSpell(player, spell, (int) message.num);
//                                case SET_SPELL_ON_CIRCLE -> PlayerUtil.setSpellAt(player, (int) message.num, spell);
//                                case REMOVE_SPELL_ON_CIRCLE -> PlayerUtil.removeSpellAt(player, (int) message.num);
//                                case COOL_DOWN -> PlayerUtil.cooldownSpell(player, spell, message.num);
//                            }
//                        });
//                    });
//                } else {// C -> S.
//                    switch (message.option) {
//                        case SELECT_SPELL -> SpellManager.selectSpellOnCircle(ctx.get().getSender(), (int) message.num);
//                        case SYNC_CIRCLE_OP -> PlayerUtil.setIntegerData(ctx.get().getSender(), PlayerRangeIntegers.SPELL_CIRCLE_MODE, IMMConfigs.defaultSpellCircle() ? 1 : 2);
//                        case CHANGE_CIRCLE_MODE -> PlayerUtil.setSpellCircleMode(ctx.get().getSender(), (int) message.num);
//                        case SET_SPELL_ON_CIRCLE -> {
//                            SpellTypes.registry().getValue(message.type).ifPresent(spell -> {
//                                PlayerUtil.setSpellAt(ctx.get().getSender(), (int) message.num, spell);
//                            });
//                        }
//                        case ACTIVATE -> {
//                            SpellManager.activateSpell(ctx.get().getSender());
//                        }
//                    }
//                }
//            });
//            ctx.get().setPacketHandled(true);
//        }
//    }

    public enum SpellOption implements StringRepresentable {

        /**
         * 学习法术，设置学习等级，若为0则没学。
         */
        LEARN,

        /**
         * 把法术设置在轮盘上(客户端 & 服务端）。
         */
        SET_SPELL_ON_CIRCLE,

        /**
         * 将法术移除出轮盘。
         */
        REMOVE_SPELL_ON_CIRCLE,

        /**
         * 法术的CD时间。
         */
        COOL_DOWN,

        /**
         * 选择轮盘上的法术，客户端按键触发后，发送给服务端。
         */
        SELECT_SPELL,

        /**
         * 更新法术操作方式在客户端的配置文件。
         */
        SYNC_CIRCLE_OP,

        /**
         * 改变轮盘操作方式。
         */
        CHANGE_CIRCLE_MODE,

        /**
         * 客户端触发法术。
         */
        ACTIVATE,

        ;

        public static final Codec<SpellOption> CODEC = StringRepresentable.fromEnum(SpellOption::values);

        @Override
        public String getSerializedName() {
            return name();
        }
    }

}
