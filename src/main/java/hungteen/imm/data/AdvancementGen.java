package hungteen.imm.data;

import com.google.common.collect.Comparators;
import hungteen.htlib.data.HTAdvancementGen;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.imm.common.advancement.trigger.PlayerLearnSpellTrigger;
import hungteen.imm.common.advancement.trigger.PlayerLearnSpellsTrigger;
import hungteen.imm.common.advancement.trigger.PlayerRealmChangeTrigger;
import hungteen.imm.common.advancement.trigger.SpiritualPearlTrigger;
import hungteen.imm.common.block.CushionBlock;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.WoolCushionBlock;
import hungteen.imm.common.block.plants.GourdGrownBlock;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.spell.spells.basic.ElementalMasterySpell;
import hungteen.imm.common.world.ElixirManager;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.common.world.structure.IMMStructures;
import hungteen.imm.util.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/15 16:48
 */
public class AdvancementGen extends HTAdvancementGen {

    public AdvancementGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelperIn) {
        super(output, Util.id(), registries, fileHelperIn, List.of(
                new ImmortalBuilder(Util.id())
        ));
    }

    private static final class ImmortalBuilder extends HTAdvancementBuilder {

        private ImmortalBuilder(String modId) {
            super(modId);
        }

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
            Advancement imMortal = root(IMMItems.GOURD_SEEDS.get(), "im_mortal", Util.mc().blockTexture("copper_block"))
                    .addCriterion("mortal", PlayerTrigger.TriggerInstance.tick())
                    .rewards(AdvancementRewards.Builder.recipe(ElixirManager.elixirRecipe(IMMItems.FIVE_FLOWERS_ELIXIR.get()))
                            .addRecipe(ElixirManager.elixirRecipe(IMMItems.SPIRITUAL_INSPIRATION_ELIXIR.get()))
                    ).save(saver, loc("im_mortal"));
            task(imMortal, Items.EMERALD, "spiritual_stone")
                    .addCriterion("get_spiritual_stone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.EMERALD))
                    .save(saver, loc("spiritual_stone"));
            Advancement eastWorld = goal(imMortal, IMMBlocks.TELEPORT_ANCHOR.get(), "east_world")
                    .addCriterion("reach_east_world", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(IMMLevels.EAST_WORLD))
                    .save(saver, loc("east_world"));
            task(imMortal, IMMItems.SPIRITUAL_PEARL.get(), "stuck_in_mortal")
                    .addCriterion("reach_east_world", SpiritualPearlTrigger.TriggerInstance.test(IMMEntities.EMPTY_CULTIVATOR.get(), 0))
                    .save(saver, loc("stuck_in_mortal"));
            Advancement.Builder cushionBuilder = task(imMortal, WoolCushionBlock.getWoolCushion(DyeColor.RED), "long_cultivation");
            BlockHelper.get().filterEntries(CushionBlock.class::isInstance).stream().sorted(Comparator.comparing(l -> l.getKey().location())).forEach(entry -> {
                cushionBuilder.addCriterion("placed_" + entry.getKey().location().getPath(), ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(entry.getValue()));
            });
            Advancement longCultivation = cushionBuilder.requirements(RequirementsStrategy.OR)
                    .save(saver, loc("long_cultivation"));
            task(eastWorld, Blocks.WHITE_TERRACOTTA, "trading_market")
                    .addCriterion("reach_market", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(IMMStructures.PLAINS_TRADING_MARKET)))
                    .save(saver, loc("trading_market"));
            task(eastWorld, Blocks.CHERRY_LOG, "spirit_lab")
                    .addCriterion("reach_lab", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(IMMStructures.SPIRIT_LAB)))
                    .save(saver, loc("spirit_lab"));
            goal(imMortal, IMMBlocks.COPPER_FURNACE.get(), "cauldron")
                    .addCriterion("place_furnace", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(IMMBlocks.COPPER_FURNACE.get()))
                    .save(saver, loc("cauldron"));
            Advancement.Builder calabashBuilder = challenge(imMortal, GourdGrownBlock.GourdTypes.RED.getGourdGrownBlock(), "calabash_brothers");
            for (GourdGrownBlock.GourdTypes gourd : GourdGrownBlock.GourdTypes.values()) {
                calabashBuilder.addCriterion("collect_" + gourd.name().toLowerCase(), InventoryChangeTrigger.TriggerInstance.hasItems(gourd.getGourdGrownBlock()));
            }
            calabashBuilder.save(saver, loc("calabash_brothers"));

            // Spells.
            Advancement spells = task(imMortal, IMMItems.SECRET_MANUAL.get(), "spells")
                    .addCriterion("learn_spells", PlayerLearnSpellsTrigger.TriggerInstance.test(1))
                    .save(saver, loc("spells"));
            Advancement.Builder elementMasteryBuilder1 = task(spells, IMMItems.SECRET_MANUAL.get(), "element_mastery_beginner");
            ElementalMasterySpell.getSpells().forEach(spell -> {
                elementMasteryBuilder1.addCriterion("learn_" + spell.getName(), PlayerLearnSpellTrigger.TriggerInstance.test(spell, 1));
            });
            Advancement elementMasteryBeginner = elementMasteryBuilder1.requirements(RequirementsStrategy.OR)
                    .save(saver, loc("element_mastery_beginner"));
            Advancement.Builder elementMasteryBuilder2 = task(elementMasteryBeginner, IMMItems.SECRET_MANUAL.get(), "element_mastery_expert");
            ElementalMasterySpell.getSpells().forEach(spell -> {
                elementMasteryBuilder2.addCriterion("learn_" + spell.getName(), PlayerLearnSpellTrigger.TriggerInstance.test(spell, 3));
            });
            Advancement elementMasteryExpert = elementMasteryBuilder2.requirements(RequirementsStrategy.OR)
                    .save(saver, loc("element_mastery_expert"));
            Advancement.Builder elementMasteryBuilder3 = task(elementMasteryExpert, IMMItems.SECRET_MANUAL.get(), "element_master");
            ElementalMasterySpell.getSpells().forEach(spell -> {
                elementMasteryBuilder3.addCriterion("learn_" + spell.getName(), PlayerLearnSpellTrigger.TriggerInstance.test(spell, 5));
            });
            elementMasteryBuilder3.requirements(RequirementsStrategy.OR)
                    .save(saver, loc("element_master"));

            Advancement spellMaster = task(spells, IMMItems.SECRET_MANUAL.get(), "spell_master")
                    .addCriterion("learn_spells", PlayerLearnSpellsTrigger.TriggerInstance.test(10))
                    .save(saver, loc("spell_master"));

            // Realms.
            Advancement spiritual_1 = goal(longCultivation, IMMItems.SPIRITUAL_INSPIRATION_ELIXIR.get(), "spiritual_1")
                    .addCriterion("reach_target_realm", PlayerRealmChangeTrigger.TriggerInstance.test(RealmTypes.SPIRITUAL_LEVEL_1))
                    .rewards(AdvancementRewards.Builder.recipe(ElixirManager.elixirRecipe(IMMItems.GATHER_BREATH_ELIXIR.get())))
                    .save(saver, loc("spiritual_1"));
            Advancement spiritual_2 = goal(spiritual_1, IMMItems.GATHER_BREATH_ELIXIR.get(), "spiritual_2")
                    .addCriterion("reach_target_realm", PlayerRealmChangeTrigger.TriggerInstance.test(RealmTypes.SPIRITUAL_LEVEL_2))
                    .rewards(AdvancementRewards.Builder.recipe(ElixirManager.elixirRecipe(IMMItems.REFINE_BREATH_ELIXIR.get())))
                    .save(saver, loc("spiritual_2"));
            Advancement spiritual_3 = goal(spiritual_2, IMMItems.REFINE_BREATH_ELIXIR.get(), "spiritual_3")
                    .addCriterion("reach_target_realm", PlayerRealmChangeTrigger.TriggerInstance.test(RealmTypes.SPIRITUAL_LEVEL_3))
//                    .rewards(AdvancementRewards.Builder.recipe(ElixirManager.elixirRecipe(IMMItems.GATHER_BREATH_ELIXIR.get())))
                    .save(saver, loc("spiritual_3"));
        }

        private String loc(String name){
            return Util.prefixName("immortal/" + name);
        }
    }

}
