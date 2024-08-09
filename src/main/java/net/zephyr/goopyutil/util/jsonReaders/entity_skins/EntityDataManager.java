package net.zephyr.goopyutil.util.jsonReaders.entity_skins;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.zephyr.goopyutil.GoopyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Environment(EnvType.CLIENT)
public class EntityDataManager extends SinglePreparationResourceReloader<Map<EntityType<?>, GoopyUtilEntityData>> {
    static final Gson GSON = new Gson();
    private Map<EntityType<?>, List<EntitySkin>> Skins = new HashMap<>();
    private Map<EntityType<?>, GoopyUtilEntityData> EntityData = new HashMap<>();

    public List<EntitySkin> getSkins(EntityType<?> type){
        if(Skins.containsKey(type)) return Skins.get(type);
        return null;
    }

    public EntitySkin getSkin(EntityType<?> type, String textureName){
        List<EntitySkin> skins = Skins.get(type);
        for(EntitySkin layer : skins){
            if(Objects.equals(layer.getName(), textureName))
                return layer;
        }
        return getDefault(type);
    }
    public EntitySkin getDefault(EntityType<?> type){
        List<EntitySkin> skins = Skins.get(type);
        for(EntitySkin layer : skins){
            if(Objects.equals(layer.getName(), "default"))
                return layer;
        }
        return null;
    }
    public GoopyUtilEntityData getEntityData(EntityType<?> type){
        return EntityData.get(type);
    }

    @Override
    protected Map<EntityType<?>, GoopyUtilEntityData> prepare(ResourceManager resourceManager, Profiler profiler) {
        Map<EntityType<?>, GoopyUtilEntityData> data = new HashMap<>();
        for (String string : resourceManager.getAllNamespaces()) {
            try {
                for(EntityType<?> entityType : Registries.ENTITY_TYPE) {
                    String path = "entitydata/" + Registries.ENTITY_TYPE.getId(entityType).getPath() + "_data.json";
                    List<Resource> list = resourceManager.getAllResources(Identifier.of(string, path));
                    for (Resource resource : list) {
                        try (BufferedReader reader = resource.getReader();) {
                            GoopyUtilEntityData skinEntry = JsonHelper.deserialize(GSON, reader, GoopyUtilEntityData.class);
                            data.put(entityType, skinEntry);
                        } catch (RuntimeException runtimeException) {
                            GoopyUtil.LOGGER.warn("Invalid {} in resourcepack: '{}'", "entity_skins.json", resource.getPackId(), runtimeException);
                        }
                    }
                }
            } catch (IOException iOException) {
                // empty catch block
            }
        }
        return data;
    }

    @Override
    protected void apply(Map<EntityType<?>, GoopyUtilEntityData> prepared, ResourceManager manager, Profiler profiler) {
        this.EntityData.clear();
        this.Skins.clear();
        this.EntityData = prepared;
        for(EntityType<?> type : prepared.keySet()){
            this.Skins.put(type, prepared.get(type).skins());
            for(EntitySkin skin : prepared.get(type).skins()) {
                System.out.println(skin.getName());
            }
        }
    }
}
