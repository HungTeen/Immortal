package hungteen.imm.client;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.render.entity.EmptyEffectRender;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.client.extension.TalismanExtension;
import hungteen.imm.client.gui.overlay.CommonOverlay;
import hungteen.imm.client.gui.overlay.ElementOverlay;
import hungteen.imm.client.gui.overlay.MeditationOverlay;
import hungteen.imm.client.gui.overlay.SpellOverlay;
import hungteen.imm.client.gui.screen.InscriptionTableScreen;
import hungteen.imm.client.gui.tooltip.ClientArtifactToolTip;
import hungteen.imm.client.gui.tooltip.ClientManualToolTip;
import hungteen.imm.client.gui.tooltip.ClientTalismanToolTip;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.bake.IMMBakeModels;
import hungteen.imm.client.model.entity.BiFangModel;
import hungteen.imm.client.model.entity.GrassCarpModel;
import hungteen.imm.client.model.entity.SilkWormModel;
import hungteen.imm.client.model.entity.golem.CopperGolemModel;
import hungteen.imm.client.model.entity.golem.CreeperGolemModel;
import hungteen.imm.client.model.entity.golem.IronGolemModel;
import hungteen.imm.client.model.entity.golem.SnowGolemModel;
import hungteen.imm.client.model.entity.human.villager.PillagerModel;
import hungteen.imm.client.model.entity.human.villager.VillagerModel;
import hungteen.imm.client.model.entity.misc.CubeModel;
import hungteen.imm.client.model.entity.misc.ElementCrystalModel;
import hungteen.imm.client.model.entity.misc.TornadoModel;
import hungteen.imm.client.model.entity.spirit.*;
import hungteen.imm.client.render.entity.creature.monster.BiFangRender;
import hungteen.imm.client.render.entity.creature.monster.SharpStakeRender;
import hungteen.imm.client.render.entity.golem.CopperGolemRender;
import hungteen.imm.client.render.entity.golem.CreeperGolemRender;
import hungteen.imm.client.render.entity.golem.IronGolemRender;
import hungteen.imm.client.render.entity.golem.SnowGolemRender;
import hungteen.imm.client.render.entity.misc.*;
import hungteen.imm.client.render.entity.spirit.*;
import hungteen.imm.common.block.plants.GourdGrownBlock;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.item.blockitem.GourdBlockItem;
import hungteen.imm.common.item.elixir.ElixirItem;
import hungteen.imm.common.item.talisman.DurationTalismanItem;
import hungteen.imm.common.menu.IMMMenuTypes;
import hungteen.imm.common.menu.tooltip.ArtifactToolTip;
import hungteen.imm.common.menu.tooltip.ManualToolTip;
import hungteen.imm.common.menu.tooltip.TalismanToolTip;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-02 14:59
 **/
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {

    @SubscribeEvent
    public static void setUpClient(FMLClientSetupEvent ev){
        ev.enqueueWork(() -> {
            ClientHandler.registerItemProperties();
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        /* Misc Entity */
        event.registerEntityRenderer(IMMEntities.ELEMENT_AMETHYST.get(), ElementCrystalRender::new);
        event.registerEntityRenderer(IMMEntities.TELEPORT_FORMATION.get(), EmptyEffectRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_PEARL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IMMEntities.FLYING_ITEM.get(), FlyingItemEntityRender::new);
        event.registerEntityRenderer(IMMEntities.THROWING_ITEM.get(), ThrowingItemEntityRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_FLAME.get(), EmptyEffectRender::new);
        event.registerEntityRenderer(IMMEntities.POISON_WIND.get(), EmptyEffectRender::new);
        event.registerEntityRenderer(IMMEntities.TORNADO.get(), TornadoRender::new);

        /* Creature */
//        event.registerEntityRenderer(ImmortalEntities.GRASS_CARP.get(), GrassCarpRender::new);
//        event.registerEntityRenderer(ImmortalEntities.SILK_WORM.get(), SilkWormRender::new);

        /* Monster */
        event.registerEntityRenderer(IMMEntities.SHARP_STAKE.get(), SharpStakeRender::new);
        event.registerEntityRenderer(IMMEntities.BI_FANG.get(), BiFangRender::new);

        /* Spirit */
        event.registerEntityRenderer(IMMEntities.METAL_SPIRIT.get(), MetalSpiritRender::new);
        event.registerEntityRenderer(IMMEntities.WOOD_SPIRIT.get(), WoodSpiritRender::new);
        event.registerEntityRenderer(IMMEntities.WATER_SPIRIT.get(), WaterSpiritRender::new);
        event.registerEntityRenderer(IMMEntities.FIRE_SPIRIT.get(), FireSpiritRender::new);
        event.registerEntityRenderer(IMMEntities.EARTH_SPIRIT.get(), EarthSpiritRender::new);

        /* Golem */
        event.registerEntityRenderer(IMMEntities.IRON_GOLEM.get(), IronGolemRender::new);
        event.registerEntityRenderer(IMMEntities.SNOW_GOLEM.get(), SnowGolemRender::new);
        event.registerEntityRenderer(IMMEntities.CREEPER_GOLEM.get(), CreeperGolemRender::new);
        event.registerEntityRenderer(IMMEntities.COPPER_GOLEM.get(), CopperGolemRender::new);

        /* Talisman */
        event.registerEntityRenderer(IMMEntities.SPROUT_TALISMAN.getEntityType(), RangeEffectTalismanRender::new);
        event.registerEntityRenderer(IMMEntities.WOOD_HEALING_TALISMAN.getEntityType(), RangeEffectTalismanRender::new);

        /* Block Entity */
//        event.registerBlockEntityRenderer(IMMBlockEntities.SPIRITUAL_FURNACE.get(), FurnaceBlockEntityRender::new);
    }

    /**
     * {@link LayerDefinitions}
     */
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        /* Misc */
        event.registerLayerDefinition(IMMModelLayers.ELEMENT_CRYSTAL, ElementCrystalModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.TORNADO, TornadoModel::createBodyLayer);

        /* Human */
        event.registerLayerDefinition(IMMModelLayers.VILLAGER, VillagerModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.PILLAGER, PillagerModel::createBodyLayer);

        /* Creature */
        event.registerLayerDefinition(IMMModelLayers.GRASS_CARP, GrassCarpModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.SILK_WORM, SilkWormModel::createBodyLayer);

        /* Monster */
        event.registerLayerDefinition(IMMModelLayers.SHARP_STAKE, CubeModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.BI_FANG, BiFangModel::createBodyLayer);

        /* Spirit */
        event.registerLayerDefinition(IMMModelLayers.METAL_SPIRIT, MetalSpiritModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.WOOD_SPIRIT, WoodSpiritModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.WATER_SPIRIT, WaterSpiritModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.FIRE_SPIRIT, FireSpiritModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.EARTH_SPIRIT, EarthSpiritModel::createBodyLayer);

        /* Golem */
        event.registerLayerDefinition(IMMModelLayers.IRON_GOLEM, IronGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.SNOW_GOLEM, SnowGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.CREEPER_GOLEM, CreeperGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.COPPER_GOLEM, CopperGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerMenuScreen(RegisterMenuScreensEvent event){
        event.register(IMMMenuTypes.INSCRIPTION_TABLE.get(), InscriptionTableScreen::new);
        //        MenuScreens.initialize(IMMMenus.CULTIVATOR_TRADE.get(), MerchantTradeScreen::new);
//        MenuScreens.initialize(IMMMenus.SPIRITUAL_FURNACE.get(), SpiritualFurnaceScreen::new);
//        MenuScreens.initialize(IMMMenus.ELIXIR_ROOM.get(), ElixirRoomScreen::new);
////        MenuScreens.initialize(ImmortalMenus.SMITHING_ARTIFACT.get(), SmithingArtifactScreen::new);
//        MenuScreens.initialize(IMMMenus.GOLEM_INVENTORY.get(), GolemInventoryScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_CRAFT.get(), RuneCraftScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_GATE.get(), RuneGateScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_BIND.get(), RuneBindScreen::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event){
        event.registerItem(new TalismanExtension(), ItemHelper.get().filterValues(DurationTalismanItem.class::isInstance).toArray(Item[]::new));
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiLayersEvent event){
        event.registerAboveAll(Util.prefix("qi_bar"), CommonOverlay::renderQiBar);
        event.registerAboveAll(Util.prefix("spell_circle"), SpellOverlay::renderSpellCircle);
        event.registerAboveAll(Util.prefix("prepared_spell"), SpellOverlay::renderPreparedSpell);
        event.registerAboveAll(Util.prefix("elements"), ElementOverlay::renderElementOverBar);
        event.registerAboveAll(Util.prefix("meditation"), MeditationOverlay::renderMeditation);
//        event.registerAboveAll("smithing_progress_bar", (gui, graphics, partialTick, screenWidth, screenHeight) -> {
//            if(RenderEventHandler.canRenderOverlay()){
//                RenderEventHandler.renderSmithingBar(graphics, screenHeight, screenWidth);
//            }
//        });
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event){
        event.register((blockState, getter, pos, id) -> {
            if(blockState.getBlock() instanceof GourdGrownBlock gourdGrownBlock){
                return gourdGrownBlock.getType().getColor();
            }
            return ColorHelper.BLACK.rgb();
        }, BlockUtil.getGourds().stream().map(Pair::getSecond).toArray(GourdGrownBlock[]::new));
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        ItemHelper.get().filterValues(ElixirItem.class::isInstance).stream().map(ElixirItem.class::cast).forEach(elixirItem -> {
            event.register(elixirItem::getColor, elixirItem);
        });
        event.register((stack, id) -> {
            if(stack.getItem() instanceof GourdBlockItem gourdBlockItem){
                return gourdBlockItem.getGourdType().getColor();
            }
            return ColorHelper.BLACK.rgb();
        }, BlockUtil.getGourds().stream().map(Pair::getSecond).toArray(GourdGrownBlock[]::new));
    }

    @SubscribeEvent
    public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event){
//        event.initialize(ElementToolTip.class, ClientElementToolTip::new);
        event.register(ArtifactToolTip.class, ClientArtifactToolTip::new);
        event.register(ManualToolTip.class, ClientManualToolTip::new);
        event.register(TalismanToolTip.class, ClientTalismanToolTip::new);
    }

    @SubscribeEvent
    public static void registerLevelRenderStages(RenderLevelStageEvent.RegisterStageEvent event){
//        LevelRenderStages.init(event);
    }

    @SubscribeEvent
    public static void bakeModel(ModelEvent.ModifyBakingResult event) {
        IMMBakeModels.registerBakeModels(event);
    }

    @SubscribeEvent
    public static void bakeModel(ModelEvent.RegisterAdditional event) {
        IMMBakeModels.registerBakeModels(event);
    }

}
