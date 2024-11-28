package hungteen.imm.util;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.*;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.capability.IMMAttachments;
import hungteen.imm.common.capability.player.CultivationData;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.capability.player.SpellData;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-24 16:23
 **/
public class PlayerUtil {

    public static boolean notConsume(Player player) {
        return player.getAbilities().instabuild;
    }

    public static boolean isCreativeOrSpectator(Player player) {
        return player.isCreative() || player.isSpectator();
    }

    public static void setCoolDown(Player player, Item item, int coolDown) {
        player.getCooldowns().addCooldown(item, coolDown);
    }

    /**
     * Add distance parameter.
     */
    public static boolean stillValid(ContainerLevelAccess levelAccess, Player player, Block block, double distance) {
        return levelAccess.evaluate((level, blockPos) -> level.getBlockState(blockPos).is(block) && player.distanceToSqr(MathHelper.toVec3(blockPos)) <= distance, true);
    }

    /**
     * 往背包添加物品，如果失败则掉落。
     *
     * @return True if not add into inventory.
     */
    public static boolean addItem(Player player, ItemStack stack) {
        if (player.addItem(stack)) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return true;
        } else {
            ItemEntity itementity = player.drop(stack, false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setThrower(player);
            }
            return false;
        }
    }

    public static boolean isInCD(Player player, Item item) {
        return player.getCooldowns().isOnCooldown(item);
    }

    /* Data Operations */

    @Deprecated(forRemoval = true)
    public static Optional<IMMPlayerData> getDataOpt(Player player) {
        return Optional.of(getData(player));
    }

    public static IMMPlayerData getData(Player player) {
        return player.getData(IMMAttachments.PLAYER_DATA);
    }

    public static <T> T getData(Player player, Function<IMMPlayerData, T> function) {
        return function.apply(getData(player));
    }

    public static void setData(Player player, Consumer<IMMPlayerData> consumer) {
        consumer.accept(getData(player));
    }

    /* Operations About Spiritual Roots */

    /**
     * 重置玩家的灵根。
     */
    public static void resetRoots(Player player) {
        setData(player, l -> {
            CultivationData cultivationData = l.getCultivationData();
            cultivationData.clearSpiritualRoot();
            CultivationManager.getQiRoots(player.getRandom()).forEach(cultivationData::addRoot);
        });
    }

    public static boolean hasRoot(Player player, QiRootType root) {
        return getData(player, l -> l.getCultivationData().hasRoot(root));
    }

    public static List<QiRootType> getRoots(Player player) {
        return getData(player, l -> l.getCultivationData().getRoots());
    }

    public static boolean knowRoots(Player player) {
        return getIntegerData(player, IMMPlayerData.IntegerData.KNOW_SPIRITUAL_ROOTS) > 0;
    }

    /**
     * 学习了业元素精通=知道了业元素的存在。
     */
    public static boolean knowSpiritElement(Player player) {
        return getData(player, l -> l.getSpellData().hasLearnedSpell(SpellTypes.SPIRIT_MASTERY, 1));
    }

    public static List<QiRootType> filterSpiritRoots(Player player, List<QiRootType> roots) {
        return roots.stream().filter(root -> {
            if (root == QiRootTypes.SPIRIT) {
                return knowSpiritElement(player);
            }
            return true;
        }).toList();
    }

    public static List<Element> filterElements(Player player, List<Element> elements) {
        return elements.stream().filter(element -> {
            if (element == Element.SPIRIT) {
                return knowSpiritElement(player);
            }
            return true;
        }).toList();
    }

    /* Operations About Spells */

    public static void learnSpell(Player player, SpellType spell, int level) {
        setData(player, data -> data.getSpellData().learnSpell(spell, level));
    }

    public static void forgetSpell(Player player, SpellType spell) {
        setData(player, data -> data.getSpellData().forgetSpell(spell));
    }

    /**
     * @param pos 法术轮盘的位置。
     * @param spell 安装的法术。
     */
    public static void setSpellAt(Player player, int pos, @Nullable SpellType spell) {
        setData(player, data -> {
            SpellData spellData = data.getSpellData();
            if (spell == null) {
                spellData.removeSpellAt(pos);
            } else {
                // 没有学会则先学。
                if (!spellData.hasLearnedSpell(spell, 1)) {
                    spellData.learnSpell(spell, 1);
                }
                spellData.setSpellAt(pos, spell);
            }
        });
    }

    public static void removeSpellAt(Player player, int pos) {
        setData(player, data -> data.getSpellData().removeSpellAt(pos));
    }

    public static void cooldownSpell(Player player, SpellType spell, long num) {
        setData(player, data -> data.getSpellData().cooldownSpell(spell, num));
    }

    @Nullable
    public static SpellType getSpellAt(Player player, int pos) {
        return getData(player, data -> data.getSpellData().getSpellAt(pos));
    }

    public static boolean isSpellOnCircle(Player player, SpellType spell) {
        return getData(player, data -> data.getSpellData().isSpellOnCircle(spell));
    }

    public static boolean isSpellOnCoolDown(Player player, SpellType spell) {
        return getData(player, data -> data.getSpellData().isSpellOnCoolDown(spell));
    }

    public static float getSpellCooldownValue(Player player, SpellType spell) {
        return getData(player, data -> data.getSpellData().getSpellCoolDown(spell));
    }

    public static boolean hasLearnedSpell(Player player, SpellType spell) {
        return hasLearnedSpell(player, spell, 1);
    }

    public static boolean hasLearnedSpell(Player player, SpellType spell, int level) {
        return getData(player, data -> data.getSpellData().hasLearnedSpell(spell, level));
    }

    public static int getSpellLevel(Player player, SpellType spell) {
        return getData(player, data -> data.getSpellData().getSpellLevel(spell));
    }

    @Nullable
    public static SpellType getPreparingSpell(Player player) {
        return getData(player, data -> data.getSpellData().getPreparingSpell());
    }

    public static void setPreparingSpell(Player player, @Nullable SpellType type) {
        setData(player, data -> data.getSpellData().setPreparingSpell(type));
    }

    /* Sect Related Methods */

    public static float getSectRelation(Player player, ISectType sectType) {
        return getData(player, m -> m.getSectRelation(sectType));
    }

    public static void setSectRelation(Player player, ISectType sectType, float value) {
        getData(player).setSectRelation(sectType, value);
    }

    public static void addSectRelation(Player player, ISectType sectType, float value) {
        getData(player).addSectRelation(sectType, value);
    }

    /* Experience Related Methods */

    public static void setExperience(Player player, ExperienceType type, float value) {
        setData(player, data -> data.getCultivationData().setExperience(type, value));
    }

    public static void addExperience(Player player, ExperienceType type, float value) {
        setData(player, data -> data.getCultivationData().addExperience(type, value));
    }

    public static float getExperience(Player player, ExperienceType type) {
        return getData(player, l -> l.getCultivationData().getExperience(type));
    }

    public static float getCultivation(Player player) {
        return getData(player, l -> l.getCultivationData().getCultivation());
    }

    public static float getEachMaxCultivation(Player player) {
        return getData(player, l -> CultivationManager.getEachCultivation(player, l.getCultivationData().getRealmType()));
    }

    public static float getMaxCultivation(Player player) {
        return getData(player, l -> CultivationManager.getRequiredCultivation(player, l.getCultivationData().getRealmType()));
    }

    public static boolean reachThreshold(Player player) {
        return getPlayerRealmStage(player).canLevelUp() || getCultivation(player) >= getMaxCultivation(player);
    }

    /* Operations about Player Range Data */

    public static void setIntegerData(Player player, IMMPlayerData.IntegerData rangeData, int value) {
        getData(player).setIntegerData(rangeData, value);
    }

    public static void addIntegerData(Player player, IMMPlayerData.IntegerData rangeData, int value) {
        getData(player).addIntegerData(rangeData, value);
    }

    public static int getIntegerData(Player player, IMMPlayerData.IntegerData rangeData) {
        return getData(player, m -> m.getIntegerData(rangeData));
    }

    public static void setFloatData(Player player, IMMPlayerData.FloatData rangeData, float value) {
        getData(player).setFloatData(rangeData, value);
    }

    public static void addFloatData(Player player, IMMPlayerData.FloatData rangeData, float value) {
        getData(player).addFloatData(rangeData, value);
    }

    public static float getFloatData(Player player, IMMPlayerData.FloatData rangeData) {
        return getData(player, m -> m.getFloatData(rangeData));
    }

    public static void addQiAmount(Player player, float amount) {
        addFloatData(player, IMMPlayerData.FloatData.QI_AMOUNT, amount);
    }

    public static void setQiAmount(Player player, float amount) {
        setFloatData(player, IMMPlayerData.FloatData.QI_AMOUNT, amount);
    }

    public static float getQiAmount(Player player) {
        return getFloatData(player, IMMPlayerData.FloatData.QI_AMOUNT);
    }

    public static float getMaxQi(Player player) {
        return (float) EntityUtil.getMaxQi(player);
    }

    public static boolean isQiFull(Player player) {
        return getQiAmount(player) >= getMaxQi(player);
    }

    /**
     * 待客户端配置文件更新该值。
     */
    public static boolean requireSyncCircle(Player player) {
        return getSpellCircleMode(player) == 0;
    }

    public static boolean useDefaultCircle(Player player) {
        return getSpellCircleMode(player) == 1;
    }

    public static int getSpellCircleMode(Player player) {
        return getIntegerData(player, IMMPlayerData.IntegerData.SPELL_CIRCLE_MODE);
    }

    public static void setSpellCircleMode(Player player, int mode) {
        setIntegerData(player, IMMPlayerData.IntegerData.SPELL_CIRCLE_MODE, mode);
    }

    public static boolean sitToMeditate(Player player, BlockPos pos, float yOffset, boolean relyOnBlock) {
        if (!isSitInMeditation(player)) {
            final Vec3 vec = MathHelper.toVec3(pos);
            List<Monster> list = player.level().getEntitiesOfClass(Monster.class, MathHelper.getAABB(MathHelper.toVec3(pos), 8F, 5F), (entity) -> {
                return entity.isPreventingPlayerRest(player);
            });
            if (list.isEmpty()) {
                if (EntityUtil.sitToMeditate(player, pos, yOffset, relyOnBlock)) {
                    setIntegerData(player, IMMPlayerData.IntegerData.IS_MEDITATING, 1);
                    return true;
                }
            } else {
                PlayerHelper.sendTipTo(player, TipUtil.info("unsafe_surround").withStyle(ChatFormatting.RED));
            }
        }
        return false;
    }

    public static void quitResting(Player player) {
        if (isSitInMeditation(player)) {
            setIntegerData(player, IMMPlayerData.IntegerData.IS_MEDITATING, 0);
            player.stopRiding();
        }
    }

    public static boolean isSitInMeditation(Player player) {
        return getIntegerData(player, IMMPlayerData.IntegerData.IS_MEDITATING) > 0;
    }

    public static RealmType getPlayerRealm(Player player) {
        return getData(player, data -> data.getCultivationData().getRealmType());
    }

    public static RealmStage getPlayerRealmStage(Player player) {
        return getPlayerRealm(player).getStage();
    }

    public static CultivationType getCultivationType(Player player) {
        return getPlayerRealm(player).getCultivationType();
    }

}
