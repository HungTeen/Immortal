package hungteen.imm.compat.jade;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.*;
import hungteen.imm.api.entity.HasQi;
import hungteen.imm.common.cultivation.*;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 15:31
 */
public class IMMEntityProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {

    public static final IMMEntityProvider INSTANCE = new IMMEntityProvider();
    private static final ResourceLocation UID = Util.prefix("entity");

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        PlayerHelper.getClientPlayer().ifPresent(player -> {
            // 灵眼的作用。
            if(PlayerUtil.isSpellOnCircle(player, SpellTypes.SPIRIT_EYES)) {
                if (PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 1) && CultivationManager.mayHaveRoots(entityAccessor.getEntity())) {
                    List<QiRootType> roots = PlayerUtil.filterSpiritRoots(player, EntityUtil.getRoots(entityAccessor.getEntity()));
                    iTooltip.add(QiRootTypes.getCategory().append(": ").append(QiRootTypes.getRoots(roots)));
                }
                if (PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 2)) {
                    final RealmType playerRealm = PlayerUtil.getPlayerRealm(player);
                    final RealmType realm = RealmManager.getRealm(entityAccessor.getEntity());
                    final MutableComponent component = RealmTypes.getCategory().append(": ");
                    if (RealmManager.hasRealmGap(playerRealm, realm) && !RealmManager.compare(playerRealm, realm)) {
                        final int gap = RealmManager.getRealmGap(playerRealm, realm);
                        // 只有一个大境界的差距也是可以显示的。
                        if (gap == 1) {
                            iTooltip.add(component.append(realm.getComponent()));
                        } else {
                            iTooltip.add(TipUtil.UNKNOWN);
                        }
                    } else {
                        iTooltip.add(component.append(realm.getComponent()));
                    }
                }
            }
            // 要开启调试模式。
            if(Util.isDebugMode()){
                final List<Component> debugComponents = new ArrayList<>();
                final Map<Element, Float> elements = ElementManager.getElements(entityAccessor.getEntity());
                final List<Element> list = PlayerUtil.filterElements(player, Arrays.stream(Element.values()).toList());
                if(!list.isEmpty()){
                    for (Element element : list) {
                        if (!elements.containsKey(element)) {
                            continue;
                        }
                        final float amount = elements.get(element);
                        final boolean robust = (amount > 0);
                        final int ticks = ElementManager.getLeftTick(entityAccessor.getEntity(), element, robust);
                        final Component time = ticks < 0 ? TipUtil.UNKNOWN : TipUtil.info("left_second", String.format("%.1f", ticks / 20F));
                        debugComponents.add(ElementManager.getName(element, robust).append(String.format(": %.1f", Math.abs(amount))).append("(").append(time).append(")").withStyle(ChatFormatting.WHITE));
                    }
                }
                // 必须是创造模式或旁观模式 ！
                if(PlayerUtil.isCreativeOrSpectator(player)){
                    final CompoundTag nbt = entityAccessor.getServerData();
                    if(nbt.contains("Mana") && nbt.contains("MaxMana")){
                        debugComponents.add(TipUtil.tooltip("mana", String.format("%.1f", nbt.getFloat("Mana")), String.format("%.1f", nbt.getFloat("MaxMana"))));
                    }
                }
                if(! debugComponents.isEmpty()){
                    iTooltip.add(TipUtil.info("debug").withStyle(ChatFormatting.BOLD));
                    debugComponents.forEach(iTooltip::add);
                }
            }
        });
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
        // 除了玩家外的实体并没有同步mana到客户端。
        if(entityAccessor.getEntity() instanceof HasQi manaEntity){
            compoundTag.putFloat("Mana", manaEntity.getQiAmount());
            compoundTag.putFloat("MaxMana", manaEntity.getMaxQiAmount());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

}
