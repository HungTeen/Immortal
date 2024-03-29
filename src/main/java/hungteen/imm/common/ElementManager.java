package hungteen.imm.common;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.htlib.util.records.HTColor;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.capability.entity.IMMEntityCapability;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.misc.ElementCrystal;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.ReactionPacket;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMEntityTags;
import hungteen.imm.util.Colors;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.MathUtil;
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
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/1 15:19
 */
public class ElementManager {

    public static final int ESCAPE_UPDATE_CD = 20;
    public static final int DISAPPEAR_WARN_CD = 50;
    public static final int DISAPPEAR_WARN_AMOUNT = 5;
    public static final int DISAPPEAR_CD = 5;
    public static final int DISPLAY_ROBUST_CD = 10;
    private static final float DECAY_SPEED = 0.03F;
    private static final float DECAY_VALUE = 0.1F;
    private static final Map<Elements, Elements> TARGET_ELEMENTS = new EnumMap<>(Elements.class);
    private static final Map<Elements, TagKey<Block>> ELEMENT_BLOCK_TAGS = new EnumMap<>(Elements.class);
    private static final Map<Elements, Supplier<SimpleParticleType>> ELEMENT_PARTICLE_MAP = new EnumMap<>(Elements.class);
    private static final Map<Elements, HTColor> ELEMENT_COLOR_MAP = new EnumMap<>(Elements.class);

    static {
        TARGET_ELEMENTS.putAll(Map.of(
                Elements.METAL, Elements.WOOD,
                Elements.WOOD, Elements.EARTH,
                Elements.WATER, Elements.FIRE,
                Elements.FIRE, Elements.METAL,
                Elements.EARTH, Elements.WATER,
                Elements.SPIRIT, Elements.SPIRIT
        ));
        ELEMENT_BLOCK_TAGS.putAll(Map.of(
                Elements.METAL, IMMBlockTags.METAL_ELEMENT_ATTACHED_BLOCKS,
                Elements.WOOD, IMMBlockTags.WOOD_ELEMENT_ATTACHED_BLOCKS,
                Elements.WATER, IMMBlockTags.WATER_ELEMENT_ATTACHED_BLOCKS,
                Elements.FIRE, IMMBlockTags.FIRE_ELEMENT_ATTACHED_BLOCKS,
                Elements.EARTH, IMMBlockTags.EARTH_ELEMENT_ATTACHED_BLOCKS,
                Elements.SPIRIT, IMMBlockTags.SPIRIT_ELEMENT_ATTACHED_BLOCKS
        ));
        ELEMENT_PARTICLE_MAP.putAll(Map.of(
                Elements.METAL, IMMParticles.METAL_ELEMENT,
                Elements.WOOD, IMMParticles.WOOD_ELEMENT,
                Elements.WATER, IMMParticles.WATER_ELEMENT,
                Elements.FIRE, IMMParticles.FIRE_ELEMENT,
                Elements.EARTH, IMMParticles.EARTH_ELEMENT,
                Elements.SPIRIT, IMMParticles.SPIRIT_ELEMENT
        ));
        ELEMENT_COLOR_MAP.putAll(Map.of(
                Elements.METAL, ColorHelper.LITTLE_YELLOW1,
                Elements.WOOD, ColorHelper.GREEN,
                Elements.WATER, ColorHelper.DARK_BLUE,
                Elements.FIRE, ColorHelper.RED,
                Elements.EARTH, ColorHelper.GRAY,
                Elements.SPIRIT, ColorHelper.PURPLE
        ));
    }

