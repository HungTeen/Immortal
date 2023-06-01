package hungteen.imm.data;

import hungteen.htlib.data.HTItemModelGen;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.api.interfaces.IElixirItem;
import hungteen.imm.common.ElixirManager;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.impl.registry.IMMWoods;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.artifacts.MeleeAttackItem;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.item.runes.filter.FilterRuneItem;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:32
 **/
public class ItemModelGen extends HTItemModelGen {

    public ItemModelGen(PackOutput output, ExistingFileHelper helper) {
        super(output, Util.id(), helper);
    }

    @Override
    protected void registerModels() {
        /* Special model generated by blockbench. */
        addedItems.addAll(Arrays.asList(
//                PVZItems.PEA_GUN.get()
        ));

        /* Single add block items so that except adding by method below. */
        Arrays.asList(
                IMMItems.RICE_SEEDS.get(), IMMItems.JUTE_SEEDS.get(), IMMItems.GOURD_SEEDS.get()
        ).forEach(item -> {
            genNormalModel(item);
            this.addedItems.add(item);
        });

        /* Woods */
        IMMWoods.woods().forEach(this::woodIntegration);

        /* Block-items with tex in block folder */
        Arrays.asList(
                IMMBlocks.MULBERRY_SAPLING
        ).forEach(block -> {
            genItemModelWithBlock(block.get().asItem());
        });

        gen(IMMBlocks.TELEPORT_ANCHOR.get(), block -> genBlockModel(block, name(block) + "_0"));

        /* For large hand held item. */
        ItemUtil.getLargeHeldItems().forEach(item -> {
            final ResourceLocation location = ItemUtil.getLargeHeldLocation(item);
            this.addedItems.add(item);
            genNormal(name(item), ItemHelper.itemTexture(item));
            genLargeHeld(location.getPath(), StringHelper.itemTexture(location));
        });

        /* Banner patterns */
        Arrays.asList(
                IMMItems.CONTINUOUS_MOUNTAIN_PATTERN, IMMItems.FLOWING_CLOUD_PATTERN, IMMItems.FOLDED_THUNDER_PATTERN, IMMItems.RHOMBUS_PATTERN
        ).forEach(item -> {
            this.addedItems.add(item.get());
            this.genNormal(item.getId().getPath(), Util.mcPrefix("item/flower_banner_pattern"));
        });

        /* Runes */
        ItemHelper.get().filterEntries(BehaviorRuneItem.class::isInstance).forEach(entry -> {
            this.addedItems.add(entry.getValue());
            this.genNormal(entry.getKey().location().getPath(), StringHelper.itemTexture(Util.prefix("behavior_rune")));
        });
        ItemHelper.get().filterEntries(FilterRuneItem.class::isInstance).forEach(entry -> {
            this.addedItems.add(entry.getValue());
            this.genNormal(entry.getKey().location().getPath(), StringHelper.itemTexture(Util.prefix("filter_rune")));
        });

        /* For mostly common items. */
        for (Item item : ForgeRegistries.ITEMS) {
            if (!Util.in(key(item)) || addedItems.contains(item)){
                continue;
            }
            if (item instanceof SpawnEggItem) {
                // for spawn eggs.
                addedItems.add(item);
                getBuilder(name(item)).parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
            } else if(item instanceof IElixirItem){
                // for elixir items.
                addedItems.add(item);
                genNormal(name(item), Util.prefix("item/elixir"), ElixirManager.getOuterLayer(((IElixirItem) item).getElixirRarity()));
            } else if(item instanceof MeleeAttackItem){
                addedItems.add(item);
                genHeld(name(item), ItemHelper.itemTexture(item));
            } else if (item instanceof BlockItem) {
                // normal block items.
                genBlockModel(((BlockItem) item).getBlock());
            }
        }

        /*
        Last step for all normal item models.
         */
        for (Item item : ForgeRegistries.ITEMS) {
            if (Util.in(key(item)) && !addedItems.contains(item)) {
                genNormal(name(item), Util.prefix("item/" + name(item)));
            }
        }
    }

    protected ItemModelBuilder genLargeHeld(String name, ResourceLocation... layers) {
        return this.gen(name, Util.prefixName("item/large_handheld"), layers);
    }

}
