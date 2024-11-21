package hungteen.imm.common.entity.human.cultivator;

import com.mojang.serialization.Dynamic;
import hungteen.imm.api.records.Spell;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.spell.spells.basic.IntimidationSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-22 22:16
 **/
public class SpiritualBeginnerCultivator extends Cultivator {

    public SpiritualBeginnerCultivator(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected SimpleContainer createInventory() {
        return new SimpleContainer(16);
    }

    @Override
    protected List<Spell> createLearnSpells() {
        return List.of(Spell.create(SpellTypes.INTIMIDATION));
    }

    @Override
    public boolean canUseSpell(ISpellType spell) {
        if(spell == SpellTypes.INTIMIDATION){
            return IntimidationSpell.canUseOn(this, this.getTarget());
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (flag && source.getEntity() instanceof LivingEntity) {
                SpiritualCultivatorAi.wasHurtBy(this, (LivingEntity)source.getEntity());
            }
            return flag;
        }
    }

    @Override
    public void updateBrain(ServerLevel level) {
        this.getBrain().tick(level, this);
        SpiritualCultivatorAi.updateActivity(this);
    }

    @Override
    public Brain<SpiritualBeginnerCultivator> getBrain() {
        return (Brain<SpiritualBeginnerCultivator>)super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return SpiritualCultivatorAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<SpiritualBeginnerCultivator> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

}
