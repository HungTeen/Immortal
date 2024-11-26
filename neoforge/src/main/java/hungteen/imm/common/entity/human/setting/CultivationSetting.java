package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.QiRootTypes;

import java.util.List;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-13 22:07
 **/
public record CultivationSetting(Optional<RealmType> realm, List<QiRootType> roots) {

    public static final Codec<CultivationSetting> CODEC = RecordCodecBuilder.<CultivationSetting>mapCodec(instance -> instance.group(
            Codec.optionalField("realm", RealmTypes.registry().byNameCodec(), true).forGetter(CultivationSetting::realm),
            QiRootTypes.registry().byNameCodec().listOf().fieldOf("roots").forGetter(CultivationSetting::roots)
    ).apply(instance, CultivationSetting::new)).codec();

}
