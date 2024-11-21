package hungteen.imm.data.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-29 12:34
 **/
public class SmithingRecipeBuilder implements RecipeBuilder {

    private final int needSmithingValue;
    private final float speedMultiple;
    private final Item result;
    private final int count;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    protected final int requireLevel;
    protected final boolean needRecovery;
    @Nullable
    private String group;

    public SmithingRecipeBuilder(ItemLike itemLike, int count, int requireLevel, boolean needRecovery, int needSmithingValue, float speedMultiple) {
        this.result = itemLike.asItem();
        this.count = count;
        this.requireLevel = requireLevel;
        this.needRecovery = needRecovery;
        this.needSmithingValue = needSmithingValue;
        this.speedMultiple = speedMultiple;
    }

    public SmithingRecipeBuilder define(Character character, TagKey<Item> tagKey) {
        return this.define(character, Ingredient.of(tagKey));
    }

    public SmithingRecipeBuilder define(Character character, ItemLike itemLike) {
        return this.define(character, Ingredient.of(itemLike));
    }

    public SmithingRecipeBuilder define(Character character, Ingredient ingredient) {
        if (this.key.containsKey(character)) {
            throw new IllegalArgumentException("Symbol '" + character + "' is already defined!");
        } else if (character == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(character, ingredient);
            return this;
        }
    }

    public SmithingRecipeBuilder pattern(String patternString) {
        if (!this.rows.isEmpty() && patternString.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(patternString);
            return this;
        }
    }

    @Override
    public SmithingRecipeBuilder unlockedBy(String name, Criterion<?> triggerInstance) {
        this.advancement.addCriterion(name, triggerInstance);
        return this;
    }

    @Override
    public SmithingRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput p_126141_, ResourceLocation consumer) {
        this.ensureValid(consumer);
//        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(consumer)).rewards(AdvancementRewards.Builder.recipe(consumer)).requirements(RequirementsStrategy.OR);
//        p_126141_.accept(new SmithingRecipeBuilder.SmithingResult(
//                consumer,
//                this.result,
//                this.count,
//                this.requireLevel,
//                this.needRecovery,
//                this.needSmithingValue,
//                this.speedMultiple,
//                this.group == null ? "" : this.group,
//                this.rows,
//                this.key,
//                this.advancement,
//                new ResourceLocation(consumer.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + consumer.getPath()))
//        );
    }

    private void ensureValid(ResourceLocation location) {
//        if (this.rows.isEmpty()) {
//            throw new IllegalStateException("No pattern is defined for shaped recipe " + location + "!");
//        } else {
//            Set<Character> set = Sets.newHashSet(this.key.keySet());
//            set.remove(' ');
//
//            for(String s : this.rows) {
//                for(int i = 0; i < s.length(); ++i) {
//                    char c0 = s.charAt(i);
//                    if (!this.key.containsKey(c0) && c0 != ' ') {
//                        throw new IllegalStateException("Pattern in recipe " + location + " uses undefined symbol '" + c0 + "'");
//                    }
//
//                    set.remove(c0);
//                }
//            }
//
//            if (!set.isEmpty()) {
//                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + location);
//            } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
//                throw new IllegalStateException("Shaped recipe " + location + " only takes in a single item - should it be a shapeless recipe instead?");
//            } else if (this.advancement.getCriteria().isEmpty()) {
//                throw new IllegalStateException("No way of obtaining recipe " + location);
//            }
//        }
    }

//    public static class SmithingResult extends ShapedRecipeBuilder.Result {
//
//        protected final int requireLevel;
//        protected final boolean needRecovery;
//        private final int needSmithingValue;
//        private final float speedMultiple;
//
//        public SmithingResult(ResourceLocation location, Item result, int count, int requireLevel, boolean needRecovery, int needSmithingValue, float speedMultiple, String group, List<String> pattern, Map<Character, Ingredient> keys, Advancement.Builder builder, ResourceLocation advancement) {
//            super(location, result, count, group, pattern, keys, builder, advancement);
//            this.requireLevel = requireLevel;
//            this.needRecovery = needRecovery;
//            this.needSmithingValue = needSmithingValue;
//            this.speedMultiple = speedMultiple;
//        }
//
//        @Override
//        public void serializeRecipeData(JsonObject jsonObject) {
//            super.serializeRecipeData(jsonObject);
//            jsonObject.addProperty("require_level", this.requireLevel);
//            jsonObject.addProperty("need_recovery", this.needRecovery);
//            jsonObject.addProperty("require_smithing_value", this.needSmithingValue);
//            jsonObject.addProperty("speed_multiple", this.speedMultiple);
//        }
//
//        @Override
//        public RecipeSerializer<?> getType() {
//            return ImmortalRecipes.SMITHING_ARTIFACT_SERIALIZER.get();
//        }
//
//    }
}
