package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactCategory;
import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.impl.ArtifactCategories;
import hungteen.imm.common.item.IMMDataComponents;
import hungteen.imm.common.menu.tooltip.TalismanToolTip;
import hungteen.imm.util.BehaviorUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/3 15:58
 */
public abstract class TalismanItem extends Item implements ArtifactItem {

    public static final ResourceLocation ACTIVATED = Util.prefix("activated");
    public static final ResourceLocation ACTIVATING = Util.prefix("activating");
    public static final MutableComponent NO_TARGET = TipUtil.info("talisman.no_target");

    public TalismanItem(Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new TalismanToolTip(stack, getSpell()));
    }

    @Override
    public Component getName(ItemStack stack) {
        TalismanSpell spell = getSpell();
        if(spell.getMaxLevel() > 1){
            return spell.getComponent().append("(").append(TipUtil.misc("talisman." + getSpellLevel(stack))).append(")");
        }
        return spell.getComponent();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.addAll(SpellManager.getSpellTooltips(new Spell(getSpell(), getSpellLevel(stack))));
    }

    public boolean canUseTalisman(ItemStack stack, LivingEntity living){
        return true;
    }

    public static Optional<Vec3> getTargetPosition(Entity entity){
        if(entity instanceof Player){
            HitResult hitResult = EntityUtil.getHitResult(entity, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY);
            if(hitResult instanceof EntityHitResult entityHitResult){
                return Optional.of(entityHitResult.getEntity().position());
            } else if(hitResult instanceof BlockHitResult blockHitResult){
                return Optional.of(blockHitResult.getLocation());
            }
        } else if(entity instanceof LivingEntity living){
            Optional<LivingEntity> target = BehaviorUtil.getAttackTarget(living);
            if(target.isPresent()){
                return Optional.of(target.get().position());
            } else if(living instanceof Mob mob){
                if(mob.getTarget() != null){
                    return Optional.of(mob.getTarget().position());
                }
            }
        }
        return Optional.empty();
    }

    public abstract TalismanSpell getSpell();

    public int getCoolDown(ItemStack stack, LivingEntity entity){
        return getSpell().getCooldown();
    }

    public float getQiCost(ItemStack stack, Entity entity){
        return getSpell().getConsumeQi();
    }

    public static void setSpellLevel(ItemStack stack, int level) {
        stack.set(IMMDataComponents.SPELL_LEVEL, level);
    }

    public static int getSpellLevel(ItemStack stack) {
        return stack.getOrDefault(IMMDataComponents.SPELL_LEVEL, 1);
    }

    public static Component noEnoughQiTip(float qiCost) {
        return TipUtil.info("talisman.no_enough_qi", String.format("%.1f", qiCost));
    }

    public static Component requireQiRoots(){
        return TipUtil.tooltip("talisman.require_qi_roots");
    }

    public static Component requireElements() {
        return TipUtil.tooltip("talisman.require_elements");
    }


    @Override
    public int getMaxSpellSlot(ItemStack stack) {
        return 0;
    }

    @Override
    public ArtifactCategory getArtifactCategory() {
        return ArtifactCategories.TALISMAN;
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return ArtifactRank.COMMON;
    }
}
