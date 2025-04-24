package net.deepseek.v1.chainmining.data;

import net.deepseek.v1.chainmining.core.armor.ModEquipmentAssetKeys;
import net.minecraft.client.data.EquipmentAssetProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModEquipmentModelProvider extends EquipmentAssetProvider {

    protected final DataOutput.PathResolver pathResolver;

    public ModEquipmentModelProvider(DataOutput output) {
        super(output);
        this.pathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "cmr/equipment");
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Identifier, EquipmentModel> map = new HashMap<>();
        ModEquipmentAssetKeys.accept((id, model) -> {
            if (map.putIfAbsent(id, model) != null) {
                throw new IllegalStateException("Tried to register equipment model twice for id: " + id);
            }
        });
        return DataProvider.writeAllToPath(writer, EquipmentModel.CODEC, this.pathResolver, map);
    }

    public String getName() {
        return "Equipment Model Definitions";
    }
}
