package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.SimpleWeightedList;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.common.cultivation.SpellTypes;
import net.minecraft.util.RandomSource;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/8 12:24
 **/
public record SpellSetting(SimpleWeightedList<Spell> spells) {

    public static final Codec<Spell> SPELL_CODEC = RecordCodecBuilder.<Spell>mapCodec(instance -> instance.group(
            SpellTypes.registry().byNameCodec().fieldOf("spell").forGetter(Spell::spell),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("level", 1).forGetter(Spell::level)
    ).apply(instance, Spell::new)).codec();

    public static final Codec<SpellSetting> CODEC = RecordCodecBuilder.<SpellSetting>mapCodec(instance -> instance.group(
            SimpleWeightedList.wrappedCodec(SPELL_CODEC).fieldOf("spells").forGetter(SpellSetting::spells)
    ).apply(instance, SpellSetting::new)).codec();

    public Optional<Spell> getSpell(RandomSource random){
        return spells().getItem(random);
    }

}