    /**
     * 在世界更新末尾更新，仅服务端。
     */
    public static void tickElements(Entity entity) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
            if (canReactionOn(entity)) {
                // Update Misc.
                cap.update();
                // Element Reactions.
                final Iterator<IElementReaction> iterator = cap.getPossibleReactions().iterator();
                while (iterator.hasNext()) {
                    final IElementReaction reaction = iterator.next();
                    final float scale = reaction.match(entity);
                    if (scale > 0) {
                        reaction.doReaction(entity, (float) (MathUtil.log2(scale + 1)));
                        reaction.consume(entity, scale);
                        if (!reaction.once()) {
                            cap.setActiveScale(reaction, scale);
                        }
                        NetworkHandler.sendToNearByClient(entity.level(), entity.position(), 60, new ReactionPacket(entity.getId(), reaction));
                    } else {
                        iterator.remove();
                    }
                }
            }
            // Decay Elements.
            for (int i = 0; i < 2; ++i) {
                final boolean robust = (i == 0);
                for (Elements element : Elements.values()) {
                    if (cap.hasElement(element, robust)) {
                        final float decayAmount = getDecayAmount(entity, element, robust);
                        cap.addElementAmount(element, robust, -decayAmount);
                    }
                }
            }
        });
    }

    public static Elements getTargetElement(Elements element) {
        return TARGET_ELEMENTS.getOrDefault(element, Elements.SPIRIT);
    }

    public static void clientTickElements(Level level, Entity entity) {
        for (Elements element : Elements.values()) {
            if(element == Elements.SPIRIT) continue; // 业元素不显示粒子。
            final float elementAmount = getAmount(entity, element, false);
            if (level.getRandom().nextFloat() < 0.2F) {
                final int particleCount = Math.min(Mth.ceil(elementAmount / 15), 5);
                if (particleCount > 0) {
                    ParticleHelper.spawnParticles(level, getParticle(element), entity.position().add(0, entity.getBbHeight() / 2, 0), particleCount, entity.getBbWidth(), entity.getBbHeight() / 2, 0);
                }
            }
        }
    }

    /**
     * Tick附着元素。
     */
    public static void attachElement(LivingEntity entity) {
        if (entity.tickCount % 5 == 0 && entity.getRandom().nextFloat() < 0.1F) {
            if(entity.isInWaterOrRain()){
                addElementAmount(entity, Elements.WATER, false, 1F);
                return;
            }
            final BlockState state = entity.level().getBlockState(entity.getOnPos());
            for (Map.Entry<Elements, TagKey<Block>> entry : ELEMENT_BLOCK_TAGS.entrySet()) {
                if(state.is(entry.getValue())){
                    addElementAmount(entity, entry.getKey(), false, 1F);
                    break;
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
                addElementAmount(entity, Elements.FIRE, false, 1F);
            } else if (source.is(DamageTypes.LAVA)) { // 岩浆。
                addElementAmount(entity, Elements.FIRE, false, 2F);
            }
        }
    }

    public static float getDecayFactor(Entity entity) {
        if (entity instanceof LivingEntity living && living.getAttributes().hasAttribute(IMMAttributes.ELEMENT_DECAY_FACTOR.get())) {
            return (float) living.getAttributeValue(IMMAttributes.ELEMENT_DECAY_FACTOR.get());
        }
        if (entity instanceof ElementCrystal) {
            return 0.25F;
        }
        return 1F;
    }

    public static float getDecayAmount(Entity entity, Elements element, boolean robust) {
        return getDecayAmount(getElementAmount(entity, element, robust), getDecayFactor(entity));
    }

    public static float getDecayAmount(float amount, float factor) {
        return factor * (amount * DECAY_SPEED + DECAY_VALUE);
    }

    public static float getDecayAmount(int tick, float amount, float factor) {
        final float an = (float) Math.pow(1 - factor * DECAY_SPEED, tick);
        return an * amount - (1 - an) * DECAY_VALUE / DECAY_SPEED;
    }

    public static int getLeftTick(Entity entity, Elements element, boolean robust) {
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
            } else l = mid + 1;
        }
        return res;
    }

    /**
     * Server side only.
     */
    public static void ifActiveReaction(Entity entity, IElementReaction reaction, BiConsumer<IElementReaction, Float> consumer, Runnable runnable) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
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
    public static void ifActiveReaction(Entity entity, IElementReaction reaction, BiConsumer<IElementReaction, Float> consumer) {
        ifActiveReaction(entity, reaction, consumer, () -> {
        });
    }

    /**
     * Server side only.
     */
    public static boolean isActiveReaction(Entity entity, IElementReaction reaction) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.isActiveReaction(reaction), false);
    }

    /**
     * Server side only.
     */
    public static float getActiveScale(Entity entity, IElementReaction reaction) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.getActiveScale(reaction), 0F);
    }

    public static void setElementAmount(Entity entity, Elements element, boolean robust, float value) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.setElementAmount(element, robust, value));
    }

    public static void addElementAmount(Entity entity, Elements element, boolean robust, float value, float maxValue) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.addElementAmount(element, robust, value, maxValue));
    }

    public static void addElementAmount(Entity entity, Elements element, boolean robust, float value) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> cap.addElementAmount(element, robust, value));
    }

    public static float getElementAmount(Entity entity, Elements element, boolean robust) {
        return EntityUtil.getCapabilityResult(entity, cap -> cap.getElementAmount(element, robust), 0F);
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static float getAmount(Entity entity, Elements element, boolean mustRobust) {
        return EntityUtil.getCapabilityResult(entity, cap -> {
            return cap.getElementAmount(element, true) + (mustRobust ? 0 : cap.getElementAmount(element, false));
        }, 0F);
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static boolean hasElement(Entity entity, Elements element, boolean mustRobust) {
        return EntityUtil.getCapabilityResult(entity, cap -> {
            return cap.hasElement(element, true) || (!mustRobust && cap.hasElement(element, false));
        }, false);
    }

    public static boolean hasElement(Entity entity, boolean mustRobust) {
        for (Elements element : Elements.values()) {
            if (ElementManager.hasElement(entity, element, mustRobust)) {
                return true;
            }
        }
        return false;
    }

    public static HTColor getElementColor(Entity entity, boolean mustRobust) {
        final Colors.ColorMixer mixer = Colors.mixer();
        for (Elements element : Elements.values()) {
            if (hasElement(entity, element, mustRobust)) {
                mixer.add(getElementColor(element));
            }
        }
        return mixer.mix();
    }

    /**
     * @param mustRobust true的时候仅考虑强元素，否则为强弱元素之和。
     */
    public static void consumeAmount(Entity entity, Elements element, boolean mustRobust, float amount) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
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
        EntityUtil.getOptCapability(entity).ifPresent(IMMEntityCapability::clearElements);
    }

    public static Map<Elements, Float> getElements(Entity entity) {
        return EntityUtil.getCapabilityResult(entity, IMMEntityCapability::getElementMap, Map.of());
    }

    public static void setQuenchBladeDamage(Entity entity, float damage, boolean force) {
        EntityUtil.getOptCapability(entity).ifPresent(cap -> {
            if (!force) {
                cap.setQuenchBladeDamage(Math.max(cap.getQuenchBladeDamage(), damage));
            } else {
                cap.setQuenchBladeDamage(damage);
            }
        });
    }

    public static float getQuenchBladeDamage(Entity entity) {
        return EntityUtil.getCapabilityResult(entity, IMMEntityCapability::getQuenchBladeDamage, 0F);
    }

    public static ParticleOptions getParticle(Elements element) {
        return ELEMENT_PARTICLE_MAP.getOrDefault(element, IMMParticles.SPIRITUAL_MANA).get();
    }

    public static HTColor getElementColor(Elements element) {
        return ELEMENT_COLOR_MAP.getOrDefault(element, ColorHelper.WHITE);
    }

    public static void clearActiveReactions(Entity entity) {
        EntityUtil.getOptCapability(entity).ifPresent(IMMEntityCapability::clearElements);
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
    public static boolean needWarn(Entity entity, Elements element, boolean robust, float amount) {
        return (amount < DISAPPEAR_WARN_AMOUNT) && (amount / getDecayAmount(entity, element, robust) < DISAPPEAR_WARN_CD);
    }

    public static boolean canSeeElements(Player player, Entity entity, double distanceSqr) {
        return distanceSqr < 1000;
    }

    public static MutableComponent name(Elements element) {
        return TipUtil.misc("element." + element.name().toLowerCase());
    }

    public static MutableComponent getName(Elements element, boolean robust) {
        return name(element).append("(").append(TipUtil.misc("element." + (robust ? "robust" : "weak"))).append(")");
    }

}
