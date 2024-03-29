package hungteen.imm;

import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.CommonRegister;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.advancement.AdvancementHandler;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.blockentity.IMMBlockEntities;
import hungteen.imm.common.capability.CapabilityHandler;
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
import hungteen.imm.common.impl.raid.IMMRaidHandler;
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
        modBus.addListener(EventPriority.NORMAL, IMMEntities::registerPlacements);
        modBus.addListener(EventPriority.NORMAL, IMMCreativeTabs::fillCreativeTabs);
        defferRegister(modBus);

        /* Forge Bus Events */
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addGenericListener(Entity.class, CapabilityHandler::attachEntityCapabilities);
        forgeBus.addGenericListener(LevelChunk.class, CapabilityHandler::attachChunkCapabilities);
        forgeBus.addListener(EventPriority.NORMAL, CommandHandler::init);
//        forgeBus.addListener(EventPriority.NORMAL, IMMDataPacks::addDataPack);
        forgeBus.addListener(EventPriority.NORMAL, ImmortalMod::serverStarted);
        forgeBus.addListener(EventPriority.LOWEST, RealmManager::realmAttackGap);

        /* Misc Initialization */
        IMMConfigs.init();
        AdvancementHandler.init();

        /* Custom Registry */
        coreRegister();
    }

    /**
     * register minecraft stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void defferRegister(IEventBus modBus) {
        /* HungTeen Registers */
        CultivationTypes.registry().register(modBus);
        RealmTypes.registry().register(modBus);
        SpellTypes.registry().register(modBus);
        ElixirEffects.registry().register(modBus);
        HumanSettings.registry().register(modBus);
        ManualTypes.registry().register(modBus);
        RequirementTypes.registry().register(modBus);
        SecretManuals.registry().register(modBus);
        SpiritualTypes.registry().register(modBus);

        /* Deferred Registers */
        IMMItems.register(modBus);
        IMMBlocks.register(modBus);
        IMMEntities.register(modBus);
        IMMAttributes.register(modBus);
        IMMCreativeTabs.register(modBus);
        IMMBlockEntities.register(modBus);
        IMMEffects.register(modBus);
        IMMSchedules.register(modBus);
        IMMRecipes.register(modBus);
        IMMRecipeSerializers.register(modBus);
        IMMMenus.register(modBus);
        IMMBannerPatterns.register(modBus);
        IMMParticles.register(modBus);
        IMMDataSerializers.register(modBus);
        IMMMemories.register(modBus);
        IMMSensors.register(modBus);
        IMMActivities.register(modBus);
        IMMPoiTypes.register(modBus);
        IMMProfessions.register(modBus);
        IMMSounds.register(modBus);
//        IMMPoolTypes.register(modBus);
        IMMStructureTypes.register(modBus);
        IMMStructurePieces.register(modBus);
        IMMFeatures.register(modBus);
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
        IMMDummyEntities.init();
        IMMRaidHandler.init();

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
            CapabilityHandler.init();
        });
        NetworkHandler.init();
    }

    public static void serverStarted(ServerStartedEvent event){
    }

}
