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
            builder.comment("Settings about qi.").push("Qi Settings");
            {

                builder.comment("Settings about qi roots.").push("Qi Roots Settings");
                {
                    final float[] chances = new float[]{0, 0.2F, 0.45F, 0.2F, 0.1F};
                    for (int i = 0; i < Constants.MAX_ROOT_AMOUNT; i++) {
                        qiSettings.rootChances[i] = builder
                                .translation("config.immortal.root_chance_" + i)
                                .comment("The chance that players have " + i + " qi roots.")
                                .defineInRange("RootChance" + i, chances[i], 0, 1);
                    }

                    final int[] weights = new int[]{100, 100, 100, 100, 100, 40};
                    for (int i = 0; i < Element.values().length; i++) {
                        qiSettings.rootWeights[i] = builder
                                .translation("config.immortal." + Element.values()[i].name().toLowerCase() + "_root_weight")
                                .comment("The weight that players have " + Element.values()[i].name().toLowerCase() + " spiritual root.")
                                .defineInRange(Element.values()[i].getSerializedName() + "RootChance", weights[i], 0, 1000000);
                    }

                }
                builder.pop();
            }
            builder.pop();

            builder.comment("Settings about elements.").push("Element Settings");
            {
                elementSettings.elementDecaySpeed = builder
                        .translation("config.immortal.element_decay_speed")
                        .comment("The speed that elements decay, can not be zero !")
                        .defineInRange("ElementDecaySpeed", ElementManager.DEFAULT_DECAY_SPEED, 0, 1);

                elementSettings.elementDecayValue = builder
                        .translation("config.immortal.element_decay_value")
                        .comment("The value that elements decay each tick, can not be zero !")
                        .defineInRange("ElementDecayValue", ElementManager.DEFAULT_DECAY_VALUE, 0, 1);
            }
            builder.pop();

            builder.comment("Settings about realms.").push("Realm Settings");
            {
                realmSettings.realmReceiveDamageReduction = builder
                        .translation("config.immortal.realm_receive_damage_reduction")
                        .comment("The damage reduction that player receive when they are in a higher realm.")
                        .defineInRange("RealmReceiveDamageReduction", 0.5, 0, 1);

                realmSettings.realmDealDamageIncrease = builder
                        .translation("config.immortal.realm_deal_damage_increase")
                        .comment("The damage increase that player deal when they are in a higher realm.")
                        .defineInRange("RealmDealDamageIncrease", 0.1, 0, 1);

                realmSettings.breakThroughFailReduction = builder
                        .translation("config.immortal.break_through_fail_reduction")
                        .comment("The reduction factor that player get when they failed at the break through.")
                        .defineInRange("BreakThroughFailReduction", 0.75, 0, 1);
            }

            builder.comment("Settings about blocks.").push("Block Settings");
            {
                blockSettings.furnaceExplodeCD = builder
                        .translation("config.immortal.furnace_explode_cd")
                        .comment("How long will furnace explode when it went wrong.")
                        .defineInRange("FurnaceExplodeCD", 200, 0, 1000000);
            }
            builder.pop();
        }


        public QiSettings qiSettings = new QiSettings();
        public ElementSettings elementSettings = new ElementSettings();
        public RealmSettings realmSettings = new RealmSettings();
        public BlockSettings blockSettings = new BlockSettings();

        public static class QiSettings {
            public final ModConfigSpec.DoubleValue[] rootChances = new ModConfigSpec.DoubleValue[Constants.MAX_ROOT_AMOUNT];
            public final ModConfigSpec.IntValue[] rootWeights = new ModConfigSpec.IntValue[Element.values().length];
        }

        public static class ElementSettings {
            public ModConfigSpec.DoubleValue elementDecaySpeed;
            public ModConfigSpec.DoubleValue elementDecayValue;
        }

        public static class RealmSettings {
            public ModConfigSpec.DoubleValue realmReceiveDamageReduction;
            public ModConfigSpec.DoubleValue realmDealDamageIncrease;
            public ModConfigSpec.DoubleValue breakThroughFailReduction;
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
        return qiSettings().rootChances[count].get();
    }

    public static double[] getRootChances(){
        double[] chances = new double[Constants.MAX_ROOT_AMOUNT];
        for (int i = 0; i < Constants.MAX_ROOT_AMOUNT; i++) {
            chances[i] = getRootCountChance(i);
        }
        return chances;
    }

    public static int getRootWeight(Element element){
        return qiSettings().rootWeights[element.ordinal()].get();
    }

    public static Common.QiSettings qiSettings(){
        return common().qiSettings;
    }

    public static Common.ElementSettings elementSettings(){
        return common().elementSettings;
    }

    public static Common.RealmSettings realmSettings(){
        return common().realmSettings;
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
