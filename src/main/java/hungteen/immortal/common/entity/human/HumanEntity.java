package hungteen.immortal.common.entity.human;

import com.google.common.collect.ImmutableList;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IHuman;
import hungteen.immortal.api.registry.IInventoryLootType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.entity.ImmortalDataSerializers;
import hungteen.immortal.common.entity.ImmortalGrowableCreature;
import hungteen.immortal.common.entity.ai.ImmortalMemories;
import hungteen.immortal.common.entity.ai.ImmortalSensors;
import hungteen.immortal.common.impl.codec.HumanSettings;
import hungteen.immortal.utils.BehaviorUtil;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * 村民、刁民、玩家都算人类。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 18:23
 **/
public abstract class HumanEntity extends ImmortalGrowableCreature implements IHuman {

    private static final EntityDataAccessor<CompoundTag> ROOTS = SynchedEntityData.defineId(HumanEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<HumanSettings.HumanSetting> HUMAN_SETTING = SynchedEntityData.defineId(HumanEntity.class, ImmortalDataSerializers.HUMAN_SETTING.get());
    private static final EntityDataAccessor<List<HumanSettings.CommonTradeEntry>> TRADE_ENTRIES = SynchedEntityData.defineId(HumanEntity.class, ImmortalDataSerializers.COMMON_TRADE_ENTRIES.get());
    private List<ISpiritualType> rootsCache;
    @javax.annotation.Nullable
    private Player tradingPlayer;
    private final SimpleContainer inventory;

    public HumanEntity(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
        this.inventory = this.createInventory();
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ROOTS, new CompoundTag());
        entityData.define(HUMAN_SETTING, HumanSettings.DEFAULT.getValue());
        entityData.define(TRADE_ENTRIES, List.of());
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData groupData, @org.jetbrains.annotations.Nullable CompoundTag compoundTag) {
        if(!accessor.isClientSide()){
            PlayerUtil.getSpiritualRoots(accessor.getRandom()).forEach(this::addSpiritualRoots);
            this.updateHumanSetting();
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, compoundTag);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.level.getProfiler().push("HumanBrain");
        this.updateBrain((ServerLevel) this.level);
        this.level.getProfiler().pop();
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.ATTACK_DAMAGE, 1D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 40D)
                .add(ForgeMod.ATTACK_RANGE.get(), 3D)
                .add(Attributes.ATTACK_SPEED, 4D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    /**
     * 填充背包。
     */
    public void updateHumanSetting(){
        HumanSettings.getHumanSetting(getInventoryLootType(), this.getRandom()).ifPresent(l -> {
            this.setHumanSetting(l);
            l.fillInventory(this.getInventory(), this.getRandom());
            l.fillTrade(this, this.getRandom());
        });
    }

    /**
     * Refresh brain.
     * @param level is the server side level.
     */
    public void refreshBrain(ServerLevel level){

    }

    /**
     * Used for update brain.
     * @param level is the server side level.
     */
    public abstract void updateBrain(ServerLevel level);

    public boolean hasItemStack(Predicate<ItemStack> predicate){
        for(int i = 0; i < this.getInventory().getContainerSize(); ++ i){
            if(predicate.test(this.getInventory().getItem(i))){
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> filterFromInventory(Predicate<ItemStack> predicate){
        List<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < this.getInventory().getContainerSize(); ++ i){
            if(predicate.test(this.getInventory().getItem(i))){
                list.add(this.getInventory().getItem(i));
            }
        }
        return list;
    }

    /**
     * Switch one predicate item from inventory to equipment slot.
     */
    public boolean switchInventory(EquipmentSlot equipmentSlot, Predicate<ItemStack> predicate){
        for(int i = 0; i < this.getInventory().getContainerSize(); ++ i){
            if(predicate.test(this.getInventory().getItem(i))){
                ItemStack stack = this.getItemBySlot(equipmentSlot).copy();
                this.setItemSlot(equipmentSlot, this.getInventory().getItem(i).copy());
                this.getInventory().setItem(i, stack);
                return true;
            }
        }
        return false;
    }

    /**
     * Switch one predicate item from inventory to hand.
     */
    public boolean switchInventory(InteractionHand hand, Predicate<ItemStack> predicate){
        return switchInventory(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, predicate);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float strength) {
        final ItemStack projectile = this.getProjectile(this.getMainHandItem());
        if(this.getMainHandItem().getItem() instanceof BowItem){
            AbstractArrow abstractarrow = this.getArrow(projectile, strength);
            abstractarrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 2F, 2F);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            if(this.getRandom().nextFloat() < 0.4F) {
                abstractarrow.setCritArrow(true);
            }
            this.level.addFreshEntity(abstractarrow);
        }
    }

    @Override
    public ItemStack getProjectile(ItemStack stack) {
        if (stack.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)stack.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return net.minecraftforge.common.ForgeHooks.getProjectile(this, stack, itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack);
        } else {
            return net.minecraftforge.common.ForgeHooks.getProjectile(this, stack, ItemStack.EMPTY);
        }
    }

    @Override
    public ItemStack eat(Level level, ItemStack itemStack) {
        if(itemStack.isEdible()){
            FoodProperties foodProperties = itemStack.getFoodProperties(this);
            if(foodProperties != null){
                this.heal(foodProperties.getNutrition() * 0.5F);
            }
        }
        return super.eat(level, itemStack);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
        final double reach = this.getAttributeValue(ForgeMod.ATTACK_RANGE.get());
        return Math.max(reach * reach + livingEntity.getBbWidth(), super.getMeleeAttackRangeSqr(livingEntity));
    }

    /**
     * 不设置的话，会卡在一个地方不能前进。
     */
    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return this.distanceToSqr(entity) <= this.getMeleeAttackRangeSqr(entity);
    }

