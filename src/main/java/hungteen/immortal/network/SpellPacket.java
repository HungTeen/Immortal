package hungteen.immortal.network;

import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.impl.Spells;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
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

    /**
     * spell only empty when option is SELECT or Next or ACTIVATE_AT.
     */
    public SpellPacket(ISpell spell, SpellOptions option, long num) {
        this.type = spell != null ? spell.getName() : "empty";
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
                if(ctx.get().getDirection().getOriginationSide() == LogicalSide.SERVER){
                    if (message.option == SpellOptions.SELECT || message.option == SpellOptions.NEXT) {
                        Optional.ofNullable(hungteen.htlib.util.PlayerUtil.getClientPlayer()).ifPresent(player -> {
                            switch (message.option) {
                                case SELECT -> PlayerUtil.selectSpell(player, message.num);
                                case NEXT -> PlayerUtil.nextSpell(player, message.num);
                                case ACTIVATE_AT -> PlayerUtil.activateSpellAt(player);
                            }
                        });
                        return;
                    }
                    getSpellByName(message.type).ifPresent(spell -> {
                        Optional.ofNullable(hungteen.htlib.util.PlayerUtil.getClientPlayer()).ifPresent(player -> {
                            switch (message.option){
                                case LEARN -> PlayerUtil.learnSpell(player, spell, (int) message.num);
                                case FORGET -> PlayerUtil.forgetSpell(player, spell);
                                case SET -> PlayerUtil.setSpellList(player, (int) message.num, spell);
                                case REMOVE -> PlayerUtil.removeSpellList(player, (int) message.num, spell);
                                case ACTIVATE -> PlayerUtil.activateSpell(player, spell, message.num);
                            }
                        });

                    });
                } else{
                    switch (message.option){
                        case SELECT -> PlayerUtil.selectSpell(ctx.get().getSender(), message.num);
                        case NEXT -> PlayerUtil.nextSpell(ctx.get().getSender(), message.num);
                        case ACTIVATE_AT -> PlayerUtil.activateSpellAt(ctx.get().getSender());
                    }
                }
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
        SELECT,
        NEXT,
        ACTIVATE_AT
    }

}
