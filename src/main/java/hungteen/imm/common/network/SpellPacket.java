package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.IMMConfigs;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 22:46
 **/
public class SpellPacket {

    private final String type;
    private final SpellOptions option;
    private final long num;

    public SpellPacket(SpellOptions option) {
        this(option, 0);
    }

    public SpellPacket(SpellOptions option, long num) {
        this(null, option, num);
    }

    /**
     * spell only empty when option is CLEAR_SET.
     */
    public SpellPacket(@Nullable ISpellType spell, SpellOptions option, long num) {
        this.type = spell != null ? spell.getRegistryName() : "empty";
        this.option = option;
        this.num = num;
    }

    public SpellPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readUtf();
        this.option = SpellOptions.values()[buffer.readInt()];
        this.num = buffer.readLong();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.type);
        buffer.writeInt(this.option.ordinal());
        buffer.writeLong(this.num);
    }

    public static class Handler {
        public static void onMessage(SpellPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                // S -> C.
                if (ctx.get().getDirection().getOriginationSide() == LogicalSide.SERVER) {
                    SpellTypes.registry().getValue(message.type).ifPresent(spell -> {
                        PlayerHelper.getClientPlayer().ifPresent(player -> {
                            switch (message.option) {
                                case LEARN -> PlayerUtil.learnSpell(player, spell, (int) message.num);
                                case SET_SPELL_ON_CIRCLE -> PlayerUtil.setSpellAt(player, (int) message.num, spell);
                                case REMOVE_SPELL_ON_CIRCLE -> PlayerUtil.removeSpellAt(player, (int) message.num);
                                case COOL_DOWN -> PlayerUtil.cooldownSpell(player, spell, message.num);
                            }
                        });
                    });
                } else {// C -> S.
                    switch (message.option) {
                        case SELECT_SPELL -> SpellManager.selectSpellOnCircle(ctx.get().getSender(), (int) message.num);
                        case SYNC_CIRCLE_OP -> PlayerUtil.setIntegerData(ctx.get().getSender(), PlayerRangeIntegers.SPELL_CIRCLE_MODE, IMMConfigs.defaultSpellCircle() ? 1 : 2);
                        case CHANGE_CIRCLE_MODE -> PlayerUtil.setSpellCircleMode(ctx.get().getSender(), (int) message.num);
                        case SET_SPELL_ON_CIRCLE -> {
                            SpellTypes.registry().getValue(message.type).ifPresent(spell -> {
                                PlayerUtil.setSpellAt(ctx.get().getSender(), (int) message.num, spell);
                            });
                        }
                        case ACTIVATE -> {
                            SpellManager.activateSpell(ctx.get().getSender());
                        }
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public enum SpellOptions {

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
    }

}
