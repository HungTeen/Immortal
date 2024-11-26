package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/2 10:40
 **/
public class GatherBreathElixir extends ElixirItem {

    public GatherBreathElixir() {
        super(ColorHelper.DYE_BROWN.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            if (PlayerUtil.getPlayerRealmStage(player).canLevelUp()) {
                CultivationManager.checkAndAddBreakThroughProgress(player, (float) RandomHelper.getMinMax(player.getRandom(), 0.35F, 0.55F));
            } else {
                CultivationManager.checkAndAddBreakThroughProgress(player, 1F);
            }
        }
    }

    @Override
    public Optional<RealmType> getLowestRealm(CultivationType cultivationType) {
        return cultivationType == CultivationTypes.SPIRITUAL ? Optional.of(RealmTypes.QI_REFINING) : Optional.empty();
    }

    @Override
    public Optional<RealmType> getHighestRealm(CultivationType cultivationType) {
        return getLowestRealm(cultivationType);
    }

    @Override
    public Rarity getElixirRarity() {
        return Rarity.UNCOMMON;
    }
}
