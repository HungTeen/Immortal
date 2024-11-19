package hungteen.imm.common.world.entity.trial;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:09
 **/
public class BreakThroughTrial {
//        extends AbstractRaid {
//
//    private final float difficulty;
//    private ServerPlayer trialPlayer;
//    private UUID uuid;
//
//    public BreakThroughTrial(ServerLevel serverLevel, ServerPlayer trialPlayer, float difficulty, BreakThroughRaid raidComponent) {
//        super(IMMDummyEntities.BREAK_THROUGH_TRIAL, serverLevel, trialPlayer.position(), raidComponent);
//        this.difficulty = difficulty;
//        this.trialPlayer = trialPlayer;
//        this.uuid = trialPlayer.getUUID();
//    }
//
//    public BreakThroughTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag raidTag) {
//        super(dummyEntityType, level, raidTag);
//        this.difficulty = raidTag.getFloat("Difficulty");
//    }
//
//    /**
//     * 突破时玩家死亡不会真的死亡，但是突破真的失败。
//     * {@link hungteen.imm.common.event.IMMLivingEvents#onLivingDeath(LivingDeathEvent)}
//     */
//    public static boolean checkTrialFail(ServerPlayer player) {
//        final List<BreakThroughTrial> trials = DummyEntityManager.getDummyEntities(player.serverLevel()).stream()
//                .filter(BreakThroughTrial.class::isInstance).map(BreakThroughTrial.class::cast).filter(l -> {
//                    return player.equals(l.getTrialPlayer()) && player.distanceToSqr(l.position) < 3000;
//                }).toList();
//        if (trials.size() > 0) {
//            trials.forEach(BreakThroughTrial::onLoss);
//            player.setHealth(2.0F); // 给点保底的血量。
//            player.addEffect(EffectHelper.viewEffect(MobEffects.REGENERATION, 100, 1));
//            player.addEffect(EffectHelper.viewEffect(MobEffects.BLINDNESS, 100, 1));
//            player.addEffect(EffectHelper.viewEffect(MobEffects.CONFUSION, 60, 0));
//            player.addEffect(EffectHelper.viewEffect(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void load(CompoundTag tag) {
//        super.load(tag);
//        if (tag.contains("TrialPlayer")) {
//            this.uuid = tag.getUUID("TrialPlayer");
//        }
//    }
//
//    @Override
//    public CompoundTag save(CompoundTag tag) {
//        tag.putFloat("Difficulty", this.difficulty);
//        if (this.trialPlayer != null) {
//            tag.putUUID("TrialPlayer", this.trialPlayer.getUUID());
//        }
//        return super.save(tag);
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//    }
//
//    @Override
//    protected void updatePlayers() {
//        super.updatePlayers();
//    }
//
//    @Override
//    protected boolean needStop() {
//        return super.needStop() || (this.getTrialPlayer() == null || ! this.getDefenders().contains(this.getTrialPlayer()));
//    }
//
//    @Override
//    protected void onVictory() {
//        super.onVictory();
//        Optional.ofNullable(this.getTrialPlayer()).ifPresent(player -> {
//            if(this.getRaidComponent() instanceof BreakThroughRaid raid){
//                RealmManager.breakThrough(player, raid.getTargetRealm(), raid.getTargetStage());
//            }
//        });
//    }
//
//    @Override
//    protected void onLoss() {
//        super.onLoss();
//        getRaiders().forEach(Entity::discard);
//    }
//
//    @Override
//    public boolean addRaider(Entity raider) {
//        if(super.addRaider(raider)){
//            if(raider instanceof IKarmaEntity karmaEntity){
//                karmaEntity.onSpawn(this.getDifficulty());
//            } else {
//                karmaSpawn(raider);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    protected void karmaSpawn(Entity raider) {
//        if(raider instanceof Zombie zombie){
//        }
//    }
//
//    @Nullable
//    public ServerPlayer getTrialPlayer() {
//        if (this.trialPlayer == null && this.uuid != null && this.getLevel() instanceof ServerLevel serverLevel) {
//            if (serverLevel.getPlayerByUUID(this.uuid) instanceof ServerPlayer serverPlayer) {
//                this.trialPlayer = serverPlayer;
//            }
//        }
//        return this.trialPlayer;
//    }
//
//    public float getDifficulty() {
//        return difficulty;
//    }
}
