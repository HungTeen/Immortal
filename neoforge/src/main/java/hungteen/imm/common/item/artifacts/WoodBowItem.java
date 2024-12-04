package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.RandomUtil;
import hungteen.imm.util.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 15:07
 **/
public class WoodBowItem extends BowItem implements ArtifactItem {

    public static final ResourceLocation PULL = Util.prefix("pull");
    public static final ResourceLocation PULLING = Util.prefix("pulling");

    public WoodBowItem() {
        super(ItemUtil.artifact(ArtifactRank.MODERATE).durability(500));
    }

    public WoodBowItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
        // 有概率根据灵根附着元素到箭矢上。
        if(shooter.getRandom().nextFloat() < 0.5F){
            attachElementOnArrow(shooter, projectile, 5, 10);
        }
    }

    public static void attachElementOnArrow(LivingEntity shooter, Projectile projectile, float value, float maxValue){
        List<Element> elements = CultivationManager.getElements(shooter).toList();
        if(! elements.isEmpty()){
            Element element = RandomUtil.choose(shooter.getRandom(), elements);
            ElementManager.addElementAmount(projectile, element, false, value, maxValue);
        }
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return ItemUtil.getRank(stack);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 25;
    }
}
