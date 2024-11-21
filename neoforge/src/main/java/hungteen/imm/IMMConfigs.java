package hungteen.imm;

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
                    ruleSettings.noRootChance = builder
                            .translation("config.immortal.no_root_chance")
                            .comment("The chance that players have no spiritual root.")
                            .defineInRange("NoRootChance", 0.0, 0, 1);

                    ruleSettings.oneRootChance = builder
                            .translation("config.immortal.one_root_chance")
                            .comment("The chance that players have one spiritual root.")
                            .defineInRange("OneRootChance", 0.1, 0, 1);

                    ruleSettings.twoRootChance = builder
                            .translation("config.immortal.two_root_chance")
                            .comment("The chance that players have two spiritual roots.")
                            .defineInRange("TwoRootChance", 0.2, 0, 1);

                    ruleSettings.threeRootChance = builder
                            .translation("config.immortal.three_root_chance")
                            .comment("The chance that players have three spiritual roots.")
                            .defineInRange("ThreeRootChance", 0.3, 0, 1);

                    ruleSettings.fourRootChance = builder
                            .translation("config.immortal.four_root_chance")
                            .comment("The chance that players have four spiritual roots.")
                            .defineInRange("FourRootChance", 0.3, 0, 1);

                    ruleSettings.metalRootWeight = builder
                            .translation("config.immortal.metal_root_weight")
                            .comment("The weight that players have metal spiritual root.")
                            .defineInRange("MetalRootChance", 100, 0, 1000000);

                    ruleSettings.woodRootWeight = builder
                            .translation("config.immortal.wood_root_weight")
                            .comment("The weight that players have wood spiritual root.")
                            .defineInRange("WoodRootChance", 100, 0, 1000000);

                    ruleSettings.waterRootWeight = builder
                            .translation("config.immortal.water_root_weight")
                            .comment("The weight that players have water spiritual root.")
                            .defineInRange("WaterRootChance", 100, 0, 1000000);

                    ruleSettings.fireRootWeight = builder
                            .translation("config.immortal.fire_root_weight")
                            .comment("The weight that players have fire spiritual root.")
                            .defineInRange("FireRootChance", 100, 0, 1000000);

                    ruleSettings.earthRootWeight = builder
                            .translation("config.immortal.earth_root_weight")
                            .comment("The weight that players have earth spiritual root.")
                            .defineInRange("EarthRootChance", 100, 0, 1000000);

                    ruleSettings.spiritRootWeight = builder
                            .translation("config.immortal.spirit_root_weight")
                            .comment("The weight that players have spirit spiritual root.")
                            .defineInRange("SpiritRootChance", 40, 0, 1000000);

//                    RuleSettings.windRootWeight = builder
//                            .translation("config.immortal.wind_root_weight")
//                            .comment("The weight that players have wind spiritual root (valid only when one spirit root).")
//                            .defineInRange("WindRootChance", 30, 0, 1000000);


                }
                builder.pop();
//                RuleSettings.CanSpawnDefaultMonster = builder
//                        .translation("config.pvz.rule.spawn_monster")
//                        .comment("if turn to false, there will have no monster of other monsters spawn in overworld except pvz zombies.")
//                        .define("CanSpawnDefaultMonster", true);

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
        public BlockSettings blockSettings = new BlockSettings();

        public static class RuleSettings {
            public ModConfigSpec.DoubleValue noRootChance;
            public ModConfigSpec.DoubleValue oneRootChance;
            public ModConfigSpec.DoubleValue twoRootChance;
            public ModConfigSpec.DoubleValue threeRootChance;
            public ModConfigSpec.DoubleValue fourRootChance;
            public ModConfigSpec.IntValue metalRootWeight;
            public ModConfigSpec.IntValue woodRootWeight;
            public ModConfigSpec.IntValue waterRootWeight;
            public ModConfigSpec.IntValue fireRootWeight;
            public ModConfigSpec.IntValue earthRootWeight;
            public ModConfigSpec.IntValue spiritRootWeight;
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
        }

        public ModConfigSpec.BooleanValue defaultSpellCircle;

    }

    /* Common */

    public static double getNoRootChance() {
        return ruleSettings().noRootChance.get();
    }

    public static double getOneRootChance() {
        return ruleSettings().oneRootChance.get();
    }

    public static double getTwoRootChance() {
        return ruleSettings().twoRootChance.get();
    }

    public static double getThreeRootChance() {
        return ruleSettings().threeRootChance.get();
    }

    public static double getFourRootChance() {
        return ruleSettings().fourRootChance.get();
    }

    public static int getMetalWeight() {
        return ruleSettings().metalRootWeight.get();
    }

    public static int getWoodWeight() {
        return ruleSettings().woodRootWeight.get();
    }

    public static int getWaterWeight() {
        return ruleSettings().waterRootWeight.get();
    }

    public static int getFireWeight() {
        return ruleSettings().fireRootWeight.get();
    }

    public static int getEarthWeight() {
        return ruleSettings().earthRootWeight.get();
    }

    public static int getSpiritWeight() {
        return ruleSettings().spiritRootWeight.get();
    }

    public static Common.RuleSettings ruleSettings(){
        return COMMON_CONFIG.ruleSettings;
    }

    /* Client */

    public static boolean defaultSpellCircle() {
        return CLIENT_CONFIG.defaultSpellCircle.get();
    }

}
