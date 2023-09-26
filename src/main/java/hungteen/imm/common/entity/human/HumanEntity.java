package hungteen.imm.common.entity.human;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.interfaces.IHuman;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.entity.IMMDataSerializers;
import hungteen.imm.common.entity.IMMGrowableMob;
import hungteen.imm.common.entity.ai.IMMMemories;
import hungteen.imm.common.entity.ai.IMMSensors;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.entity.human.setting.HumanSettings;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.menu.ImmortalMenuProvider;
import hungteen.imm.common.menu.MerchantTradeMenu;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.TradeOffersPacket;
import hungteen.imm.util.BehaviorUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 村民、刁民、玩家都算人类。
 *
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 18:23
 **/
public abstract class HumanEntity extends IMMGrowableMob implements IHuman {

    private static final EntityDataAccessor<HumanSetting> HUMAN_SETTING = SynchedEntityData.defineId(HumanEntity.class, IMMDataSerializers.HUMAN_SETTING.get());
    private static final EntityDataAccessor<HumanSectData> SECT_DATA = SynchedEntityData.defineId(HumanEntity.class, IMMDataSerializers.HUMAN_SECT_DATA.get());
    private TradeOffers tradeOffers = new TradeOffers();
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
        entityData.define(HUMAN_SETTING, HumanSettings.registry().getValue(level(), HumanSettings.DEFAULT));
        entityData.define(SECT_DATA, new HumanSectData(Optional.empty(), Optional.empty()));
    }

