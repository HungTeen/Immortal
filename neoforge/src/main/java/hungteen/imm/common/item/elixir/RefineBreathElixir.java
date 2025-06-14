package hungteen.imm.common.item.elixir;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.common.cultivation.RealmTypes;
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
public class RefineBreathElixir extends ElixirItem {

    public RefineBreathElixir() {
        super(withRarity(Rarity.UNCOMMON), ColorHelper.DYE_RED.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            if (CultivationManager.canLevelUp(player)) {
                CultivationManager.addBreakThroughProgress(player, (float) RandomHelper.getMinMax(player.getRandom(), 0.16F, 0.38F));
            } else {
                CultivationManager.addBreakThroughProgress(player, 1F);
            }
        }
    }

    @Override
    public Optional<RealmType> getLowestRealm(CultivationType cultivationType) {
        return cultivationType == CultivationTypes.QI ? Optional.of(RealmTypes.FOUNDATION.pre()) : Optional.empty();
    }

    @Override
    public Optional<RealmType> getHighestRealm(CultivationType cultivationType) {
        return getLowestRealm(cultivationType);
    }
}
