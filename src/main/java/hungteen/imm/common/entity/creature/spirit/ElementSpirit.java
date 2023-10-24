package hungteen.imm.common.entity.creature.spirit;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.IMMMob;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/8 20:59
 **/
public abstract class ElementSpirit extends IMMMob {

    private boolean attachedElement = false;

    public ElementSpirit(EntityType<? extends IMMMob> type, Level level) {
        super(type, level);
    }

    public void updateRealm(){
        final float amount = ElementManager.getElementAmount(this, Elements.SPIRIT, false);
        final int level = Mth.ceil(amount / ElementReactions.SummonSpiritReaction.SPIRIT_COST);
        this.setRealm(getRealmByLevel(level));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(IMMAttributes.ELEMENT_DECAY_FACTOR.get(), 0.1)
                ;
    }

    private static IRealmType getRealmByLevel(int level) {
        switch (level) {
            case 1 -> {
                return RealmTypes.SPIRITUAL_LEVEL_1;
            }
            case 2 -> {
                return RealmTypes.SPIRITUAL_LEVEL_2;
            }
            default -> {
                return RealmTypes.SPIRITUAL_LEVEL_3;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(EntityHelper.isServer(this)){
            if(! this.attachedElement){
                final float amount = (float) RandomHelper.getMinMax(getRandom(), 5F, 30F);
                ElementManager.addElementAmount(this, Elements.SPIRIT, false, amount);
                ElementManager.addElementAmount(this, getElement(), false, amount);
                this.attachedElement = true;
                this.updateRealm();
            }
            if(this.tickCount % 5 == 0){
                // No Support Element.
                if(! ElementManager.hasElement(this, Elements.SPIRIT, false) && ! ElementManager.hasElement(this, getElement(), false)){
                    this.setHealth(0);
                }
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("AttachedElement")){
            this.attachedElement = tag.getBoolean("AttachedElement");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("attachedElement", this.attachedElement);
    }

    public void setAttachedElement(boolean attachedElement) {
        this.attachedElement = attachedElement;
    }

    @Override
    protected Collection<ISpiritualType> createSpiritualRoots(ServerLevelAccessor accessor) {
        return List.of(SpiritualTypes.SPIRIT, getSpiritualRoot());
    }

    public abstract ISpiritualType getSpiritualRoot();

    public abstract Elements getElement();

}
