package hungteen.imm.client.render;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-02 20:37
 **/
public class IMMRenderTypes {

    protected static final RenderStateShard.ShaderStateShard ENTITY_TRANSLUCENT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEntityTranslucentShader);

//    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL = Util.memoize((p_269667_, p_269668_) -> {
//        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
//        .setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
//        .setTextureState(new RenderStateShard.TextureStateShard(p_269667_, false, false))
//        .setTransparencyState(NO_TRANSPARENCY)
//        .setCullState(NO_CULL)
//        .setLightmapState(LIGHTMAP)
//        .setOverlayState(OVERLAY)
//        .createCompositeState(p_269668_);
//        return create("entity_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
//    });

//    private static final Function<ResourceLocation, RenderType> EYES = Util.memoize((p_269683_) -> {
//        RenderStateShard.TextureStateShard renderstateshard$texturestateshard =
//        new RenderStateShard.TextureStateShard(p_269683_, false, false);
//        return create("eyes", DefaultVertexFormat.NEW_ENTITY,
//        VertexFormat.Mode.QUADS, 256, false, true,
//        RenderType.CompositeState.builder()
//        .setShaderState(RENDERTYPE_EYES_SHADER)
//        .setTextureState(renderstateshard$texturestateshard)
//        .setTransparencyState(ADDITIVE_TRANSPARENCY)
//        .setWriteMaskState(COLOR_WRITE)
//        .createCompositeState(false));
//    });

//    private static final Function<ResourceLocation, RenderType> ELEMENTS = Util.memoize((location) -> {
//        final RenderStateShard.TextureStateShard stateShard = new RenderStateShard.TextureStateShard(location, false, false);
//        final RenderType.CompositeState state = RenderType.CompositeState.builder()
//                .setShaderState(ENTITY_TRANSLUCENT_SHADER)
//                .setTextureState(stateShard)
//                .setTransparencyState(ADDITIVE_TRANSPARENCY)
//                .setOverlayState(RenderType.OVERLAY)
//                .createCompositeState(false);
//        return RenderType.create("eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, state);
//    });
}
