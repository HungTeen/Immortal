package hungteen.imm;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.common.RegistryHandler;
import hungteen.imm.common.advancement.AdvancementHandler;
import hungteen.imm.common.command.IMMCommandHandler;
import hungteen.imm.common.cultivation.realm.RealmNode;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.item.IMMCreativeTabs;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.compat.minecraft.VanillaCultivationCompat;
import hungteen.imm.data.DataGenHandler;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

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
        modBus.addListener(EventPriority.NORMAL, IMMEntities::addEntityAttributes);
        modBus.addListener(EventPriority.NORMAL, IMMEntities::registerPlacements);
        modBus.addListener(EventPriority.NORMAL, IMMCreativeTabs::fillCreativeTabs);
        RegistryHandler.defferRegister(modBus);

        /* Forge Bus Events */
        final IEventBus forgeBus = NeoForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, IMMCommandHandler::init);
//        forgeBus.addListener(EventPriority.NORMAL, IMMDataPacks::addDataPack);
        forgeBus.addListener(EventPriority.NORMAL, IMMInitializer::serverStarted);
//        forgeBus.addListener(EventPriority.LOWEST, RealmManager::realmAttackGap);

        /* Misc Initialization */
        IMMConfigs.init(container);
        AdvancementHandler.init();

        /* Custom Registry */
        RegistryHandler.initialize();
    }

    public static void setUp(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
////            PotionRecipeHandler.registerPotionRecipes();
            RegistryHandler.registerCompostable();
            VanillaCultivationCompat.fillEntityMap();
            RealmNode.updateRealmTree();
        });
        NetworkHandler.init();
    }

    public static void serverStarted(ServerStartedEvent event){
    }

}
