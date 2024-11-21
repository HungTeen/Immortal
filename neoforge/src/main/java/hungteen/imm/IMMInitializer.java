package hungteen.imm;

import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.CommonRegister;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.advancement.AdvancementHandler;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.blockentity.IMMBlockEntities;
import hungteen.imm.common.capability.IMMAttachments;
import hungteen.imm.common.command.CommandHandler;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.IMMDataSerializers;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.ai.*;
import hungteen.imm.common.entity.human.setting.HumanSettings;
import hungteen.imm.common.impl.codec.ElixirEffects;
import hungteen.imm.common.impl.manuals.ManualTypes;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.common.impl.manuals.requirments.RequirementTypes;
import hungteen.imm.common.impl.registry.*;
import hungteen.imm.common.item.IMMCreativeTabs;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.menu.IMMMenus;
import hungteen.imm.common.misc.IMMBannerPatterns;
import hungteen.imm.common.misc.IMMSounds;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.recipe.IMMRecipeSerializers;
import hungteen.imm.common.recipe.IMMRecipes;
import hungteen.imm.common.rune.behavior.BehaviorRunes;
import hungteen.imm.common.rune.filter.FilterRuneTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.world.entity.IMMDummyEntities;
import hungteen.imm.common.world.feature.IMMFeatures;
import hungteen.imm.common.world.structure.IMMStructurePieces;
import hungteen.imm.common.world.structure.IMMStructureTypes;
import hungteen.imm.data.DataGenHandler;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-23 23:32
 **/
@Mod(IMMAPI.MOD_ID)
public class IMMInitializer {

    public static final String MOD_ID = IMMAPI.id();

    public IMMInitializer(IEventBus modBus, ModContainer container) {
        /* Mod Bus Events */
        modBus.addListener(EventPriority.NORMAL, IMMInitializer::setUp);
        modBus.addListener(EventPriority.NORMAL, DataGenHandler::dataGen);
//        modBus.addListener(EventPriority.NORMAL, CapabilityHandler::registerCapabilities);
        modBus.addListener(EventPriority.NORMAL, IMMInitializer::register);
        modBus.addListener(EventPriority.NORMAL, IMMEntities::addEntityAttributes);
        modBus.addListener(EventPriority.NORMAL, IMMEntities::registerPlacements);
        modBus.addListener(EventPriority.NORMAL, IMMCreativeTabs::fillCreativeTabs);
        defferRegister(modBus);

        /* Forge Bus Events */
        final IEventBus forgeBus = NeoForge.EVENT_BUS;
//        forgeBus.addListener(Entity.class, CapabilityHandler::attachEntityCapabilities);
//        forgeBus.addListener(LevelChunk.class, CapabilityHandler::attachChunkCapabilities);
        forgeBus.addListener(EventPriority.NORMAL, CommandHandler::init);
//        forgeBus.addListener(EventPriority.NORMAL, IMMDataPacks::addDataPack);
        forgeBus.addListener(EventPriority.NORMAL, IMMInitializer::serverStarted);
//        forgeBus.addListener(EventPriority.LOWEST, RealmManager::realmAttackGap);

        /* Misc Initialization */
        IMMConfigs.init(container);
        AdvancementHandler.init();

        /* Custom Registry */
        coreRegister();
    }

    public static void defferRegister(IEventBus modBus) {
        /* HungTeen Registers */
        CultivationTypes.registry().initialize();
        RealmTypes.registry().initialize();
        SpellTypes.registry().initialize();
        ElixirEffects.registry().initialize();
        HumanSettings.registry().initialize();
        ManualTypes.registry().initialize();
        RequirementTypes.registry().initialize();
        SecretManuals.registry().initialize();
        QiRootTypes.registry().initialize();

        /* Deferred Registers */
        IMMItems.initialize(modBus);
        IMMBlocks.initialize(modBus);
        IMMEntities.initialize(modBus);
        IMMAttributes.initialize(modBus);
        IMMCreativeTabs.initialize(modBus);
        IMMBlockEntities.initialize(modBus);
        IMMEffects.initialize(modBus);
        IMMSchedules.initialize(modBus);
        IMMRecipes.initialize(modBus);
        IMMRecipeSerializers.initialize(modBus);
        IMMMenus.initialize(modBus);
        IMMBannerPatterns.initialize(modBus);
        IMMParticles.initialize(modBus);
        IMMDataSerializers.initialize(modBus);
        IMMMemories.initialize(modBus);
        IMMSensors.initialize(modBus);
        IMMActivities.initialize(modBus);
        IMMPoiTypes.initialize(modBus);
        IMMProfessions.initialize(modBus);
        IMMSounds.initialize(modBus);
//        IMMPoolTypes.initialize(modBus);
        IMMStructureTypes.initialize(modBus);
        IMMStructurePieces.initialize(modBus);
        IMMFeatures.initialize(modBus);
        IMMAttachments.register(modBus);
    }

    public static void register(RegisterEvent event) {
        if(NeoHelper.canRegister(event, ItemHelper.get())){
            IMMEntities.registerSpawnEggs(event);
            IMMBlocks.registerBlockItems(event);
            IMMItems.registerItems(event);
        } else if(NeoHelper.canRegister(event, BlockHelper.get())){
            IMMBlocks.registerBlocks(event);
        }
    }

    public static void coreRegister() {
        IMMWoods.register();
        IMMDummyEntities.init();
//        IMMRaidHandler.init();

        TradeTypes.TradeType.register();
        PlayerRangeFloats.registry();
        PlayerRangeIntegers.registry();
        ElementReactions.registry();
        BehaviorRunes.register();
        FilterRuneTypes.register();
        SectTypes.register();
//        ItemUtil.registerLargeHeldItems();
    }

    public static void setUp(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
////            PotionRecipeHandler.registerPotionRecipes();
            CommonRegister.registerCompostable();
            RealmManager.init();
//            CapabilityHandler.init();
        });
        NetworkHandler.init();
    }

    public static void serverStarted(ServerStartedEvent event){
    }

}
