package hungteen.immortal;

import hungteen.immortal.block.ImmortalBlocks;
import hungteen.immortal.blockentity.ImmortalBlockEntities;
import hungteen.immortal.capability.CapabilityHandler;
import hungteen.immortal.client.particle.ImmortalParticles;
import hungteen.immortal.command.CommandHandler;
import hungteen.immortal.data.DataGenHandler;
import hungteen.immortal.entity.ImmortalEntities;
import hungteen.immortal.impl.*;
import hungteen.immortal.item.ImmortalItems;
import hungteen.immortal.menu.ImmortalMenus;
import hungteen.immortal.network.NetworkHandler;
import hungteen.immortal.world.LevelManager;
import hungteen.immortal.world.biome.BiomeManager;
import hungteen.immortal.world.biome.ImmortalBiomes;
import hungteen.immortal.world.dimension.ImmortalDimensions;
import hungteen.immortal.world.structure.ImmortalStructures;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-23 23:32
 **/
@Mod(ImmortalMod.MOD_ID)
public class ImmortalMod {

    // Mod ID.
    public static final String MOD_ID = "immortal";
    // Proxy of Server and Client.
    public static CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public ImmortalMod() {
        //get mod event bus.
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, ImmortalMod::setUp);
        modBus.addListener(EventPriority.NORMAL, DataGenHandler::dataGen);
        modBus.addListener(EventPriority.NORMAL, CapabilityHandler::registerCapabilities);
        modBus.addGenericListener(Block.class, ImmortalBlocks::registerBlocks);
        modBus.addGenericListener(Item.class, ImmortalBlocks::registerBlockItems);
        modBus.addGenericListener(Item.class, ImmortalItems::registerItems);

        //get forge event bus.
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, CapabilityHandler::attachCapabilities);
        forgeBus.addListener(EventPriority.NORMAL, CommandHandler::init);

        ImmortalConfigs.init();

        defferRegister(modBus);
        coreRegister();
    }

    /**
     * register forge stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void defferRegister(IEventBus modBus) {
        ImmortalItems.ITEMS.register(modBus);
        ImmortalBlocks.BLOCKS.register(modBus);
        ImmortalEntities.ENTITY_TYPES.register(modBus);
        ImmortalBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ImmortalParticles.PARTICLE_TYPES.register(modBus);
        ImmortalMenus.CONTAINER_TYPES.register(modBus);
        ImmortalStructures.STRUCTURES.register(modBus);
        ImmortalDimensions.register();
        ImmortalBiomes.register();
    }

    /**
     * register custom stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void coreRegister() {
        SpiritualRoots.SpiritualRoot.register();
        Spells.Spell.register();
        EffectRunes.EffectRune.register();
        GetterRunes.GetterRune.register();
        PlayerDatas.PlayerData.register();
        Realms.Realm.register();
    }

    public static void setUp(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
//            PVZRecipeTypes.registerRecipeTypes();
//            PVZBiomes.registerBiomes();
//            PVZFeatures.registerFeatures();
////            PotionRecipeHandler.registerPotionRecipes();
//            CommonRegister.registerCompostable();
//            CommonRegister.registerAxeStrips();
//            BiomeUtil.initBiomeSet();
//            SpawnRegister.registerEntitySpawns();
//            PVZDimensions.register();
            LevelManager.registerSpiritualLevels();
            BiomeManager.registerSpiritualBiomes();
            ElixirManager.registerElixirIngredients();
        });

        NetworkHandler.init();
    }

}
