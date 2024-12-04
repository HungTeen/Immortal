package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmType;

/**
 * 大境界，每个大境界会包含若干个小境界。
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/1 23:04
 **/
public interface MultiRealm {

    /**
     * @return 所有的小境界。
     */
    RealmType[] getRealms();

    /**
     * @param realm 小境界。
     * @return 是否包含该小境界。
     */
    default boolean hasRealm(RealmType realm){
        for(RealmType type : getRealms()){
            if(type == realm){
                return true;
            }
        }
        return false;
    }
}
