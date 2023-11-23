package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/2 10:40
 **/
public class RefineBreathElixir extends ElixirItem {

    public RefineBreathElixir() {
        super(ColorHelper.DYE_RED.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            if (PlayerUtil.getPlayerRealmStage(player).canLevelUp()) {
                RealmManager.checkAndAddBreakThroughProgress(player, (float) RandomHelper.getMinMax(player.getRandom(), 0.16F, 0.38F));
            } else {
                RealmManager.checkAndAddBreakThroughProgress(player, 1F);
            }
        }
    }

    @Override
    public Optional<IRealmType> getLowestRealm(ICultivationType cultivationType) {
        return cultivationType == CultivationTypes.SPIRITUAL ? Optional.of(RealmTypes.SPIRITUAL_LEVEL_2) : Optional.empty();
    }

    @Override
    public Optional<IRealmType> getHighestRealm(ICultivationType cultivationType) {
        return getLowestRealm(cultivationType);
    }

    @Override
    public Rarity getElixirRarity() {
        return Rarity.UNCOMMON;
    }
}
