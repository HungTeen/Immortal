package hungteen.imm.client.model.bake;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.util.ItemUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-06 12:56
 **/
public class LargeHeldItemBakeModel implements BakedModel {

    private final BakedModel heldModel;
    private final BakedModel guiModel;

    public LargeHeldItemBakeModel(Item item, Map<ModelResourceLocation, BakedModel> bakedModels) {
        this.heldModel = bakedModels.get(getHeldModelLocation(item));
        this.guiModel = bakedModels.get(getModelLocation(item));
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext displayContext, PoseStack poseStack, boolean applyLeftHandTransform) {
        switch (displayContext) {
            case GUI: return this.guiModel.applyTransform(displayContext, poseStack, applyLeftHandTransform);
            default : return this.heldModel.applyTransform(displayContext, poseStack, applyLeftHandTransform);
        }
    }

    public static ModelResourceLocation getModelLocation(Item item){
        return new ModelResourceLocation(ItemHelper.get().getKey(item), IMMBakeModels.INVENTORY);
    }

    public static ModelResourceLocation getHeldModelLocation(Item item){
        return new ModelResourceLocation(ItemUtil.getLargeHeldLocation(item), IMMBakeModels.INVENTORY);
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
