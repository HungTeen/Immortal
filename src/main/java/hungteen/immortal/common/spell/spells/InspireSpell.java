package hungteen.immortal.common.spell.spells;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.immortal.api.EntityBlockResult;
import hungteen.immortal.common.impl.registry.RealmTypes;
import hungteen.immortal.common.spell.SpellTypes;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/6 19:07
 */
public class InspireSpell extends SpellTypes.SpellType {

    private static final Component NO_ROOT_FOR_INSPIRATION = Component.translatable("info.immortal.no_root_for_inspiration");
    private static final Component CLOSE_TO_INSPIRATION = Component.translatable("info.immortal.close_to_inspiration");

    public InspireSpell(SpellTypes.SpellProperties properties) {
        super("inspiration", properties);
    }

    @Override
    public boolean onActivate(LivingEntity owner, EntityBlockResult result, int level) {
        if(owner instanceof Player player){
            final int rootCount = PlayerUtil.getSpiritualRoots(player).size();
            if(rootCount == 0){
                PlayerHelper.sendTipTo(player, NO_ROOT_FOR_INSPIRATION);
            } else {
                //TODO 启灵有点粗糙
                if(RandomHelper.chance(player.getRandom(), 0.9F / rootCount)){
                    PlayerUtil.setRealm(player, RealmTypes.SPIRITUAL_LEVEL_1);
                } else {
                    PlayerHelper.sendTipTo(player, CLOSE_TO_INSPIRATION);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPassiveSpell() {
        return false;
    }
}
