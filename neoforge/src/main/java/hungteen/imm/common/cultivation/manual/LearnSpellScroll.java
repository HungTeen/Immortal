package hungteen.imm.common.cultivation.manual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.spell.ScrollContent;
import hungteen.imm.api.spell.ScrollType;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.impl.ScrollTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:33
 */
public record LearnSpellScroll(SpellType spellType, int level) implements ScrollContent {
    
    public static final MapCodec<LearnSpellScroll> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SpellTypes.registry().byNameCodec().fieldOf("spell").forGetter(LearnSpellScroll::spellType),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("level").forGetter(LearnSpellScroll::level)
    ).apply(instance, LearnSpellScroll::new));

    @Override
    public boolean canLearn(Level level, Player player) {
        if(PlayerUtil.hasLearnedSpell(player, spellType(), level())){
//            PlayerHelper.sendTipTo(player, TipUtil.info("already_learned_spell"));
            return false;
        }
        return true;
    }

    @Override
    public void learn(Player player) {
        PlayerUtil.learnSpell(player, spellType(), level());
    }

    @Override
    public MutableComponent getInfo() {
        return spellType().getSpellDesc(level());
    }

    @Override
    public ScrollType<?> getType() {
        return ScrollTypes.LEARN_SPELL;
    }

    @Override
    public ResourceLocation getIcon() {
        return spellType().getSpellTexture();
    }
}