    public double getAttackCoolDown(){
        final double speed = this.getAttributeValue(Attributes.ATTACK_SPEED);
        return speed == 0 ? 1000 : 1 / speed;
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem item) {
        return true;
    }

    protected AbstractArrow getArrow(ItemStack stack, float strength) {
        return ProjectileUtil.getMobArrow(this, stack, strength);
    }

    protected SimpleContainer createInventory(){
        return new SimpleContainer(8);
    }

    protected ImmutableList<MemoryModuleType<?>> getMemoryModules() {
        return ImmutableList.of(
                /* Nearest Living Entities Sensor */
                MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                /* Nearest Players Sensor */
                MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                /* Nearest Item Sensor */
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                /* Nearest Bed Sensor */
                MemoryModuleType.NEAREST_BED,
                /* Hurt By Sensor */
                MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY,
                /* MoveToTargetSink Behavior*/
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.WALK_TARGET,
                /* InteractWithDoor Behavior */
                MemoryModuleType.DOORS_TO_CLOSE,
                /* GoToWantedItem Behavior */
                MemoryModuleType.LOOK_TARGET,
                /* LookAnInteract Behavior */
                MemoryModuleType.INTERACTION_TARGET,
                /* Fight */
                MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN,
                /* Interact With */
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.ANGRY_AT,
                /* Custom */
                ImmortalMemories.UNABLE_MELEE_ATTACK.get(),
                ImmortalMemories.UNABLE_RANGE_ATTACK.get(),
                ImmortalMemories.NEAREST_BOAT.get(),
                ImmortalMemories.NEAREST_PROJECTILE.get()
        );
    }

