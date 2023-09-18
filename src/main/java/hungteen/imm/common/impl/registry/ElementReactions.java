package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.ElementAmethyst;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-01 19:49
 **/
public class ElementReactions {

    private static final HTSimpleRegistry<IElementReaction> REACTIONS = HTRegistryManager.createSimple(Util.prefix("element_reaction"));

    /* 相生 */

    public static final IElementReaction CRYSTALLIZATION = register(new GenerationReaction(
            "crystallization", true, Elements.EARTH, 10, Elements.METAL, 4) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 1200, (int) (scale * 2)));
            }
        }
    });

    public static final IElementReaction QUENCH_BLADE = register(new GenerationReaction(
            "quench_blade", false, Elements.METAL, 0.5F, Elements.WATER, 0.25F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    public static final IElementReaction GROWING = register(new GenerationReaction(
            "growing", false, Elements.WATER, 0.5F, Elements.WOOD, 0.2F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if(entity instanceof LivingEntity living){
                living.heal(0.25F * scale);
            }
        }
    });

    public static final IElementReaction FLAMING = register(new GenerationReaction(
            "flaming", false, Elements.WOOD, 0.5F, Elements.FIRE, 0.25F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if(entity.level() instanceof ServerLevel level && level.getRandom().nextFloat() < 0.3F){
                ParticleHelper.spawnParticles(level, ParticleTypes.FLAME, entity.getX(), entity.getY(0.5F), entity.getZ(), 5, 0.1F);
            }
            EntityHelper.getPredicateEntities(entity, EntityHelper.getEntityAABB(entity, 4, 3), Entity.class, JavaHelper::alwaysTrue).forEach(target -> {
                if(target.level().getRandom().nextFloat() < 0.1F){
                    ElementManager.addElementAmount(target, Elements.FIRE, false, scale * 5);
                }
            });
        }
    });

    public static final IElementReaction ASHING = register(new GenerationReaction(
            "ashing", true, Elements.FIRE, 10, Elements.EARTH, 4) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if(! EntityUtil.isManaFull(entity)){
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
            "solidification", true, Elements.EARTH, 5, Elements.WATER, 8) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(IMMEffects.SOLIDIFICATION.get(), (int) (100 * scale), Mth.ceil(scale)));
            }
        }
    });

    public static final IElementReaction VAPORIZATION = register(new InhibitionReaction(
            "vaporization", true, Elements.WATER, 3, Elements.FIRE, 8) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                List<MobEffectInstance> effects = living.getActiveEffects().stream().filter(instance -> instance.getEffect().getCategory() == MobEffectCategory.HARMFUL).toList();
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

    public static final IElementReaction MELTING = register(new InhibitionReaction(
            "melting", true, Elements.FIRE, 4, Elements.METAL, 8) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            entity.setSecondsOnFire((int) (4 * scale));
            entity.level().explode(null, entity.getX(), entity.getY(), entity.getZ(), 1.5F * scale, Level.ExplosionInteraction.NONE);
        }
    });

    public static final IElementReaction CUTTING = register(new InhibitionReaction(
            "cutting", false, Elements.METAL, 0.15F, Elements.WOOD, 0.4F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    public static final IElementReaction PARASITISM = register(new InhibitionReaction(
            "parasitism", true, Elements.WOOD, 0.2F, Elements.EARTH, 0.4F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    public static MutableComponent getCategory(){
        return TipUtil.misc("element_reaction");
    }

    public static IHTSimpleRegistry<IElementReaction> registry() {
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
                //相生反应后一元素的特判。
                if(entry.amount() == 0){
                    return ElementManager.hasElement(entity, entry.element(), entry.mustRobust()) ? 10000000F : 0F;
                }
                return ElementManager.getAmount(entity, entry.element(), entry.mustRobust()) / entry.amount();
            }).min(Float::compareTo).orElse(0F);
            return once() ? ratio : Math.min(1F, ratio);
        }

        @Override
        public void consume(Entity entity, float scale) {
            elements.forEach(entry -> {
                ElementManager.consumeAmount(entity, entry.element(), entry.mustRobust(), entry.amount() * scale);
            });
        }

        protected boolean canSpawnAmethyst(Entity entity){
            return allRobust(entity) && (this.once() || (entity.level().getRandom().nextFloat() < 0.75F && entity.tickCount % 10 == 5));
        }

        private boolean allRobust(Entity entity) {
            return elements.stream().allMatch(entry -> {
                return ElementManager.hasElement(entity, entry.element(), entry.mustRobust());
            });
        }

        protected Optional<ElementAmethyst> spawnAmethyst(Entity entity){
            final ElementAmethyst amethyst = IMMEntities.ELEMENT_AMETHYST.get().create(entity.level());
            if(amethyst != null){
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
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.misc("reaction." + getName());
        }
    }


    /**
     * 相生反应。
     */
    private static abstract class GenerationReaction extends ElementReaction {

        private final Elements ingredient;
        private final Elements production;
        private final float ingredientAmount;
        private final float productionAmount;
        private final boolean spawnAmethyst;

        private GenerationReaction(String name, boolean once, Elements main, float mainAmount, Elements off, float productionAmount) {
            super(name, once, 100, List.of(new ElementEntry(main, true, mainAmount), new ElementEntry(off, false, 0F)));
            this.ingredient = main;
            this.production = off;
            this.ingredientAmount = mainAmount;
            this.productionAmount = productionAmount;
            this.spawnAmethyst = (main == Elements.EARTH || off == Elements.EARTH);
        }

        @Override
        public void doReaction(Entity entity, float scale) {
            if(this.spawnAmethyst && canSpawnAmethyst(entity)){
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

        private final Elements ingredient;
        private final Elements production;
        private final float mainAmount;
        private final float offAmount;
        private final boolean spawnAmethyst;

        private InhibitionReaction(String name, boolean once, Elements main, float mainAmount, Elements off, float offAmount) {
            super(name, once, 80, List.of(new ElementEntry(main, false, mainAmount), new ElementEntry(off, false, offAmount)));
            this.ingredient = main;
            this.production = off;
            this.mainAmount = mainAmount;
            this.offAmount = offAmount;
            this.spawnAmethyst = (main == Elements.EARTH || off == Elements.EARTH);
        }

        @Override
        public void doReaction(Entity entity, float scale) {
            if(this.spawnAmethyst && canSpawnAmethyst(entity)){
                this.spawnAmethyst(entity).ifPresent(elementAmethyst -> {
                    final float multiplier = scale * (this.once() ? 1 : 20);
                    ElementManager.addElementAmount(elementAmethyst, this.ingredient, true, this.mainAmount * multiplier);
                    ElementManager.addElementAmount(elementAmethyst, this.production, true, this.offAmount * multiplier);
                    entity.level().addFreshEntity(elementAmethyst);
                });
            }
        }
    }

    record ElementEntry(Elements element, boolean mustRobust, float amount){

    }

}
