package hungteen.immortal.common.entity.human;

import com.mojang.authlib.GameProfile;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:16
 **/
public class Cultivator extends HumanEntity{

    private static final EntityDataAccessor<Integer> CULTIVATOR_TYPE = SynchedEntityData.defineId(Cultivator.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SLIM = SynchedEntityData.defineId(Cultivator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<CompoundTag> ROOTS = SynchedEntityData.defineId(Cultivator.class, EntityDataSerializers.COMPOUND_TAG);
    private List<ISpiritualRoot> rootsCache;

    public Cultivator(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CULTIVATOR_TYPE, CultivatorTypes.HUNG_TEEN.ordinal());
        entityData.define(SLIM, false);
        entityData.define(ROOTS, new CompoundTag());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compoundTag) {
        if(!accessor.isClientSide()){
            final int type = accessor.getRandom().nextInt(CultivatorTypes.values().length);
            this.setCultivatorType(CultivatorTypes.values()[type]);
            this.setSlim(accessor.getRandom().nextInt(2) == 0);
            PlayerUtil.getSpiritualRoots(accessor.getRandom()).forEach(this::addSpiritualRoots);
        }
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("CultivatorType")){
            this.setCultivatorType(CultivatorTypes.values()[tag.getInt("CultivatorType")]);
        }
        if(tag.contains("CultivatorSlim")){
            this.setSlim(tag.getBoolean("CultivatorSlim"));
        }
        if(tag.contains("CultivatorRoots")){
            setRootTag(tag.getCompound("CultivatorRoots"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CultivatorType", this.getCultivatorType().ordinal());
        tag.putBoolean("CultivatorSlim", this.isSlim());
        tag.put("CultivatorRoots", this.getRootTag());
    }

    public void setCultivatorType(CultivatorTypes skin){
        entityData.set(CULTIVATOR_TYPE, skin.ordinal());
    }

    public CultivatorTypes getCultivatorType(){
        return CultivatorTypes.values()[entityData.get(CULTIVATOR_TYPE)];
    }

    public void setSlim(boolean slim) {
        entityData.set(SLIM, slim);
    }

    public boolean isSlim() {
        return entityData.get(SLIM);
    }

    public void addSpiritualRoots(ISpiritualRoot spiritualRoot){
        final CompoundTag tag = getRootTag();
        tag.putBoolean(spiritualRoot.getRegistryName(), true);
        this.rootsCache.add(spiritualRoot);
        setRootTag(tag);
    }

    @Override
    public Collection<ISpiritualRoot> getSpiritualRoots() {
        if(this.rootsCache == null){
            this.rootsCache = new ArrayList<>();
            ImmortalAPI.get().getSpiritualRoots().forEach(root -> {
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

    /**
     * 定制散修，只添加对模组有贡献的玩家，不可修改！
     * Only add players that contribute to the mod.
     */
    public enum CultivatorTypes {

        HUNG_TEEN("_PangTeen_", "6b78b0dc-ed31-4da6-86fb-8d36092b1023", 10),
        NEW_COMETS("_NewComets_", "f8010ab1-f2ce-45d9-ab78-72b513dc5d52"),
        GRASS_CARP("GrassCarp", "9017a74b-ccf4-4896-a3c4-f5ab241ecda1", 25),
        YU("Yu______","314c6ac6-eb95-4994-96eb-85ee2b676a83"),
        ;

        private final UUID uuid;
        private final String name;
        private GameProfile gameProfile;
        private final int weight;

        CultivatorTypes(String name, String uuid) {
            this(name, uuid, 100);
        }

        CultivatorTypes(String name, String uuid, int weight) {
            this.name = name;
            this.uuid = UUID.fromString(uuid);
            this.weight = weight;
        }

        public UUID getUUID() {
            return uuid;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public void setGameProfile(GameProfile gameProfile) {
            this.gameProfile = gameProfile;
        }

        public GameProfile getGameProfile() {
            return gameProfile;
        }
    }
}
