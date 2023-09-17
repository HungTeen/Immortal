package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-13 22:07
 **/
public record CultivationSetting(Optional<IRealmType> realm, List<ISpiritualType> roots) {

    public static final Codec<CultivationSetting> CODEC = RecordCodecBuilder.<CultivationSetting>mapCodec(instance -> instance.group(
            Codec.optionalField("realm", RealmTypes.registry().byNameCodec()).forGetter(CultivationSetting::realm),
            SpiritualTypes.registry().byNameCodec().listOf().fieldOf("roots").forGetter(CultivationSetting::roots)
    ).apply(instance, CultivationSetting::new)).codec();

}
