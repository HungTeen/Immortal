package hungteen.imm.client.level;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/22 16:42
 **/
public class SpiritWorldEffects extends DimensionSpecialEffects {

    public SpiritWorldEffects() {
        super(Float.NaN, false, DimensionSpecialEffects.SkyType.END, true, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor.scale(0.15F);
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }
}
