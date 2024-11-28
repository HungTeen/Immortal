package hungteen.imm.client;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.render.entity.EmptyEffectRender;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.client.gui.overlay.CommonOverlay;
import hungteen.imm.client.gui.overlay.ElementOverlay;
import hungteen.imm.client.gui.overlay.MeditationOverlay;
import hungteen.imm.client.gui.overlay.SpellOverlay;
import hungteen.imm.client.gui.tooltip.ClientManualToolTip;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.*;
import hungteen.imm.client.model.entity.golem.CopperGolemModel;
import hungteen.imm.client.model.entity.golem.CreeperGolemModel;
import hungteen.imm.client.model.entity.golem.IronGolemModel;
import hungteen.imm.client.model.entity.golem.SnowGolemModel;
import hungteen.imm.client.model.entity.spirit.*;
import hungteen.imm.client.model.entity.villager.PillagerModel;
import hungteen.imm.client.model.entity.villager.VillagerModel;
import hungteen.imm.client.render.entity.creature.monster.BiFangRender;
import hungteen.imm.client.render.entity.creature.monster.SharpStakeRender;
import hungteen.imm.client.render.entity.golem.CopperGolemRender;
import hungteen.imm.client.render.entity.golem.CreeperGolemRender;
import hungteen.imm.client.render.entity.golem.IronGolemRender;
import hungteen.imm.client.render.entity.golem.SnowGolemRender;
import hungteen.imm.client.render.entity.human.CommonVillagerRender;
import hungteen.imm.client.render.entity.human.CultivatorRender;
import hungteen.imm.client.render.entity.misc.ElementCrystalRender;
import hungteen.imm.client.render.entity.misc.FlyingItemEntityRender;
import hungteen.imm.client.render.entity.misc.ThrowingItemEntityRender;
import hungteen.imm.client.render.entity.misc.TornadoRender;
import hungteen.imm.client.render.entity.spirit.*;
import hungteen.imm.common.block.plants.GourdGrownBlock;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.item.blockitem.GourdBlockItem;
import hungteen.imm.common.item.elixirs.ElixirItem;
import hungteen.imm.common.item.talismans.TalismanItem;
import hungteen.imm.common.menu.tooltip.ManualToolTip;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-02 14:59
 **/
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {

    @SubscribeEvent
    public static void setUpClient(FMLClientSetupEvent ev){
        ClientHandler.registerCultivatorTypes();
        ev.enqueueWork(() -> {
            registerScreen();
            registerItemProperties();
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

        /* Human */
        event.registerEntityRenderer(IMMEntities.COMMON_VILLAGER.get(), CommonVillagerRender::new);
        event.registerEntityRenderer(IMMEntities.EMPTY_CULTIVATOR.get(), CultivatorRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(), CultivatorRender::new);

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

//        /* undead */
//        event.registerEntityRenderer(ImmortalEntities.SPIRITUAL_ZOMBIE.get(), SpiritualZombieRender::new);

        /* Golem */
        event.registerEntityRenderer(IMMEntities.IRON_GOLEM.get(), IronGolemRender::new);
        event.registerEntityRenderer(IMMEntities.SNOW_GOLEM.get(), SnowGolemRender::new);
        event.registerEntityRenderer(IMMEntities.CREEPER_GOLEM.get(), CreeperGolemRender::new);
        event.registerEntityRenderer(IMMEntities.COPPER_GOLEM.get(), CopperGolemRender::new);

        /* Block Entity */
//        event.registerBlockEntityRenderer(IMMBlockEntities.SPIRITUAL_FURNACE.get(), FurnaceBlockEntityRender::new);
    }

    /**
     * {@link LayerDefinitions}
     */
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LayerDefinition INNER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);
        LayerDefinition OUTER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);

        /* Misc */
        event.registerLayerDefinition(IMMModelLayers.ELEMENT_CRYSTAL, ElementCrystalModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.TORNADO, TornadoModel::createBodyLayer);

        /* Human */
        event.registerLayerDefinition(IMMModelLayers.VILLAGER, VillagerModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.PILLAGER, PillagerModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, false));
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_OUTER_ARMOR, () -> OUTER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_SLIM, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, true));
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_SLIM_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_SLIM_OUTER_ARMOR, () -> OUTER_ARMOR);

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

        /* Zombie */
        event.registerLayerDefinition(IMMModelLayers.SPIRITUAL_ZOMBIE, SpiritualZombieModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.SPIRITUAL_ZOMBIE_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.SPIRITUAL_ZOMBIE_OUTER_ARMOR, () -> OUTER_ARMOR);

        /* Golem */
        event.registerLayerDefinition(IMMModelLayers.IRON_GOLEM, IronGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.SNOW_GOLEM, SnowGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.CREEPER_GOLEM, CreeperGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.COPPER_GOLEM, CopperGolemModel::createBodyLayer);
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

//    @SubscribeEvent
//    public static void registerBlockColors(RegisterColorHandlersEvent.Block event){
//        event.initialize((blockState, getter, pos, id) -> {
//            if(blockState.getBlock() instanceof GourdGrownBlock gourdGrownBlock){
//                return gourdGrownBlock.getType().getColor();
//            }
//            return ColorHelper.BLACK.rgb();
//        }, BlockUtil.getGourds().stream().map(Pair::getSecond).toArray(GourdGrownBlock[]::new));
//    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        ItemHelper.get().filterValues(ElixirItem.class::isInstance).stream().map(ElixirItem.class::cast).forEach(elixirItem -> {
            event.register((stack, id) -> elixirItem.getColor(id), elixirItem);
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
//        event.initialize(ArtifactToolTip.class, ClientArtifactToolTip::new);
        event.register(ManualToolTip.class, ClientManualToolTip::new);
    }

    @SubscribeEvent
    public static void registerLevelRenderStages(RenderLevelStageEvent.RegisterStageEvent event){
//        LevelRenderStages.init(event);
    }

    @SubscribeEvent
    public static void bakeModel(ModelEvent.ModifyBakingResult event) {
//        IMMBakeModels.registerBakeModels(event);
    }

    @SubscribeEvent
    public static void bakeModel(ModelEvent.RegisterAdditional event) {
//        IMMBakeModels.registerBakeModels(event);
    }


    public static void registerScreen() {
//        MenuScreens.initialize(IMMMenus.CULTIVATOR_TRADE.get(), MerchantTradeScreen::new);
//        MenuScreens.initialize(IMMMenus.SPIRITUAL_FURNACE.get(), SpiritualFurnaceScreen::new);
//        MenuScreens.initialize(IMMMenus.ELIXIR_ROOM.get(), ElixirRoomScreen::new);
////        MenuScreens.initialize(ImmortalMenus.SMITHING_ARTIFACT.get(), SmithingArtifactScreen::new);
//        MenuScreens.initialize(IMMMenus.GOLEM_INVENTORY.get(), GolemInventoryScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_CRAFT.get(), RuneCraftScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_GATE.get(), RuneGateScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_BIND.get(), RuneBindScreen::new);
    }

    public static void registerItemProperties(){
        ItemHelper.get().filterValues(TalismanItem.class::isInstance).forEach(talisman -> {
            ItemProperties.register(talisman, TalismanItem.ACTIVATE_PROPERTY, (stack, level, entity, val) -> {
                return TalismanItem.isActivated(stack) ? 1F : 0F;
            });
        });
    }
}
