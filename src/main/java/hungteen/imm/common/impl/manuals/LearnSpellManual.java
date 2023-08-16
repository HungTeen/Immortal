package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.IManualType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:33
 */
public record LearnSpellManual(ISpellType spellType, int level) implements IManualContent {
    
    public static final Codec<LearnSpellManual> CODEC = RecordCodecBuilder.<LearnSpellManual>mapCodec(instance -> instance.group(
            SpellTypes.registry().byNameCodec().fieldOf("spell").forGetter(LearnSpellManual::spellType),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("level").forGetter(LearnSpellManual::level)
    ).apply(instance, LearnSpellManual::new)).codec();
    
    
    @Override
    public void learn(Player player) {
        PlayerUtil.learnSpell(player, spellType(), level());
    }

    @Override
    public List<Component> getInfo() {
        return List.of(spellType().getSpellDesc(level()));
    }

    @Override
    public IManualType<?> getType() {
        return ManualTypes.LEARN_SPELL;
    }
}
