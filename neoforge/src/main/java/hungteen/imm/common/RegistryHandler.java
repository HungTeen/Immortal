package hungteen.imm.common;

import hungteen.htlib.common.HTLibProxy;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.impl.BlockHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.client.render.IMMEntityRenderers;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.IMMPoiTypes;
import hungteen.imm.common.blockentity.IMMBlockEntities;
import hungteen.imm.common.capability.IMMAttachments;
import hungteen.imm.common.cultivation.*;
import hungteen.imm.common.cultivation.rune.behavior.BehaviorRunes;
import hungteen.imm.common.cultivation.rune.filter.FilterRuneTypes;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.IMMDataSerializers;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.ai.*;
import hungteen.imm.common.entity.effect.IMMEffects;
import hungteen.imm.common.entity.human.HumanSettings;
import hungteen.imm.common.impl.codec.ElixirEffects;
import hungteen.imm.common.cultivation.impl.ScrollTypes;
import hungteen.imm.common.cultivation.impl.SecretManuals;
import hungteen.imm.common.cultivation.manual.requirement.RequirementTypes;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.impl.registry.TradeTypes;
import hungteen.imm.common.item.*;
import hungteen.imm.common.menu.IMMMenuTypes;
import hungteen.imm.common.recipe.IMMRecipeSerializers;
import hungteen.imm.common.recipe.IMMRecipes;
import hungteen.imm.common.world.entity.IMMDummyEntities;
import hungteen.imm.common.world.feature.IMMFeatures;
import hungteen.imm.common.world.structure.IMMStructurePieces;
import hungteen.imm.common.world.structure.IMMStructureTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/25 10:14
 **/
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = IMMAPI.MOD_ID)
public class RegistryHandler {

    /**
     * 给所有生物添加灵气上限属性。
     */
    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event){
        event.getTypes().forEach(type -> {
            if(! event.has(type, IMMAttributes.MAX_QI_AMOUNT.holder())){
                event.add(type, IMMAttributes.MAX_QI_AMOUNT.holder(), 0D);
            }
        });
    }

    @SubscribeEvent
    public static void postRegister(RegisterEvent event) {
        if(NeoHelper.canRegister(event, ItemHelper.get())){
            IMMEntities.registerSpawnEggs(event);
            IMMBlocks.registerBlockItems(event);
            IMMItems.registerItems(event);
        } else if(NeoHelper.canRegister(event, BlockHelper.get())){
            IMMBlocks.registerBlocks(event);
        }
    }

    public static void defferRegister(IEventBus modBus) {
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
        IMMMenuTypes.initialize(modBus);
        IMMParticles.initialize(modBus);
        IMMDataSerializers.initialize(modBus);
        IMMMemories.initialize(modBus);
        IMMSensors.initialize(modBus);
        IMMActivities.initialize(modBus);
        IMMPoiTypes.initialize(modBus);
        IMMProfessions.initialize(modBus);
        IMMSounds.initialize(modBus);
        IMMDataComponents.initialize(modBus);
//        IMMPoolTypes.initialize(modBus);
        IMMStructureTypes.initialize(modBus);
        IMMStructurePieces.initialize(modBus);
        IMMFeatures.initialize(modBus);
        IMMAttachments.initialize(modBus);
        IMMMapDecorationTypes.initialize(modBus);
    }

    public static void initialize() {
        /* HTLib */
//        IMMWoods.postRegister();
        IMMDummyEntities.initialize();

        /* IMM */
        CultivationTypes.registry().initialize();
        RealmTypes.registry().initialize();
        TriggerConditions.registry().initialize();
        SpellTypes.registry().initialize();
        InscriptionTypes.registry().initialize();
        ElixirEffects.registry().initialize();
        HumanSettings.registry().initialize();
        ScrollTypes.registry().initialize();
        RequirementTypes.registry().initialize();
        SecretManuals.registry().initialize();
        QiRootTypes.registry().initialize();
        TradeTypes.TradeType.register();
        ElementReactions.registry().initialize();
        BehaviorRunes.register();
        FilterRuneTypes.register();
        SectTypes.register();
//        ItemUtil.registerLargeHeldItems();
        /* Client */
        HTLibProxy.get().runOnClient(() -> () -> {
            IMMEntityRenderers.initialize();
        });
    }

    /**
     * {@link IMMInitializer#setUp(FMLCommonSetupEvent)}
     */
    public static void registerCompostable() {
        BlockHelper.registerCompostable(0.3F, IMMBlocks.MULBERRY_LEAVES.get());
        BlockHelper.registerCompostable(0.3F, IMMBlocks.MULBERRY_SAPLING.get());
        BlockHelper.registerCompostable(0.65F, IMMItems.MULBERRY.get());
    }
}
