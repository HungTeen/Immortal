package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.ILearnRequirement;
import hungteen.imm.api.spell.IRequirementType;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/17 15:16
 */
public record RealmRequirement(RealmType realmType, boolean lowest) implements ILearnRequirement {

    public static final MapCodec<RealmRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RealmTypes.registry().byNameCodec().fieldOf("realm_type").forGetter(RealmRequirement::realmType),
            Codec.BOOL.optionalFieldOf("lowest", true).forGetter(RealmRequirement::lowest)
    ).apply(instance, RealmRequirement::new));

    public static ILearnRequirement create(RealmType type, boolean lowest){
        return new RealmRequirement(type, lowest);
    }

    @Override
    public boolean check(Level level, Player player) {
        final int realm = PlayerUtil.getPlayerRealm(player).getRealmValue();
        return lowest() ? (realm >= realmType().getRealmValue()) : (realm <= realmType().getRealmValue());
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        return TipUtil.manual("requirement." + (lowest() ? "lowest" : "highest"), realmType().getComponent());
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.REALM;
    }
}
