package hungteen.imm.common.cultivation.impl;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.artifact.ArtifactCategory;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/18 19:02
 **/
public interface ArtifactCategories {

    HTCustomRegistry<ArtifactCategory> CATEGORIES = HTRegistryManager.custom(Util.prefix("artifact_category"));

    ArtifactCategory DEFAULT = register("default");
    ArtifactCategory TALISMAN = register("talisman");

    static ArtifactCategory register(String name) {
        return registry().register(Util.prefix(name), new ArtifactCategoryImpl(name));
    }

    static HTCustomRegistry<ArtifactCategory> registry(){
        return CATEGORIES;
    }

    record ArtifactCategoryImpl(String name) implements ArtifactCategory {

        @Override
        public MutableComponent getComponent() {
            return TipUtil.misc("artifact_category." + name);
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }
}
