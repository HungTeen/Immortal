package hungteen.immortal;

import hungteen.immortal.client.ClientProxy;
import hungteen.immortal.common.ElixirManager;
import hungteen.immortal.common.RealmManager;
import hungteen.immortal.common.ai.ImmortalSchedules;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.ImmortalBlockEntities;
import hungteen.immortal.common.capability.CapabilityHandler;
import hungteen.immortal.client.particle.ImmortalParticles;
import hungteen.immortal.common.command.CommandHandler;
import hungteen.immortal.data.DataGenHandler;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.impl.*;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.menu.ImmortalMenus;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.world.LevelManager;
import hungteen.immortal.common.world.biome.BiomeManager;
import hungteen.immortal.common.world.biome.ImmortalBiomes;
import hungteen.immortal.common.world.dimension.ImmortalDimensions;
import hungteen.immortal.common.world.structure.ImmortalStructures;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        modBus.addGenericListener(Item.class, ImmortalEntities::registerSpawnEggs);
        modBus.addGenericListener(Item.class, ImmortalBlocks::registerBlockItems);
        modBus.addGenericListener(Item.class, ImmortalItems::registerItems);
        modBus.addListener(EventPriority.NORMAL, ImmortalEntities::addEntityAttributes);

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
        ImmortalSchedules.SCHEDULES.register(modBus);
        ImmortalDimensions.register();
        ImmortalBiomes.register();
    }

    /**
     * register custom stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void coreRegister() {
        SpiritualRoots.SpiritualRoot.register();
        Spells.Spell.register();
        PlayerDatas.PlayerData.register();
        Realms.Realm.register();
        MemoryRunes.MemoryRune.register();
        SensorRunes.SensorRune.register();
        BehaviorRunes.BehaviorRune.register();
        ElixirTypes.ElixirType.register();
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
            RealmManager.registerUpgradeList();
        });

        NetworkHandler.init();
    }

}
