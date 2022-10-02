package hungteen.immortal;

import hungteen.immortal.capability.CapabilityHandler;
import hungteen.immortal.command.CommandHandler;
import hungteen.immortal.entity.ImmortalEntities;
import hungteen.immortal.impl.Spells;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.network.NetworkHandler;
import hungteen.immortal.world.structure.ImmortalStructures;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
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

    public ImmortalMod() {
        //get mod event bus.
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, ImmortalMod::setUp);
        modBus.addListener(EventPriority.NORMAL, CapabilityHandler::registerCapabilities);

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
        ImmortalEntities.ENTITY_TYPES.register(modBus);
        ImmortalStructures.STRUCTURES.register(modBus);
    }

    /**
     * register custom stuffs at {@link ImmortalMod#ImmortalMod()}.
     */
    public static void coreRegister() {
        SpiritualRoots.SpiritualRoot.register();
        Spells.Spell.register();
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
        });

        NetworkHandler.init();
    }

}
