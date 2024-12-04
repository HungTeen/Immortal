package hungteen.imm.common.capability;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.capability.entity.IMMEntityData;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.capability.player.PlayerDataProvider;
import hungteen.imm.util.Util;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2024/11/21 15:41
 **/
public interface IMMAttachments {

    HTVanillaRegistry<AttachmentType<?>> ATTACHMENTS = HTRegistryManager.vanilla(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Util.id());

    HTHolder<AttachmentType<IMMPlayerData>> PLAYER_DATA = register("player_data", () -> {
        return AttachmentType.serializable(PlayerDataProvider::create)
                .copyOnDeath()
                .copyHandler(new PlayerDataProvider())
                .build();
    });

    HTHolder<AttachmentType<IMMEntityData>> ENTITY_DATA = register("entity_data", () -> {
        return AttachmentType.serializable(IMMEntityData::create)
                .build();
    });

    private static <T> HTHolder<AttachmentType<T>> register(String name, Supplier<AttachmentType<T>> supplier){
        return registry().register(name, supplier);
    }

    static void initialize(IEventBus eventBus){
        NeoHelper.initRegistry(registry(), eventBus);
    }

    static HTVanillaRegistry<AttachmentType<?>> registry(){
        return ATTACHMENTS;
    }
}
