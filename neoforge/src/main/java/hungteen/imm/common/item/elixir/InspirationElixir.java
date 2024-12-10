package hungteen.imm.common.item.elixir;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmLevel;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.RealmManager;
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
 * @create 2023/10/1 22:48
 **/
public class InspirationElixir extends ElixirItem {

    public InspirationElixir() {
        super(withRarity(Rarity.UNCOMMON), ColorHelper.AQUA.rgb());
    }

    /**
     * 启灵丸，帮助凡人突破到炼气期。
     */
    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (EntityHelper.isServer(livingEntity) && livingEntity instanceof Player player) {
            if(RealmManager.getRealm(livingEntity).getRealmLevel() == RealmLevel.MORTALITY){
                CultivationManager.addBreakThroughProgress(player, 1F);
            } else {
                CultivationManager.addElixir(player, 1);
            }
        }
    }

    @Override
    public Optional<RealmType> getLowestRealm(CultivationType cultivationType) {
        return Optional.of(RealmTypes.MORTALITY);
    }

}
