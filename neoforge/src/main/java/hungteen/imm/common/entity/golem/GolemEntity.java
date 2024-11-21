package hungteen.imm.common.entity.golem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.entity.ai.behavior.golem.GolemBehavior;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.menu.GolemInventoryMenu;
import hungteen.imm.common.menu.ImmortalMenuProvider;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-24 09:58
 **/
public abstract class GolemEntity extends IMMMob implements ContainerListener {

    public static final String GOLEM_ID = "GolemId";
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected final Collection<MemoryModuleType<?>> memoryModules = new HashSet<>();
    protected final Collection<Pair<Integer, BehaviorControl<GolemEntity>>> behaviorModules = new ArrayList<>();
    protected GolemRuneContainer runeInventory;
    protected SimpleContainer itemInventory;
    private Player interactPlayer;

    public GolemEntity(EntityType<? extends GolemEntity> type, Level level) {
        super(type, level);
        this.createInventory();
//        this.refreshBrain();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.level().getProfiler().push("TickGolemBrain");
        this.tickBrain();
        this.level().getProfiler().pop();
    }

    protected void tickBrain(){
        this.getBrain().tick((ServerLevel) this.level(), this);
    }

    public void refreshBrain(){
        NbtOps nbtops = NbtOps.INSTANCE;
        this.brain = this.makeBrain(new Dynamic<>(nbtops, nbtops.createMap(ImmutableMap.of(nbtops.createString("memories"), nbtops.emptyMap()))));
        this.registerBehaviors(this.getBrain());
    }

    @Override
    protected Brain<GolemEntity> makeBrain(Dynamic<?> dynamic) {
        return this.brainProvider().makeBrain(dynamic);
    }

    @Override
    protected Brain.Provider<GolemEntity> brainProvider() {
        return Brain.provider(this.memoryModules == null ? List.of() : this.memoryModules, List.of());
    }

    @Override
    public Brain<GolemEntity> getBrain() {
        return (Brain<GolemEntity>) super.getBrain();
    }

    public void registerBehaviors(Brain<GolemEntity> brain){
        brain.addActivity(Activity.CORE, ImmutableList.copyOf(this.behaviorModules));
//        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.CORE);
        brain.useDefaultActivity();
    }

    protected void createInventory() {
        {
            final SimpleContainer container = this.runeInventory;
            this.runeInventory = new GolemRuneContainer(this, this.getRuneInventorySize());
            if (container != null) {
                container.removeListener(this);
                final int len = Math.min(container.getContainerSize(), this.runeInventory.getContainerSize());
                for(int j = 0; j < len; ++ j) {
                    ItemStack itemstack = container.getItem(j);
                    if (!itemstack.isEmpty()) {
                        this.runeInventory.setItem(j, itemstack.copy());
                    }
                }
            }
            this.runeInventory.addListener(this);
        }
        {
            final SimpleContainer container = this.itemInventory;
            this.itemInventory = new SimpleContainer(this.getItemInventorySize());
            if (container != null) {
                container.removeListener(this);
                final int len = Math.min(container.getContainerSize(), this.itemInventory.getContainerSize());
                for(int j = 0; j < len; ++ j) {
                    ItemStack itemstack = container.getItem(j);
                    if (!itemstack.isEmpty()) {
                        this.itemInventory.setItem(j, itemstack.copy());
                    }
                }
            }
            this.itemInventory.addListener(this);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND && canInteractWith(player)){
            if(player instanceof ServerPlayer serverPlayer){
                this.setInteractPlayer(player);
                serverPlayer.openMenu(new ImmortalMenuProvider() {
                    @Override
                    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        return new GolemInventoryMenu(id, inventory, GolemEntity.this.getId());
                    }
                }, (data) -> {
                    data.writeInt(GolemEntity.this.getId());
                });
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    protected boolean canInteractWith(Player player){
        return this.interactPlayer == null;
    }

    public void stopInteracting(){
        this.setInteractPlayer(null);
    }

    @Override
    public void containerChanged(Container container) {

    }

    public void updateRuneInventory(){
        this.memoryModules.clear();
        this.behaviorModules.clear();
        for(int i = 0; i < this.runeInventory.getContainerSize(); ++ i){
            final ItemStack stack = runeInventory.getItem(i);
            if(stack.getItem() instanceof BehaviorRuneItem behaviorRuneItem){
                GolemBehavior behavior = behaviorRuneItem.getRune().getBehaviorFactory().create(stack);
                this.behaviorModules.add(Pair.of(i, behavior));
                behavior.entryCondition.forEach((memory, status) -> {
                    this.memoryModules.add(memory);
                });
            }
        }
        this.refreshBrain();
    }

    @Override
    public @org.jetbrains.annotations.Nullable Entity changeDimension(DimensionTransition transition) {
        this.stopInteracting();
        return super.changeDimension(transition);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        this.stopInteracting();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return this.getOwnerUUID().isEmpty();
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    public abstract int getRuneInventorySize();

    /**
     * 近战攻击的冷却时间
     */
    public int getMeleeAttackCD(){
        return 0;
    }

    /**
     * 远程攻击的冷却时间
     */
    public int getRangeAttackCD(){
        return 0;
    }

    public int getItemInventorySize() {
        return 0;
    }

    public Player getInteractPlayer() {
        return this.interactPlayer;
    }

    public void setInteractPlayer(Player interactPlayer) {
        this.interactPlayer = interactPlayer;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getOwnerUUID().isPresent()) {
            tag.putUUID("GolemOwner", this.getOwnerUUID().get());
        }
        if (this.runeInventory != null) {
            tag.put("GolemRuneInventory", this.runeInventory.createTag(registryAccess()));
        }
        if(this.itemInventory != null) {
            tag.put("GolemItemInventory", this.itemInventory.createTag(registryAccess()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("GolemOwner")){
            this.setOwnerUUID(tag.getUUID("GolemOwner"));
        }
        if(tag.contains("GolemRuneInventory")){
            this.runeInventory.fromTag(NBTUtil.list(tag, "GolemRuneInventory"), registryAccess());
        }
        if(tag.contains("GolemItemInventory")){
            this.itemInventory.fromTag(NBTUtil.list(tag, "GolemItemInventory"), registryAccess());
        }
        this.refreshBrain();
    }

    public Container getRuneInventory(){
        return this.runeInventory;
    }

    public Container getItemInventory() {
        return itemInventory;
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public ICultivationType getCultivationType() {
        return CultivationTypes.SPIRITUAL;
    }

    public static class GolemRuneContainer extends SimpleContainer {

        private final GolemEntity golem;

        public GolemRuneContainer(GolemEntity golem, int size) {
            super(size);
            this.golem = golem;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void startOpen(Player player) {
            super.startOpen(player);
            this.golem.updateRuneInventory();
        }

        @Override
        public void stopOpen(Player player) {
            super.stopOpen(player);
            this.golem.updateRuneInventory();
        }
    }

}
