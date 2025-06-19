package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.ArtifactCategory;
import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.QiManager;
import hungteen.imm.common.cultivation.impl.ArtifactCategories;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.RandomUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 15:07
 **/
public class WoodBowItem extends BowItem implements ArtifactItem {

    public static final ResourceLocation PULLED = Util.prefix("pulled");
    public static final ResourceLocation PULLING = Util.prefix("pulling");
    private static final float QI_COST = 10;

    public WoodBowItem() {
        super(new Properties().stacksTo(1).durability(500));
    }

    public WoodBowItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
        // 有概率根据灵根附着元素到箭矢上。
        if(shooter.getRandom().nextFloat() < 0.5F){
            attachElementOnArrow(shooter, projectile, 1, 2);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(ItemUtil.desc(stack, QI_COST));
    }

    public static void attachElementOnArrow(LivingEntity shooter, Projectile projectile, float percent, float maxPercent){
        if(QiManager.hasEnoughQi(shooter, QI_COST)) {
            List<Element> elements = ElementManager.getElements(shooter).toList();
            if (!elements.isEmpty()) {
                Element element = RandomUtil.choose(shooter.getRandom(), elements);
                ElementManager.addPercentElement(projectile, element, false, percent, maxPercent);
                QiManager.addQi(shooter, -QI_COST);
            }
        }
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return ArtifactRank.MODERATE;
    }

    @Override
    public ArtifactCategory getArtifactCategory() {
        return ArtifactCategories.DEFAULT;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 25;
    }
}
