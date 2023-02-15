package hungteen.immortal.common.entity.human.cultivator;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.UUID;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 16:45
 **/
public enum CultivatorTypes {

    COMMON(Component.empty(), null, null, null, 1000),
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
    private final int weight;

    CultivatorTypes(Component displayName, ResourceLocation skinLocation, int weight) {
        this(displayName, null, null, skinLocation, weight);
    }

    CultivatorTypes(String displayName, String uuid, int weight) {
        this(Component.literal(displayName), displayName, uuid, weight);
    }

    CultivatorTypes(Component displayName, String profileName, String uuid, int weight) {
        this(displayName, profileName, UUID.fromString(uuid), null, weight);
    }

    CultivatorTypes(Component displayName, String profileName, UUID profileUUID, ResourceLocation skinLocation, int weight) {
        this.displayName = displayName;
        this.profileName = profileName;
        this.profileUUID = profileUUID;
        this.skinLocation = skinLocation;
        this.weight = weight;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Optional<ResourceLocation> getSkinLocation(){
        return Optional.ofNullable(this.skinLocation);
    }

    public Optional<UUID> getProfileUUID() {
        return Optional.ofNullable(profileUUID);
    }

    public Optional<String> getProfileName() {
        return Optional.ofNullable(profileName);
    }

    public int getWeight() {
        return weight;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public Optional<GameProfile> getGameProfile() {
        return Optional.ofNullable(gameProfile);
    }
}
