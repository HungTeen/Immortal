package hungteen.imm.compat.jade;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 15:31
 */
public class IMMEntityProvider {
//    implements
// IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
//
//    public static final IMMEntityProvider INSTANCE = new IMMEntityProvider();
//    private static final ResourceLocation UID = Util.prefix("entity");
//
//    @Override
//    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
////        PlayerHelper.getClientPlayer().ifPresent(player -> {
////            final IElementHelper helper = iTooltip.getElementHelper();
////            if(PlayerUtil.isSpellOnCircle(player, SpellTypes.SPIRIT_EYES)) {
////                if (PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 1) && RealmManager.mayHaveRoots(entityAccessor.getEntity())) {
////                    List<ISpiritualType> roots = PlayerUtil.filterSpiritRoots(player, EntityUtil.getRoots(entityAccessor.getEntity()));
////                    iTooltip.add(helper.text(SpiritualTypes.getCategory().append(": ").append(SpiritualTypes.getRoots(roots))));
////                }
////                if (PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 2)) {
////                    final IRealmType playerRealm = RealmManager.getRealm(player);
////                    final IRealmType realm = RealmManager.getRealm(entityAccessor.getEntity());
////                    final RealmStages stage = RealmManager.getRealmStage(entityAccessor.getEntity()).orElse(null);
////                    final MutableComponent component = RealmTypes.getCategory().append(": ");
////                    if (RealmManager.hasRealmGap(playerRealm, realm) && !RealmManager.compare(playerRealm, realm)) {
////                        final int gap = RealmManager.getRealmGap(playerRealm, realm);
////                        if (gap == 1) {
////                            iTooltip.add(helper.text(component.append(realm.getComponent())));
////                        } else {
////                            iTooltip.add(helper.text(TipUtil.UNKNOWN));
////                        }
////                    } else {
////                        iTooltip.add(helper.text(component.append(RealmManager.getRealmInfo(realm, stage))));
////                    }
////                }
////            }
////            // 要开启调试模式。
////            if(Util.isDebugMode()){
////                final List<IElement> debugComponents = new ArrayList<>();
////                final Map<Elements, Float> elements = ElementManager.getElements(entityAccessor.getEntity());
////                final List<Elements> list = PlayerUtil.filterElements(player, Arrays.stream(Elements.values()).toList());
////                if(list.size() > 0){
////                    for (Elements element : list) {
////                        if (!elements.containsKey(element)) continue;
////                        final float amount = elements.get(element);
////                        final boolean robust = (amount > 0);
////                        final int ticks = ElementManager.getLeftTick(entityAccessor.getEntity(), element, robust);
////                        final Component time = ticks < 0 ? TipUtil.UNKNOWN : TipUtil.info("left_second", String.format("%.1f", ticks / 20F));
////                        debugComponents.add(helper.text(ElementManager.getName(element, robust).append(String.format(": %.1f", Math.abs(amount))).append("(").append(time).append(")").withStyle(ChatFormatting.WHITE)));
////                    }
////                }
////                // 必须是创造模式或旁观模式 ！
////                if(PlayerUtil.isCreativeOrSpectator(player)){
////                    final CompoundTag nbt = entityAccessor.getServerData();
////                    if(nbt.contains("Mana") && nbt.contains("MaxMana")){
////                        debugComponents.add(helper.text(TipUtil.tooltip("mana", String.format("%.1f", nbt.getFloat("Mana")), String.format("%.1f", nbt.getFloat("MaxMana")))));
////                    }
////                }
////                if(! debugComponents.isEmpty()){
////                    iTooltip.add(helper.text(TipUtil.info("debug").withStyle(ChatFormatting.BOLD)));
////                    debugComponents.forEach(iTooltip::add);
////                }
////            }
////        });
//    }
//
//    @Override
//    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
//        // 除了玩家外的实体并没有同步mana到客户端。
//        if(entityAccessor.getEntity() instanceof IHasMana manaEntity){
//            compoundTag.putFloat("Mana", manaEntity.getMana());
//            compoundTag.putFloat("MaxMana", manaEntity.getMaxQi());
//        }
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return UID;
//    }

}
