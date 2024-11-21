package hungteen.imm.common.capability.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2024/11/21 17:07
 **/
public class PlayerDataProvider implements IAttachmentCopyHandler<IMMPlayerData> {

    public static final PlayerDataProvider INSTANCE = new PlayerDataProvider();

    public static IMMPlayerData create(IAttachmentHolder holder){
        if(holder instanceof Player player){
            return new IMMPlayerData(player);
        }
        return new IMMPlayerData();
    }

    @Override
    public @Nullable IMMPlayerData copy(IMMPlayerData playerDataManager, IAttachmentHolder holder, HolderLookup.Provider provider) {
        IMMPlayerData data = create(holder);
        data.deserializeNBT(provider, playerDataManager.serializeNBT(provider));
        data.syncToClient();
        return data;
    }

}
