package hungteen.imm.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.common.world.ElixirManager;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.List;
import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/18 14:54
 */
public record ElixirEffect(Holder<MobEffect> effect, List<Integer> bits) {

    public static final Codec<ElixirEffect> CODEC = RecordCodecBuilder.<ElixirEffect>mapCodec(instance -> instance.group(
            EffectHelper.get().getHolderCodec().fieldOf("effect").forGetter(ElixirEffect::effect),
            Codec.intRange(0, ElixirManager.BITS - 1).listOf().fieldOf("bits").forGetter(ElixirEffect::bits)
    ).apply(instance, ElixirEffect::new)).codec();

    public int maxMatchCnt(Map<Integer, Integer> bins){
        return bits.stream().map(bit -> bins.getOrDefault(bit, 0)).min(Integer::compareTo).orElse(0);
    }
}
