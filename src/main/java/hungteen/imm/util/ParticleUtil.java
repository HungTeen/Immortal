package hungteen.imm.util;

import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.client.particle.ImmortalParticles;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 10:17
 **/
public class ParticleUtil {

    public static void spawnLineSpiritualParticle(Level world, float red, float green, float blue, Vec3 origin, Vec3 target, float particleRatio, int particleCountEach, double offsetScale) {
        double distance = origin.distanceTo(target);
        int particleNum = Mth.ceil(distance * (double)particleRatio);

        for(int i = 0; i < particleNum; ++i) {
            for(int j = 0; j < particleCountEach; ++j) {
                Vec3 pos = origin.add(target.subtract(origin).normalize().scale(Math.max(1.0D, distance - 2.0D) / (double)particleNum * (double)(i + 1) / (double)particleRatio)).add(RandomHelper.vec3Range(world.getRandom(), offsetScale));
                ParticleHelper.spawnParticles(world, ImmortalParticles.SPIRITUAL_MANA.get(), pos, red, green, blue);
            }
        }

    }
}
