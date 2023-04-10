package hungteen.imm.common.entity.golem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import hungteen.imm.common.entity.IMMCreature;
import hungteen.imm.common.entity.ai.behavior.golem.GolemBehavior;
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
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 09:58
 **/
public abstract class GolemEntity extends IMMCreature implements ContainerListener {

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
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(OWNER_UUID, Optional.empty());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.level.getProfiler().push("GolemBrain");
        this.getBrain().tick((ServerLevel) this.level, this);
        this.level.getProfiler().pop();
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
            if(player instanceof ServerPlayer){
                this.setInteractPlayer(player);
                NetworkHooks.openScreen((ServerPlayer) player, new ImmortalMenuProvider() {
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
                GolemBehavior behavior = behaviorRuneItem.getBehaviorRune().getBehaviorFactory().create(stack);
                this.behaviorModules.add(Pair.of(i, behavior));
                behavior.entryCondition.forEach((memory, status) -> {
                    this.memoryModules.add(memory);
                });
            }
        }
        this.refreshBrain();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Entity changeDimension(ServerLevel level, ITeleporter teleporter) {
        this.stopInteracting();
        return super.changeDimension(level, teleporter);
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
    public boolean canBeLeashed(Player player) {
        return this.getOwnerUUID().isPresent() && this.getOwnerUUID().get().equals(player.getUUID());
    }

    public abstract int getRuneInventorySize();

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
            tag.put("GolemRuneInventory", this.runeInventory.createTag());
        }
        if(this.itemInventory != null) {
            tag.put("GolemItemInventory", this.itemInventory.createTag());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("GolemOwner")){
            this.setOwnerUUID(tag.getUUID("GolemOwner"));
        }
        if(tag.contains("GolemRuneInventory")){
            this.runeInventory.fromTag(NBTUtil.list(tag, "GolemRuneInventory"));
        }
        if(tag.contains("GolemItemInventory")){
            this.itemInventory.fromTag(NBTUtil.list(tag, "GolemItemInventory"));
        }
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
