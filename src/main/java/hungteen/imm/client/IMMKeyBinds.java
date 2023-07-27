package hungteen.imm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 21:42
 **/
public class IMMKeyBinds {

    public static final KeyMapping SPELL_CIRCLE = new KeyMapping("key.imm.spell_circle", InputConstants.KEY_Z, "key.categories.imm");

    /**
     * {@link ClientRegister#setUpClient(FMLClientSetupEvent)}
     */
    public static void register(RegisterKeyMappingsEvent event){
        event.register(SPELL_CIRCLE);
    }

    public static int getKeyValue(KeyMapping key){
        return key.getKey().getValue();
    }

    public static boolean isMouseInput(KeyMapping key) {
        return key.getKey().getType() == InputConstants.Type.MOUSE;
    }

    public static boolean displayingSpellCircle(){
        return IMMKeyBinds.SPELL_CIRCLE.isDown();
    }
}
