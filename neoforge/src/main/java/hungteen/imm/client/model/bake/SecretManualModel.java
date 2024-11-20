package hungteen.imm.client.model.bake;

import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.item.SecretManualItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/28 14:50
 */
public class SecretManualModel implements BakedModel {
    private final BakedModel originalModel;
    private final ItemOverrides itemHandler;

    public SecretManualModel(BakedModel originalModel, ModelBakery loader) {
        this.originalModel = originalModel;
        final BlockModel missing = (BlockModel) loader.getModel(ModelBakery.MISSING_MODEL_LOCATION);

        this.itemHandler = new ItemOverrides(new ModelBaker() {

            @Override
            public @org.jetbrains.annotations.Nullable UnbakedModel getTopLevelModel(ModelResourceLocation modelResourceLocation) {
                return null;
            }

            @Override
            public @org.jetbrains.annotations.Nullable BakedModel bake(ResourceLocation location, ModelState state, Function<Material, TextureAtlasSprite> sprites) {
                return null;
            }

            @Override
            public @org.jetbrains.annotations.Nullable BakedModel bakeUncached(UnbakedModel unbakedModel, ModelState modelState, Function<Material, TextureAtlasSprite> function) {
                return null;
            }

            @Override
            public Function<Material, TextureAtlasSprite> getModelTextureGetter() {
                return null;
            }

            @Override
            public UnbakedModel getModel(ResourceLocation p_252194_) {
                return null;
            }

            @org.jetbrains.annotations.Nullable
            @Override
            public BakedModel bake(ResourceLocation p_250776_, ModelState p_251280_) {
                return null;
            }
        }, missing, Collections.emptyList()) {
            @Override
            public BakedModel resolve(@NotNull BakedModel original, @NotNull ItemStack stack,
                                      @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
                final Optional<SecretManual> manual = SecretManualItem.getSecretManual(world, stack);
                if (manual.isPresent()) {
                    final ModelResourceLocation modelPath = new ModelResourceLocation(manual.get().model(), "inventory");
                    return Minecraft.getInstance().getModelManager().getModel(modelPath);
                }
                return original;
            }
        };
    }

    @NotNull
    @Override
    public ItemOverrides getOverrides() {
        return itemHandler;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@javax.annotation.Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return originalModel.getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return originalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return originalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return originalModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return originalModel.isCustomRenderer();
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return originalModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return originalModel.getTransforms();
    }
}