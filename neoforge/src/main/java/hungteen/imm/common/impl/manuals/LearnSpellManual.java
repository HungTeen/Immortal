package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.IManualType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:33
 */
public record LearnSpellManual(ISpellType spellType, int level) implements IManualContent {
    
    public static final MapCodec<LearnSpellManual> CODEC = RecordCodecBuilder.<LearnSpellManual>mapCodec(instance -> instance.group(
            SpellTypes.registry().byNameCodec().fieldOf("spell").forGetter(LearnSpellManual::spellType),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("level").forGetter(LearnSpellManual::level)
    ).apply(instance, LearnSpellManual::new));

    @Override
    public boolean canLearn(Level level, Player player) {
        if(PlayerUtil.hasLearnedSpell(player, spellType(), level())){
            PlayerHelper.sendTipTo(player, TipUtil.info("already_learned_spell"));
            return false;
        }
        return true;
    }

    @Override
    public void learn(Player player) {
        PlayerUtil.learnSpell(player, spellType(), level());
    }

    @Override
    public MutableComponent getManualTitle() {
        return SpellManager.spellName(spellType(), level());
    }

    @Override
    public MutableComponent getInfo() {
        return spellType().getSpellDesc(level());
    }

    @Override
    public IManualType<?> getType() {
        return ManualTypes.LEARN_SPELL;
    }

    @Override
    public ResourceLocation getTexture() {
        return spellType().getSpellTexture();
    }
}
