package hungteen.imm.common.impl.raid;

import hungteen.htlib.api.interfaces.raid.ISpawnComponent;
import hungteen.htlib.common.impl.spawn.DurationSpawn;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.spawn.OnceSpawn;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMSpawnComponents {

    public static final ResourceKey<ISpawnComponent> ONCE_3_ZOMBIES = create("once_3_zombies");
    public static final ResourceKey<ISpawnComponent> DURATION_10_ZOMBIES = create("duration_10_zombies");
    public static final ResourceKey<ISpawnComponent> ONCE_1_SKELETON = create("once_1_skeleton");
    public static final ResourceKey<ISpawnComponent> DURATION_5_SKELETONS = create("duration_5_skeletons");

    public static void register(BootstapContext<ISpawnComponent> context){
        context.register(ONCE_3_ZOMBIES, new OnceSpawn(
                builder(EntityType.ZOMBIE).build(), 3
        ));
        context.register(DURATION_10_ZOMBIES, new DurationSpawn(
                builder(EntityType.ZOMBIE).build(), 180, 20, 1, 0
        ));
        context.register(ONCE_1_SKELETON, new OnceSpawn(
                builder(EntityType.SKELETON).build(), 1
        ));
        context.register(DURATION_5_SKELETONS, new DurationSpawn(
                builder(EntityType.SKELETON).build(), 120, 30, 1, 0
        ));
    }

    public static HTSpawnComponents.SpawnSettingBuilder builder(EntityType<?> type){
        return HTSpawnComponents.builder().entityType(type).enableDefaultSpawn(false);
    }

    public static ResourceKey<ISpawnComponent> create(String name){
        return HTSpawnComponents.registry().createKey(Util.prefix(name));
    }

}
