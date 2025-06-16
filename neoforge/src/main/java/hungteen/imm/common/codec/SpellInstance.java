package hungteen.imm.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.TriggerCondition;
import hungteen.imm.common.cultivation.TriggerConditions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 10:44
 **/
public record SpellInstance(Spell spell, TriggerCondition condition) {

    public static final Codec<SpellInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Spell.CODEC.fieldOf("spell").forGetter(SpellInstance::spell),
            TriggerConditions.registry().byNameCodec().fieldOf("condition").forGetter(SpellInstance::condition)
    ).apply(instance, SpellInstance::new));
    public static final Codec<List<SpellInstance>> CODEC_LIST = CODEC.listOf();
    public static final StreamCodec<RegistryFriendlyByteBuf, List<SpellInstance>> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC_LIST);

}
