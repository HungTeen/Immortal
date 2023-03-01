package hungteen.immortal;

import hungteen.immortal.client.ClientProxy;
import hungteen.immortal.client.particle.ImmortalParticles;
import hungteen.immortal.common.CommonRegister;
import hungteen.immortal.common.ElixirManager;
import hungteen.immortal.common.RealmManager;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.ImmortalBlockEntities;
import hungteen.immortal.common.capability.CapabilityHandler;
import hungteen.immortal.common.command.CommandHandler;
import hungteen.immortal.common.datapack.ImmortalDataPacks;
import hungteen.immortal.common.entity.ImmortalDataSerializers;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.common.entity.ai.*;
import hungteen.immortal.common.impl.registry.ImmortalWoods;
import hungteen.immortal.common.impl.registry.InventoryLootTypes;
import hungteen.immortal.common.impl.registry.SpellTypes;
import hungteen.immortal.common.impl.registry.SpiritualTypes;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.menu.ImmortalMenus;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.recipe.ImmortalRecipes;
import hungteen.immortal.common.world.ImmortalNoiseGenSettings;
import hungteen.immortal.common.world.LevelManager;
import hungteen.immortal.common.world.biome.BiomeManager;
import hungteen.immortal.common.world.biome.ImmortalBiomes;
import hungteen.immortal.common.world.dimension.ImmortalDimensions;
import hungteen.immortal.common.world.feature.ImmortalConfiguredFeatures;
import hungteen.immortal.common.world.feature.ImmortalPlacedFeatures;
import hungteen.immortal.common.world.structure.ImmortalProcessors;
import hungteen.immortal.common.world.structure.ImmortalStructures;
import hungteen.immortal.common.world.structure.ImmortalTemplatePools;
import hungteen.immortal.data.DataGenHandler;
import hungteen.immortal.common.impl.*;
import hungteen.immortal.utils.ItemUtil;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-23 23:32
 **/
@Mod(ImmortalMod.MOD_ID)
public class ImmortalMod {

    public static final String MOD_ID = "immortal";

    public static CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public ImmortalMod() {
        /* Mod Bus Events */
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, ImmortalMod::setUp);
        modBus.addListener(EventPriority.NORMAL, DataGenHandler::dataGen);
        modBus.addListener(EventPriority.NORMAL, CapabilityHandler::registerCapabilities);
        modBus.addListener(EventPriority.NORMAL, ImmortalMod::register);
        modBus.addListener(EventPriority.NORMAL, ImmortalEntities::addEntityAttributes);
        defferRegister(modBus);

        /* Forge Bus Events */
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, CapabilityHandler::attachCapabilities);
        forgeBus.addListener(EventPriority.NORMAL, CommandHandler::init);
        forgeBus.addListener(EventPriority.NORMAL, ImmortalDataPacks::addDataPack);
        forgeBus.addListener(EventPriority.NORMAL, ImmortalMod::serverStarted);

        /* Config Setup */
        ImmortalConfigs.init();

        /* Custom Registry */
        coreRegister();
    }

    /**
     * register minecraft stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void defferRegister(IEventBus modBus) {
        ImmortalBiomes.register();
        ImmortalStructures.register();

        ImmortalItems.ITEMS.register(modBus);
        ImmortalBlocks.BLOCKS.register(modBus);
        ImmortalEntities.ENTITY_TYPES.register(modBus);
        ImmortalBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ImmortalMenus.CONTAINER_TYPES.register(modBus);
        ImmortalSchedules.SCHEDULES.register(modBus);
        ImmortalRecipes.RECIPE_SERIALIZERS.register(modBus);
        ImmortalRecipes.RECIPE_TYPES.register(modBus);
        ImmortalParticles.register(modBus);
        ImmortalDataSerializers.register(modBus);
        ImmortalMemories.register(modBus);
        ImmortalSensors.register(modBus);
        ImmortalActivities.register(modBus);
        ImmortalProcessors.register(modBus);
        ImmortalTemplatePools.register(modBus);
        ImmortalPoiTypes.register(modBus);
        ImmortalProfessions.register(modBus);
        ImmortalConfiguredFeatures.register(modBus);
        ImmortalPlacedFeatures.register(modBus);
        ImmortalNoiseGenSettings.register(modBus);
        ImmortalDimensions.register(modBus);
    }

    /**
     * register minecraft stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void register(RegisterEvent event) {
        if(ForgeRegistries.ITEMS.equals(event.getForgeRegistry())){
            ImmortalEntities.registerSpawnEggs(event);
            ImmortalBlocks.registerBlockItems(event);
            ImmortalItems.registerItems(event);
        } else if(ForgeRegistries.BLOCKS.equals(event.getForgeRegistry())){
            ImmortalBlocks.registerBlocks(event);
        }
    }

    /**
     * register custom stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void coreRegister() {
        ImmortalWoods.register();
        SpiritualTypes.SpiritualType.register();
        SpellTypes.SpellType.register();
        PlayerRangeNumbers.PlayerData.register();
        RealmTypes.RealmType.register();
        MemoryRunes.MemoryRune.register();
        SensorRunes.SensorRune.register();
        BehaviorRunes.BehaviorRune.register();
        InventoryLootTypes.InventoryLootType.register();
//        ItemUtil.registerLargeHeldItems();
    }

    public static void setUp(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
//            PVZFeatures.registerFeatures();
////            PotionRecipeHandler.registerPotionRecipes();
//            SpawnRegister.registerEntitySpawns();
            CommonRegister.registerCompostable();
            LevelManager.registerSpiritualLevels();
            BiomeManager.registerSpiritualBiomes();
            ElixirManager.init();
            RealmManager.updateRealmTree();
            CapabilityHandler.init();
        });
        NetworkHandler.init();
    }

    public static void serverStarted(ServerStartedEvent event){
    }

}
