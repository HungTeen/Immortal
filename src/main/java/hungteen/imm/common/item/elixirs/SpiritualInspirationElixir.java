package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/1 22:48
 **/
public class SpiritualInspirationElixir extends ElixirItem {

    public SpiritualInspirationElixir() {
        super(ColorHelper.AQUA.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            if (PlayerUtil.getSpiritualRoots(player).size() > 0) {
                PlayerUtil.addFloatData(player, PlayerRangeFloats.BREAK_THROUGH_PROGRESS, 1F);
            } else {
                PlayerHelper.sendTipTo(player, TipUtil.info("no_root"));
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
        return Rarity.UNCOMMON;
    }

}
