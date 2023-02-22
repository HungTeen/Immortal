package hungteen.immortal.common.impl;

import hungteen.immortal.api.registry.IArtifactTier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-14 15:53
 **/
public class ImmortalTiers {

    public static final IArtifactTier STONE = new ArtifactTier(-5, 0, 131, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS));
    public static final IArtifactTier BRONZE = new ArtifactTier(0F, 1, 300, () -> Ingredient.of(Items.COPPER_INGOT));

    public static class ArtifactTier implements IArtifactTier {

        private final float attackDamage;
        private final float attackSpeed;
        private final float attackRange;
        private final int artifactLevel;
        private final int durability;
        private final Supplier<Ingredient> repairIngredient;

        public ArtifactTier(float attackDamage, int artifactLevel, int durability, Supplier<Ingredient> repairIngredient) {
            this(attackDamage, 0, 0, artifactLevel, durability, repairIngredient);
        }

        public ArtifactTier(float attackDamage, float attackSpeed, float attackRange, int artifactLevel, int durability, Supplier<Ingredient> repairIngredient) {
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
            this.attackRange = attackRange;
            this.artifactLevel = artifactLevel;
            this.durability = durability;
            this.repairIngredient = repairIngredient;
        }

        @Override
        public float getAttackDamage() {
            return this.attackDamage;
        }

        @Override
        public float getAttackSpeed() {
            return this.attackSpeed;
        }

        @Override
        public float getAttackRange() {
            return this.attackRange;
        }

        @Override
        public int getArtifactLevel() {
            return artifactLevel;
        }

        @Override
        public int getUses() {
            return durability;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }
    }
}
