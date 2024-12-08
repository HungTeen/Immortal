package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/7 23:10
 **/
public record HumanPredicateSetting(EntityType<?> type, List<RealmType> matchRealms, List<QiRootType> requireRoots) {

    public static final Codec<HumanPredicateSetting> CODEC = RecordCodecBuilder.<HumanPredicateSetting>mapCodec(instance -> instance.group(
            EntityHelper.get().getCodec().fieldOf("type").forGetter(HumanPredicateSetting::type),
            RealmTypes.registry().byNameCodec().listOf().fieldOf("match_realms").forGetter(HumanPredicateSetting::matchRealms),
            QiRootTypes.registry().byNameCodec().listOf().fieldOf("require_roots").forGetter(HumanPredicateSetting::requireRoots)
    ).apply(instance, HumanPredicateSetting::new)).codec();

    /**
     * 判断是否能和具体的人类匹配。
     */
    public boolean match(HumanLikeEntity human){
        if(human.getType().equals(type)){
            // 匹配境界。
            if (! matchRealms.isEmpty()) {
                if(! matchRealms.contains(human.getRealm())){
                    return false;
                }
            }
            // 匹配灵根。
            for(QiRootType root : requireRoots){
                if(! human.hasRoot(root)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
