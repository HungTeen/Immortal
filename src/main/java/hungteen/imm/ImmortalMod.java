package hungteen.imm;

import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.CommonRegister;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.blockentity.IMMBlockEntities;
import hungteen.imm.common.capability.CapabilityHandler;
import hungteen.imm.common.command.CommandHandler;
import hungteen.imm.common.datapack.IMMDataPacks;
import hungteen.imm.common.entity.IMMDataSerializers;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.ai.*;
import hungteen.imm.common.impl.BehaviorRunes;
import hungteen.imm.common.impl.MemoryRunes;
import hungteen.imm.common.impl.SensorRunes;
import hungteen.imm.common.impl.codec.HumanSettings;
import hungteen.imm.common.impl.registry.*;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.menu.IMMMenus;
import hungteen.imm.common.misc.IMMBannerPatterns;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.recipe.IMMRecipes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.world.LevelManager;
import hungteen.imm.common.world.levelgen.biome.BiomeManager;
import hungteen.imm.common.world.levelgen.feature.ImmortalConfiguredFeatures;
import hungteen.imm.data.DataGenHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
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

    public static final String MOD_ID = "imm";

    public static CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public ImmortalMod() {
        /* Mod Bus Events */
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, ImmortalMod::setUp);
        modBus.addListener(EventPriority.NORMAL, DataGenHandler::dataGen);
        modBus.addListener(EventPriority.NORMAL, CapabilityHandler::registerCapabilities);
        modBus.addListener(EventPriority.NORMAL, ImmortalMod::register);
        modBus.addListener(EventPriority.NORMAL, IMMEntities::addEntityAttributes);
        modBus.addListener(EventPriority.NORMAL, CommonRegister::fillCreativeTabs);
        defferRegister(modBus);

        /* Forge Bus Events */
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, CapabilityHandler::attachEntityCapabilities);
        forgeBus.addGenericListener(LevelChunk.class, CapabilityHandler::attachChunkCapabilities);
        forgeBus.addListener(EventPriority.NORMAL, CommandHandler::init);
        forgeBus.addListener(EventPriority.NORMAL, IMMDataPacks::addDataPack);
        forgeBus.addListener(EventPriority.NORMAL, ImmortalMod::serverStarted);

        /* Config Setup */
        IMMConfigs.init();

        /* Custom Registry */
        coreRegister();
    }

    /**
     * register minecraft stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void defferRegister(IEventBus modBus) {
        IMMItems.register(modBus);
        IMMBlocks.register(modBus);
        IMMEntities.register(modBus);
        IMMBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ImmortalSchedules.SCHEDULES.register(modBus);
        IMMRecipes.RECIPE_SERIALIZERS.register(modBus);
        IMMRecipes.RECIPE_TYPES.register(modBus);
        IMMMenus.register(modBus);
        IMMBannerPatterns.register(modBus);
        IMMParticles.register(modBus);
        IMMDataSerializers.register(modBus);
        ImmortalMemories.register(modBus);
        ImmortalSensors.register(modBus);
        ImmortalActivities.register(modBus);
        ImmortalPoiTypes.register(modBus);
        ImmortalProfessions.register(modBus);
        ImmortalConfiguredFeatures.register(modBus);
    }

    /**
     * register minecraft stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void register(RegisterEvent event) {
        if(ForgeRegistries.ITEMS.equals(event.getForgeRegistry())){
            IMMEntities.registerSpawnEggs(event);
            IMMBlocks.registerBlockItems(event);
            IMMItems.registerItems(event);
        } else if(ForgeRegistries.BLOCKS.equals(event.getForgeRegistry())){
            IMMBlocks.registerBlocks(event);
        }
    }

    /**
     * register custom stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void coreRegister() {
        IMMWoods.register();

        HumanSettings.register();

        TradeTypes.TradeType.register();
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
            RealmManager.updateRealmTree();
            CapabilityHandler.init();
        });
        NetworkHandler.init();
    }

    public static void serverStarted(ServerStartedEvent event){
    }

}
