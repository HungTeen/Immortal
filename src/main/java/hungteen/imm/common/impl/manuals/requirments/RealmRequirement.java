package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/17 15:16
 */
public record RealmRequirement(IRealmType realmType, boolean lowest) implements ILearnRequirement {

    public static final Codec<RealmRequirement> CODEC = RecordCodecBuilder.<RealmRequirement>mapCodec(instance -> instance.group(
            RealmTypes.registry().byNameCodec().fieldOf("realm_type").forGetter(RealmRequirement::realmType),
            Codec.BOOL.optionalFieldOf("lowest", true).forGetter(RealmRequirement::lowest)
    ).apply(instance, RealmRequirement::new)).codec();

    @Override
    public boolean check(Level level, Player player) {
        final int realm = PlayerUtil.getPlayerRealm(player).getRealmValue();
        return lowest() ? (realm >= realmType().getRealmValue()) : (realm <= realmType().getRealmValue());
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo() {
        return TipUtil.misc("requirement." + (lowest() ? "lowest" : "highest"), realmType().getComponent().toString());
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.REALM;
    }
}
