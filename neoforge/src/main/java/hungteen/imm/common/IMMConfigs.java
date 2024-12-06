package hungteen.imm.common;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.util.Constants;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-24 16:17
 **/
public class IMMConfigs {

    private static Common COMMON_CONFIG;
    private static Client CLIENT_CONFIG;

    public static void init(ModContainer container){
        {
            final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
            container.registerConfig(ModConfig.Type.COMMON, specPair.getRight());
            COMMON_CONFIG = specPair.getLeft();
        }
        {
            final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
            container.registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
            CLIENT_CONFIG = specPair.getLeft();
        }
    }

    public static class Common {

        public Common(ModConfigSpec.Builder builder) {
            //World Settings.
            builder.comment("Settings about global rules.").push("Rule Settings");
            {

                builder.comment("Settings about spiritual roots.").push("Spiritual Roots Settings");
                {
                    final float[] chances = new float[]{0, 0.2F, 0.45F, 0.2F, 0.1F};
                    for (int i = 0; i < Constants.MAX_ROOT_AMOUNT; i++) {
                        ruleSettings.rootChances[i] = builder
                                .translation("config.immortal.root_chance_" + i)
                                .comment("The chance that players have " + i + " spiritual roots.")
                                .defineInRange("RootChance" + i, chances[i], 0, 1);
                    }

                    final int[] weights = new int[]{100, 100, 100, 100, 100, 40};
                    for (int i = 0; i < Element.values().length; i++) {
                        ruleSettings.rootWeights[i] = builder
                                .translation("config.immortal." + Element.values()[i].name().toLowerCase() + "_root_weight")
                                .comment("The weight that players have " + Element.values()[i].name().toLowerCase() + " spiritual root.")
                                .defineInRange(Element.values()[i].getSerializedName() + "RootChance", weights[i], 0, 1000000);
                    }


                }
                builder.pop();
//                RuleSettings.CanSpawnDefaultMonster = builder
//                        .translation("config.pvz.rule.spawn_monster")
//                        .comment("if turn to false, there will have no monster of other monsters spawn in overworld except pvz zombies.")
//                        .define("CanSpawnDefaultMonster", true);

            }
            builder.pop();

            builder.comment("Settings about elements.").push("Element Settings");
            {
                elementSettings.elementDecaySpeed = builder
                        .translation("config.immortal.element_decay_speed")
                        .comment("The speed that elements decay, can not be zero !")
                        .defineInRange("ElementDecaySpeed", ElementManager.DECAY_SPEED, 0, 1);

                elementSettings.elementDecayValue = builder
                        .translation("config.immortal.element_decay_value")
                        .comment("The value that elements decay each tick, can not be zero !")
                        .defineInRange("ElementDecayValue", ElementManager.DECAY_VALUE, 0, 1);
            }
            builder.pop();

            builder.comment("Settings about blocks.").push("Block Settings");
            {
                blockSettings.furnaceExplodeCD = builder
                        .translation("config.immortal.furnace_explode_cd")
                        .comment("How long will furnace explode when it went wrong.")
                        .defineInRange("FurnaceExplodeCD", 200, 0, 1000000);
            }
            builder.pop();
        }


        public RuleSettings ruleSettings = new RuleSettings();
        public ElementSettings elementSettings = new ElementSettings();
        public BlockSettings blockSettings = new BlockSettings();

        public static class RuleSettings {
            public final ModConfigSpec.DoubleValue[] rootChances = new ModConfigSpec.DoubleValue[Constants.MAX_ROOT_AMOUNT];
            public final ModConfigSpec.IntValue[] rootWeights = new ModConfigSpec.IntValue[Element.values().length];
        }

        public static class ElementSettings {
            public ModConfigSpec.DoubleValue elementDecaySpeed;
            public ModConfigSpec.DoubleValue elementDecayValue;
        }

        public static class BlockSettings {
            public ModConfigSpec.IntValue furnaceExplodeCD;
        }

    }

    public static class Client {

        public Client(ModConfigSpec.Builder builder) {
                defaultSpellCircle = builder
                        .translation("config.immortal.default_spell_circle")
                        .comment("Use the default setting to control spell circle.")
                        .define("DefaultSpellCircle", true);

                displayElementIconAboveHead = builder
                        .translation("config.immortal.display_element_icon_above_head")
                        .comment("Display the element icon above the player's head.")
                        .define("DisplayElementIconAboveHead", true);
        }

        public ModConfigSpec.BooleanValue defaultSpellCircle;
        public ModConfigSpec.BooleanValue displayElementIconAboveHead;

    }

    /* Common */

    public static double getRootCountChance(int count){
        return ruleSettings().rootChances[count].get();
    }

    public static double[] getRootChances(){
        double[] chances = new double[Constants.MAX_ROOT_AMOUNT];
        for (int i = 0; i < Constants.MAX_ROOT_AMOUNT; i++) {
            chances[i] = getRootCountChance(i);
        }
        return chances;
    }

    public static int getRootWeight(Element element){
        return ruleSettings().rootWeights[element.ordinal()].get();
    }

    public static Common.RuleSettings ruleSettings(){
        return common().ruleSettings;
    }

    public static Common.ElementSettings elementSettings(){
        return common().elementSettings;
    }

    public static Common common(){
        return COMMON_CONFIG;
    }

    /* Client */

    public static boolean defaultSpellCircle() {
        return client().defaultSpellCircle.get();
    }

    public static boolean displayElementIconAboveHead() {
        return client().displayElementIconAboveHead.get();
    }

    public static Client client(){
        return CLIENT_CONFIG;
    }

}
