package hungteen.imm.common.cultivation.spell.talisman;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.MathUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/18 20:57
 **/
public class FireballSpell extends SpellTypeImpl implements TalismanSpell {

    public FireballSpell() {
        super("fireball", property(InscriptionTypes.JOSS_PAPER).cd(120).qi(40));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        final Level level = context.level();
        final LivingEntity owner = context.owner();
        final RandomSource random = owner.getRandom();
        EntityUtil.playSound(level, owner, SoundEvents.BLAZE_SHOOT);
        for(int i = 0; i < 1; ++ i){
            final Vec3 vec = owner.getLookAngle().normalize().scale(2F);
            final LargeFireball fireball = new LargeFireball(level, owner, vec, 2);
            final double forward = RandomHelper.doubleRange(random, 1);
            final double side = RandomHelper.doubleRange(random, 2);
            final double dy = RandomHelper.doubleRange(random, 0.5);
            final double dx = Mth.sin(MathUtil.toRadian(owner.getYRot()));
            final double dz = Mth.cos(MathUtil.toRadian(owner.getYRot()));
            fireball.setPos(owner.position().add(dx * forward + dz * side, dy + owner.getEyeHeight(), dz * forward + dx * side));
            level.addFreshEntity(fireball);
        }
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity owner) {
        return 40;
    }

    @Override
    public List<Element> requireElements() {
        return List.of(Element.FIRE);
    }

    @Override
    public ItemStack getTalismanItem(int level) {
        return new ItemStack(IMMItems.FIREBALL_TALISMAN.get());
    }
}
