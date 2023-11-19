package hungteen.imm.common.blockentity;

import hungteen.htlib.common.blockentity.ItemHandlerBlockEntity;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.item.artifacts.FlameGourd;
import hungteen.imm.common.menu.furnace.SpiritualFurnaceMenu;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 09:30
 **/
public class SpiritualFurnaceBlockEntity extends ItemHandlerBlockEntity implements MenuProvider {

    protected final ItemStackHandler itemHandler = new ItemStackHandler(4);
    protected final ContainerData accessData = new ContainerData() {
        @Override
        public int get(int id) {
            switch (id) {
                case 0: return SpiritualFurnaceBlockEntity.this.currentFlameValue;
                case 1: return SpiritualFurnaceBlockEntity.this.triggered ? 1 : 0;
                case 2: return SpiritualFurnaceBlockEntity.this.maxFlameValue;
            }
            Util.error("Unable to find suitable for Id");
            return 0;
        }

        @Override
        public void set(int id, int value) {
            switch (id) {
                case 0 -> SpiritualFurnaceBlockEntity.this.currentFlameValue = value;
                case 1 -> SpiritualFurnaceBlockEntity.this.triggered = (value == 1);
                case 2 -> SpiritualFurnaceBlockEntity.this.maxFlameValue = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private int currentFlameValue = 0;
    private int maxFlameValue = 0;
    private boolean triggered = false;
    private boolean displayBlockPattern = false;
    private long lastInteractTime = 0;
    private BlockPattern.BlockPatternMatch lastMatch;
    protected FunctionalFurnaceBlockEntity functionalBlockEntity;

    public SpiritualFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMMBlockEntities.SPIRITUAL_FURNACE.get(), blockPos, blockState);
    }

    public SpiritualFurnaceBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public void opened() {
        this.getFunctionalBlockEntity();
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, SpiritualFurnaceBlockEntity blockEntity) {
        if(blockEntity.triggered){
            if(blockEntity.enoughFlame()){
                blockEntity.smelting(1);
                if(level.getRandom().nextFloat() < 0.02F){
                    BlockUtil.playSound(level, blockPos, SoundEvents.FIRE_AMBIENT);
                }
            } else {
                blockEntity.stop();
            }
            blockEntity.setChanged();
        }
        if(state.getValue(SpiritualFurnaceBlock.LIT) ^ blockEntity.triggered){
            level.setBlock(blockPos, state.setValue(SpiritualFurnaceBlock.LIT, blockEntity.triggered), 3);
        }
    }

    public void start(){
        this.triggered = true;
        this.setChanged();
    }

    public void stop(){
        this.triggered = false;
        this.currentFlameValue = 0;
        this.setChanged();
    }

    public void smelting(int consumeValue){
        currentFlameValue = Math.max(0, currentFlameValue - consumeValue);
        if(currentFlameValue == 0){
            if(this.requireMoreFlame()){
                while(FlameGourd.getFlameAmount(getGourd()) > 0 && currentFlameValue < consumeValue){
                    FlameGourd.addFlameAmount(getGourd(), FlameGourd.getFlameLevel(getGourd()), -1);
                    currentFlameValue += getGourdFlameValue();
                    getFirstStone().ifPresent(stack -> {
                        if(stack.is(IMMItemTags.SPIRITUAL_STONES_LEVEL_ONE)){
                            currentFlameValue += 40;
                        }
//                        else if(stack.is(IMMItemTags.SPIRITUAL_STONES_LEVEL_TWO)){
//                            currentFlameValue += 80;
//                        }
                    });
                }
                maxFlameValue = currentFlameValue;
            } else {
                this.stop();
            }
        }
        this.setChanged();
    }

    public boolean enoughFlame(){
        return this.currentFlameValue > 0 || FlameGourd.getFlameAmount(getGourd()) > 0;
    }

    public boolean requireMoreFlame(){
        return this.getFunctionalBlockEntity() != null && this.getFunctionalBlockEntity().requireFlame();
    }

    public int getGourdFlameValue(){
        return getSpiritualFlameValue(FlameGourd.getFlameLevel(getGourd()));
    }

    public static int getSpiritualFlameValue(int level){
        return level * 100;
    }

    private ItemStack getGourd(){
        return this.getItemHandler().getStackInSlot(0);
    }

    public Optional<ItemStack> getFirstStone(){
        for(int i = 1; i < 4; ++ i){
            if(this.getItemHandler().getStackInSlot(i).is(IMMItemTags.SPIRITUAL_STONES)){
                return Optional.of(this.getItemHandler().getStackInSlot(i));
            }
        }
        return Optional.empty();
    }

//    @Override
//    public @org.jetbrains.annotations.Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
//        return ClientboundBlockEntityDataPacket.create(this);
//    }
//
//    @Override
//    public CompoundTag getUpdateTag() {
//        CompoundTag tag = super.getUpdateTag();
//        tag.putBoolean("DisplayBlockPattern", this.displayBlockPattern);
//        return tag;
//    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("HasTriggered")){
            this.triggered = tag.getBoolean("HasTriggered");
        }
        if(tag.contains("CurrentFlameValue")){
            this.currentFlameValue = tag.getInt("CurrentFlameValue");
        }
        if(tag.contains("MaxFlameValue")){
            this.maxFlameValue = tag.getInt("MaxFlameValue");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("HasTriggered", this.triggered);
        tag.putInt("CurrentFlameValue", this.currentFlameValue);
        tag.putInt("MaxFlameValue", this.maxFlameValue);
    }

    @Nullable
    public BlockPattern.BlockPatternMatch getLastMatch(){
        if(this.lastMatch == null){
            this.lastMatch = SpiritualFurnaceBlock.getMatch(this.level, this.getBlockPos());
        }
        return this.lastMatch;
    }

    @Nullable
    public FunctionalFurnaceBlockEntity getFunctionalBlockEntity(){
        if(this.functionalBlockEntity == null){
            if(this.getLastMatch() != null){
                this.functionalBlockEntity = SpiritualFurnaceBlock.getFunctionalBlockEntity(this.getLastMatch());
                if(this.functionalBlockEntity == null){
                    this.lastMatch = null;
                }
            }
        }
        return this.functionalBlockEntity;
    }

    public boolean displayBlockPattern() {
        return isDisplayBlockPattern() && getLastMatch() == null;
    }

    public boolean canInteractWith(){
        return this.getLevel() != null && this.getLevel().getGameTime() > this.lastInteractTime + 10;
    }

    public boolean isDisplayBlockPattern() {
        return displayBlockPattern;
    }

    public void setDisplayBlockPattern(Level level, boolean displayBlockPattern) {
        this.displayBlockPattern = displayBlockPattern;
        this.lastInteractTime = level.getGameTime();
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SpiritualFurnaceMenu(id, inventory, this.accessData, this.getBlockPos());
    }

    public IRealmType getArtifactType() {
        return this.getBlockState().getBlock() instanceof IArtifactBlock ? ((IArtifactBlock) this.getBlockState().getBlock()).getRealm(this.getBlockState()) : RealmTypes.NOT_IN_REALM;
    }
}
