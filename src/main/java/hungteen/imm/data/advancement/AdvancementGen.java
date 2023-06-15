package hungteen.imm.data.advancement;

import hungteen.htlib.data.HTAdvancementGen;
import hungteen.imm.util.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/15 16:48
 */
public class AdvancementGen extends HTAdvancementGen {

    public AdvancementGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelperIn) {
        super(output, Util.id(), registries, fileHelperIn, generators());
    }

    private static AdvancementGenerator adventure(){
        return (registries, saver, existingFileHelper) -> {
//            builder().display(Items.EMERALD, Component.translatable("")).save(saver, )
        };
    }

    private static List<AdvancementGenerator> generators(){
        return List.of(
                adventure()
        );
    }

//    private static Advancement.Builder builder(ItemLike itemLike, String title){
//        return Advancement.Builder.advancement().display(
//                itemLike,
//                Util.get().lang("advancement", title),
//                Util.get().lang("advancement", title),
//                );
//    }

    private static Advancement.Builder builder(){
        return Advancement.Builder.advancement();
    }

}
