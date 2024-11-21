package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.registry.HTSimpleRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.creature.spirit.ElementSpirit;
import hungteen.imm.common.entity.misc.ElementCrystal;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-01 19:49
 **/
public class ElementReactions {

    private static final HTSimpleRegistry<IElementReaction> REACTIONS = HTRegistryManager.simple(Util.prefix("element_reaction"));

    /* 特殊反应 */

    public static final IElementReaction METAL_GILDING = register(new GildingReaction(
            "metal_gilding", 10, Element.METAL, 8)
    );

    public static final IElementReaction WOOD_GILDING = register(new GildingReaction(
            "wood_gilding", 10, Element.WOOD, 8)
    );

    public static final IElementReaction WATER_GILDING = register(new GildingReaction(
            "water_gilding", 10, Element.WATER, 10)
    );

    public static final IElementReaction FIRE_GILDING = register(new GildingReaction(
            "fire_gilding", 10, Element.FIRE, 10)
    );

    public static final IElementReaction BURNING = register(new FlamingReaction(
            "burning", false, 120, false, 0.5F, 0.25F)
    );

    /* 化灵 */

    public static final IElementReaction METAL_SPIRIT = register(new SummonSpiritReaction<>(
            "metal_spirit", IMMEntities.METAL_SPIRIT, Element.METAL, 10
    ));

    public static final IElementReaction WOOD_SPIRIT = register(new SummonSpiritReaction<>(
            "wood_spirit", IMMEntities.WOOD_SPIRIT, Element.WOOD, 10
    ));

    public static final IElementReaction WATER_SPIRIT = register(new SummonSpiritReaction<>(
            "water_spirit", IMMEntities.WATER_SPIRIT, Element.WATER, 10
    ));

    public static final IElementReaction FIRE_SPIRIT = register(new SummonSpiritReaction<>(
            "fire_spirit", IMMEntities.FIRE_SPIRIT, Element.FIRE, 10
    ));

    public static final IElementReaction EARTH_SPIRIT = register(new SummonSpiritReaction<>(
            "earth_spirit", IMMEntities.EARTH_SPIRIT, Element.EARTH, 10
    ));

    /* 相生 */

