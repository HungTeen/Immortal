package hungteen.imm.common.cultivation;

import hungteen.htlib.util.HTColor;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.capability.entity.IMMEntityData;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.misc.ElementCrystal;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMEntityTags;
import hungteen.imm.util.Colors;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/1 15:19
 */
public class ElementManager {

    public static final int ESCAPE_UPDATE_CD = 20;
    public static final int DISAPPEAR_WARN_CD = 50;
    public static final int DISAPPEAR_WARN_AMOUNT = 5;
    public static final int DISAPPEAR_CD = 5;
    public static final int DISPLAY_ROBUST_CD = 10;
    private static final float DECAY_SPEED = 0.03F;
    private static final float DECAY_VALUE = 0.1F;
    private static final Map<Element, Element> TARGET_ELEMENTS = new EnumMap<>(Element.class);
    private static final Map<Element, TagKey<Block>> ELEMENT_BLOCK_TAGS = new EnumMap<>(Element.class);
    private static final Map<Element, Supplier<SimpleParticleType>> ELEMENT_PARTICLE_MAP = new EnumMap<>(Element.class);
    private static final Map<Element, HTColor> ELEMENT_COLOR_MAP = new EnumMap<>(Element.class);

    static {
        TARGET_ELEMENTS.putAll(Map.of(
                Element.METAL, Element.WOOD,
                Element.WOOD, Element.EARTH,
                Element.WATER, Element.FIRE,
                Element.FIRE, Element.METAL,
                Element.EARTH, Element.WATER,
                Element.SPIRIT, Element.SPIRIT
        ));
        ELEMENT_BLOCK_TAGS.putAll(Map.of(
                Element.METAL, IMMBlockTags.METAL_ELEMENT_ATTACHED_BLOCKS,
                Element.WOOD, IMMBlockTags.WOOD_ELEMENT_ATTACHED_BLOCKS,
                Element.WATER, IMMBlockTags.WATER_ELEMENT_ATTACHED_BLOCKS,
                Element.FIRE, IMMBlockTags.FIRE_ELEMENT_ATTACHED_BLOCKS,
                Element.EARTH, IMMBlockTags.EARTH_ELEMENT_ATTACHED_BLOCKS,
                Element.SPIRIT, IMMBlockTags.SPIRIT_ELEMENT_ATTACHED_BLOCKS
        ));
        ELEMENT_PARTICLE_MAP.putAll(Map.of(
                Element.METAL, IMMParticles.METAL_ELEMENT,
                Element.WOOD, IMMParticles.WOOD_ELEMENT,
                Element.WATER, IMMParticles.WATER_ELEMENT,
                Element.FIRE, IMMParticles.FIRE_ELEMENT,
                Element.EARTH, IMMParticles.EARTH_ELEMENT,
                Element.SPIRIT, IMMParticles.SPIRIT_ELEMENT
        ));
        ELEMENT_COLOR_MAP.putAll(Map.of(
                Element.METAL, ColorHelper.LITTLE_YELLOW1,
                Element.WOOD, ColorHelper.GREEN,
                Element.WATER, ColorHelper.DARK_BLUE,
                Element.FIRE, ColorHelper.RED,
                Element.EARTH, ColorHelper.GRAY,
                Element.SPIRIT, ColorHelper.PURPLE
        ));
    }

