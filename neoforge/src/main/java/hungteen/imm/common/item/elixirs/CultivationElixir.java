package hungteen.imm.common.item.elixirs;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-03 17:04
 **/
public abstract class CultivationElixir {
//        extends ElixirItem{
//
//    private final int cultivation;
//
//    public CultivationElixir(int cultivation, Rarity rarity, int color) {
//        super(rarity, color);
//        this.cultivation = cultivation;
//    }
//
//    @Override
//    protected void eatElixir(Level level, LivingEntity livingEntity, ItemStack stack, Accuracies accuracy) {
//        if(! level.isClientSide){
//            if(livingEntity instanceof Player){
//                PlayerUtil.addExperience((Player) livingEntity, ExperienceTypes.ELIXIR, getCultivation(accuracy));
//            }
//        }
//    }
//
//    public float getCultivation(Accuracies accuracy){
//        float multiply = 0;
//        switch (accuracy) {
//            case COMMON -> multiply = 1;
//            case NICE -> multiply = 1.25F;
//            case EXCELLENT -> multiply = 1.5F;
//            case PERFECT -> multiply = 1.75F;
//            case MASTER -> multiply = 2F;
//        }
//        return this.cultivation * multiply;
//    }
//
//    @Override
//    protected List<Object> getUsagesComponentArgs(Accuracies accuracy) {
//        return List.of(getCultivation(accuracy));
//    }
//
//    public static class GatherBreathElixir extends CultivationElixir{
//
//        public GatherBreathElixir() {
//            super(4, Rarity.COMMON, ColorHelper.YELLOW_GREEN.rgb());
//        }
//
//        @Override
//        protected Optional<Boolean> checkEating(Level level, LivingEntity livingEntity, ItemStack stack) {
//            return lessEqualThan(RealmTypes.SPIRITUAL_LEVEL_3).apply(IMMAPI.get().getRealm(livingEntity));
//        }
//    }

}
