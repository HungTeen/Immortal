package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmType;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:19
 **/
public class MultiLevelRealm implements MultiRealm {

    private final RealmType[] realmTypes;

    public MultiLevelRealm(RealmType[] realmTypes) {
        this.realmTypes = new RealmType[realmTypes.length];
        System.arraycopy(realmTypes, 0, this.realmTypes, 0, realmTypes.length);
    }

    public RealmType getRealmType(RealmLevel period){
        return level(period.ordinal());
    }

    public RealmType level(int level){
        return realmTypes[Math.clamp(level - 1, 0, realmTypes.length - 1)];
    }

    public RealmType first(){
        return level(1);
    }

    @Override
    public RealmType[] getRealms() {
        return realmTypes;
    }
}
