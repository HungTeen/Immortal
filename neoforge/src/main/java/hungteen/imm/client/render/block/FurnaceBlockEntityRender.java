package hungteen.imm.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.htlib.client.render.blockentity.HTBlockEntityRender;
import hungteen.imm.client.render.IMMRenderTypes;
import hungteen.imm.common.block.IMMBlockPatterns;
import hungteen.imm.common.blockentity.SpiritualFurnaceBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.model.data.ModelData;

public class FurnaceBlockEntityRender extends HTBlockEntityRender<SpiritualFurnaceBlockEntity> {

    private final BlockRenderDispatcher dispatcher;

    public FurnaceBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(SpiritualFurnaceBlockEntity furnace, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(furnace, partialTicks, stack, bufferIn, combinedLightIn, combinedOverlayIn);
        if(furnace.displayBlockPattern()){
            BlockPos pos = furnace.getBlockPos();
            VertexConsumer builder = bufferIn.getBuffer(IMMRenderTypes.DUMMY_BLOCK);

            stack.pushPose();
            stack.translate(-pos.getX(), -pos.getY(), -pos.getZ());

            IMMBlockPatterns.getFurnacePattern().getBlockStates(pos.mutable(), 3, 4, 2).forEach(pair -> {
                if (furnace.getLevel() != null && furnace.getLevel().isEmptyBlock(pair.getFirst())) {
                    stack.pushPose();
                    stack.translate(pair.getFirst().getX(), pair.getFirst().getY(), pair.getFirst().getZ());
                    this.dispatcher.renderBatched(pair.getSecond(), pair.getFirst(), furnace.getLevel(), stack, builder, false, furnace.getRandom(), ModelData.EMPTY, null);
                    stack.popPose();
                }
            });

            stack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(SpiritualFurnaceBlockEntity blockEntity) {
        return blockEntity.displayBlockPattern();
    }
}
