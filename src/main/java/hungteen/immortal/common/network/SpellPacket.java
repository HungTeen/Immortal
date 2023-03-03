package hungteen.immortal.common.network;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.immortal.common.SpellManager;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.utils.PlayerUtil;
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
     * spell only empty when option is SELECT or Next or ACTIVATE_AT.
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
            ctx.get().enqueueWork(()->{
                // S -> C.
                if(ctx.get().getDirection().getOriginationSide() == LogicalSide.SERVER){
                    if (message.option == SpellOptions.SELECT) {
                        Optional.ofNullable(PlayerHelper.getClientPlayer()).ifPresent(player -> {
                            PlayerUtil.selectSpell(player, message.num);
                        });
                        return;
                    }
                    ImmortalAPI.get().spellRegistry().ifPresent(l -> {
                        l.getValue(message.type).ifPresent(spell -> {
                            Optional.ofNullable(PlayerHelper.getClientPlayer()).ifPresent(player -> {
                                switch (message.option) {
                                    case LEARN -> PlayerUtil.learnSpell(player, spell, (int) message.num);
                                    case SET -> PlayerUtil.setSpellList(player, (int) message.num, spell);
                                    case REMOVE -> PlayerUtil.removeSpellList(player, (int) message.num, spell);
                                    case ACTIVATE -> PlayerUtil.activateSpell(player, spell, message.num);
                                    case COOLDOWN -> PlayerUtil.cooldownSpell(player, spell, message.num);
                                }
                            });
                        });
                    });
                } else{// C -> S.
                    switch (message.option){
                        case SELECT -> PlayerUtil.selectSpell(ctx.get().getSender(), message.num);
                        case ACTIVATE_AT -> SpellManager.checkActivateSpell(ctx.get().getSender());
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

    public enum SpellOptions {
        LEARN,
        SET,
        REMOVE,
        ACTIVATE,
        COOLDOWN,
        SELECT,
        ACTIVATE_AT
    }

}