    /**
     * 仅服务端。
     */
    public static void tickElements(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.getProfiler().push("TickElements");
            attachElement(entity);
            IMMEntityData data = EntityUtil.getData(entity);
            data.tick();
            serverLevel.getProfiler().pop();
        }
    }

    /**
     * Tick附着元素。
     */
    public static void attachElement(Entity entity) {
        if (entity.tickCount % 5 == 0 && entity.getRandom().nextFloat() < 0.1F) {
            if (entity.isInWaterOrRain()) {
                addElementAmount(entity, Element.WATER, false, 1F);
                return;
            }
            final BlockState state = entity.level().getBlockState(entity.getOnPos());
            for (Map.Entry<Element, TagKey<Block>> entry : ELEMENT_BLOCK_TAGS.entrySet()) {
                if (state.is(entry.getValue())) {
                    addElementAmount(entity, entry.getKey(), false, 1F);
                    break;
                }
            }
        }
    }

    public static Element getTargetElement(Element element) {
        return TARGET_ELEMENTS.getOrDefault(element, Element.SPIRIT);
    }

    public static void clientTickElements(Level level, Entity entity) {
        if (level.getRandom().nextFloat() < 0.2F) {
            for (Element element : Element.values()) {
                if (element == Element.SPIRIT) {
                    continue; // 业元素不显示粒子。
                }
                final float elementAmount = getAmount(entity, element, false);
                final int particleCount = Math.min(Mth.ceil(elementAmount / 15), 5);
                if (particleCount > 0) {
                    ParticleHelper.spawnParticles(level, getParticle(element), entity.position().add(0, entity.getBbHeight() / 2, 0), particleCount, entity.getBbWidth(), entity.getBbHeight() / 2, 0);
                }
            }
        }
    }

    /**
     * 受到伤害之前，附着元素。
     */
    public static void attachDamageElement(ServerLevel level, LivingEntity entity, DamageSource source) {
        if (entity.getRandom().nextFloat() < 0.2F) {
            if (source.is(DamageTypes.IN_FIRE)) { // 篝火。
                addElementAmount(entity, Element.FIRE, false, 1F);
            } else if (source.is(DamageTypes.LAVA)) { // 岩浆。
                addElementAmount(entity, Element.FIRE, false, 2F);
            }
        }
    }

    public static float getDecayFactor(Entity entity) {
        if (entity instanceof LivingEntity living && living.getAttributes().hasAttribute(IMMAttributes.ELEMENT_DECAY_FACTOR.holder())) {
            return (float) living.getAttributeValue(IMMAttributes.ELEMENT_DECAY_FACTOR.holder());
        }
        if (entity instanceof ElementCrystal) {
            return 0.25F;
        }
        return 1F;
    }

    public static float getDecayAmount(Entity entity, Element element, boolean robust) {
        return getDecayAmount(getElementAmount(entity, element, robust), getDecayFactor(entity));
    }

    public static float getDecayAmount(float amount, float factor) {
        return factor * (amount * DECAY_SPEED + DECAY_VALUE);
    }

    public static float getDecayAmount(int tick, float amount, float factor) {
        final float an = (float) Math.pow(1 - factor * DECAY_SPEED, tick);
        return an * amount - (1 - an) * DECAY_VALUE / DECAY_SPEED;
    }

    public static int getLeftTick(Entity entity, Element element, boolean robust) {
        return getLeftTick(getElementAmount(entity, element, robust), getDecayFactor(entity));
    }

    /**
     * 二分法计算元素剩余附着时间。
     *
     * @return -1 if it can not calculate.
     */
    public static int getLeftTick(float amount, float factor) {
        int l = 0, r = 1000000;
        int res = -1;
        while (l <= r) {
            int mid = l + r >> 1;
            if (getDecayAmount(mid, amount, factor) <= 0) {
                res = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return res;
    }

    /**
     * Server side only.
     */
    public static void ifActiveReaction(Entity entity, ElementReaction reaction, BiConsumer<ElementReaction, Float> consumer, Runnable runnable) {
        EntityUtil.setData(entity, cap -> {
            if (cap.isActiveReaction(reaction)) {
                consumer.accept(reaction, cap.getActiveScale(reaction));
            } else {
                runnable.run();
            }
        });
    }

    /**
     * Server side only.
     */
    public static void ifActiveReaction(Entity entity, ElementReaction reaction, BiConsumer<ElementReaction, Float> consumer) {
        ifActiveReaction(entity, reaction, consumer, () -> {
        });
    }

    /**
     * Server side only.
     */
    public static boolean isActiveReaction(Entity entity, ElementReaction reaction) {
        return EntityUtil.getData(entity, cap -> cap.isActiveReaction(reaction));
    }

    /**
     * Server side only.
     */
    public static float getActiveScale(Entity entity, ElementReaction reaction) {
        return EntityUtil.getData(entity, cap -> cap.getActiveScale(reaction));
    }

    public static void setElementAmount(Entity entity, Element element, boolean robust, float value) {
        EntityUtil.setData(entity, cap -> cap.setElementAmount(element, robust, value));
    }

    public static void addElementAmount(Entity entity, Element element, boolean robust, float value, float maxValue) {
        EntityUtil.setData(entity, cap -> cap.addElementAmount(element, robust, value, maxValue));
    }

    public static void addElementAmount(Entity entity, Element element, boolean robust, float value) {
        EntityUtil.setData(entity, cap -> cap.addElementAmount(element, robust, value));
    }

    public static float getElementAmount(Entity entity, Element element, boolean robust) {
        return EntityUtil.getData(entity, cap -> cap.getElementAmount(element, robust));
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static float getAmount(Entity entity, Element element, boolean mustRobust) {
        return EntityUtil.getData(entity, cap -> {
            return cap.getElementAmount(element, true) + (mustRobust ? 0 : cap.getElementAmount(element, false));
        });
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static boolean hasElement(Entity entity, Element element, boolean mustRobust) {
        return EntityUtil.getData(entity, cap -> {
            return cap.hasElement(element, true) || (!mustRobust && cap.hasElement(element, false));
        });
    }

    public static boolean hasElement(Entity entity, boolean mustRobust) {
        for (Element element : Element.values()) {
            if (ElementManager.hasElement(entity, element, mustRobust)) {
                return true;
            }
        }
        return false;
    }

    public static HTColor getElementColor(Entity entity, boolean mustRobust) {
        final Colors.ColorMixer mixer = Colors.mixer();
        for (Element element : Element.values()) {
            if (hasElement(entity, element, mustRobust)) {
                mixer.add(getElementColor(element));
            }
        }
        return mixer.mix();
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static void consumeAmount(Entity entity, Element element, boolean mustRobust, float amount) {
        EntityUtil.setData(entity, cap -> {
            final float robustAmount = cap.getElementAmount(element, true);
            if (robustAmount >= amount || mustRobust) {
                cap.addElementAmount(element, true, -amount);
            } else {
                cap.setElementAmount(element, true, 0);
                cap.addElementAmount(element, false, robustAmount - amount);
            }
        });
    }

    public static void clearElements(Entity entity) {
        EntityUtil.setData(entity, IMMEntityData::clearElements);
    }

    public static Map<Element, Float> getElements(Entity entity) {
        return EntityUtil.getData(entity, IMMEntityData::getElementMap);
    }

    public static void setQuenchBladeDamage(Entity entity, float damage, boolean force) {
        EntityUtil.setData(entity, cap -> {
            if (!force) {
                cap.setQuenchBladeDamage(Math.max(cap.getQuenchBladeDamage(), damage));
            } else {
                cap.setQuenchBladeDamage(damage);
            }
        });
    }

    public static float getQuenchBladeDamage(Entity entity) {
        return EntityUtil.getData(entity, IMMEntityData::getQuenchBladeDamage);
    }

    public static ParticleOptions getParticle(Element element) {
        return ELEMENT_PARTICLE_MAP.getOrDefault(element, IMMParticles.QI).get();
    }

    public static HTColor getElementColor(Element element) {
        return ELEMENT_COLOR_MAP.getOrDefault(element, ColorHelper.WHITE);
    }

    public static void clearActiveReactions(Entity entity) {
        EntityUtil.setData(entity, IMMEntityData::clearElements);
    }

    public static boolean canReactionOn(Entity entity) {
        return !entity.getType().is(IMMEntityTags.NO_ELEMENT_REACTIONS);
    }

    public static boolean displayRobust(Entity entity) {
        return (entity.tickCount % (DISPLAY_ROBUST_CD << 1)) < DISPLAY_ROBUST_CD;
    }

    public static boolean notDisappear(Entity entity) {
        return (entity.tickCount % (DISAPPEAR_CD << 1)) < DISAPPEAR_CD;
    }

    /**
     * Amount need below threshold and last time is less than threshold.
     */
    public static boolean needWarn(Entity entity, Element element, boolean robust, float amount) {
        return (amount < DISAPPEAR_WARN_AMOUNT) && (amount / getDecayAmount(entity, element, robust) < DISAPPEAR_WARN_CD);
    }

    public static boolean canSeeElements(Player player, Entity entity, double distanceSqr) {
        return distanceSqr < 1000;
    }

    public static MutableComponent name(Element element) {
        return TipUtil.misc("element." + element.name().toLowerCase());
    }

    public static MutableComponent getName(Element element, boolean robust) {
        return name(element).append("(").append(TipUtil.misc("element." + (robust ? "robust" : "weak"))).append(")");
    }

}
