package hungteen.immortal.data;

import hungteen.htlib.data.HTItemModelGen;
import hungteen.htlib.util.helper.ItemHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.immortal.api.interfaces.IElixirItem;
import hungteen.immortal.common.ElixirManager;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.item.artifacts.MeleeAttackItem;
import hungteen.immortal.utils.ItemUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
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

    public ItemModelGen(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Util.id(), helper);
    }

    @Override
    protected void registerModels() {
        /*
        Special model generated by blockbench.
         */
        addedItems.addAll(Arrays.asList(
//                PVZItems.PEA_GUN.get()
//                ItemRegister.PEA_GUN.get(), ItemRegister.ZOMBIE_FLAG.get(), ItemRegister.BOBSLE_CAR.get(),
//                ItemRegister.SCREEN_DOOR.get(), ItemRegister.JACK_BOX.get(), ItemRegister.BALLOON.get(),
//                ItemRegister.WARNING_SIGN.get(), ItemRegister.ZOMBIE_DOLL.get(), ItemRegister.POLE.get(),
//                ItemRegister.BUCKET_HEAD.get(), ItemRegister.RESOURCE_COLLECTOR.get()
        ));

        /*
        Single add block items so that except adding by method below.
         */
        Arrays.asList(
                ImmortalItems.GOURD_SEEDS.get()
        ).forEach(item -> {
            genNormalModel(item);
            this.addedItems.add(item);
        });

        /*
        Fence.
         */
        Arrays.asList(
                ImmortalBlocks.MULBERRY_FENCE, ImmortalBlocks.MULBERRY_BUTTON
        ).forEach(block -> {
            genBlockModel(block.get(), block.getId().getPath() + "_inventory");
        });

        /*
        Trap Door.
         */
        Arrays.asList(
                ImmortalBlocks.MULBERRY_TRAPDOOR
        ).forEach(block -> {
            genBlockModel(block.get(), block.getId().getPath() + "_bottom");
        });

        /*
        Block-items with tex in item/
         */
        Arrays.asList(
                ImmortalBlocks.MULBERRY_DOOR.get().asItem(), ImmortalItems.MULBERRY_SIGN.get()
        ).forEach(i -> {
            genNormalModel(i);
            this.addedItems.add(i);
        });

        /*
        Block-items with tex in block/
         */
        Arrays.asList(
                ImmortalBlocks.MULBERRY_SAPLING
        ).forEach(block -> {
            genItemModelWithBlock(block.get().asItem());
        });

        /*
        For hand held item.
         */
//        Arrays.asList(
//                PVZItems.ORIGIN_AXE.get(), PVZItems.ORIGIN_HOE.get(), PVZItems.ORIGIN_PICKAXE.get(), PVZItems.ORIGIN_SHOVEL.get(), PVZItems.ORIGIN_SWORD.get()
//        ).forEach(i -> {
//            addedItems.add(i);
//            genHeld(i.getRegistryName().getPath(), Util.prefix("item/" + i.getRegistryName().getPath()));
//        });

        /*
        For large hand held item.
         */
        ItemUtil.getLargeHeldItems().forEach(item -> {
            final ResourceLocation location = ItemUtil.getLargeHeldLocation(item);
            this.addedItems.add(item);
            genNormal(name(item), ItemHelper.itemTexture(item));
            genLargeHeld(location.getPath(), StringHelper.itemTexture(location));
        });

        //3 types of sun storage sapling.
//        genSameModelsWithAdd(ItemRegister.SUN_STORAGE_SAPLING.get(), ItemRegister.SMALL_SUN_STORAGE_SAPLING.get(), ItemRegister.LARGE_SUN_STORAGE_SAPLING.get(), ItemRegister.ONCE_SUN_STORAGE_SAPLING.get());

        /*
        For mostly common items.
         */
        for (Item item : ForgeRegistries.ITEMS) {
            if (!Util.in(key(item)) || addedItems.contains(item)){
                continue;
            }
            if (item instanceof SpawnEggItem) { // for spawn eggs.
                addedItems.add(item);
                getBuilder(name(item)).parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
            } else if(item instanceof IElixirItem){ // for elixir items.
                addedItems.add(item);
                genNormal(name(item), Util.prefix("item/elixir"), ElixirManager.getOuterLayer(((IElixirItem) item).getElixirRarity()));
            } else if(item instanceof MeleeAttackItem){
                addedItems.add(item);
                genHeld(name(item), ItemHelper.itemTexture(item));
            } else if (item instanceof BlockItem) { // normal block items.
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
