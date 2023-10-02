package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 15:56
 **/
public class FiveFlowersElixir extends ElixirItem {

    public FiveFlowersElixir() {
        super(ColorHelper.LITTLE_YELLOW1.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            final int len = PlayerUtil.getSpiritualRoots(player).size();
            if (len == 0 || player.getRandom().nextFloat() < 0.5F) {
                player.addEffect(EffectHelper.viewEffect(MobEffects.HARM, 10, 1));
            } else {
                PlayerUtil.addFloatData(player, PlayerRangeFloats.BREAK_THROUGH_PROGRESS, player.getRandom().nextFloat() * 0.8F);
            }
        }
    }

    @Override
    public Optional<IRealmType> getLowestRealm(ICultivationType cultivationType) {
        return cultivationType == CultivationTypes.MORTAL ? Optional.of(RealmTypes.MORTALITY) : Optional.empty();
    }

    @Override
    public Optional<IRealmType> getHighestRealm(ICultivationType cultivationType) {
        return getLowestRealm(cultivationType);
    }

    @Override
    public Rarity getElixirRarity() {
        return Rarity.COMMON;
    }
}
