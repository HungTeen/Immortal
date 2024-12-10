package hungteen.imm.common.entity.human.cultivator;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 19:07
 **/
public class RealityPlayer extends PlayerLikeEntity{

    public RealityPlayer(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
        this.setNoAi(true);
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(this.tickCount % 20 == 0){
                this.getPlayerOpt().ifPresent(p -> {
                    // 和玩家的装备保持一致。
                    for(EquipmentSlot slot : EquipmentSlot.values()){
                        this.setItemSlot(slot, p.getItemBySlot(slot));
                    }
                    // 玩家不在精神领域时，会自动消失。
                    if(! p.level().dimension().equals(IMMLevels.SPIRIT_WORLD)){
                        this.discard();
                    }
                });
            }
        }
    }

    @Override
    public Set<QiRootType> getRoots() {
        return this.getPlayerOpt().map(p -> new HashSet<>(PlayerUtil.getRoots(p))).orElse(new HashSet<>());
    }

    @Override
    public RealmType getRealm() {
        return this.getPlayerOpt().map(PlayerUtil::getPlayerRealm).orElse(RealmTypes.MORTALITY);
    }
}
