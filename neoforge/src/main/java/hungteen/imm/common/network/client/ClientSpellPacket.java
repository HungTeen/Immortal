package hungteen.imm.common.network.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.network.ClientPacketContext;
import hungteen.htlib.common.network.packet.PlayToClientPacket;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-29 22:46
 **/
public record ClientSpellPacket(SpellType spell, SpellOption option, long num) implements PlayToClientPacket {

    public static final Type<ClientSpellPacket> TYPE = new Type<>(Util.prefix("client_spell"));
    public static final Codec<ClientSpellPacket> CODEC = RecordCodecBuilder.<ClientSpellPacket>mapCodec(instance -> instance.group(
            SpellTypes.registry().byNameCodec().fieldOf("spell").forGetter(ClientSpellPacket::spell),
            SpellOption.CODEC.fieldOf("option").forGetter(ClientSpellPacket::option),
            Codec.LONG.fieldOf("num").forGetter(ClientSpellPacket::num)
    ).apply(instance, ClientSpellPacket::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientSpellPacket> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public ClientSpellPacket(SpellOption option){
        this(SpellTypes.BURNING, option, 0);
    }

    @Override
    public void process(ClientPacketContext clientPacketContext) {
        Player player = clientPacketContext.player();
        switch (option()) {
            case LEARN -> PlayerUtil.learnSpell(player, spell(), (int) num());
            case SET_SPELL_ON_CIRCLE -> PlayerUtil.setSpellAt(player, (int) num(), spell());
            case REMOVE_SPELL_ON_CIRCLE -> PlayerUtil.removeSpellAt(player, (int) num());
            case COOL_DOWN -> PlayerUtil.cooldownSpell(player, spell(), num());
            case PREPARING_SPELL -> PlayerUtil.setPreparingSpell(player, spell());
            case CLEAR_PREPARING_SPELL -> PlayerUtil.setPreparingSpell(player, null);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

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

        PREPARING_SPELL,

        CLEAR_PREPARING_SPELL,

        ;

        public static final Codec<SpellOption> CODEC = StringRepresentable.fromEnum(SpellOption::values);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

}
