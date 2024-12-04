package hungteen.imm.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL14;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-02 20:37
 **/
public class IMMRenderType extends RenderType{

    private static final TransparencyStateShard DUMMY_TRANSPARENCY = new TransparencyStateShard("ghost_transparency",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.3F);
            },
            () -> {
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });

    public static final RenderType DUMMY_BLOCK = create(
            Util.prefixName("dummy_block"),
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            2097152,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_SOLID_SHADER)
                    .setTextureState(BLOCK_SHEET)
                    .setTransparencyState(DUMMY_TRANSPARENCY)
                    .createCompositeState(false)
    );

    public static RenderType elementIcon(ResourceLocation location, float u, float v) {
        return create(
                "element_icon",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(location, false, false))
                        .setTexturingState(new RenderStateShard.OffsetTexturingStateShard(u, v))
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(false)
        );
    }

//    public static final RenderType LIGHT_ENTITY = create(
//            "eyes",
//            DefaultVertexFormat.NEW_ENTITY,
//            VertexFormat.Mode.QUADS,
//            256,
//            false,
//            true,
//            RenderType.CompositeState.builder()
//                    .setShaderState(RENDERTYPE_EYES_SHADER)
//                    .setTextureState(new RenderStateShard.TextureStateShard(p_286170_, false, false))
//                    .setTransparencyState(ADDITIVE_TRANSPARENCY)
//                    .setWriteMaskState(COLOR_WRITE)
//                    .createCompositeState(false)
//    );
            /**
             * NO USE.
             */
    private IMMRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

}
