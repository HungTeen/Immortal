package hungteen.immortal.common.entity.golem;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import hungteen.immortal.common.entity.ImmortalCreature;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.item.runes.RuneItem;
import hungteen.immortal.common.menu.GolemMenu;
import hungteen.immortal.common.menu.ImmortalMenuProvider;
import hungteen.immortal.common.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.AxolotlAi;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 09:58
 **/
public abstract class GolemEntity extends ImmortalCreature implements ContainerListener {

    public static final String GOLEM_ID = "GolemId";
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(GolemEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected SimpleContainer inventory;
    protected final Collection<MemoryModuleType<?>> memoryModules = new ArrayList<>();
    protected final Collection<SensorType<? extends Sensor<? super GolemEntity>>> sensorModules = new ArrayList<>();
    protected final Collection<Pair<Integer, Behavior<? super GolemEntity>>> behaviorModules = new ArrayList<>();

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
//        this.level.getProfiler().push("GolemActivity");
//        HumanEntity.AI.updateActivity(this);
//        this.level.getProfiler().pop();
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
        return this.memoryModules == null ? Brain.provider(ImmutableList.of(), ImmutableList.of()) : Brain.provider(this.memoryModules, this.sensorModules);
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

    protected int getInventorySize() {
        return 27;
    }

    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
//        this.updateContainerEquipment();
//        this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.inventory));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND && player.getItemInHand(hand).isEmpty()){
            if(player instanceof ServerPlayer){
                NetworkHooks.openGui((ServerPlayer) player, new ImmortalMenuProvider() {
                    @Override
                    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        return new GolemMenu(id, inventory, GolemEntity.this.getId());
                    }
                }, (data) -> {
                    data.writeInt(GolemEntity.this.getId());
                });
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void containerChanged(Container container) {
        this.memoryModules.clear();
        this.sensorModules.clear();
        this.behaviorModules.clear();
        for(int i = 0; i < container.getContainerSize(); ++ i){
            final ItemStack stack = container.getItem(i);
            switch (RuneItem.getRuneTypes(stack)){
                case MEMORY -> {
                    RuneItem.getMemoryType(stack).ifPresent(memoryRune -> {
                        this.memoryModules.add(memoryRune.getMemoryType().get());
                    });
                }
                case SENSOR -> {
                    RuneItem.getSensorType(stack).ifPresent(sensorRune -> {
                        this.sensorModules.add(sensorRune.getSensorType().get());
                    });
                }
                case BEHAVIOR -> {
                    final int priority = i;
                    RuneItem.getBehaviorType(stack).ifPresent(behaviorRune -> {
                        this.behaviorModules.add(Pair.of(priority, behaviorRune.getBehaviorFunction().apply(this)));
                    });
                }
            }
        }
        this.refreshBrain();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getOwnerUUID().isPresent()) {
            tag.putUUID("GolemOwner", this.getOwnerUUID().get());
        }
        if (this.inventory != null) {
            ListTag listtag = new ListTag();
            for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putByte("Slot", (byte)i);
                    itemstack.save(compoundtag);
                    listtag.add(compoundtag);
                }
            }
            tag.put("GolemInventory", listtag);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("GolemOwner")){
            this.setOwnerUUID(tag.getUUID("GolemOwner"));
        }
        if(tag.contains("GolemInventory")){
            ListTag listtag = tag.getList("GolemInventory", 10);
            for(int i = 0; i < listtag.size(); ++i) {
                CompoundTag compoundtag = listtag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                if (j >= 0 && j < this.inventory.getContainerSize()) {
                    this.inventory.setItem(j, ItemStack.of(compoundtag));
                }
            }
        }
    }

    public Container getGolemInventory(){
        return this.inventory;
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

}
