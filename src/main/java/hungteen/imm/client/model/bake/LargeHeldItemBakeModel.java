package hungteen.imm.client.model.bake;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.util.ItemUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-06 12:56
 **/
public class LargeHeldItemBakeModel implements BakedModel {

    private final BakedModel heldModel;
    private final BakedModel guiModel;

    public LargeHeldItemBakeModel(Item item, ModelManager manager) {
        this.heldModel = manager.getModel(getHeldModelLocation(item));
        this.guiModel = manager.getModel(getModelLocation(item));
    }

    @Override
    public BakedModel applyTransform(ItemTransforms.TransformType transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        switch (transformType) {
            case GUI: return this.guiModel.applyTransform(transformType, poseStack, applyLeftHandTransform);
            default : return this.heldModel.applyTransform(transformType, poseStack, applyLeftHandTransform);
        }
    }

    public static ResourceLocation getModelLocation(Item item){
        return new ModelResourceLocation(ItemHelper.get().getKey(item), ImmortalBakeModels.INVENTORY);
    }

    public static ResourceLocation getHeldModelLocation(Item item){
        return new ModelResourceLocation(ItemUtil.getLargeHeldLocation(item), ImmortalBakeModels.INVENTORY);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return this.heldModel.getQuads(state, direction, random);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        return this.heldModel.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.heldModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.heldModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.heldModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() { // never run this.
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.heldModel.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return this.heldModel.getParticleIcon(data);
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.heldModel.getOverrides();
    }
}
