package hungteen.imm.api.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.common.cultivation.SpellTypes;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-03 19:36
 **/
public record Spell(SpellType spell, int level) {

    public static final Codec<Spell> CODEC = RecordCodecBuilder.<Spell>mapCodec(instance -> instance.group(
            SpellTypes.registry().byNameCodec().fieldOf("spell").forGetter(Spell::spell),
            Codec.INT.fieldOf("level").forGetter(Spell::level)
    ).apply(instance, Spell::new)).codec();

    public static Spell create(SpellType spell){
        return create(spell, 1);
    }

    public static Spell create(SpellType spell, int level){
        return new Spell(spell, level);
    }

}
