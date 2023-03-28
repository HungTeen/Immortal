package hungteen.imm.common.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.SpellTypes;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 15:09
 */
public class SpellTutorials {

    /**
     * 不是全局数据包！
     */
    private static final HTCodecRegistry<SpellTutorial> TUTORIALS = HTRegistryManager.create(SpellTutorial.class, "spell_tutorials", () -> SpellTutorial.CODEC);


    public record SpellTutorial(int treasureWeight, int tradeWeight, List<TutorialEntry> spells) {
        public static final Codec<SpellTutorial> CODEC = RecordCodecBuilder.<SpellTutorial>mapCodec(instance -> instance.group(
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("treasure_weight", 0).forGetter(SpellTutorial::treasureWeight),
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("trade_weight", 0).forGetter(SpellTutorial::tradeWeight),
                TutorialEntry.CODEC.listOf().fieldOf("spells").forGetter(SpellTutorial::spells)
        ).apply(instance, SpellTutorial::new)).codec();
    }

    public record TutorialEntry(ISpellType spellType, int spellLevel, List<Pair<ISpellType, Integer>> preSpells) {
        public static final Codec<TutorialEntry> CODEC = RecordCodecBuilder.<TutorialEntry>mapCodec(instance -> instance.group(
                SpellTypes.registry().byNameCodec().fieldOf("spell_type").forGetter(TutorialEntry::spellType),
                Codec.intRange(0, Integer.MAX_VALUE).fieldOf("spell_level").forGetter(TutorialEntry::spellLevel),
                Codec.mapPair(
                        SpellTypes.registry().byNameCodec().fieldOf("learned_spell"),
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("spell_level")
                ).codec().listOf().optionalFieldOf("require_learned_spells", List.of()).forGetter(TutorialEntry::preSpells)
        ).apply(instance, TutorialEntry::new)).codec();
    }
}