//    @Override
//    public void writeSpawnData(FriendlyByteBuf buffer) {
//        super.writeSpawnData(buffer);
//        if (this.tradeOffers != null) {
//            this.tradeOffers.writeToStream(buffer);
//        }
//    }
//
//    @Override
//    public void readSpawnData(FriendlyByteBuf additionalData) {
//        super.readSpawnData(additionalData);
//        this.tradeOffers = TradeOffers.createFromStream(additionalData);
//    }

    @org.jetbrains.annotations.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData groupData, @org.jetbrains.annotations.Nullable CompoundTag compoundTag) {
        if (!accessor.isClientSide()) {
            this.updateHumanSetting();
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, compoundTag);
    }

    @Override
    protected Collection<ISpiritualType> createSpiritualRoots(ServerLevelAccessor accessor) {
        return PlayerUtil.getSpiritualRoots(accessor.getRandom());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.level().getProfiler().push("HumanBrain");
        this.updateBrain((ServerLevel) this.level());
        this.level().getProfiler().pop();
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
                .add(ForgeMod.ENTITY_REACH.get(), 3D)
                .add(Attributes.ATTACK_SPEED, 4D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    /**
     * 填充背包 & 交易列表。
     */
    public void updateHumanSetting() {
        HumanSettings.getRandomSetting(level(), getType(), this.getRandom()).ifPresent(l -> {
            this.setHumanSetting(l);
            l.fillInventory(this.getInventory(), this.getRandom());
            l.fillTrade(this, this.getRandom());
        });
    }

    /**
     * Refresh brain.
     * @param level is the server side level.
     */
    public void refreshBrain(ServerLevel level) {

    }

    /**
     * Used for update brain.
     * @param level is the server side level.
     */
    public abstract void updateBrain(ServerLevel level);

    public void fillSpecialTrade(TradeOffers offers, RandomSource random){

    }

    public boolean hasItemStack(Predicate<ItemStack> predicate) {
        for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            if (predicate.test(this.getInventory().getItem(i))) {
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> filterFromInventory(Predicate<ItemStack> predicate) {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            if (predicate.test(this.getInventory().getItem(i))) {
                list.add(this.getInventory().getItem(i));
            }
        }
        return list;
    }

    /**
     * Switch one predicate item from inventory to equipment slot.
     */
    public boolean switchInventory(EquipmentSlot equipmentSlot, Predicate<ItemStack> predicate) {
        for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            if (predicate.test(this.getInventory().getItem(i))) {
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
    public boolean switchInventory(InteractionHand hand, Predicate<ItemStack> predicate) {
        return switchInventory(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, predicate);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float strength) {
        final ItemStack projectile = this.getProjectile(this.getMainHandItem());
        if (this.getMainHandItem().getItem() instanceof BowItem) {
            AbstractArrow abstractarrow = this.getArrow(projectile, strength);
            abstractarrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 2F, 2F);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            if (this.getRandom().nextFloat() < 0.4F) {
                abstractarrow.setCritArrow(true);
            }
            this.level().addFreshEntity(abstractarrow);
        }
    }

    @Override
    public ItemStack getProjectile(ItemStack stack) {
        if (stack.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem) stack.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return net.minecraftforge.common.ForgeHooks.getProjectile(this, stack, itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack);
        } else {
            return net.minecraftforge.common.ForgeHooks.getProjectile(this, stack, ItemStack.EMPTY);
        }
    }

    @Override
    public ItemStack eat(Level level, ItemStack itemStack) {
        if (itemStack.isEdible()) {
            FoodProperties foodProperties = itemStack.getFoodProperties(this);
            if (foodProperties != null) {
                this.heal(foodProperties.getNutrition() * 0.5F);
            }
        }
        return super.eat(level, itemStack);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity livingEntity) {
        final double reach = this.getAttributeValue(ForgeMod.ENTITY_REACH.get());
        return Math.max(reach * reach + livingEntity.getBbWidth(), super.getMeleeAttackRangeSqr(livingEntity));
    }

    /**
     * 不设置的话，会卡在一个地方不能前进。
     */
    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return this.distanceToSqr(entity) <= this.getMeleeAttackRangeSqr(entity);
    }

    public double getAttackCoolDown() {
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

    protected SimpleContainer createInventory() {
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
                IMMMemories.UNABLE_MELEE_ATTACK.get(),
                IMMMemories.UNABLE_RANGE_ATTACK.get(),
                IMMMemories.NEAREST_BOAT.get(),
                IMMMemories.NEAREST_PROJECTILE.get()
        );
    }

    protected ImmutableList<SensorType<? extends Sensor<? super HumanEntity>>> getSensorModules() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS,
                SensorType.NEAREST_BED,
                SensorType.HURT_BY,
                IMMSensors.NEAREST_BOAT.get(),
                IMMSensors.HAS_PROJECTILE_NEARBY.get()
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
        if (!this.spawnEggMatch(itemstack) && this.canTradeWith(player)) {
//            if (hand == InteractionHand.MAIN_HAND) {
//                player.awardStat(Stats.TALKED_TO_VILLAGER);
//            }
            if (EntityHelper.isServer(this) && player instanceof ServerPlayer serverPlayer) {
                this.setTradingPlayer(player);
                NetworkHooks.openScreen(serverPlayer, new ImmortalMenuProvider() {
                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        return new MerchantTradeMenu(id, inventory, HumanEntity.this);
                    }
                });
                this.updateTradeOffers();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void notifyTrade(TradeOffer offer) {
        offer.consume();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        if (EntityHelper.isServer(this)) {
            this.setTradeOffers(this.getTradeOffers());
        }
//        this.rewardTradeXp(offer);
//        if (this.tradingPlayer instanceof ServerPlayer) {
//            CriteriaTriggers.TRADE.trigger((ServerPlayer)this.tradingPlayer, this, offer.getResult());
//        }
//        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.TradeWithVillagerEvent(this.tradingPlayer, offer, this));
    }

    public boolean canTradeWith(Player player) {
        return this.isAlive() && !this.isTrading() && !this.isBaby() && BehaviorUtil.isIdle(this) && !player.isSecondaryUseActive();
    }

    @Override
    public void setTradingPlayer(@javax.annotation.Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Override
    @Nullable
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public TradeOffers getTradeOffers() {
        return this.tradeOffers;
    }

    @Override
    public void setTradeOffers(TradeOffers tradeOffers) {
        this.tradeOffers = tradeOffers;
        this.updateTradeOffers();
    }

    public void updateTradeOffers(){
        if(getTradingPlayer() instanceof ServerPlayer player && player.containerMenu instanceof MerchantTradeMenu){
            NetworkHandler.sendToClient(player, new TradeOffersPacket(player.containerMenu.containerId, this.tradeOffers));
        }
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

    public void fillInventoryWith(ItemStack stack, int minCount, int maxCount) {
        fillInventoryWith(stack, 1F, minCount, maxCount);
    }

    public void fillInventoryWith(ItemStack stack, float chance, int count) {
        fillInventoryWith(stack, chance, count, count);
    }

    public void fillInventoryWith(ItemStack stack, float chance, int minCount, int maxCount) {
        if (RandomHelper.chance(this.getRandom(), chance) && this.getInventory().canAddItem(stack)) {
            final int count = RandomHelper.getMinMax(this.getRandom(), minCount, maxCount);
            for (int i = 0; i < count; ++i) {
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

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_TRADE;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Inventory")) {
            this.inventory.fromTag(tag.getList("Inventory", 10));
        }
        if (tag.contains("InventoryLoot")) { // allow setting loot by nbt.
            CodecHelper.parse(ResourceKey.codec(HumanSettings.registry().getRegistryKey()), tag.get("InventoryLoot"))
                    .result().ifPresent(key -> {
                        HumanSettings.registry().getValue(level(), key).fillInventory(this.getInventory(), this.getRandom());
                    });
        }
        this.setTradeOffers(new TradeOffers(tag));
        if (tag.contains("HumanSetting")) {
            CodecHelper.parse(HumanSetting.CODEC, tag.get("HumanSetting"))
                    .result().ifPresent(this::setHumanSetting);
        }
        if (tag.contains("HumanSectData")) {
            CodecHelper.parse(HumanSectData.CODEC, tag.get("HumanSectData")).result().ifPresent(this::setSectData);
        }
        if (this.level() instanceof ServerLevel) {
            this.refreshBrain((ServerLevel) this.level());
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inventory", this.inventory.createTag());
        if (this.tradeOffers != null) {
            this.tradeOffers.addToTag(tag);
        }
        HumanSetting.CODEC.encodeStart(NbtOps.INSTANCE, this.getHumanSetting())
                .result().ifPresent(l -> tag.put("HumanSetting", l));
        CodecHelper.encodeNbt(HumanSectData.CODEC, this.getSectData())
                .result().ifPresent(l -> tag.put("HumanSectData", l));
    }

    @Override
    public Optional<ISectType> getOuterSect() {
        return this.getSectData().outerSect();
    }

    @Override
    public Optional<ISectType> getInnerSect() {
        return this.getSectData().innerSect();
    }

    public HumanSetting getHumanSetting() {
        return entityData.get(HUMAN_SETTING);
    }

    public void setHumanSetting(HumanSetting humanSetting) {
        entityData.set(HUMAN_SETTING, humanSetting);
    }

    public void setSectData(HumanSectData data) {
        entityData.set(SECT_DATA, data);
    }

    public HumanSectData getSectData() {
        return entityData.get(SECT_DATA);
    }

    @Override
    public boolean isClientSide() {
        return this.level().isClientSide();
    }

    public record HumanSectData(Optional<ISectType> outerSect, Optional<ISectType> innerSect) {

        public static final Codec<HumanSectData> CODEC = RecordCodecBuilder.<HumanSectData>mapCodec(instance -> instance.group(
                Codec.optionalField("outer_sect", SectTypes.registry().byNameCodec()).forGetter(HumanSectData::outerSect),
                Codec.optionalField("inner_sect", SectTypes.registry().byNameCodec()).forGetter(HumanSectData::innerSect)
        ).apply(instance, HumanSectData::new)).codec();

    }

}
