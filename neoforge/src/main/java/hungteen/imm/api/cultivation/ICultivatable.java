package hungteen.imm.api.cultivation;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 12:57
 **/
public interface ICultivatable {

    /**
     * 获取生物的境界
     * @return Realm type of entity.
     */
    RealmType getRealm();

    /**
     * 获取生物的修行方式。
     * @return Cultivate type of entity.
     */
    default CultivationType getCultivationType(){
        return getRealm().getCultivationType();
    }

}