    public static final IElementReaction CRYSTALLIZATION = register(new GenerationReaction(
            "crystallization", true, Element.EARTH, 10, Element.METAL, 4) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 1200, (int) (scale * 2)));
            }
        }
    });

    public static final IElementReaction QUENCH_BLADE = register(new GenerationReaction(
            "quench_blade", false, Element.METAL, 0.5F, Element.WATER, 0.25F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    public static final IElementReaction GROWING = register(new GenerationReaction(
            "growing", false, Element.WATER, 0.5F, Element.WOOD, 0.2F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.heal(0.25F * scale);
            }
        }
    });

    public static final IElementReaction FLAMING = register(new FlamingReaction(
            "flaming", false, 100, true, 0.5F, 0.25F)
    );

    public static final IElementReaction ASHING = register(new GenerationReaction(
            "ashing", true, Element.FIRE, 10, Element.EARTH, 4) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (!EntityUtil.isManaFull(entity)) {
                EntityUtil.addMana(entity, scale * 20);
            } else {
                if (entity instanceof LivingEntity living) {
                    living.heal(4F * scale);
                }
            }
        }
    });

    /* 相克 */

    public static final IElementReaction SOLIDIFICATION = register(new InhibitionReaction(
            "solidification", true, Element.EARTH, 5, Element.WATER, 8) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(IMMEffects.SOLIDIFICATION.holder(), (int) (100 * scale), Mth.ceil(scale)));
            }
        }
    });

    public static final IElementReaction VAPORIZATION = register(new InhibitionReaction(
            "vaporization", true, Element.WATER, 3, Element.FIRE, 8) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                List<MobEffectInstance> effects = living.getActiveEffects().stream().filter(instance -> instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL).toList();
                if (!effects.isEmpty()) {
                    AreaEffectCloud areaeffectcloud = new AreaEffectCloud(entity.level(), entity.getX(), entity.getY(), entity.getZ());
                    areaeffectcloud.setRadius(2.5F);
                    areaeffectcloud.setRadiusOnUse(-0.5F);
                    areaeffectcloud.setWaitTime(10);
                    areaeffectcloud.setDuration((int) (areaeffectcloud.getDuration() * scale / 2));
                    areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

                    for (MobEffectInstance mobeffectinstance : effects) {
                        areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
                    }

                    entity.level().addFreshEntity(areaeffectcloud);
                }
            }
        }
    });

    public static final IElementReaction MELTING = register(new MeltingReaction(
            "melting", true, 5, Element.FIRE, 4, Element.METAL, 8)
    );

    public static final IElementReaction CUTTING = register(new CuttingReaction(
            "cutting", false, 1F, Element.METAL, 0.15F, Element.WOOD, 0.4F) {
        /**
         * See {@link hungteen.imm.common.event.IMMLivingEvents#onLivingDamage(LivingDamageEvent)}.
         */
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    public static final IElementReaction PARASITISM = register(new InhibitionReaction(
            "parasitism", true, Element.WOOD, 0.2F, Element.EARTH, 0.4F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    public static MutableComponent getCategory() {
        return TipUtil.misc("element_reaction");
    }

    public static HTSimpleRegistry<IElementReaction> registry() {
        return REACTIONS;
    }

    public static IElementReaction register(IElementReaction reaction) {
        return REACTIONS.register(reaction);
    }

    private static abstract class ElementReaction implements IElementReaction {

        private final String name;
        private final boolean once;
        private final int priority;
        protected final List<ElementEntry> elements;

        private ElementReaction(String name, boolean once, int priority, List<ElementEntry> elements) {
            this.name = name;
            this.once = once;
            this.priority = priority;
            this.elements = elements;
        }

        @Override
        public float match(Entity entity) {
            final float ratio = elements.stream().map(entry -> {
                return getMatchAmount(entity, entry);
            }).min(Float::compareTo).orElse(0F);
            return once ? ratio : Math.min(1F, ratio);
        }

        protected float getMatchAmount(Entity entity, ElementEntry entry) {
            //相生反应后一元素的特判。
            if (entry.amount() == 0) {
                return ElementManager.hasElement(entity, entry.element(), entry.mustRobust()) ? 10000000F : 0F;
            }
            return ElementManager.getAmount(entity, entry.element(), entry.mustRobust()) / entry.amount();

        }

        @Override
        public void consume(Entity entity, float scale) {
            elements.forEach(entry -> {
                ElementManager.consumeAmount(entity, entry.element(), entry.mustRobust(), entry.amount() * scale);
            });
        }

        protected boolean canSpawnAmethyst(Entity entity) {
            return allRobust(entity) && (this.once() || (entity.level().getRandom().nextFloat() < 0.75F && entity.tickCount % 10 == 5));
        }

        private boolean allRobust(Entity entity) {
            return elements.stream().allMatch(entry -> {
                return ElementManager.hasElement(entity, entry.element(), entry.mustRobust());
            });
        }

        protected Optional<ElementCrystal> spawnAmethyst(Entity entity) {
            final ElementCrystal amethyst = IMMEntities.ELEMENT_AMETHYST.get().create(entity.level());
            if (amethyst != null) {
                amethyst.setPos(entity.position());
                final double dx = RandomHelper.doubleRange(entity.level().getRandom(), 0.1F);
                final double dy = entity.level().getRandom().nextDouble() * 0.1F;
                final double dz = RandomHelper.doubleRange(entity.level().getRandom(), 0.1F);
                amethyst.setDeltaMovement(dx, dy, dz);
            }
            return Optional.ofNullable(amethyst);
        }

        @Override
        public boolean once() {
            return once;
        }

        @Override
        public int priority() {
            return priority;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.misc("reaction." + name());
        }
    }


    /**
     * 相生反应。
     */
    private static abstract class GenerationReaction extends ElementReaction {

        private final Element ingredient;
        private final Element production;
        private final float ingredientAmount;
        private final float productionAmount;
        private final boolean spawnAmethyst;

        private GenerationReaction(String name, boolean once, Element main, float mainAmount, Element off, float productionAmount) {
            this(name, once, 100, main, mainAmount, off, productionAmount);
        }

        private GenerationReaction(String name, boolean once, int priority, Element main, float mainAmount, Element off, float productionAmount) {
            super(name, once, priority, List.of(new ElementEntry(main, true, mainAmount), new ElementEntry(off, false, 0F)));
            this.ingredient = main;
            this.production = off;
            this.ingredientAmount = mainAmount;
            this.productionAmount = productionAmount;
            this.spawnAmethyst = (main == Element.EARTH || off == Element.EARTH);
        }

        @Override
        public void doReaction(Entity entity, float scale) {
            if (this.spawnAmethyst && canSpawnAmethyst(entity)) {
                this.spawnAmethyst(entity).ifPresent(elementAmethyst -> {
                    final float multiplier = scale * (this.once() ? 1 : 20);
                    ElementManager.addElementAmount(elementAmethyst, this.ingredient, true, this.ingredientAmount * multiplier);
                    ElementManager.addElementAmount(elementAmethyst, this.production, true, this.productionAmount * multiplier);
                    entity.level().addFreshEntity(elementAmethyst);
                });
            }
            ElementManager.addElementAmount(entity, this.production, false, this.productionAmount * scale);
        }

    }

    /**
     * 相克反应。
     */
    private static abstract class InhibitionReaction extends ElementReaction {

        private final Element ingredient;
        private final Element production;
        private final float mainAmount;
        private final float offAmount;
        private final boolean spawnAmethyst;

        private InhibitionReaction(String name, boolean once, Element main, float mainAmount, Element off, float offAmount) {
            super(name, once, 80, List.of(new ElementEntry(main, false, mainAmount), new ElementEntry(off, false, offAmount)));
            this.ingredient = main;
            this.production = off;
            this.mainAmount = mainAmount;
            this.offAmount = offAmount;
            this.spawnAmethyst = (main == Element.EARTH || off == Element.EARTH);
        }

        @Override
        public void doReaction(Entity entity, float scale) {
            if (this.spawnAmethyst && canSpawnAmethyst(entity)) {
                this.spawnAmethyst(entity).ifPresent(elementAmethyst -> {
                    final float multiplier = scale * (this.once() ? 1 : 20);
                    ElementManager.addElementAmount(elementAmethyst, this.ingredient, true, this.mainAmount * multiplier);
                    ElementManager.addElementAmount(elementAmethyst, this.production, true, this.offAmount * multiplier);
                    entity.level().addFreshEntity(elementAmethyst);
                });
            }
        }
    }

    /**
     * (强木 + 火)为基本相生反应，但是(弱木 + 火)也能发生相生反应！
     */
    private static class FlamingReaction extends GenerationReaction {

        private final boolean robustReaction;

        private FlamingReaction(String name, boolean once, int priority, boolean robustReaction, float woodCostAmount, float fireGenAmount) {
            super(name, once, priority, Element.WOOD, woodCostAmount, Element.FIRE, fireGenAmount);
            this.robustReaction = robustReaction;
        }

        @Override
        protected float getMatchAmount(Entity entity, ElementEntry entry) {
            if(entry.element() == Element.WOOD && ! this.robustReaction){
                return ElementManager.getElementAmount(entity, Element.WOOD, false);
            }
            return super.getMatchAmount(entity, entry);
        }

        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if(! this.robustReaction && entity.level().getRandom().nextFloat() < 0.1F){
                entity.setRemainingFireTicks(Math.min(2, (int)(scale * 5)) * 20);
                entity.hurt(IMMDamageSources.elementReaction(entity), scale * 2);
            }
            if (entity.level() instanceof ServerLevel level && level.getRandom().nextFloat() < 0.3F) {
                ParticleHelper.spawnParticles(level, ParticleTypes.FLAME, entity.getX(), entity.getY(0.5F), entity.getZ(), 5, 0.1F);
            }
            EntityHelper.getPredicateEntities(entity, EntityHelper.getEntityAABB(entity, 4, 3), Entity.class, JavaHelper::alwaysTrue).forEach(target -> {
                if (target.level().getRandom().nextFloat() < 0.1F) {
                    ElementManager.addElementAmount(target, Element.FIRE, false, scale * 5);
                }
            });
        }
    }

    /**
     * （火 + 金）相克反应。
     */
    private static class MeltingReaction extends InhibitionReaction {

        private final float waterAmount;

        private MeltingReaction(String name, boolean once, float waterAmount, Element main, float mainAmount, Element off, float offAmount) {
            super(name, once, main, mainAmount, off, offAmount);
            this.waterAmount = waterAmount;
        }

        @Override
        public float match(Entity entity) {
            final float scale = super.match(entity);
            if(!ElementManager.hasElement(entity, Element.WATER, false)){
                return scale;
            } else if(entity.level().getRandom().nextFloat() < 0.05F) {
                final float amount = Math.min(ElementManager.getAmount(entity, Element.WATER, false), this.waterAmount * scale);
                return Math.min(scale, amount / this.waterAmount) / 5;
            }
            return 0F;
        }

        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            float seconds = 4 * scale;
            float range = 1.5F * scale;
            if(ElementManager.hasElement(entity, Element.FIRE, true)){
                seconds *= 1.5F;
            }
            if(ElementManager.hasElement(entity, Element.METAL, true)){
                range += Math.sqrt(scale + 1) / 2;
            }

            final boolean hasWater = ElementManager.hasElement(entity, Element.WATER, false);
            // 水介入反应导致爆炸被抑制。
            if(hasWater){
                seconds *= 0.5F;
                range *= 0.6F;
            }
            entity.setRemainingFireTicks(((int) seconds + 2) * 20);
            entity.level().explode(null, entity.getX(), entity.getY(), entity.getZ(), range, Level.ExplosionInteraction.NONE);
            // 爆炸后再附着元素。
            if(hasWater){
                EntityUtil.forRange(entity, LivingEntity.class, range * 2, range, EntityHelper::isEntityValid, (target, factor) -> {
                    ElementManager.addElementAmount(target, Element.WATER, false, scale * 10F * factor);
                });
            }
        }

        @Override
        public void consume(Entity entity, float scale) {
            super.consume(entity, scale);
            if(ElementManager.hasElement(entity, Element.WATER, false)){
                ElementManager.consumeAmount(entity, Element.WATER, false, this.waterAmount * scale);
            }
        }
    }

    /**
     * (强木 + 火)为基本相生反应，但是(弱木 + 火)也能发生相生反应！
     */
    public static class CuttingReaction extends InhibitionReaction {

        private final float waterAmount;

        private CuttingReaction(String name, boolean once, float waterAmount, Element main, float mainAmount, Element off, float offAmount) {
            super(name, once, main, mainAmount, off, offAmount);
            this.waterAmount = waterAmount;
        }

        public float getWaterAmount() {
            return waterAmount;
        }
    }

    /**
     * (强土 + 其他元素)为镀光反应。
     */
    public static class GildingReaction extends ElementReaction {

        private static final List<GildingReaction> REACTIONS = new ArrayList<>();
        private final Element element;
        private final float amount;

        private GildingReaction(String name, float mainAmount, Element off, float offAmount) {
            super(name, false, 150, List.of(new ElementEntry(Element.EARTH, true, mainAmount), new ElementEntry(off, false, offAmount)));
            this.element = off;
            this.amount = offAmount;
            REACTIONS.add(this);
        }

        public static void ifGlidingActive(Entity attacker, Entity target){
            REACTIONS.forEach(reaction -> {
                ElementManager.ifActiveReaction(attacker, reaction, (action, scale) -> {
                    ElementManager.addElementAmount(target, reaction.element, false, reaction.amount * scale * 0.6F);
                });
            });
        }

        @Override
        public void doReaction(Entity entity, float scale) {

        }
    }

    /**
     * 化灵反应。
     */
    public static class SummonSpiritReaction<T extends ElementSpirit> extends ElementReaction {

        public static final int SPIRIT_COST = 10;
        private final Supplier<EntityType<T>> spiritType;
        private final Element element;
        private final float amount;

        private SummonSpiritReaction(String name, Supplier<EntityType<T>> spiritType, Element element, float amount) {
            super(name, true, 200, List.of(
                    new ElementEntry(Element.SPIRIT, true, SPIRIT_COST),
                    new ElementEntry(element, true, amount)
            ));
            this.spiritType = spiritType;
            this.element = element;
            this.amount = amount;
        }

        @Override
        public void doReaction(Entity entity, float scale) {
            if(entity.level() instanceof ServerLevel serverLevel){
                EntityUtil.spawn(serverLevel, this.spiritType.get(), entity.position()).ifPresent(spirit -> {
                    ElementManager.addElementAmount(spirit, Element.SPIRIT, false, scale * SPIRIT_COST);
                    ElementManager.addElementAmount(spirit, element, false, scale * amount);
                });
            }
        }
    }

    record ElementEntry(Element element, boolean mustRobust, float amount) {

    }

}
