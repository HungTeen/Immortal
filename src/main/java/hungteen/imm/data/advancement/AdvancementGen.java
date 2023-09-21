package hungteen.imm.data.advancement;

import hungteen.htlib.data.HTAdvancementGen;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.util.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

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

        protected ImmortalBuilder(String modId) {
            super(modId);
        }

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
            Advancement imMortal = root(IMMItems.GOURD_SEEDS.get(), "im_mortal", Util.mc().blockTexture("copper_block"))
                    .addCriterion("mortal", PlayerTrigger.TriggerInstance.tick())
                    .save(saver, loc("im_mortal"));
            task(imMortal, Items.EMERALD, "spiritual_stone")
                    .addCriterion("get_spiritual_stone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.EMERALD))
                    .save(saver, loc("spiritual_stone"));
            task(imMortal, IMMBlocks.TELEPORT_ANCHOR.get(), "east_world")
                    .addCriterion("reach_east_world", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(IMMLevels.EAST_WORLD))
                    .save(saver, loc("east_world"));
//            task(imMortal, Items.CHAINMAIL_HELMET, "never_be_alone")
//                    .addCriterion("interact_with_cultivator", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity())
//                    .save(saver, loc("never_be_alone"));
//            builder().display(Items.EMERALD, Component.translatable("")).save(saver, )
        }

        private String loc(String name){
            return Util.prefixName("immortal/" + name);
        }
    }

}
