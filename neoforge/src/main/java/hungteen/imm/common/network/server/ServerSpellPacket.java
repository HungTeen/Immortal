package hungteen.imm.common.network.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ServerPacketContext;
import hungteen.htlib.common.network.packet.PlayToServerPacket;
import hungteen.imm.IMMConfigs;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-29 22:46
 **/
public record ServerSpellPacket(Optional<SpellType> spell, SpellOption option, long num) implements PlayToServerPacket {

    public static final Type<ServerSpellPacket> TYPE = new Type<>(Util.prefix("server_spell"));
    public static final Codec<ServerSpellPacket> CODEC = RecordCodecBuilder.<ServerSpellPacket>mapCodec(instance -> instance.group(
            Codec.optionalField("spell", SpellTypes.registry().byNameCodec(), true).forGetter(ServerSpellPacket::spell),
            SpellOption.CODEC.fieldOf("option").forGetter(ServerSpellPacket::option),
            Codec.LONG.fieldOf("num").forGetter(ServerSpellPacket::num)
    ).apply(instance, ServerSpellPacket::new)).codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerSpellPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public ServerSpellPacket(SpellOption option) {
        this(option, 0);
    }

    public ServerSpellPacket(SpellOption option, long num) {
        this(Optional.empty(), option, num);
    }

    public ServerSpellPacket(SpellType spell, SpellOption option, long num) {
        this(Optional.of(spell), option, num);
    }

    @Override
    public void process(ServerPacketContext serverPacketContext) {
        ServerPlayer player = serverPacketContext.player();
        switch (option()) {
            case SELECT_SPELL -> SpellManager.selectSpellOnCircle(player, (int) num());
            case SYNC_CIRCLE_OP -> PlayerUtil.setSpellCircleMode(player, IMMConfigs.defaultSpellCircle() ? 1 : 2);
            case CHANGE_CIRCLE_MODE -> PlayerUtil.setSpellCircleMode(player, (int) num());
            case SET_SPELL_ON_CIRCLE -> {
                spell().ifPresent(spell -> PlayerUtil.setSpellAt(player, (int) num(), spell));
            }
            case REMOVE_SPELL_ON_CIRCLE -> {
                PlayerUtil.removeSpellAt(player, (int) num());
            }
            case ACTIVATE -> {
                SpellManager.activateSpell(player);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum SpellOption implements StringRepresentable {

        /**
         * 把法术设置在轮盘上(客户端 & 服务端）。
         */
        SET_SPELL_ON_CIRCLE,

        REMOVE_SPELL_ON_CIRCLE,
        
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
