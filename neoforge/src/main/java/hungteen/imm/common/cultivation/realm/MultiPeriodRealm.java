package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmType;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:19
 **/
public class MultiPeriodRealm implements MultiRealm {

    private final RealmType[] realmTypes = new RealmType[RealmPeriod.values().length];

    public MultiPeriodRealm(RealmType[] realmTypes) {
        if(realmTypes.length != RealmPeriod.values().length){
            throw new IllegalArgumentException("The length of realmTypes must be equal to the length of RealmPeriod.values().");
        }
        System.arraycopy(realmTypes, 0, this.realmTypes, 0, realmTypes.length);
    }

    public RealmType getRealmType(RealmPeriod period){
        return realmTypes[period.ordinal()];
    }

    public RealmType pre(){
        return realmTypes[0];
    }

    public RealmType mid(){
        return realmTypes[1];
    }

    public RealmType post(){
        return realmTypes[2];
    }

    public RealmType top(){
        return realmTypes[realmTypes.length - 1];
    }

    @Override
    public RealmType[] getRealms() {
        return realmTypes;
    }
}
