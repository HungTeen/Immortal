package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-01 15:56
 **/
public class FiveFlowersElixir extends ElixirItem {

    public FiveFlowersElixir() {
        super(ColorHelper.LITTLE_YELLOW1.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            final int len = PlayerUtil.getRoots(player).size();
            if (len == 0 || player.getRandom().nextFloat() < 0.5F) {
                player.addEffect(EffectHelper.viewEffect(MobEffects.HARM, 10, 1));
            } else {
                CultivationManager.checkAndAddBreakThroughProgress(player, player.getRandom().nextFloat() * 0.8F);
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
        return Rarity.COMMON;
    }
}
