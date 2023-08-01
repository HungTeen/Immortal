package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.imm.common.RealmManager;
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
public class FiveFlowersElixir extends BreakThroughElixir {

    public FiveFlowersElixir() {
        super(Rarity.UNCOMMON, ColorHelper.LITTLE_YELLOW1.rgb());
    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return same(RealmTypes.MORTALITY).apply(RealmManager.getEntityRealm(livingEntity));
    }

    @Override
    public float getBonusProgress(Player player, RandomSource random, ItemStack stack, Accuracies accuracy) {
        final int len = PlayerUtil.getSpiritualRoots(player).size();
        if(len == 0 || random.nextFloat() < 0.5F){
            player.addEffect(EffectHelper.viewEffect(MobEffects.HARM, 10, 1));
            return 0F;
        }
        return random.nextFloat() * 0.8F;
    }
}
