package hungteen.immortal.common.world.data;

import hungteen.htlib.util.helper.EntityHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.immortal.common.entity.formation.Formation;
import hungteen.immortal.common.network.FormationPacket;
import hungteen.immortal.common.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.server.ServerStartedEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-21 23:08
 **/
public class Formations extends SavedData {

    private static final String FORMATION_FILE_ID = "formations";
    private final ServerLevel level;
    private final Set<UUID> formationSet = new HashSet<>();

    public Formations(ServerLevel level) {
        this.level = level;
        this.setDirty();
    }

    public static List<Formation> getFormations(ServerLevel serverLevel) {
        return get(serverLevel).formationSet.stream()
                .map(serverLevel::getEntity)
                .filter(Formation.class::isInstance)
                .map(Formation.class::cast)
                .toList();
    }

    public static void addFormation(ServerLevel serverLevel, Formation formation){
        get(serverLevel).add(formation);
    }

    public static void removeFormation(ServerLevel serverLevel, Formation formation){
        get(serverLevel).remove(formation);
    }

    public void sync(boolean add, UUID id){
        PlayerHelper.getServerPlayers(this.level).forEach(player -> {
            NetworkHandler.sendToClient(player, new FormationPacket(this.level.dimension(), add, this.level.getEntity(id).getId()));
        });
    }

    /**
     * Note : Can not use isAlive to check, because EntityJoinLevelEvent is before that. <br>
     * Note : entityId is not sync to the Level currently, thats why ignore checking. <br>
     * {@link Formation#onFirstSpawn()}
     */
    public void add(Formation formation){
        if(EntityHelper.isEntityValid(formation)){
            add(formation.getUUID());
            this.setDirty();
        }
    }

    /**
     * Only run when server started {@link hungteen.immortal.ImmortalMod#serverStarted(ServerStartedEvent)}.
     */
    public void update(){
        // remove formation that does not exist.
        this.formationSet.removeIf(id -> {
            if(! EntityHelper.isEntityValid(this.level.getEntity(id))){
                this.sync(false, id);
                return true;
            }
            return false;
        });
        // add existed formation that out of the set.
        this.level.getEntities().getAll().forEach(entity -> {
            if(entity instanceof Formation && ! this.formationSet.contains(entity.getUUID())){
                this.add((Formation) entity);
            }
        });
        this.setDirty();
    }

    public void remove(Formation formation){
        remove(formation.getUUID());
        this.setDirty();
    }

    protected void add(UUID id){
        this.formationSet.add(id);
        this.sync(true, id);
    }

    protected void remove(UUID id){
        this.formationSet.remove(id);
        this.sync(false, id);
    }

    public static Formations get(ServerLevel level){
        return level.getDataStorage().computeIfAbsent(compound -> load(level, compound), () -> new Formations(level), FORMATION_FILE_ID);
    }

    private static Formations load(ServerLevel level, CompoundTag tag) {
        final Formations formations = new Formations(level);
        final int count = tag.getInt("FormationCount");
        for(int i = 0; i < count; ++i) {
            formations.add(tag.getUUID("Formation_UUID_" + i));
        }
        return formations;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("FormationCount", this.formationSet.size());
        final List<UUID> list = this.formationSet.stream().toList();
        for(int i = 0; i < list.size(); ++ i){
            tag.putUUID("Formation_UUID_" + i, list.get(i));
        }
        return tag;
    }

}
