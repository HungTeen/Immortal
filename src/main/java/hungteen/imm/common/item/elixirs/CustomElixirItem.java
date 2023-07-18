package hungteen.imm.common.item.elixirs;

import hungteen.htlib.util.helper.ColorHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/18 15:31
 */
public class CustomElixirItem extends ElixirItem{

    private static final String EFFECTS = "ElixirEffects";

    public CustomElixirItem() {
        super(Rarity.COMMON, ColorHelper.BLACK.rgb());
    }

    @Override
    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
        getEffects(stack).forEach(livingEntity::addEffect);
    }

    @Override
    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
        return Optional.of(true);
    }

    public static void setEffects(ItemStack stack, List<MobEffectInstance> effects){
        final ListTag tag = getEffectList(stack);
        effects.forEach(instance -> {
            tag.add(instance.save(new CompoundTag()));
        });
        stack.getOrCreateTag().put(EFFECTS, tag);
    }

    public static List<MobEffectInstance> getEffects(ItemStack stack){
        return getEffectList(stack).stream()
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast)
                .map(MobEffectInstance::load).toList();
    }

    private static ListTag getEffectList(ItemStack stack){
        return stack.getOrCreateTag().contains(EFFECTS) ? stack.getOrCreateTag().getList(EFFECTS, Tag.TAG_COMPOUND) : new ListTag();
    }
}
