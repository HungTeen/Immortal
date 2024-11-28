package hungteen.imm.common.cultivation.reaction;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.ElementCrystal;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:29
 **/
public abstract class ElementReactionImpl implements ElementReaction {

    private final String name;
    private final boolean once;
    private final int priority;
    protected final List<ElementEntry> elements;

    public ElementReactionImpl(String name, boolean once, int priority, List<ElementEntry> elements) {
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
