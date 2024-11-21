package hungteen.imm.common.item.elixirs;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-04 11:12
 **/
public class AbstinenceElixir {
//        extends ElixirItem{
//
//    public AbstinenceElixir() {
//        super(Rarity.COMMON, ColorHelper.WHITE.rgb());
//    }
//
//    @Override
//    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
//        if(! level.isClientSide){
//            livingEntity.addEffect(EffectHelper.effect(MobEffects.SATURATION, getDuration(accuracy), 2));
//        }
//    }
//
//    public int getDuration(Accuracies accuracy){
//        switch (accuracy) {
//            case COMMON: return 24000;
//            case NICE: return 30000;
//            case EXCELLENT: return 36000;
//            case PERFECT: return 48000;
//            case MASTER: return 72000;
//            default: return 0;
//        }
//    }
//
//    @Override
//    protected List<Object> getUsagesComponentArgs(Accuracies accuracy) {
//        return List.of(getDuration(accuracy) / 20);
//    }
//
//    @Override
//    protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
//        return lessEqualThan(RealmTypes.SPIRITUAL_LEVEL_3).apply(getRealm(livingEntity));
//    }
}
