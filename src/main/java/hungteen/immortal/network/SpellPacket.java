package hungteen.immortal.network;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.impl.Spells;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
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

    public SpellPacket(ISpell spell, SpellOptions option, long num) {
        this.type = spell.getName();
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
                getSpellByName(message.type).ifPresent(spell -> {
                    Optional.ofNullable(hungteen.htlib.util.PlayerUtil.getClientPlayer()).ifPresent(player -> {
                        switch (message.option){
                            case LEARN -> PlayerUtil.learnSpell(player, spell);
                            case FORGET -> PlayerUtil.forgetSpell(player, spell);
                            case SET -> PlayerUtil.setSpellList(player, (int) message.num, spell);
                            case REMOVE -> PlayerUtil.removeSpellList(player, (int) message.num, spell);
                            case ACTIVATE -> PlayerUtil.activateSpell(player, spell, message.num);
                        }
                    });

                });
            });
            ctx.get().setPacketHandled(true);
        }
    }

    private static Optional<ISpell> getSpellByName(String name) {
        final List<ISpell> spells = ImmortalAPI.get().getSpells();
        for(int i = 0; i < spells.size(); ++ i){
            if(spells.get(i).getName().equals(name)){
                return Optional.ofNullable(spells.get(i));
            }
        }
        return Optional.empty();
    }

    public enum SpellOptions {
        LEARN,
        FORGET,
        SET,
        REMOVE,
        ACTIVATE,
    }

}
