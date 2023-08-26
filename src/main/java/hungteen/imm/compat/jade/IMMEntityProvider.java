package hungteen.imm.compat.jade;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/18 15:31
 */
public class IMMEntityProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {

    public static final IMMEntityProvider INSTANCE = new IMMEntityProvider();
    private static final ResourceLocation UID = Util.prefix("entity");

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        PlayerHelper.getClientPlayer().ifPresent(player -> {
            final IElementHelper helper = iTooltip.getElementHelper();
            if(PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 1)){
                List<ISpiritualType> roots = PlayerUtil.filterSpiritRoots(player, EntityUtil.getSpiritualRoots(entityAccessor.getEntity()));
                iTooltip.add(helper.text(SpiritualTypes.getCategory().append(": ").append(SpiritualTypes.getSpiritualRoots(roots))));
            }
            if(PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 2)){
                final IRealmType playerRealm = RealmManager.getEntityRealm(player);
                final IRealmType realm = RealmManager.getEntityRealm(entityAccessor.getEntity());
                final RealmStages stage = RealmManager.getRealmStage(entityAccessor.getEntity());
                final MutableComponent component = RealmTypes.getCategory().append(": ");
                if(RealmManager.hasRealmGap(playerRealm, realm) && !RealmManager.compare(playerRealm, realm)){
                    final int gap = RealmManager.getRealmGap(playerRealm, realm);
                    if(gap == 1){
                        iTooltip.add(helper.text(component.append(realm.getComponent())));
                    } else {
                        iTooltip.add(helper.text(TipUtil.UNKNOWN));
                    }
                } else {
                    iTooltip.add(helper.text(component.append(RealmManager.getRealmInfo(realm, stage))));
                }
            }
            if(Util.isDebugMode()){
                final List<IElement> debugComponents = new ArrayList<>();
                final Map<Elements, Float> elements = ElementManager.getElements(entityAccessor.getEntity());
                final List<Elements> list = PlayerUtil.filterElements(ClientUtil.player(), Arrays.stream(Elements.values()).toList());
                if(list.size() > 0){
                    for (Elements element : list) {
                        if (!elements.containsKey(element)) continue;
                        final float amount = elements.get(element);
                        final boolean robust = (elements.get(element) > 0);
                        debugComponents.add(helper.text(ElementManager.getName(element, robust).append(String.format(": %.1f", Math.abs(amount))).withStyle(ChatFormatting.WHITE)));
                    }
                }
                if(! debugComponents.isEmpty()){
                    iTooltip.add(helper.text(TipUtil.info("debug").withStyle(ChatFormatting.BOLD)));
                    debugComponents.forEach(iTooltip::add);
                }
            }
        });
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {

    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

}