    protected ImmutableList<SensorType<? extends Sensor<? super HumanEntity>>> getSensorModules() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.NEAREST_BED,
                SensorType.HURT_BY,
                ImmortalSensors.NEAREST_BOAT.get(),
                ImmortalSensors.HAS_PROJECTILE_NEARBY.get()
        );
    }

    @Override
    protected void pickUpItem(ItemEntity item) {
        InventoryCarrier.pickUpItem(this, this, item);
    }

    @Override
    public boolean wantsToPickUp(ItemStack itemStack) {
        return super.wantsToPickUp(itemStack);
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public boolean shouldShowName() {
        return super.shouldShowName();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(! this.spawnEggMatch(itemstack) && this.canTradeWith(player)) {
//            if (hand == InteractionHand.MAIN_HAND) {
//                player.awardStat(Stats.TALKED_TO_VILLAGER);
//            }
            if (!this.level.isClientSide) {
                this.setTradingPlayer(player);
//                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    public boolean canTradeWith(Player player){
        return this.isAlive() && !this.isTrading() && !this.isBaby() && BehaviorUtil.isIdle(this);
    }

    public void setTradingPlayer(@javax.annotation.Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Nullable
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    protected void stopTrading() {
        this.setTradingPlayer(null);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Entity changeDimension(ServerLevel serverLevel, ITeleporter teleporter) {
        this.stopTrading();
        return super.changeDimension(serverLevel, teleporter);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        this.stopTrading();
    }
    
    public void fillInventoryWith(ItemStack stack, int minCount, int maxCount){
        fillInventoryWith(stack, 1F, minCount, maxCount);
    }

    public void fillInventoryWith(ItemStack stack, float chance, int count){
        fillInventoryWith(stack, chance, count, count);
    }

    public void fillInventoryWith(ItemStack stack, float chance, int minCount, int maxCount) {
        if(RandomHelper.chance(this.getRandom(), chance) && this.getInventory().canAddItem(stack)){
            final int count = RandomHelper.getMinMax(this.getRandom(), minCount, maxCount);
            for(int i = 0; i < count; ++ i){
                this.getInventory().addItem(stack);
            }
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    /**
     * 人类全部不会自然刷新。
     */
    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    /**
     * 可恶，人类又不是牲畜！
     */
    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    public abstract IInventoryLootType getInventoryLootType();

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("CultivatorRoots")){
            setRootTag(tag.getCompound("CultivatorRoots"));
        }
        if(tag.contains("Inventory")){
            this.inventory.fromTag(tag.getList("Inventory", 10));
        }
        if(tag.contains("InventoryLoot")){ // allow setting loot by nbt.
            HumanSettings.registry().getValue(tag.getString("InventoryLoot")).ifPresent(l -> {
                l.fillInventory(this.getInventory(), this.getRandom());
            });
        }
        if(tag.contains("HumanSetting")){
            HumanSettings.HumanSetting.CODEC.parse(NbtOps.INSTANCE, tag.get("HumanSetting"))
                    .result().ifPresent(this::setHumanSetting);
        }
        if(tag.contains("CommonTradeEntries")){
            HumanSettings.CommonTradeEntry.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("CommonTradeEntries"))
                    .result().ifPresent(this::setCommonTradeEntries);
        }
        if (this.level instanceof ServerLevel) {
            this.refreshBrain((ServerLevel)this.level);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("CultivatorRoots", this.getRootTag());
        tag.put("Inventory", this.inventory.createTag());
        HumanSettings.HumanSetting.CODEC.encodeStart(NbtOps.INSTANCE, this.getHumanSetting())
                .result().ifPresent(l -> tag.put("HumanSetting", l));
        HumanSettings.CommonTradeEntry.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.getCommonTradeEntries())
                .result().ifPresent(l -> tag.put("CommonTradeEntries", l));
    }

    public void addSpiritualRoots(ISpiritualType spiritualRoot){
        final CompoundTag tag = getRootTag();
        tag.putBoolean(spiritualRoot.getRegistryName(), true);
        if(this.rootsCache == null){
            this.rootsCache = new ArrayList<>();
        }
        this.rootsCache.add(spiritualRoot);
        setRootTag(tag);
    }

    @Override
    public Collection<ISpiritualType> getSpiritualTypes() {
        if(this.rootsCache == null && ImmortalAPI.get().spiritualRegistry().isPresent()){
            this.rootsCache = new ArrayList<>();
            ImmortalAPI.get().spiritualRegistry().get().getValues().forEach(root -> {
                if(getRootTag().contains(root.getRegistryName()) && getRootTag().getBoolean(root.getRegistryName())){
                    this.rootsCache.add(root);
                }
            });
        }
        return this.rootsCache;
    }

    public CompoundTag getRootTag(){
        return entityData.get(ROOTS);
    }

    public void setRootTag(CompoundTag rootTag) {
        entityData.set(ROOTS, rootTag);
    }

    public HumanSettings.HumanSetting getHumanSetting(){
        return entityData.get(HUMAN_SETTING);
    }

    public void setHumanSetting(HumanSettings.HumanSetting humanSetting) {
        entityData.set(HUMAN_SETTING, humanSetting);
    }

    public List<HumanSettings.CommonTradeEntry> getCommonTradeEntries(){
        return entityData.get(TRADE_ENTRIES);
    }

    public void setCommonTradeEntries(List<HumanSettings.CommonTradeEntry> entries) {
        entityData.set(TRADE_ENTRIES, entries);
    }

}
