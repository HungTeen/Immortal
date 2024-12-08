package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.common.impl.registry.SectTypes;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/8 12:15
 **/
public record HumanSectData(Optional<ISectType> outerSect, Optional<ISectType> innerSect) {

    public static final Codec<HumanSectData> CODEC = RecordCodecBuilder.<HumanSectData>mapCodec(instance -> instance.group(
            Codec.optionalField("outer_sect", SectTypes.registry().byNameCodec(), true).forGetter(HumanSectData::outerSect),
            Codec.optionalField("inner_sect", SectTypes.registry().byNameCodec(), true).forGetter(HumanSectData::innerSect)
    ).apply(instance, HumanSectData::new)).codec();

}
