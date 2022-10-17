package hungteen.immortal.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 22:44
 **/
public class ImmortalTeleporters {

    public static final ITeleporter INSTANCE = new ITeleporter(){
        @Nullable
        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            return new PortalInfo(Vec3.ZERO, Vec3.ZERO, 0, 0);
        }
    };
}
