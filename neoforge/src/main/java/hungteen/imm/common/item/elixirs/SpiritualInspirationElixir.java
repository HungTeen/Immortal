package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/1 22:48
 **/
public class SpiritualInspirationElixir extends ElixirItem {

    public SpiritualInspirationElixir() {
        super(ColorHelper.AQUA.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            if (!PlayerUtil.getRoots(player).isEmpty()) {
                CultivationManager.checkAndAddBreakThroughProgress(player, 1F);
            } else {
                PlayerHelper.sendTipTo(player, TipUtil.info("no_root"));
            }
        }
    }

    @Override
    public Optional<RealmType> getLowestRealm(CultivationType cultivationType) {
        return cultivationType == CultivationTypes.NONE ? Optional.of(RealmTypes.MORTALITY) : Optional.empty();
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
