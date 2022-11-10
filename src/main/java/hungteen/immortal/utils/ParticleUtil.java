package hungteen.immortal.utils;

import hungteen.htlib.util.Triple;
import hungteen.htlib.util.helper.ParticleHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.immortal.client.particle.ImmortalParticles;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-04 10:17
 **/
public class ParticleUtil {

    public static void spawnLineSpiritualParticle(Level world, Triple<Float, Float, Float> color, Vec3 origin, Vec3 target, float particleRatio, int particleCountEach, double offsetScale) {
        double distance = origin.distanceTo(target);
        int particleNum = Mth.ceil(distance * (double)particleRatio);

        for(int i = 0; i < particleNum; ++i) {
            for(int j = 0; j < particleCountEach; ++j) {
                Vec3 pos = origin.add(target.subtract(origin).normalize().scale(Math.max(1.0D, distance - 2.0D) / (double)particleNum * (double)(i + 1) / (double)particleRatio)).add(RandomHelper.vec3Range(world.getRandom(), offsetScale));
                ParticleHelper.spawnParticles(world, ImmortalParticles.SPIRITUAL_RELEASING.get(), pos, color.getLeft(), color.getMid(), color.getRight());
            }
        }

    }
}
