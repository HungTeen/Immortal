package hungteen.imm.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.ImmortalConfigs;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.impl.registry.PlayerRangeNumbers;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
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

    /**
     * spell only empty when option is CLEAR_SET.
     */
    public SpellPacket(ISpellType spell, SpellOptions option, long num) {
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
                    // No spell operation must run first !
                    if (message.option == SpellOptions.CLEAR_SET) {
                        Optional.ofNullable(PlayerHelper.getClientPlayer()).ifPresent(PlayerUtil::clearSpellSet);
                        return;
                    }
                    SpellTypes.registry().getValue(message.type).ifPresent(spell -> {
                        Optional.ofNullable(PlayerHelper.getClientPlayer()).ifPresent(player -> {
                            switch (message.option) {
                                case LEARN -> PlayerUtil.learnSpell(player, spell, (int) message.num);
                                case SET_POS_ON_CIRCLE -> PlayerUtil.setSpellList(player, (int) message.num, spell);
                                case REMOVE_POS_ON_CIRCLE -> PlayerUtil.removeSpellList(player, (int) message.num, spell);
                                case COOL_DOWN -> PlayerUtil.cooldownSpell(player, spell, message.num);
                                case ADD_SET -> PlayerUtil.addSpellSet(player, spell);
                                case REMOVE_SET -> PlayerUtil.removeSpellSet(player, spell);
                            }
                        });
                    });
                } else {// C -> S.
                    switch (message.option) {
                        case ACTIVATE -> SpellManager.checkActivateSpell(ctx.get().getSender(), (int) message.num);
                        case SYNC_CIRCLE_OP -> PlayerUtil.setIntegerData(ctx.get().getSender(), PlayerRangeNumbers.DEFAULT_SPELL_CIRCLE, ImmortalConfigs.defaultSpellCircle() ? 1 : 2);
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
         * 发法术设置在轮盘上。
         */
        SET_POS_ON_CIRCLE,

        /**
         * 将法术移除出轮盘。
         */
        REMOVE_POS_ON_CIRCLE,

        /**
         * 法术的CD时间。
         */
        COOL_DOWN,

        /**
         * 客户端按键触发后， 发送给服务端。
         */
        ACTIVATE,

        /**
         * 添加到常驻法术。
         */
        ADD_SET,

        /**
         * 从常驻法术移除。
         */
        REMOVE_SET,

        /**
         * 清空常驻法术。
         */
        CLEAR_SET,

        /**
         * 更新法术操作方式在客户端的配置文件。
         */
        SYNC_CIRCLE_OP
    }

}
