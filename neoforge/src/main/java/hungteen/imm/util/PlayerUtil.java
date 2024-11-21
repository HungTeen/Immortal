package hungteen.imm.util;

import hungteen.htlib.api.registry.RangeNumber;
import hungteen.htlib.util.WeightedList;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.IMMConfigs;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.enums.ExperienceTypes;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.capability.IMMAttachments;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.impl.registry.QiRootTypes;
import hungteen.imm.common.spell.SpellTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static boolean sitToMeditate(Player player, BlockPos pos, float yOffset, boolean relyOnBlock) {
        if (!isSitInMeditation(player)) {
            final Vec3 vec = MathHelper.toVec3(pos);
            List<Monster> list = player.level().getEntitiesOfClass(Monster.class, MathHelper.getAABB(MathHelper.toVec3(pos), 8F, 5F), (entity) -> {
                return entity.isPreventingPlayerRest(player);
            });
            if (list.isEmpty()) {
                if (EntityUtil.sitToMeditate(player, pos, yOffset, relyOnBlock)) {
                    setIntegerData(player, PlayerRangeIntegers.MEDITATE_TICK, 1);
                    return true;
                }
            } else {
                PlayerHelper.sendTipTo(player, TipUtil.info("unsafe_surround").withStyle(ChatFormatting.RED));
            }
        }
        return false;
    }

    public static void quitMeditate(Player player) {
        if (isSitInMeditation(player)) {
            setIntegerData(player, PlayerRangeIntegers.MEDITATE_TICK, 0);
            player.stopRiding();
        }
    }

    public static boolean isSitInMeditation(Player player) {
        return getIntegerData(player, PlayerRangeIntegers.MEDITATE_TICK) > 0;
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

    /**
     * 玩家灵根的生成规则： <br>
     * 1. 首先依据概率选择是几个灵根（0 - 5）。 <br>
     * 2. 如果是1个灵根，那么依据权重在普通灵根和异灵根中选择一个。 <br>
     * 3. 否则依据权重在普通五行灵根中选择若干个。 <br>
     *
     * @return 随机生成的灵根。
     */
    public static List<QiRootType> getSpiritualRoots(RandomSource random) {
        final double[] rootChances = {IMMConfigs.getNoRootChance(), IMMConfigs.getOneRootChance(), IMMConfigs.getTwoRootChance(), IMMConfigs.getThreeRootChance(), IMMConfigs.getFourRootChance()};
        return getSpiritualRoots(random, rootChances);
    }

    /**
     * @param rootChances 每个灵根数量的概率，建议数组长度为 5。
     */
    public static List<QiRootType> getSpiritualRoots(RandomSource random, double[] rootChances) {
        double chance = random.nextDouble();
        for (int i = 0; i < Math.max(Constants.MAX_ROOT_AMOUNT, rootChances.length); ++i) {
            if (chance < rootChances[i]) {
                return randomSpawnRoots(random, i);
            }
            chance -= rootChances[i];
        }
        return randomSpawnRoots(random, 5);
    }

    /**
     * 只有灵根数量为 1 时，才需要考虑变异灵根。
     * {@link #getSpiritualRoots(RandomSource)}
     */
    private static List<QiRootType> randomSpawnRoots(RandomSource random, int rootCount) {
        final List<QiRootType> rootChosen = new ArrayList<>();
        if (rootCount == 1) {
            rootChosen.addAll(WeightedList.create(QiRootTypes.registry().getValues().stream().toList()).getRandomItems(random, 1, true));
        } else if (rootCount > 1) {
            rootChosen.addAll(WeightedList.create(QiRootTypes.registry().getValues().stream()
                    .filter(JavaHelper.not(QiRootType::isSpecialRoot))
                    .collect(Collectors.toList())
            ).getRandomItems(random, rootCount, true));
        }
        return rootChosen;
    }

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
    public static void resetSpiritualRoots(Player player) {
        setData(player, l -> {
            l.clearSpiritualRoot();
            getSpiritualRoots(player.getRandom()).forEach(l::addRoot);
        });
    }

    /* Operations About Spells */

    public static void learnSpell(Player player, ISpellType spell, int level) {
        setData(player, l -> l.learnSpell(spell, level));
    }

    public static void forgetSpell(Player player, ISpellType spell) {
        setData(player, l -> l.forgetSpell(spell));
    }

    public static void learnAllSpell(Player player, int level) {
        setData(player, data -> SpellTypes.registry().getValues().forEach(spell -> data.learnSpell(spell, level)));
    }

    public static void forgetAllSpell(Player player) {
        setData(player, IMMPlayerData::forgetAllSpells);
    }

    public static void setSpellAt(Player player, int pos, @Nullable ISpellType spell) {
        setData(player, data -> {
            if (spell == null) {
                data.removeSpellAt(pos);
            } else {
                // 没有学会则先学。
                if (!data.hasLearnedSpell(spell, 1)) {
                    data.learnSpell(spell, 1);
                }
                data.setSpellAt(pos, spell);
            }
        });
    }

    public static void removeSpellAt(Player player, int pos) {
        getData(player).removeSpellAt(pos);
    }

    public static void cooldownSpell(Player player, ISpellType spell, long num) {
        getData(player).cooldownSpell(spell, num);
    }

    @Nullable
    public static ISpellType getSpellAt(Player player, int pos) {
        return getData(player, m -> m.getSpellAt(pos));
    }

    public static boolean isSpellOnCircle(Player player, ISpellType spell) {
        return getData(player, m -> m.isSpellOnCircle(spell));
    }

    public static boolean isSpellOnCoolDown(Player player, ISpellType spell) {
        return getData(player, m -> m.isSpellOnCoolDown(spell));
    }

    public static float getSpellCDValue(Player player, ISpellType spell) {
        return getData(player, m -> m.getSpellCoolDown(spell));
    }

    public static boolean hasLearnedSpell(Player player, ISpellType spell) {
        return hasLearnedSpell(player, spell, 1);
    }

    public static boolean hasLearnedSpell(Player player, ISpellType spell, int level) {
        return getData(player, m -> m.hasLearnedSpell(spell, level));
    }

    public static int getSpellLevel(Player player, ISpellType spell) {
        return getData(player, m -> m.getSpellLevel(spell));
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

    public static void setExperience(Player player, ExperienceTypes type, float value) {
        getData(player).setExperience(type, value);
    }

    public static void addExperience(Player player, ExperienceTypes type, float value) {
        getData(player).addExperience(type, value);
    }

    public static float getExperience(Player player, ExperienceTypes type) {
        return getData(player, l -> l.getExperience(type));
    }

    public static float getCultivation(Player player) {
        return getData(player, IMMPlayerData::getCultivation);
    }

    /* Operations about Player Range Data */

    public static void setIntegerData(Player player, RangeNumber<Integer> rangeData, int value) {
        getData(player).setIntegerData(rangeData, value);
    }

    public static void addIntegerData(Player player, RangeNumber<Integer> rangeData, int value) {
        getData(player).addIntegerData(rangeData, value);
    }

    public static int getIntegerData(Player player, RangeNumber<Integer> rangeData) {
        return getData(player, m -> m.getIntegerData(rangeData));
    }

    public static void setFloatData(Player player, RangeNumber<Float> rangeData, float value) {
        getData(player).setFloatData(rangeData, value);
    }

    public static void addFloatData(Player player, RangeNumber<Float> rangeData, float value) {
        getData(player).addFloatData(rangeData, value);
    }

    public static float getFloatData(Player player, RangeNumber<Float> rangeData) {
        return getData(player, m -> m.getFloatData(rangeData));
    }

    public static float getMana(Player player) {
        return getFloatData(player, PlayerRangeFloats.SPIRITUAL_MANA);
    }

    public static float getMaxMana(Player player) {
        return getData(player, IMMPlayerData::getFullManaValue);
    }

    public static boolean isManaFull(Player player) {
        return getData(player, l -> l.getFloatData(PlayerRangeFloats.SPIRITUAL_MANA) >= l.getFullManaValue());
    }

    public static int getSpellCircleMode(Player player) {
        return getData(player, l -> l.getIntegerData(PlayerRangeIntegers.SPELL_CIRCLE_MODE));
    }

    public static void setSpellCircleMode(Player player, int mode) {
        getData(player).setIntegerData(PlayerRangeIntegers.SPELL_CIRCLE_MODE, mode);
    }

    public static boolean knowSpiritualRoots(Player player) {
        return getData(player, l -> l.getIntegerData(PlayerRangeIntegers.KNOW_SPIRITUAL_ROOTS) == 1);
    }

    /**
     * 学习了业元素精通=知道了业元素的存在。
     */
    public static boolean knowSpiritElement(Player player) {
        return getData(player, l -> l.hasLearnedSpell(SpellTypes.SPIRIT_MASTERY, 1));
    }

    /* Misc Operations */

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

    public static boolean hasRoot(Player player, QiRootType root) {
        return getData(player, l -> l.hasRoot(root));
    }

    public static List<QiRootType> getSpiritualRoots(Player player) {
        return getData(player, IMMPlayerData::getRoots);
    }

    public static IRealmType getPlayerRealm(Player player) {
        return getData(player, IMMPlayerData::getRealmType);
    }

    public static RealmStages getPlayerRealmStage(Player player) {
        return getData(player, IMMPlayerData::getRealmStage);
    }

    /**
     * Only used on client side.
     */
    public static void clientSetRealm(Player player, IRealmType realm) {
        checkAndSetRealm(player, realm, RealmStages.PRELIMINARY, false);
    }

    /**
     * 尝试直接改变境界。
     *
     * @return 是否改变成功。
     */
    public static boolean checkAndSetRealm(Player player, IRealmType realm, @NotNull RealmStages stage, boolean force) {
        // 自身修为达到了此境界的要求。
        return Boolean.TRUE.equals(getData(player, m -> {
            if (EntityHelper.isServer(player)) {
                // 自身修为达到了此境界的要求。
                if (m.getCultivation() >= RealmManager.getStageRequiredCultivation(realm, stage)) {
                    m.setRealmType(realm);
                    m.setRealmStage(stage);
                } else {
                    if (force) {
                        final float requiredXp = RealmManager.getStageRequiredCultivation(realm, stage) / ExperienceTypes.values().length;
                        for (ExperienceTypes type : ExperienceTypes.values()) {
                            m.setExperience(type, requiredXp);
                        }
                        m.setRealmType(realm);
                        m.setRealmStage(stage);
                    }
                    return force;
                }
            } else {
                m.setRealmType(realm);
            }
            return true;
        }));
    }

    /**
     * 直接改变境界阶段。
     *
     * @return 是否改变成功。
     */
    public static boolean checkAndSetRealmStage(Player player, RealmStages stage) {
        boolean success = true;
        IMMPlayerData data = getData(player);
        if (EntityHelper.isServer(player)) {
            // 自身修为达到了此境界阶段的要求。
            if (data.getCultivation() >= RealmManager.getStageRequiredCultivation(data.getRealmType(), stage)) {
                data.setRealmStage(stage);
            } else {
                data.setFloatData(PlayerRangeFloats.BREAK_THROUGH_PROGRESS, 0F);
                success = false;
            }
        } else {
            data.setRealmStage(stage);
        }
        return success;
    }

    public static float getEachMaxCultivation(Player player) {
        return getData(player, l -> RealmManager.getEachCultivation(l.getRealmType()));
    }

    public static float getMaxCultivation(Player player) {
        return getData(player, l -> {
            final RealmStages nextStage = RealmStages.next(l.getRealmStage());
            return RealmManager.getStageRequiredCultivation(l.getRealmType(), nextStage);
        });
    }

    public static boolean reachThreshold(Player player) {
        return getPlayerRealmStage(player).canLevelUp() || getCultivation(player) >= getMaxCultivation(player);
    }

    public static ICultivationType getCultivationType(Player player) {
        return getData(player, l -> l.getRealmType().getCultivationType());
    }

    @Nullable
    public static ISpellType getPreparingSpell(Player player) {
        return getData(player, IMMPlayerData::getPreparingSpell);
    }

    public static void setPreparingSpell(Player player, @Nullable ISpellType type) {
        getData(player).setPreparingSpell(type);
    }

}
