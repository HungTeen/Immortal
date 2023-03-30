package hungteen.imm.common.entity.human.cultivator;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

import java.util.Optional;
import java.util.UUID;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 16:45
 **/
public enum CultivatorTypes implements WeightedEntry {

    STEVE(Component.literal("Steve"), "textures/entity/player/slim/steve.png", 1000, true),
    ALEX(Component.literal("Alex"), "textures/entity/player/slim/alex.png", 1000, true),
    ARI(Component.literal("Ari"), "textures/entity/player/slim/ari.png", 600, true),
    EFE(Component.literal("Efe"), "textures/entity/player/slim/efe.png", 600, true),
    KAI(Component.literal("Kai"), "textures/entity/player/slim/kai.png", 600, true),
    MAKENA(Component.literal("Makena"), "textures/entity/player/slim/makena.png", 600, true),
    NOOR(Component.literal("Noor"), "textures/entity/player/slim/noor.png", 600, true),
    SUNNY(Component.literal("Sunny"), "textures/entity/player/slim/sunny.png", 600, true),
    ZURI(Component.literal("Zuri"), "textures/entity/player/slim/zuri.png", 600, true),
    HUNG_TEEN("_PangTeen_", "6b78b0dc-ed31-4da6-86fb-8d36092b1023", 10),
    NEW_COMETS("_NewComets_", "f8010ab1-f2ce-45d9-ab78-72b513dc5d52", 100),
    GRASS_CARP("GrassCarp", "9017a74b-ccf4-4896-a3c4-f5ab241ecda1", 25),
    YU("Yu______","314c6ac6-eb95-4994-96eb-85ee2b676a83", 100),
    ;

    private final Component displayName;
    private ResourceLocation skinLocation;
    private String profileName;
    private UUID profileUUID;
    private GameProfile gameProfile;
    private final boolean common;
    private final int weight;

    /**
     * 离线类型（没有正版号）。
     * @param displayName 玩家头上显示。
     * @param skinLocation 本地贴图。
     */
    CultivatorTypes(Component displayName, String skinLocation, int weight, boolean common) {
        this(displayName, null, null, new ResourceLocation(skinLocation), weight, common);
    }

    CultivatorTypes(String displayName, String uuid, int weight) {
        this(Component.literal(displayName), displayName, uuid, weight);
    }

    CultivatorTypes(Component displayName, String profileName, String uuid, int weight) {
        this(displayName, profileName, UUID.fromString(uuid), null, weight, false);
    }

    CultivatorTypes(Component displayName, String profileName, UUID profileUUID, ResourceLocation skinLocation, int weight, boolean common) {
        this.displayName = displayName;
        this.profileName = profileName;
        this.profileUUID = profileUUID;
        this.skinLocation = skinLocation;
        this.weight = weight;
        this.common = common;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Optional<ResourceLocation> getSkinLocation(){
        return Optional.ofNullable(this.skinLocation);
    }

    /**
     * 判断有没有正版账户。
     * @return Empty if it's an offline id.
     */
    public Optional<UUID> getProfileUUID() {
        return Optional.ofNullable(profileUUID);
    }

    public Optional<String> getProfileName() {
        return Optional.ofNullable(profileName);
    }

    /**
     * 随机生成的权重
     * @return Random spawn weight.
     */
    public Weight getWeight() {
        return Weight.of(weight);
    }

    /**
     * 不是定制类型。
     * @return Not specific type.
     */
    public boolean isCommon() {
        return common;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public Optional<GameProfile> getGameProfile() {
        return Optional.ofNullable(gameProfile);
    }
}
