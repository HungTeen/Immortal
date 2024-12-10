package hungteen.imm.common.cultivation;

import hungteen.htlib.util.HTColor;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.capability.entity.IMMEntityData;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.misc.ElementAmethyst;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 管理元素相关方法。
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/1 15:19
 */
public class ElementManager {

    public static final int SYNC_UPDATE_CD = 20;
    public static final float ONCE_COST_BASELINE = 20F;
    public static final float DURATION_COST_BASELINE = 0.25F;
    public static final double DEFAULT_DECAY_SPEED = 0.02;
    public static final double DEFAULT_DECAY_VALUE = 0.05;
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
                // 业元素不显示粒子。
                if (element == Element.SPIRIT) {
                    continue;
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
    public static void attachDamageElement(ServerLevel level, Entity entity, DamageSource source) {
        if (entity.getRandom().nextFloat() < 0.2F) {
            if (source.is(DamageTypes.IN_FIRE)) {
                // 篝火。
                addElementAmount(entity, Element.FIRE, false, 1F, 2F);
            } else if (source.is(DamageTypes.LAVA)) {
                // 岩浆。
                addElementAmount(entity, Element.FIRE, false, 2F, 4F);
            }
        }
    }

    /**
     * 获取每个实体的衰减系数。
     * @return 越大衰减越快。
     */
    public static float getDecayFactor(Entity entity) {
        // 实体有对应衰减属性。
        if (entity instanceof LivingEntity living && living.getAttributes().hasAttribute(IMMAttributes.ELEMENT_DECAY_FACTOR.holder())) {
            return (float) living.getAttributeValue(IMMAttributes.ELEMENT_DECAY_FACTOR.holder());
        }
        return switch (entity){
            case Projectile ignored -> 0.4F;
            case ElementAmethyst ignored -> 0.25F;
            default -> 1;
        };
    }

    public static float getDecayAmount(Entity entity, Element element, boolean robust) {
        return getDecayAmount(getElementAmount(entity, element, robust), getDecayFactor(entity));
    }

    public static float getDecayAmount(float amount, float factor) {
        return factor * (amount * getDecaySpeed() + getDecayValue());
    }

    public static float getDecayAmount(int tick, float amount, float factor) {
        final float an = (float) Math.pow(1 - factor * getDecaySpeed(), tick);
        return an * amount - (1 - an) * getDecayValue() / getDecaySpeed();
    }

    public static int getLeftTick(Entity entity, Element element, boolean robust) {
        return getLeftTick(getElementAmount(entity, element, robust), getDecayFactor(entity));
    }
    
    public static float getDecaySpeed(){
        return IMMConfigs.elementSettings().elementDecaySpeed.get().floatValue();
    }
    
    public static float getDecayValue(){
        return IMMConfigs.elementSettings().elementDecayValue.get().floatValue();
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
     * 在实体之间传递元素。
     * @param source 源实体。
     * @param target 目标实体。
     */
    public static void transferElement(Entity source, Entity target){
        for (Element element : Element.values()) {
            for(int i = 0; i < 2; ++ i){
                final boolean robust = (i == 0);
                final float amount = getElementAmount(source, element, robust);
                if (amount > 0) {
                    addElementAmount(target, element, robust, amount);
                    setElementAmount(source, element, robust, 0);
                }
            }
        }
    }

    /**
     * @return 获取实体的元素。
     */
    public static Stream<Element> getElements(Entity entity){
        Set<Element> elements = new HashSet<>();
        QiManager.getRoots(entity).forEach(root -> {
            elements.addAll(root.getElements());
        });
        return elements.stream();
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

    public static void addPercentElement(Entity entity, Element element, boolean robust, float percent, float maxPercent) {
        addElementAmount(entity, element, robust, ONCE_COST_BASELINE * percent, ONCE_COST_BASELINE * maxPercent);
    }

    public static void addElementAmount(Entity entity, Element element, boolean robust, float value) {
        EntityUtil.setData(entity, cap -> cap.addElementAmount(element, robust, value));
    }

    public static void addPercentElement(Entity entity, Element element, boolean robust, float percent) {
        addElementAmount(entity, element, robust, percent * ONCE_COST_BASELINE);
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

    public static Map<Element, Float> getElementMap(Entity entity) {
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

    /**
     * @param entity 如果是生物 或者 在显示标签（如元素水晶）之中。
     * @return 是否在头顶显示元素图标。
     */
    public static boolean displayElementAboveHead(Entity entity) {
        return IMMConfigs.displayElementIconAboveHead() && (entity instanceof LivingEntity || entity.getType().is(IMMEntityTags.REQUIRE_ELEMENT_DISPLAY_ENTITIES));
    }

    public static MutableComponent name(Element element) {
        return TipUtil.misc("element." + element.name().toLowerCase());
    }

    public static MutableComponent getName(Element element, boolean robust) {
        return name(element).append("(").append(TipUtil.misc("element." + (robust ? "robust" : "weak"))).append(")");
    }

}
