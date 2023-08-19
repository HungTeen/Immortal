package hungteen.imm.compat.jade;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

import java.util.List;

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
            if(PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 1)){
                List<ISpiritualType> roots = PlayerUtil.filterSpiritRoots(player, EntityUtil.getSpiritualRoots(entityAccessor.getEntity()));
                final IElementHelper elements = iTooltip.getElementHelper();
                iTooltip.add(elements.text(SpiritualTypes.getCategory().append(": ").append(SpiritualTypes.getSpiritualRoots(roots))));
            }
            if(PlayerUtil.hasLearnedSpell(player, SpellTypes.SPIRIT_EYES, 2)){
                final IRealmType playerRealm = RealmManager.getEntityRealm(player);
                final IRealmType realm = RealmManager.getEntityRealm(entityAccessor.getEntity());
                final RealmStages stage = RealmManager.getRealmStage(entityAccessor.getEntity());
                final IElementHelper elements = iTooltip.getElementHelper();
                final MutableComponent component = RealmTypes.getCategory().append(": ");
                if(RealmManager.hasRealmGap(playerRealm, realm) && !RealmManager.compare(playerRealm, realm)){
                    final int gap = RealmManager.getRealmGap(playerRealm, realm);
                    if(gap == 1){
                        iTooltip.add(elements.text(component.append(realm.getComponent())));
                    } else {
                        iTooltip.add(elements.text(TipUtil.UNKNOWN));
                    }
                } else {
                    iTooltip.add(elements.text(component.append(RealmManager.getRealmInfo(realm, stage))));
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
