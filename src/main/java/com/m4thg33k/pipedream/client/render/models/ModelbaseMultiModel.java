package com.m4thg33k.pipedream.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

import java.util.HashMap;
import java.util.Map;

public class ModelBaseMultiModel {

    protected Map<String, IBakedModel> myModels = new HashMap<String, IBakedModel>();

    public ModelBaseMultiModel()
    {

    }

    public void createModels(String modelName, String toReplace, Map<String, String> map)
    {
        OBJModel parent = ModelHelper.loadModel(modelName);

        for (String key : map.keySet())
        {
            IModel model = ModelHelper.retexture(parent, toReplace, map.get(key));
            myModels.put(key, ModelHelper.bake(model));
        }
    }

    public void renderModel(String key)
    {
        renderModel(myModels.get(key));
    }

    protected void renderModel(IBakedModel model)
    {
        renderModel(model, -1);
    }

    protected void renderModel(IBakedModel model, int color)
    {
        ModelHelper.renderModel(model, color);
    }
}
