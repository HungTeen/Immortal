package hungteen.immortal.common.item.eixirs;

import hungteen.htlib.util.ColorUtil;
import hungteen.immortal.api.ImmortalAPI;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-29 18:40
 **/
public class AntidoteElixir extends ElixirItem{

    public AntidoteElixir() {
        super(Rarity.COMMON, ColorUtil.DARK_GREEN);
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        if(! level.isClientSide){
            livingEntity.removeEffect(MobEffects.POISON);
        }
    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return immortal().apply(ImmortalAPI.get().getEntityRealm(livingEntity));
    }

}
