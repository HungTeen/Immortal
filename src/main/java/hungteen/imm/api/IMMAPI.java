package hungteen.imm.api;

import com.mojang.logging.LogUtils;
import hungteen.htlib.api.registry.HTSimpleRegistry;
import hungteen.htlib.api.util.helper.ServiceHelper;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:26
 **/
public interface IMMAPI {

    String MOD_ID = "htlib";

    Logger LOGGER = LogUtils.getLogger();

    IMMAPI INSTANCE = ServiceHelper.findService(IMMAPI.class, () -> new IMMAPI() {});

    /**
     * Obtain the Mod API, either a valid implementation if mod is present, else
     * a dummy instance instead if mod is absent.
     */
    static IMMAPI get() {
        return INSTANCE;
    }

    static String id() {
        return MOD_ID;
    }

    /**
     * @return the log instance for the mod.
     */
    static Logger logger() {
        return LOGGER;
    }

    /**
     * @return A unique version number for this version of the API.
     */
    default int apiVersion() {
        return 0;
    }

    /**
     * 注册灵根类型。<br>
     * Only registered roots can participate in human's randomly choosing roots when born.
     *
     * @return Registry interface.
     */
    default Optional<HTSimpleRegistry<ISpiritualType>> spiritualRegistry(){
        return Optional.empty();
    }

    /**
     * 注册境界类型。 <br>
     * Register the realm type so that it can be stored as nbt.
     *
     * @return Registry interface.
     */
    default Optional<HTSimpleRegistry<IRealmType>> realmRegistry(){
        return Optional.empty();
    }

    /**
     * 注册法术类型。 <br>
     * Only registered entries can be displayed on Spell Menu.
     *
     * @return Registry interface.
     */
    default Optional<HTSimpleRegistry<ISpellType>> spellRegistry(){
        return Optional.empty();
    }

//
//    /**
//     * 注册玩家Int数据类型。<br>
//     * Stored in player capabilities.
//     *
//     * @return Registry interface.
//     */
//    Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry();
//
//    /**
//     * 注册玩家Float数据类型。<br>
//     * Stored in player capabilities.
//     *
//     * @return Registry interface.
//     */
//    Optional<IHTSimpleRegistry<IRangeNumber<Float>>> floatDataRegistry();
//
//    /**
//     * 注册宗门类型。 <br>
//     *
//     * @return Registry interface.
//     */
//    Optional<IHTSimpleRegistry<ISectType>> sectRegistry();

    /**
     * 获取生物的境界。
     *
     * @param entity the entity that wants to know its realm type.
     * @return the realm type of the given entity.
     */
    default Optional<IRealmType> getEntityRealm(Entity entity){
        return Optional.empty();
    }

    /**
     * 获取玩家灵气值。
     *
     * @param player query player.
     * @return how many spiritual mana in player.
     */
    default float getSpiritualMana(Player player){
        return 0F;
    }

    /**
     * 设置维度的境界限制。
     *
     * @param level        the resource key of level.
     * @param lowestRealm  the lower bound realm value in the given level.
     * @param highestRealm the upper bound realm value in the given level.
     */
    default void registerLevelRealmSetting(ResourceKey<Level> level, int lowestRealm, int highestRealm){

    }

    /**
     * 设置群系的灵气回复速率。
     *
     * @param biome     the resource key of biome.
     * @param minChange the min change value of spiritual value in the given biome.
     * @param maxChange the max change value of spiritual value in the given biome.
     */
    default void registerBiomeRealmSetting(ResourceKey<Biome> biome, float minChange, float maxChange){

    }

    /**
     * 获取当前位置的灵气恢复值。
     *
     * @param level current level to be queried.
     * @param pos   current position to be queried.
     * @return calculate the spiritual change value at the given level and position.
     */
    default float getSpiritualRate(Level level, BlockPos pos){
        return 0F;
    }

}
