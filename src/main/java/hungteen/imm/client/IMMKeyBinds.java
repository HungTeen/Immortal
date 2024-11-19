package hungteen.imm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 21:42
 **/
public class IMMKeyBinds {

    public static final KeyMapping SPELL_CIRCLE = new KeyMapping("key.imm.spell_circle", InputConstants.KEY_Z, "key.categories.imm");
    public static final KeyMapping ACTIVATE_SPELL = new KeyMapping("key.imm.activate_spell", InputConstants.KEY_X, "key.categories.imm");

    /**
     * {@link ClientRegister#setUpClient(FMLClientSetupEvent)}
     */
    public static void register(RegisterKeyMappingsEvent event){
        event.register(SPELL_CIRCLE);
        event.register(ACTIVATE_SPELL);
    }

    public static int getKeyValue(KeyMapping key){
        return key.getKey().getValue();
    }

    public static boolean mouseDown(KeyMapping key){
        return isMouseInput(key) && key.consumeClick();
    }

    public static boolean keyDown(KeyMapping key){
        return isKeyInput(key) && key.isDown();
    }

    public static boolean isMouseInput(KeyMapping key) {
        return key.getKey().getType() == InputConstants.Type.MOUSE;
    }

    public static boolean isKeyInput(KeyMapping key) {
        return key.getKey().getType() == InputConstants.Type.KEYSYM;
    }

    public static boolean displayingSpellCircle(){
        return IMMKeyBinds.SPELL_CIRCLE.isDown();
    }
}
